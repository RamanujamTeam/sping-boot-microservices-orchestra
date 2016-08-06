package in.ramanujam.common.properties;

import org.springframework.stereotype.Component;

@Component
public class RabbitMQProperties extends AbstractProperties {
    private static final String RABBITMQ_CONTAINER_NAME = "rabbitmq.container.name";
    private static final String RABBITMQ_CONTAINER_HOST = "rabbitmq.container.host";
    private static final String RABBITMQ_CONTAINER_PORT = "rabbitmq.container.port";
    private static final String RABBITMQ_CONTAINER_EXTERNAL_PORT = "rabbitmq.container.externalPort";
    private static final String RABBITMQ_QUEUE_NAME = "rabbitmq.queue.name";

    private static final String CONFIG_FILE = "rabbitmq.properties";

    private static RabbitMQProperties instance = new RabbitMQProperties();

    private RabbitMQProperties() {
        load();
    }

    public static RabbitMQProperties getInstance() {
        return instance;
    }

    public String getRabbitMQContainerName() {
        return getAppProps().getProperty(RABBITMQ_CONTAINER_NAME);
    }

    public String getRabbitMQContainerHost() {
        return getAppProps().getProperty(RABBITMQ_CONTAINER_HOST);
    }

    public Integer getRabbitMQContainerPort() {
        return Integer.parseInt(getAppProps().getProperty(RABBITMQ_CONTAINER_PORT));
    }

    public Integer getRabbitMQContainerExternalPort() {
        return Integer.parseInt(getAppProps().getProperty(RABBITMQ_CONTAINER_EXTERNAL_PORT));
    }

    public String getRabbitmqQueueName() {
        return getAppProps().getProperty(RABBITMQ_QUEUE_NAME);
    }

    @Override
    protected String getFile() {
        return CONFIG_FILE;
    }
}
