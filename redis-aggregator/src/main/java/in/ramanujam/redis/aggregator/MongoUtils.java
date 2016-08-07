package in.ramanujam.redis.aggregator;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import in.ramanujam.common.properties.MongoDBProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;

@Component
public class MongoUtils {
    private static DBCollection collection;

    @Autowired
    private MongoDBProperties mongoProps;

    private DBCollection setupCollection() throws UnknownHostException {
        MongoClient mongo = new MongoClient(mongoProps.getMongoHost(), mongoProps.getMongoPort());
        mongo.setWriteConcern(WriteConcern.SAFE);
        DB db = mongo.getDB(mongoProps.getMongoDb());

        return db.getCollection(mongoProps.getMongoCollection());
    }

    public DBCollection getCollection() throws UnknownHostException {
        if (collection != null)
            return collection;
        else return collection = setupCollection();
    }
}