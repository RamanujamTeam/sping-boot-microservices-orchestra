package in.ramanujam.common.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQProperties {

    @Value("${rabbitmq.container.name}")
    private String rabbitmqContainerName;

    @Value("${rabbitmq.container.host}")
    private String rabbitmqContainerHost;

    @Value("${rabbitmq.container.port}")
    private int rabbitmqContainerPort;

    @Value("${rabbitmq.container.externalPort}")
    private int rabbitmqContainerExternalPort;

    @Value("${rabbitmq.queue.name}")
    private String rabbitmqQueueName;

    public String getRabbitMQContainerName() {
        return rabbitmqContainerName;
    }

    public String getRabbitMQContainerHost() {
        return rabbitmqContainerHost;
    }

    public int getRabbitMQContainerPort() {
        return rabbitmqContainerPort;
    }

    public int getRabbitMQContainerExternalPort() {
        return rabbitmqContainerExternalPort;
    }

    public String getRabbitmqQueueName() {
        return rabbitmqQueueName;
    }
}