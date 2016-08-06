package in.ramanujam.elasticsearch.aggregator;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import in.ramanujam.common.properties.MongoDBProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MongoUtils { // TODO: MongoUtils class is duplicated!
    private static DBCollection collection;

    @Autowired
    MongoDBProperties mongoProps;

    private DBCollection setupCollection() {
        MongoClient mongo = new MongoClient(mongoProps.getMongoHost(), mongoProps.getMongoPort());
        mongo.setWriteConcern(WriteConcern.SAFE);
        DB db = mongo.getDB(mongoProps.getMongoDb());

        return db.getCollection(mongoProps.getMongoCollection());
    }

    public DBCollection getCollection() {
        if (collection != null)
            return collection;
        else return collection = setupCollection();
    }
}
