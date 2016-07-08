package in.ramanujam.services.starter;

import in.ramanujam.common.properties.AbstractProperties;

/**
 * Created with IntelliJ IDEA.
 * User: Denys Konakhevych
 * Date: 30.06.2016
 * Time: 23:21
 */
public class DockerProperties extends AbstractProperties
{
  private static final String DOCKER_CERT_PATH = "docker.cert.path";
  private static final String DOCKER_HOST = "docker.host";
  private static final String DOCKER_PORT = "docker.port";
  private static final String DOCKER_TLS_VERIFY = "docker.tls.verify";
  private static final String DOCKER_API_VERSION = "docker.apiVersion";
  private static final String CONFIG_FILE = "docker.properties";

  public DockerProperties()
  {
    load();
  }

  public String getDockerCertPath()
  {
    return getAppProps().getProperty( DOCKER_CERT_PATH );
  }

  public String getDockerHost()
  {
    return getAppProps().getProperty( DOCKER_HOST );
  }

  public String getDockerPort()
  {
    return getAppProps().getProperty( DOCKER_PORT );
  }

  public Boolean getDockerTlsVerify()
  {
    return Boolean.parseBoolean( getAppProps().getProperty( DOCKER_TLS_VERIFY ) );
  }

  public String getDockerApiVersion()
  {
    return getAppProps().getProperty( DOCKER_API_VERSION );
  }

  @Override
  protected String getFile()
  {
    return CONFIG_FILE;
  }
}
