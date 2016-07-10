package in.ramanujam.services.starter;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.command.WaitContainerResultCallback;
import com.rabbitmq.client.*;
import in.ramanujam.common.model.BitcoinRecord;
import in.ramanujam.common.properties.ElasticSearchProperties;
import in.ramanujam.common.properties.RedisProperties;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.TimeoutException;

/**
 * Created with IntelliJ IDEA.
 * User: Denys Konakhevych
 * Date: 30.06.2016
 * Time: 22:13
 */
public class TranslatorStarter
{
  private static final String QUEUE_NAME = "message";
  private static boolean redisToMongoFinished = false;
  private static boolean elasticToMongoFinished = false;

  public static void main( String[] args ) throws IOException, TimeoutException, InterruptedException
  {












    DockerClient dockerClient = DockerClientFactory.getClient();

    CreateContainerResponse redisContainer = getRedisContainer( dockerClient );

    CreateContainerResponse ESContainer = getElasticSearchContainer( dockerClient );

    tryStartContainer( dockerClient, redisContainer );
    tryStartContainer( dockerClient, ESContainer );









    // TODO: run rabbitmq in docker!
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost( "localhost" ); // TODO: exctract to properties
    factory.setPort( 5672 ); // TODO: what port?
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare( QUEUE_NAME, false, false, false, null );

    Consumer consumer = new DefaultConsumer( channel) { // TODO: replace it with lambda:
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
              throws IOException
      {
        String message = new String(body, "UTF-8");
        if( "ElasticSearchToMongoFinished".equals( message ) ) // TODO: excract into properties
        {
          elasticToMongoFinished = true;
        }

        if( "RedisToMongoFinished".equals( message ) ) // TODO: excract into properties
        {
          redisToMongoFinished = true;
        }
      }
    };

    channel.basicConsume( QUEUE_NAME, true, consumer );

    while( !( redisToMongoFinished && elasticToMongoFinished ) )
    {
      Thread.sleep( 2000 );
    }
    closeConnection( channel, connection );
    dockerClient.stopContainerCmd(redisContainer.getId()).exec();
    dockerClient.stopContainerCmd(ESContainer.getId()).exec();
  }

  private static void tryStartContainer( DockerClient dockerClient, CreateContainerResponse container )
  {
    try
    {
      dockerClient.startContainerCmd( container.getId() ).exec();
    }
    catch( Exception e )
    {
      if( e.getMessage().contains( "port is already allocated" ) ) // consider this is our container from the previous program run
        System.out.println( e.getMessage() );
      else
        throw e;
    }
  }

  private static CreateContainerResponse getRedisContainer( DockerClient dockerClient )
  {
    return createContainer(dockerClient, RedisProperties.getInstance().getRedisContainerPort(),
                           RedisProperties.getInstance().getRedisContainerHost(),
                           RedisProperties.getInstance().getRedisContainerExternalPort(),
                           RedisProperties.getInstance().getRedisContainerName() );
  }

  private static CreateContainerResponse getElasticSearchContainer( DockerClient dockerClient )
  {
    ElasticSearchProperties properties = new ElasticSearchProperties();

    return createContainer(dockerClient, properties.getElasticsearchContainerPort(),
                           properties.getElasticsearchContainerHost(),
                           properties.getElasticsearchContainerExternalPort(),
                           properties.getElasticsearchContainerName() );
  }

  private static CreateContainerResponse createContainer( final DockerClient dockerClient, final Integer port, final String host,
                                                          final Integer hostPort, final String containerName )
  {
    ExposedPort exposedPort = ExposedPort.tcp(port);
    Ports portBinding = new Ports();
    portBinding.bind( exposedPort, new Ports.Binding( host, hostPort.toString() ) );

    return dockerClient.createContainerCmd( containerName )
            .withExposedPorts(exposedPort)
            .withPortBindings( portBinding)
            .exec();
  }


  private static void closeConnection( Channel channel, Connection connection ) throws IOException // TODO: hide this method
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
