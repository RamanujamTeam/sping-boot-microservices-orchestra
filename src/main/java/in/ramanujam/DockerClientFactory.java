package in.ramanujam;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import in.ramanujam.properties.DockerProperties;

/**
 * Created with IntelliJ IDEA.
 * User: Denys Konakhevych
 * Date: 30.06.2016
 * Time: 22:49
 */
public class DockerClientFactory
{
  public static DockerClient getClient()
  {
    if( isWindows() )
      return getWindowsOSClient();
    else
      return getLinuxOSClient();
  }

  public static boolean isWindows() {
    return getOSName().contains( "win" );
  }

  public static boolean isMac() {
    return getOSName().contains( "mac" );
  }

  public static boolean isUnix() {
    final String OS = getOSName();
    return (OS.contains( "nix" ) || OS.contains( "nux" ) || OS.contains("aix") );
  }

  private static String getOSName()
  {
    return System.getProperty( "os.name" ).toLowerCase();
  }

  private static DockerClient getWindowsOSClient()
  {
    DockerProperties properties = new DockerProperties();

    DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder()
            .withDockerCertPath(properties.getDockerCertPath())
            .withDockerHost("tcp://" + properties.getDockerHost() + ":" + properties.getDockerPort())
            .withDockerTlsVerify(properties.getDockerTlsVerify())
            .withApiVersion( properties.getDockerApiVersion() )
            .build();

    return DockerClientBuilder.getInstance( config ).build();
  }

  private static DockerClient getLinuxOSClient()
  {
    DockerProperties properties = new DockerProperties();

    return DockerClientBuilder.getInstance("tcp://" + properties.getDockerHost() + ":" + properties.getDockerPort()).build();
  }
}
