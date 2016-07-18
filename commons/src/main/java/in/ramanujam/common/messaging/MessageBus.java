package in.ramanujam.common.messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import in.ramanujam.common.properties.RabbitMQProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MessageBus {
    private static final MessageBus instance = new MessageBus();
    private final Channel channel;

    private MessageBus() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(RabbitMQProperties.getInstance().getRabbitMQContainerHost());
            factory.setPort(RabbitMQProperties.getInstance().getRabbitMQContainerExternalPort());
            Connection connection = factory.newConnection();
            channel = connection.createChannel();

            channel.queueDeclare(RabbitMQProperties.getInstance().getRabbitmqQueueName(), false, false, false, null);
        } catch (TimeoutException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static MessageBus getInstance() {
        return instance;
    }

    public void sendMessage(String message) {
        try {
            channel.basicPublish("", RabbitMQProperties.getInstance().getRabbitmqQueueName(), null, message.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}