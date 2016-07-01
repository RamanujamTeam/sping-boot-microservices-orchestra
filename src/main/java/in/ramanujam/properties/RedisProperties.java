package in.ramanujam.properties;

/**
 * Created with IntelliJ IDEA.
 * User: Denys Konakhevych
 * Date: 30.06.2016
 * Time: 23:21
 */
public class RedisProperties extends AbstractProperties
{

  private static final String CONTAINER_REDIS_NAME = "container.redis.name";
  private static final String CONTAINER_REDIS_HOST = "container.redis.host";
  private static final String CONTAINER_REDIS_PORT = "container.redis.port";
  private static final String CONTAINER_REDIS_EXTERNAL_PORT = "container.redis.externalPort";
  private static final String CONFIG_FILE = "config.properties";

  public RedisProperties()
  {
    load();
  }

  public String getContainerRedisName()
  {
    return getAppProps().getProperty( CONTAINER_REDIS_NAME );
  }

  public String getContainerRedisHost()
  {
    return getAppProps().getProperty( CONTAINER_REDIS_HOST );
  }

  public Integer getContainerRedisPort()
  {
    return Integer.parseInt( getAppProps().getProperty( CONTAINER_REDIS_PORT ) );
  }

  public Integer getContainerRedisExternalPort()
  {
    return Integer.parseInt( getAppProps().getProperty( CONTAINER_REDIS_EXTERNAL_PORT ) );
  }

  @Override
  protected String getFile()
  {
    return CONFIG_FILE;
  }
}
