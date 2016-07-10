package in.ramanujam.common.messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created with IntelliJ IDEA.
 * User: Denys Konakhevych
 * Date: 04.07.2016
 * Time: 19:50
 */
public class MessageBus
{
  private static final String QUEUE_NAME = "message";
  private static final MessageBus instance = new MessageBus();
  private final Channel channel;

  private MessageBus()
  {
    try
    {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost( "localhost" ); // TODO: exctract to Properties
      factory.setPort( 5672 );
      Connection connection = factory.newConnection();
      channel = connection.createChannel();

      channel.queueDeclare( QUEUE_NAME, false, false, false, null );
    }
    catch( TimeoutException | IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  public static MessageBus getInstance()
  {
    return instance;
  }

  public void sendMessage( String message )
  {
    try
    {
      channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
  }
}