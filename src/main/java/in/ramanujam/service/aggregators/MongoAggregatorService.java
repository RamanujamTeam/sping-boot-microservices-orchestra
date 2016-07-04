package in.ramanujam.service.aggregators;

import com.rabbitmq.client.*;
import in.ramanujam.model.RedisRecord;
import in.ramanujam.service.MongoAggregator;
import in.ramanujam.service.retriever.RedisRetriever;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.TimeoutException;

/**
 * Created with IntelliJ IDEA.
 * User: Denys Konakhevych
 * Date: 04.07.2016
 * Time: 20:01
 */
public class MongoAggregatorService
{
  private static final String QUEUE_NAME = "message";

  public static void main( String[] args )
  {
    try
    {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost( "192.168.99.100" );
      factory.setPort( 32773 );
      Connection connection = factory.newConnection();
      Channel channel = connection.createChannel();

      channel.queueDeclare( QUEUE_NAME, false, false, false, null );

      Consumer consumer = new DefaultConsumer(channel) {
        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                throws IOException
        {
          String message = new String(body, "UTF-8");
          if( "AGGREGATE".equals( message ) )
          {
            Collection<RedisRecord> redisRecords = RedisRetriever.retrieveAllRecords();

          }
          else if( "END".equals( message ) )
          {
            closeConnection( channel, connection );
          }
        }
      };
      channel.basicConsume( QUEUE_NAME, true, consumer );
    }
    catch( TimeoutException | IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  private static void closeConnection( Channel channel, Connection connection ) throws IOException
  {
    try
    {
      channel.close();
    }
    catch( TimeoutException e )
    {
      throw new RuntimeException( e );
    }
    finally
    {
      connection.close();
    }
  }
}
