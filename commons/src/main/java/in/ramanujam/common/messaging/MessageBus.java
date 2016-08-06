package in.ramanujam.common.messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import in.ramanujam.common.properties.RabbitMQProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class MessageBus {
    private final Channel channel;

    private RabbitMQProperties rabbitProps;

    @Autowired
    private MessageBus(RabbitMQProperties rabbitProps) {
        try {
            this.rabbitProps = rabbitProps;
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(rabbitProps.getRabbitMQContainerHost());
            factory.setPort(rabbitProps.getRabbitMQContainerExternalPort());
            Connection connection = factory.newConnection();
            channel = connection.createChannel();

            channel.queueDeclare(rabbitProps.getRabbitmqQueueName(), false, false, false, null);
        } catch (TimeoutException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String message) {
        try {
            channel.basicPublish("", rabbitProps.getRabbitmqQueueName(), null, message.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}