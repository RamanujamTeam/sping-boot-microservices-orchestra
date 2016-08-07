package in.ramanujam.common.messaging;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import in.ramanujam.common.properties.RabbitMQProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class RabbitMQUtils {
    private static final int MAX_TRIES = 200;
    private static final int RETRY_DELAY_MS = 200;

    @Autowired
    RabbitMQProperties rabbitProps;

    public Connection getConnection() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitProps.getRabbitMQContainerHost());
        factory.setPort(rabbitProps.getRabbitMQContainerExternalPort());
        return createConnection(factory);
    }

    private Connection createConnection(ConnectionFactory factory) {
        int count = 0;
        while (true) {
            try {
                Thread.sleep(RETRY_DELAY_MS); // wait for RabbitMQ docker to start
                return factory.newConnection();
            } catch (IOException | TimeoutException | InterruptedException e) {
                if (++count == MAX_TRIES) throw new RuntimeException(e);
            }
        }
    }
}
