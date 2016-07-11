package in.ramanujam.services.starter;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.command.WaitContainerResultCallback;
import com.rabbitmq.client.*;
import in.ramanujam.common.RabbitMQUtils;
import in.ramanujam.common.model.BitcoinRecord;
import in.ramanujam.common.properties.ElasticSearchProperties;
import in.ramanujam.common.properties.RabbitMQProperties;
import in.ramanujam.common.properties.RedisProperties;

import java.io.IOException;
import java.net.ConnectException;
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
  private static boolean redisToMongoFinished = false;
  private static boolean elasticToMongoFinished = false;

  public static void main( String[] args ) throws IOException, TimeoutException, InterruptedException
  {
    DockerClient dockerClient = DockerClientFactory.getClient();

    CreateContainerResponse redisContainer = getRedisContainer( dockerClient );
    CreateContainerResponse ESContainer = getElasticSearchContainer( dockerClient );
    CreateContainerResponse rabbitMQContainer = getRabbitMQContainer( dockerClient );

    tryStartContainer( dockerClient, redisContainer );
    tryStartContainer( dockerClient, ESContainer );
    tryStartContainer( dockerClient, rabbitMQContainer );

    Connection connection = RabbitMQUtils.getConnection();
    Channel channel = connection.createChannel();
    channel.queueDeclare( RabbitMQProperties.getInstance().getRabbitmqQueueName(), false, false, false, null );

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

    channel.basicConsume( RabbitMQProperties.getInstance().getRabbitmqQueueName(), true, consumer );

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

  private static CreateContainerResponse getRabbitMQContainer( DockerClient dockerClient )
  {
    return createContainer( dockerClient, RabbitMQProperties.getInstance().getRabbitMQContainerPort(),
                            RabbitMQProperties.getInstance().getRabbitMQContainerHost(),
                            RabbitMQProperties.getInstance().getRabbitMQContainerExternalPort(),
                            RabbitMQProperties.getInstance().getRabbitMQContainerName() );
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