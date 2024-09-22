import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;

public class DataManagement {
    
    MongoClient mongoClient;
    MongoDatabase database;
    MongoCollection<Document> collection;
    
    DataManagement() {

        // Connect to MongoDB
        mongoClient = new MongoClient("localhost", 27017);
        database = mongoClient.getDatabase("Attendance");
        collection = database.getCollection("query");
    }
    
    public void addData(String date, String time, String name, String phone, String email, String sex, String sector, String purpose) {
        Document doc = new Document("Date", date)
                .append("Time", time)
                .append("Name", name)
                .append("Phone", phone)
                .append("Email", email)
                .append("Sex", sex)
                .append("Sector", sector)
                .append("Purpose", purpose);

        // Insert the new data entry into the MongoDB collection
        collection.insertOne(doc);
        System.out.println("Data added to MongoDB: " + doc.toJson());
    }

    public FindIterable<Document> getSortedDocuments() {
        // Sort documents by Date field in descending order
        return collection.find().sort(Sorts.descending("Date"));
    }
}
