package org.example.database;

import java.util.List;
import java.util.Set;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import dev.morphia.Datastore;
import dev.morphia.Morphia;
import io.github.cdimascio.dotenv.Dotenv;

public class Database {
  private static Database instance; // Singleton instance
  private final Datastore datastore;

  /**
   * Private constructor to initialize the database connection
   */
  private Database() {
    // Load environment variables from .env file
    var dotenv = Dotenv.load();

    // Get the MONGO_URI from .env file
    String mongoUri = dotenv.get("MONGO_URI");
    // Connect to MongoDB
    MongoClient mongoClient = MongoClients.create(
        MongoClientSettings.builder()
            .applyToClusterSettings(builder -> builder.hosts(List.of(new ServerAddress("localhost", 27017))))
            .applyConnectionString(new com.mongodb.ConnectionString(mongoUri))
            .build());

    // Initialize Morphia
    datastore = Morphia.createDatastore(mongoClient, "learn_javalin");

    // Register all models (add new models here)
    mapModels(Set.of(
        org.example.models.User.class
    // Add more models here as needed
    ));

    // Create indexes automatically
    datastore.ensureIndexes();
  }

  /**
   * Get the single instance of the Database class (Singleton pattern)
   * 
   * @return Database instance
   */
  public static synchronized Database getInstance() {
    if (instance == null) {
      instance = new Database();
    }
    return instance;
  }

  /**
   * Get the Datastore (Morphia) instance
   * 
   * @return Datastore
   */
  public Datastore getDatastore() {
    return datastore;
  }

  /**
   * Maps multiple models to Morphia.
   * 
   * @param modelClasses Set of model classes to map
   */
  private void mapModels(Set<Class<?>> modelClasses) {
    modelClasses.forEach(modelClass -> {
      datastore.getMapper().map(modelClass);
    });
  }
}
