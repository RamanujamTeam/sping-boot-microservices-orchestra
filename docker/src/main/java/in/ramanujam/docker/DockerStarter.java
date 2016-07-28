package in.ramanujam.docker;

import com.github.dockerjava.api.DockerClient;
import com.rabbitmq.client.*;
import in.ramanujam.common.docker.DockerClientFactory;
import in.ramanujam.common.docker.DockerUtils;
import in.ramanujam.common.messaging.RabbitMQUtils;
import in.ramanujam.common.properties.ElasticSearchProperties;
import in.ramanujam.common.properties.RabbitMQProperties;
import in.ramanujam.common.properties.RedisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DockerStarter {
    private static final Logger log = LoggerFactory.getLogger(DockerStarter.class);
    private static boolean redisToMongoFinished = false;
    private static boolean elasticToMongoFinished = false;

    public static void main(String[] args) throws IOException, InterruptedException {
        DockerClient dockerClient = DockerClientFactory.getClient();

        String redisContainerId = DockerUtils.getRedisContainerId(dockerClient);
        String ESContainerId = DockerUtils.getElasticSearchContainerId(dockerClient);
        String rabbitMQContainerId = DockerUtils.getRabbitMQContainerId(dockerClient);

        DockerUtils.tryToStartContainer(dockerClient, redisContainerId);
        DockerUtils.tryToStartContainer(dockerClient, ESContainerId);
        DockerUtils.tryToStartContainer(dockerClient, rabbitMQContainerId);

        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(RabbitMQProperties.getInstance().getRabbitmqQueueName(), false, false, false, null);

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                if (ElasticSearchProperties.getInstance().getElasticsearchToMongoIsFinishedKey().equals(message)) {
                    elasticToMongoFinished = true;
                }

                if (RedisProperties.getInstance().getRedisToMongoIsFinishedKey().equals(message)) {
                    redisToMongoFinished = true;
                }
            }
        };

        channel.basicConsume(RabbitMQProperties.getInstance().getRabbitmqQueueName(), true, consumer);

        while (!(redisToMongoFinished && elasticToMongoFinished)) {
            Thread.sleep(3000);
        }
        DockerUtils.closeConnection(channel, connection);

        DockerUtils.tryToStopContainer(dockerClient, redisContainerId);
        DockerUtils.tryToStopContainer(dockerClient, ESContainerId);
        DockerUtils.tryToStopContainer(dockerClient, rabbitMQContainerId);
        log.info("DockerStarter :: Successfully finished!");
    }


}