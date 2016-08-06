package in.ramanujam.common.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MongoDBProperties{

    @Value("${mongo.host}")
    private String mongoHost;

    @Value("${mongo.port}")
    private int mongoPort;

    @Value("${mongo.db}")
    private String mongoDb;

    @Value("${mongo.collection}")
    private String mongoCollection;

    public String getMongoHost() {
        return mongoHost;
    }

    public int getMongoPort() {
        return mongoPort;
    }

    public String getMongoDb() {
        return mongoDb;
    }

    public String getMongoCollection() {
        return mongoCollection;
    }
}