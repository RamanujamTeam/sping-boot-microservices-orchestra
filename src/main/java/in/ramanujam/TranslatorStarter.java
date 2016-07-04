package in.ramanujam;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.command.WaitContainerResultCallback;
import in.ramanujam.properties.ElasticSearchProperties;
import in.ramanujam.properties.RedisProperties;

/**
 * Created with IntelliJ IDEA.
 * User: Denys Konakhevych
 * Date: 30.06.2016
 * Time: 22:13
 */
public class TranslatorStarter
{
  public static void main( String[] args )
  {
    DockerClient dockerClient = DockerClientFactory.getClient();

    CreateContainerResponse redisContainer = getRedisContainer( dockerClient );

    CreateContainerResponse ESContainer = getElasticSearchContainer( dockerClient );

    tryStartContainer( dockerClient, redisContainer );
    tryStartContainer( dockerClient, ESContainer );

    dockerClient.waitContainerCmd(redisContainer.getId())
            .exec( new WaitContainerResultCallback() )
            .awaitStatusCode();

    dockerClient.removeContainerCmd( redisContainer.getId() ).exec(); // TODO: use killContainerCmd
    dockerClient.removeContainerCmd( ESContainer.getId() ).exec();
//    dockerClient.stopContainerCmd(redisContainer.getId()).exec();
//    dockerClient.stopContainerCmd(ESContainer.getId()).exec();
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
    RedisProperties properties = new RedisProperties();

    return createContainer(dockerClient, properties.getRedisContainerPort(),
                           properties.getRedisContainerHost(),
                           properties.getRedisContainerExternalPort(),
                           properties.getRedisContainerName() );
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
}
