package in.ramanujam.common.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.NotModifiedException;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import in.ramanujam.common.properties.ElasticSearchProperties;
import in.ramanujam.common.properties.RabbitMQProperties;
import in.ramanujam.common.properties.RedisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class DockerUtils {
    private static final Logger log = LoggerFactory.getLogger(DockerUtils.class);

    public static void tryToStartContainer(DockerClient dockerClient, String containerId) {
        try {
            dockerClient.startContainerCmd(containerId).exec();
        } catch (Exception e) {
            if (e.getMessage().contains("port is already allocated")) // consider this is our container from the previous program run
                log.debug(e.getMessage());
            else
                throw e;
        }
    }

    public static void tryToStopContainer(DockerClient dockerClient, String containerId) {
        try {
            dockerClient.stopContainerCmd(containerId).exec();
        } catch (NotModifiedException e) {
        }
    }

    public static String getRedisContainerId(DockerClient dockerClient) {
        CreateContainerResponse response = createContainer(dockerClient, RedisProperties.getInstance().getRedisContainerPort(),
                RedisProperties.getInstance().getRedisContainerHost(),
                RedisProperties.getInstance().getRedisContainerExternalPort(),
                RedisProperties.getInstance().getRedisContainerName());
        return response.getId();
    }

    public static String getElasticSearchContainerId(DockerClient dockerClient) {
        ElasticSearchProperties properties = new ElasticSearchProperties();

        CreateContainerResponse response = createContainer(dockerClient, properties.getElasticsearchContainerPort(),
                properties.getElasticsearchContainerHost(),
                properties.getElasticsearchContainerExternalPort(),
                properties.getElasticsearchContainerName());
        return response.getId();
    }

    public static String getRabbitMQContainerId(DockerClient dockerClient) {
        CreateContainerResponse response = createContainer(dockerClient, RabbitMQProperties.getInstance().getRabbitMQContainerPort(),
                RabbitMQProperties.getInstance().getRabbitMQContainerHost(),
                RabbitMQProperties.getInstance().getRabbitMQContainerExternalPort(),
                RabbitMQProperties.getInstance().getRabbitMQContainerName());
        return response.getId();
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


    public static void closeConnection(Channel channel, Connection connection) throws IOException {
        try {
            channel.close();
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } finally {
            connection.close();
        }
    }
}
