package in.ramanujam;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.command.WaitContainerResultCallback;
import in.ramanujam.properties.ElasticSearchProperties;
import in.ramanujam.properties.RedisProperties;
import org.springframework.boot.SpringApplication;

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

    Info info = dockerClient.infoCmd().exec();

    CreateContainerResponse redisContainer = getRedisContainer( dockerClient );

    CreateContainerResponse ESContainer = getElasticSearchContainer( dockerClient );

    tryStartContainer( dockerClient, redisContainer );
    tryStartContainer( dockerClient, ESContainer );

    SpringApplication.run( RedisFillerMain.class, args );
    dockerClient.waitContainerCmd(redisContainer.getId())
            .exec( new WaitContainerResultCallback() )
            .awaitStatusCode(); // TODO: use waitContainerCmd!

    dockerClient.removeContainerCmd( redisContainer.getId() ).exec();
    dockerClient.removeContainerCmd( ESContainer.getId() ).exec();
//    dockerClient.stopContainerCmd(redisContainer.getId()).exec();
//    dockerClient.stopContainerCmd(ESContainer.getId()).exec();
  }

  private static void tryStartContainer( DockerClient dockerClient, CreateContainerResponse redisContainer )
  {
    try
    {
      dockerClient.startContainerCmd( redisContainer.getId() ).exec();
    }
    catch( Exception e )
    {
      if( e.getMessage().contains( "port is already allocated" ) )
        System.out.println( e.getMessage() );
      else
        throw e;
    }
  }

  private static CreateContainerResponse getRedisContainer( DockerClient dockerClient )
  {
    RedisProperties properties = new RedisProperties();

    return createContainer(dockerClient, properties.getContainerRedisPort(),
                           properties.getContainerRedisHost(),
                           properties.getContainerRedisExternalPort(),
                           properties.getContainerRedisName() );
  }

  private static CreateContainerResponse getElasticSearchContainer( DockerClient dockerClient )
  {
    ElasticSearchProperties properties = new ElasticSearchProperties();

    return createContainer(dockerClient, properties.getContainerElasticsearchPort(),
                           properties.getContainerElasticsearchHost(),
                           properties.getContainerElasticsearchExternalPort(),
                           properties.getContainerElasticsearchName() );
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
