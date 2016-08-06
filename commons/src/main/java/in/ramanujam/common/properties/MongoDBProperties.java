package in.ramanujam.common.properties;

import org.springframework.stereotype.Component;

@Component
public class MongoDBProperties extends AbstractProperties {
    private static final String MONGO_HOST = "mongo.host";
    private static final String MONGO_PORT = "mongo.port";
    private static final String MONGO_DB = "mongo.db";
    private static final String MONGO_COLLECTION = "mongo.collection";
    private static final String CONFIG_FILE = "mongodb.properties";

    public MongoDBProperties() {
        load();
    }

    public String getMongoHost() {
        return getAppProps().getProperty(MONGO_HOST);
    }

    public Integer getMongoPort() {
        return Integer.parseInt(getAppProps().getProperty(MONGO_PORT));
    }

    public String getMongoDb() {
        return getAppProps().getProperty(MONGO_DB);
    }

    public String getMongoCollection() {
        return getAppProps().getProperty(MONGO_COLLECTION);
    }

    @Override
    protected String getFile() {
        return CONFIG_FILE;
    }
}
