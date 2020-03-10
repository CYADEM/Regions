package dev.tinchx.regions.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;

public class MongoManager {

    private MongoClient client;
    @Getter
    private MongoCollection collection;

    public void load(MemorySection configuration) {
        if (configuration.getBoolean("DATABASE.MONGO.AUTHENTICATION.ENABLED")) {
            ServerAddress serverAddress = new ServerAddress(configuration.getString("DATABASE.MONGO.HOST"),
                    configuration.getInt("DATABASE.MONGO.PORT"));

            MongoCredential credential = MongoCredential.createCredential(
                    configuration.getString("DATABASE.MONGO.AUTHENTICATION.USER"), "admin",
                    configuration.getString("DATABASE.MONGO.AUTHENTICATION.PASSWORD").toCharArray());

            client = new MongoClient(serverAddress, credential, MongoClientOptions.builder().build());
        } else {
            client = new MongoClient(configuration.getString("DATABASE.MONGO.HOST"), configuration.getInt("DATABASE.MONGO.PORT"));
        }
        MongoDatabase database = client.getDatabase("Regions-" + Bukkit.getServerName().toUpperCase().replace(" ", ""));
        this.collection = database.getCollection("Regions");
    }

    public void close() {
        client.close();
    }
}