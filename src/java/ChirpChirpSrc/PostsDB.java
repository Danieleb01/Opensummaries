/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChirpChirpSrc;
import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClients;
import com.mongodb.ServerApi;
import com.mongodb.MongoClientSettings;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.ArrayList;
import java.util.List;
/**
 * Classe che implementa l'interfaccia NoSQLDatabase
 * @author Daniele
 */
public class PostsDB implements NoSQLDatabase {
    private static PostsDB istance; // Attributo che memorizza l'unica istanza della classe
    // Stringa di connessione
    private String connectionString= "mongodb://localhost:27017";
    private MongoClient client;
    private MongoDatabase db;
    
    // Metodo che restituisce l'unica istanza della classe
    public static PostsDB getIstance()
    {
        if(istance == null)
            istance= new PostsDB();
        return istance;
    }
    
    @Override
    // Metodo per connettersi al database
    public void connect() throws Exception
    {
        // Crea oggetto di tipo ConnectionString e inizializzalo passando la stringa di connessione
        ConnectionString string= new ConnectionString(connectionString);
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();        
        // Settaggio delle impostazioni di connessione, includendo anche la stringa di connessione
        MongoClientSettings settings= MongoClientSettings.builder()
                .applyConnectionString(string)
                .serverApi(serverApi)
                .build();
        client= MongoClients.create(settings);  // Connessione al database
        db= client.getDatabase("ChirpChirp!");  // Reperimento del database
        System.out.println("Connessione a MongoDB stabilita");
    }
    
    // Metodo per chiudere la connessione
    @Override
    public void close() throws Exception
    {
        client.close();
        System.out.println("Chiusura connessione MongoDB effettuata");
    }
    
    // Metodo per chiudere la connessione in modo sicuro
    @Override
    public void closeSafely()
    {
        try // Gestisci le varie eccezioni
        {
            close();
        }
        catch(Exception e)
        {
            System.out.println("Errore avvenuto: " + e.getMessage());
        }
    }
    
    // Metodo per inserire un documento all'interno del database
    @Override
    public void insert(Document document, String collectionName) throws Exception
    {
        // Reperisci la collezione
        MongoCollection<Document> collection= db.getCollection(collectionName);
        collection.insertOne(document); // Inserisci documento nel database
        System.out.println("Documento inserito nel database");
    }
    
    // Trova documenti dalla collezione in base a una query impostata
    @Override
    public ArrayList<Document> find(String collectionName, Document query)
    {
        ArrayList<Document> results= new ArrayList<Document>(); // Array di risultati
        // Reperisci collezione 
        MongoCollection<Document> collection= db.getCollection(collectionName);
        // Memorizza i risultati nell'oggetto FindIterable
        FindIterable<Document> cursor= collection.find(query);
        // Copia i risultati nell'array risultante tramite il cursore
        try (final MongoCursor<Document> cursorIterator = cursor.cursor()) 
        {
            while(cursorIterator.hasNext()) // Effettua l'operazione fintanto ci sono altri risultati
            {
                results.add(cursorIterator.next());
            }
        }
        return results; // Restituisci i risultati
    }

    // Metodo per ottenere tutti i documenti da una collezione (logica simile al metodo precedente)
    @Override
    public ArrayList<Document> find(String collectionName)
    {
        ArrayList<Document> results= new ArrayList<Document>();
        MongoCollection<Document> collection= db.getCollection(collectionName);
        FindIterable<Document> cursor= collection.find();
        try (final MongoCursor<Document> cursorIterator = cursor.cursor()) 
        {
            while(cursorIterator.hasNext()) 
                results.add(cursorIterator.next());
        }
        return results;
    }

    /* Metodo per ottenere, da una collezione, dati aggregati in base a varie impostazioni passate in
     optionsPipeline */
    @Override
    public ArrayList<Document> getAggregatedResults(String collectionName, ArrayList<Bson> optionsPipeline)
    {
        ArrayList<Document> results= new ArrayList<Document>();
        MongoCollection<Document> collection= db.getCollection(collectionName);
        /* Aggrega i risultati in base alle impostazioni presenti in optionsPipeline e copiali nell'array
        risultante */
        collection.aggregate(optionsPipeline).forEach(doc -> results.add((Document) doc));
        return results; // Restituisci l'array risultante
    }
    
    /* Metodo per ottenere tutti i valori (distinti, cioè senza duplicati) di tipo stringa assunti da 
    un campo dei documenti di una collezione.  */
    @Override
    public ArrayList<String> getDistinctStringValues(String collectionName, String field)
    {
        ArrayList results= new ArrayList<String>();
        MongoCollection<Document> collection= db.getCollection(collectionName);
        // Ottieni la lista dei valori distinti specificando il campo e il tipo
        DistinctIterable<String> docs= collection.distinct(field, String.class);
        // Copia i risultati nell'array risultante
        try (final MongoCursor<String> cursorIterator= docs.iterator())
        {
            while(cursorIterator.hasNext())
                results.add(cursorIterator.next());
        }
        return results; // Restituisci l'array
    }
}
