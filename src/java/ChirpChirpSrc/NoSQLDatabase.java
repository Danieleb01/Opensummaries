/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ChirpChirpSrc;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.ArrayList;
import java.util.List;
/**
 * Interfaccia che estende Database e definisce metodi che effettuano operazioni su database di tipo noSQL
 * @author Daniele
 */
public interface NoSQLDatabase extends Database {
    public abstract void closeSafely();
    public abstract void insert(Document document, String collectionName) throws Exception;
    public abstract ArrayList<Document> find(String collectionName, Document query);
    public abstract ArrayList<Document> find(String collectionName);
    public abstract ArrayList<Document> getAggregatedResults(String collectionName, ArrayList<Bson> optionsPipeline) throws Exception;
    public abstract ArrayList getDistinctStringValues(String collectionName, String field);
}
