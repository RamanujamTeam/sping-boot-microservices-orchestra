package in.ramanujam.common.properties;

public class RedisProperties extends AbstractProperties
{
  private static final String CONTAINER_REDIS_NAME = "redis.container.name"; // TODO: rename in REDIS_CONTAINER...
  private static final String CONTAINER_REDIS_HOST = "redis.container.host";
  private static final String CONTAINER_REDIS_PORT = "redis.container.port";
  private static final String CONTAINER_REDIS_EXTERNAL_PORT = "redis.container.externalPort";
  private static final String REDIS_HASHSET_NAME = "redis.hashset.name";
  private static final String REDIS_IS_FINISHED_KEY = "redis.isfinished.key";
  private static final String REDIS_TO_MONGO_IS_FINISHED_KEY = "redis.tomongo.isfinished.key";
  private static final String CONFIG_FILE = "redis.properties";

  private static RedisProperties instance = new RedisProperties();

  private RedisProperties()
  {
    load();
  }

  public static RedisProperties getInstance()
  {
    return instance;
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

  public String getRedisIsFinishedKey()
  {
    return getAppProps().getProperty( REDIS_IS_FINISHED_KEY );
  }

  public String getRedisToMongoIsFinishedKey()
  {
    return getAppProps().getProperty( REDIS_TO_MONGO_IS_FINISHED_KEY );
  }

  @Override
  protected String getFile()
  {
    return CONFIG_FILE;
  }
}
