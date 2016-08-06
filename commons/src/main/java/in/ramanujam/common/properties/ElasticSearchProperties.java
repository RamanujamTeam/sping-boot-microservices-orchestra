package in.ramanujam.common.properties;

import org.springframework.stereotype.Component;

@Component
public class ElasticSearchProperties extends AbstractProperties {

    private static final String ELASTICSEARCH_CONTAINER_NAME = "elasticsearch.container.name";
    private static final String ELASTICSEARCH_CONTAINER_HOST = "elasticsearch.container.host";
    private static final String ELASTICSEARCH_CONTAINER_PORT = "elasticsearch.container.port";
    private static final String ELASTICSEARCH_CONTAINER_EXTERNAL_PORT = "elasticsearch.container.externalPort";
    private static final String ELASTICSEARCH_INDEX_NAME = "elasticsearch.index.name";
    private static final String ELASTICSEARCH_TYPE_NAME = "elasticsearch.type.name";
    private static final String ELASTICSEARCH_IS_FINISHED_KEY = "elasticsearch.isfinished.key";
    private static final String ELASTICSEARCH_TO_MONGO_IS_FINISHED_KEY = "elasticsearch.tomongo.finished.key";
    private static final String CONFIG_FILE = "elasticsearch.properties";

    private static ElasticSearchProperties instance = new ElasticSearchProperties();

    public ElasticSearchProperties() {
        load();
    }

    public static ElasticSearchProperties getInstance() {
        return instance;
    }

    public String getElasticsearchContainerName() {
        return getAppProps().getProperty(ELASTICSEARCH_CONTAINER_NAME);
    }

    public String getElasticsearchContainerHost() {
        return getAppProps().getProperty(ELASTICSEARCH_CONTAINER_HOST);
    }

    public Integer getElasticsearchContainerPort() {
        return Integer.parseInt(getAppProps().getProperty(ELASTICSEARCH_CONTAINER_PORT));
    }

    public Integer getElasticsearchContainerExternalPort() {
        return Integer.parseInt(getAppProps().getProperty(ELASTICSEARCH_CONTAINER_EXTERNAL_PORT));
    }

    public String getElasticsearchIndexName() {
        return getAppProps().getProperty(ELASTICSEARCH_INDEX_NAME);
    }

    public String getElasticsearchTypeName() {
        return getAppProps().getProperty(ELASTICSEARCH_TYPE_NAME);
    }

    public String getElasticsearchIsFinishedKey() {
        return getAppProps().getProperty(ELASTICSEARCH_IS_FINISHED_KEY);
    }

    public String getElasticsearchToMongoIsFinishedKey() {
        return getAppProps().getProperty(ELASTICSEARCH_TO_MONGO_IS_FINISHED_KEY);
    }

    @Override
    protected String getFile() {
        return CONFIG_FILE;
    }
}
