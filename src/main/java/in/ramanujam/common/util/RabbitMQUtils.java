package in.ramanujam.common.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import in.ramanujam.common.properties.RabbitMQProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQUtils
{
  public static Connection getConnection() throws IOException, TimeoutException, InterruptedException
  {
//    Thread.sleep( 5000 ); // TODO: this is a stupid fix - connection factory throws exception if try to connect to docker container too promptly. We need a better solution than this :-)
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost( RabbitMQProperties.getInstance().getRabbitMQContainerHost() );
    factory.setPort( RabbitMQProperties.getInstance().getRabbitMQContainerExternalPort() );
    return factory.newConnection();
  }
}
