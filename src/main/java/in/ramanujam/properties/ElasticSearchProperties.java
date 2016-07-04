package in.ramanujam.properties;

/**
 * Created with IntelliJ IDEA.
 * User: Denys Konakhevych
 * Date: 30.06.2016
 * Time: 23:21
 */
public class ElasticSearchProperties extends AbstractProperties
{

  private static final String ELASTICSEARCH_CONTAINER_NAME = "elasticsearch.container.name";
  private static final String ELASTICSEARCH_CONTAINER_HOST = "elasticsearch.container.host";
  private static final String ELASTICSEARCH_CONTAINER_PORT = "elasticsearch.container.port";
  private static final String ELASTICSEARCH_CONTAINER_EXTERNAL_PORT = "elasticsearch.container.externalPort";
  private static final String CONFIG_FILE = "config.properties";

  public ElasticSearchProperties()
  {
    load();
  }

  public String getElasticsearchContainerName()
  {
    return getAppProps().getProperty( ELASTICSEARCH_CONTAINER_NAME );
  }

  public String getElasticsearchContainerHost()
  {
    return getAppProps().getProperty( ELASTICSEARCH_CONTAINER_HOST );
  }

  public Integer getElasticsearchContainerPort()
  {
    return Integer.parseInt( getAppProps().getProperty( ELASTICSEARCH_CONTAINER_PORT ) );
  }

  public Integer getElasticsearchContainerExternalPort()
  {
    return Integer.parseInt( getAppProps().getProperty( ELASTICSEARCH_CONTAINER_EXTERNAL_PORT ) );
  }

  @Override
  protected String getFile()
  {
    return CONFIG_FILE;
  }
}
