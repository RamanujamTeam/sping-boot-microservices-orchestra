package in.ramanujam.common.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ElasticSearchProperties {

    @Value("${elasticsearch.container.name}")
    private String elasticSearchContainerName;

    @Value("${elasticsearch.container.host}")
    private String elasticSearchContainerHost;

    @Value("${elasticsearch.container.port}")
    private int elasticSearchContainerPort;

    @Value("${elasticsearch.container.externalPort}")
    private int elasticSearchExternalPort;

    @Value("${elasticsearch.index.name}")
    private String elasticSearchIndexName;

    @Value("${elasticsearch.type.name}")
    private String elasticSearchTypeName;

    @Value("${elasticsearch.isfinished.key}")
    private String elasticSearchIsFinishedKey;

    @Value("${elasticsearch.tomongo.finished.key}")
    private String elasticSearchToMongoIsFinishedKey;

    public String getElasticsearchContainerName() {
        return elasticSearchContainerName;
    }

    public String getElasticsearchContainerHost() {
        return elasticSearchContainerHost;
    }

    public int getElasticsearchContainerPort() {
        return elasticSearchContainerPort;
    }

    public int getElasticsearchContainerExternalPort() {
        return elasticSearchExternalPort;
    }

    public String getElasticsearchIndexName() {
        return elasticSearchIndexName;
    }

    public String getElasticsearchTypeName() {
        return elasticSearchTypeName;
    }

    public String getElasticsearchIsFinishedKey() {
        return elasticSearchIsFinishedKey;
    }

    public String getElasticsearchToMongoIsFinishedKey() {
        return elasticSearchToMongoIsFinishedKey;
    }
}