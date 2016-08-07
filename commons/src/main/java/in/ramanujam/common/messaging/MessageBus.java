package in.ramanujam.common.messaging;

import com.rabbitmq.client.Channel;
import in.ramanujam.common.properties.RabbitMQProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageBus {
    private final Channel channel;

    private RabbitMQProperties rabbitProps;

    @Autowired
    private MessageBus(RabbitMQProperties rabbitProps, RabbitMQUtils rabbitUtils) {
        try {
            this.rabbitProps = rabbitProps;
            channel = rabbitUtils.getConnection().createChannel();
            channel.queueDeclare(rabbitProps.getRabbitmqQueueName(), false, false, false, null);
        } catch (IOException e) {
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