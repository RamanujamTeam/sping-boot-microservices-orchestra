package in.ramanujam.common.properties;

/**
 * Created with IntelliJ IDEA.
 * User: Denys Konakhevych
 * Date: 30.06.2016
 * Time: 23:21
 */
public class RedisProperties extends AbstractProperties
{
  private static final String CONTAINER_REDIS_NAME = "redis.container.name";
  private static final String CONTAINER_REDIS_HOST = "redis.container.host";
  private static final String CONTAINER_REDIS_PORT = "redis.container.port";
  private static final String CONTAINER_REDIS_EXTERNAL_PORT = "redis.container.externalPort";
  private static final String REDIS_HASHSET_NAME = "redis.hashset.name";
  private static final String CONFIG_FILE = "redis.properties";

  public RedisProperties()
  {
    load();
  }

  public String getRedisContainerName()
  {
    return getAppProps().getProperty( CONTAINER_REDIS_NAME );
  }

  public String getRedisContainerHost()
  {
    return getAppProps().getProperty( CONTAINER_REDIS_HOST );
  }

  public Integer getRedisContainerPort()
  {
    return Integer.parseInt( getAppProps().getProperty( CONTAINER_REDIS_PORT ) );
  }

  public Integer getRedisContainerExternalPort()
  {
    return Integer.parseInt( getAppProps().getProperty( CONTAINER_REDIS_EXTERNAL_PORT ) );
  }

  public String getRedisHashsetName()
  {
    return getAppProps().getProperty( REDIS_HASHSET_NAME );
  }

  @Override
  protected String getFile()
  {
    return CONFIG_FILE;
  }
}
