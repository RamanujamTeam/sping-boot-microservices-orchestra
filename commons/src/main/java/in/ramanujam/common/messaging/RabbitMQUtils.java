package in.ramanujam.common.messaging;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import in.ramanujam.common.properties.RabbitMQProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQUtils
{
  public static Connection getConnection()
  {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost( RabbitMQProperties.getInstance().getRabbitMQContainerHost() );
    factory.setPort( RabbitMQProperties.getInstance().getRabbitMQContainerExternalPort() );
    return createConnection( factory );
  }

  private static Connection createConnection( ConnectionFactory factory )
  {
    int count = 0;
    int maxTries = 20;
    while(true) {
      try {
        Thread.sleep( 3000 ); // wait for RabbitMQ docker to start
        return factory.newConnection();
      } catch ( IOException | TimeoutException | InterruptedException e ) {
        if (++count == maxTries) throw new RuntimeException( e );
      }
    }
  }
}
