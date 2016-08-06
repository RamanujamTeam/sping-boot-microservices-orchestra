package in.ramanujam.elasticsearch.aggregator;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import in.ramanujam.common.model.MinerRecord;
import in.ramanujam.common.properties.ElasticSearchProperties;
import org.elasticsearch.action.admin.indices.flush.FlushResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ElasticSearchAggregator {
    private static final Logger log = LoggerFactory.getLogger(ElasticSearchAggregator.class);
    private static int writeCount = 0;
    private static int removeCount = 0;
    private Client client;
    private String index;
    private String type;

    private ElasticSearchProperties elasticProps;

    @Autowired
    private ElasticSearchAggregator(ElasticSearchProperties elasticProps) {
        this.elasticProps = elasticProps;
        index = elasticProps.getElasticsearchIndexName();
        type = elasticProps.getElasticsearchTypeName();
        try {
            client = TransportClient.builder().build()
                    .addTransportAddress(new InetSocketTransportAddress(
                            InetAddress.getByName(elasticProps.getElasticsearchContainerHost()),
                            elasticProps.getElasticsearchContainerExternalPort()));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public List<MinerRecord> retrieveRecords(int size) {
        QueryBuilder qb = QueryBuilders.matchAllQuery();
        SearchResponse response;
        List<MinerRecord> minerRecords = new ArrayList<>();
        try {
            response = client.prepareSearch(index)
                    .setQuery(qb)
                    .setSize(size)
                    .execute().actionGet();

        } catch (IndexNotFoundException e) {
            return new ArrayList<>();
        }
        for (SearchHit hit : response.getHits().hits()) {
            minerRecords.add(new MinerRecord(hit.getSource()));
        }
        return minerRecords;
    }

    public void removeRecord(MinerRecord record) {
        try {
            client.prepareDelete(index, type, String.valueOf(record.getId()))
                    .execute()
                    .actionGet();

            flushElasticSearch(client, index);
        } catch (IndexNotFoundException e) {
            return;
        }
        log.info("ElasticSearchToMongo :: Removed from ElasticSearch :: Id = " + record.getId() + " count = " + ++removeCount);
    }

    public void moveRecordFromElasticSearchToMongo(MinerRecord minerRecord, DBCollection collection) {
        try {
            writeESRecordToMongo(minerRecord, collection);
            removeRecord(minerRecord);
        } catch (Exception e) {
        }
    }

    private WriteResult writeESRecordToMongo(MinerRecord esRecord, DBCollection collection) {
        BasicDBObject document = new BasicDBObject();

        BasicDBObject values = new BasicDBObject()
                .append("gender", esRecord.getGender())
                .append("first_name", esRecord.getFirstName())
                .append("last_name", esRecord.getLastName())
                .append("email", esRecord.getEmail())
                .append("ip_address", esRecord.getIpAddress());

        document.append("$set", values);

        BasicDBObject searchQuery = new BasicDBObject().append("_id", esRecord.getId());
        WriteResult result = collection.update(searchQuery, document, true, false);
        log.info("ElasticSearchToMongo :: Wrote to Mongo :: Id = " + esRecord.getId() + " count = " + ++writeCount);
        return result;
    }

    public Boolean isElasticSearchFillerFinished() {
        try {
            SearchResponse response = client.prepareSearch(index)
                    .setTypes(elasticProps.getElasticsearchIsFinishedKey()).setFrom(0).setSize(1)
                    .execute().actionGet();
            return (Boolean) response.getHits().getAt(0).getSource().get(elasticProps.getElasticsearchIsFinishedKey());
        } catch (IndexNotFoundException e) {
            return false;
        }
    }

    private void flushElasticSearch(Client client, String index) {
        FlushResponse flushResponse = client.admin().indices().flush(Requests.flushRequest(index)).actionGet();
        int failedShards = flushResponse.getFailedShards();
        if (failedShards > 0)
            throw new RuntimeException("Failed shards - " + failedShards);
    }
}