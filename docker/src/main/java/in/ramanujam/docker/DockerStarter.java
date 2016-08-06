package in.ramanujam.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.NotModifiedException;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.rabbitmq.client.*;
import in.ramanujam.common.messaging.RabbitMQUtils;
import in.ramanujam.common.properties.ElasticSearchProperties;
import in.ramanujam.common.properties.RabbitMQProperties;
import in.ramanujam.common.properties.RedisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan({"in.ramanujam.docker", "in.ramanujam.common"})
public class DockerStarter {
    private static final Logger log = LoggerFactory.getLogger(DockerStarter.class);
    private static boolean redisToMongoFinished = false;
    private static boolean elasticToMongoFinished = false;

    @Autowired
    private RedisProperties redisProps;
    @Autowired
    private ElasticSearchProperties elasticProps;
    @Autowired
    private RabbitMQUtils rabbitMQUtils;
    @Autowired
    private RabbitMQProperties rabbitProps;

    @Autowired
    private void runDockers(DockerClientFactory dockerClientFactory) throws IOException, InterruptedException {
        DockerClient dockerClient = dockerClientFactory.getClient();
        // TODO: можем автоматизировать запуск скрипта ./start_containers.sh ?
        CreateContainerResponse redisContainer = getRedisContainer(dockerClient);
        CreateContainerResponse ESContainer = getElasticSearchContainer(dockerClient);
        CreateContainerResponse rabbitMQContainer = getRabbitMQContainer(dockerClient);

        tryToStartContainer(dockerClient, redisContainer);
        tryToStartContainer(dockerClient, ESContainer);
        tryToStartContainer(dockerClient, rabbitMQContainer);

        Connection connection = rabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(rabbitProps.getRabbitmqQueueName(), false, false, false, null);

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                if (elasticProps.getElasticsearchToMongoIsFinishedKey().equals(message)) {
                    elasticToMongoFinished = true;
                }

                if (redisProps.getRedisToMongoIsFinishedKey().equals(message)) {
                    redisToMongoFinished = true;
                }
            }
        };

        channel.basicConsume(rabbitProps.getRabbitmqQueueName(), true, consumer);

        while (!(redisToMongoFinished && elasticToMongoFinished)) {
            Thread.sleep(3000);
        }
        closeConnection(channel, connection);

        tryToStopContainer(dockerClient, redisContainer);
        tryToStopContainer(dockerClient, ESContainer);
        tryToStopContainer(dockerClient, rabbitMQContainer);
        log.info("DockerStarter :: Successfully finished!");
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(DockerStarter.class, args);
    }

    private static void tryToStartContainer(DockerClient dockerClient, CreateContainerResponse container) {
        try {
            dockerClient.startContainerCmd(container.getId()).exec();
        } catch (Exception e) {
            if (e.getMessage().contains("port is already allocated")) // consider this is our container from the previous program run
                log.debug(e.getMessage());
            else
                throw e;
        }
    }

    private static void tryToStopContainer(DockerClient dockerClient, CreateContainerResponse container) {
        try {
            dockerClient.stopContainerCmd(container.getId()).exec();
        } catch (NotModifiedException e) {
        }
    }

    private CreateContainerResponse getRedisContainer(DockerClient dockerClient) {
        return createContainer(dockerClient, redisProps.getRedisContainerPort(),
                redisProps.getRedisContainerHost(),
                redisProps.getRedisContainerExternalPort(),
                redisProps.getRedisContainerName());
    }

    private CreateContainerResponse getElasticSearchContainer(DockerClient dockerClient) {
        return createContainer(dockerClient, elasticProps.getElasticsearchContainerPort(),
                elasticProps.getElasticsearchContainerHost(),
                elasticProps.getElasticsearchContainerExternalPort(),
                elasticProps.getElasticsearchContainerName());
    }

    private CreateContainerResponse getRabbitMQContainer(DockerClient dockerClient) {
        return createContainer(dockerClient, rabbitProps.getRabbitMQContainerPort(),
                rabbitProps.getRabbitMQContainerHost(),
                rabbitProps.getRabbitMQContainerExternalPort(),
                rabbitProps.getRabbitMQContainerName());
    }

    private static CreateContainerResponse createContainer(final DockerClient dockerClient, final Integer port, final String host,
                                                           final Integer hostPort, final String containerName) {
        ExposedPort exposedPort = ExposedPort.tcp(port);
        Ports portBinding = new Ports();
        portBinding.bind(exposedPort, new Ports.Binding(host, hostPort.toString()));

        return dockerClient.createContainerCmd(containerName)
                .withExposedPorts(exposedPort)
                .withPortBindings(portBinding)
                .exec();
    }


    private static void closeConnection(Channel channel, Connection connection) throws IOException {
        try {
            channel.close();
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } finally {
            connection.close();
        }
    }
}