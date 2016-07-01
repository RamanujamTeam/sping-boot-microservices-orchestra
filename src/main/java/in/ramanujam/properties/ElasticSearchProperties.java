package in.ramanujam.properties;

/**
 * Created with IntelliJ IDEA.
 * User: Denys Konakhevych
 * Date: 30.06.2016
 * Time: 23:21
 */
public class ElasticSearchProperties extends AbstractProperties
{

  private static final String CONTAINER_ELASTICSEARCH_NAME = "container.elasticsearch.name";
  private static final String CONTAINER_ELASTICSEARCH_HOST = "container.elasticsearch.host";
  private static final String CONTAINER_ELASTICSEARCH_PORT = "container.elasticsearch.port";
  private static final String CONTAINER_ELASTICSEARCH_EXTERNAL_PORT = "container.elasticsearch.externalPort";
  private static final String CONFIG_FILE = "config.properties";

  public ElasticSearchProperties()
  {
    load();
  }

  public String getContainerElasticsearchName()
  {
    return getAppProps().getProperty( CONTAINER_ELASTICSEARCH_NAME );
  }

  public String getContainerElasticsearchHost()
  {
    return getAppProps().getProperty( CONTAINER_ELASTICSEARCH_HOST );
  }

  public Integer getContainerElasticsearchPort()
  {
    return Integer.parseInt( getAppProps().getProperty( CONTAINER_ELASTICSEARCH_PORT ) );
  }

  public Integer getContainerElasticsearchExternalPort()
  {
    return Integer.parseInt( getAppProps().getProperty( CONTAINER_ELASTICSEARCH_EXTERNAL_PORT ) );
  }

  @Override
  protected String getFile()
  {
    return CONFIG_FILE;
  }
}
