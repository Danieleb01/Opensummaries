/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChirpChirpSrc;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import static com.mongodb.client.model.Sorts.descending;
import java.util.ArrayList;
import org.bson.Document;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;

/**
 * Implementazione dell'interfaccia CCAdmin
 * @author Daniele
 */
public class CCAdminImpl implements CCAdmin {
    private String username;
    
    public CCAdminImpl(String username)
    {
        this.username= username;
    }
    
    // Metodo per effettuare il login
    @Override
    public int logIn(String password)
    {
        SQLDatabase db= UserDB.getIstance("CCAdmin", "admin123");
        // Query per ottenere la password dell'utente che sta effettuando l'accesso
        String query= "SELECT password, permessi FROM utente WHERE username= ?";
        PreparedStatement stm= null;
        ResultSet result= null;
        String correctPwd= null;    // Var. per memorizzare la password corretta
        String userType= null;
        int statusCode= 0;  // Codice di stato per l'esito
        try 
        {
                db.connect();   // Connessione al database
                // Ottieni l'oggetto PreparedStatement impostandolo con la query opportuna
                stm= db.getStatement(query);
                stm.setString(1, username); // Imposta parametri query
                result= db.executeStatement(stm);   // Esegui query e memorizza il risultato nella variabile 'result'
                if(!result.isBeforeFirst()) // Se però non è stata restituita nessuna riga...
                {
                    throw new NotExistingUserException();   // Lancia eccezione
                }
                else if(result.next())  // Altrimenti estrai dalla colonna 'password' il suo valore
                {
                    userType= result.getString("permessi"); // Estrai informazione sul tipo di utente
                    if(!(userType.equals("A"))) // Se non è un admin...
                        throw new NotAnAdminException();    // Accesso negato!
                    correctPwd= result.getString("password");
                    if(correctPwd.equals(password)) // Se la password è corretta...
                    {
                        statusCode= 1;  // Login effettuato con successo
                    }
                    else
                    {
                        statusCode= 2;  // Password errata altrimenti
                    }
                }
            }
        // Eccezione che si verifica se l'username inserito non viene trovato
        catch(NotExistingUserException e) 
        {
                System.out.println(e.getMessage());
                statusCode= 3;
        }
        // Eccezione per gli errori relativi al database
        catch(SQLException e) 
        {
                System.out.println("Si e' verificato un errore: " + e.getErrorCode() + " - " + e.getMessage());
                statusCode= 4;
        }
        catch(NotAnAdminException e)
        {
            System.out.println(e.getMessage());
            statusCode= 5;
        }
        // Eccezioni generiche
        catch (Exception ex) 
        {
            System.out.println("Si e' verificato un errore: " + ex.getMessage());
            statusCode= 6;
        }
        finally
        {
            // Chiusura elementi relativi al database, compresa connessione
            db.closeEverything(stm, result);
            // Restituisci il codice di esito
            return statusCode;            
        }
    }
 
    // Metodo per ottenere i post categorizzati in base agli hashtags
    @Override
    public ArrayList categoriseMsgsByHashtags()
    {
        NoSQLDatabase db= PostsDB.getIstance();
        // Lista degli hashtag
        ArrayList<String> hashtags;
        // Array risultante
        ArrayList finalResult= new ArrayList();
        // Array per memorizzare i post di un certo hashtag
        ArrayList<Document> queryResults;
        // Var oggetto utilizzata per effettuare le query (con campo 'hashtag' inizializzato a null
        Document query= new Document().append("hashtags", null);
        // Var. oggetto per impostare il documento contenente l'hashtag e i post relativi
        Document hashtagRelatedPosts;
        // Hashtag corrente
        String currentHashtag;
        int statusCode= 0;
        try
        {
            db.connect();   // Connessione al database
            // Ottieni dal database gli hashtags (senza duplicati)
            hashtags= db.getDistinctStringValues("Posts", "hashtags");
            for(int i=0; i<hashtags.size(); i++)    // Per ogni hashtag...
            {
                currentHashtag= hashtags.get(i); // Recupera l'hashtag attuale
                System.out.println("Hashtag attuale: " + currentHashtag);
                // Se l'hashtag non è una stringa vuota (esiste quindi un messaggio senza hashtag)s
                if(!currentHashtag.equals(""))
                {
                     // Crea un nuovo documento e memorizzalo nella seguente var.
                    hashtagRelatedPosts= new Document();
                    // Inserisci l'hashtag i cui post fanno riferimento
                    hashtagRelatedPosts.append("Hashtag", currentHashtag);
                    // Imposta la query al hashtag corrente
                    query.replace("hashtags", currentHashtag);
                    // Trova i post
                    queryResults= db.find("Posts", query);
                    // Inserisci i risultati nel documento contenuto in hashtagRelatedPosts
                    hashtagRelatedPosts.append("Posts", queryResults);
                    // Inseriscilo nell'array risultante
                    finalResult.add(hashtagRelatedPosts);
                }
            }
        }
        catch(Exception e)  // Gestisci eccezioni
        {
            System.out.println("Errore verificato: " + e.getMessage());
            statusCode= 1;
        }
        finally
        {
            db.closeSafely();   // Chiudi connessione database
            finalResult.add(statusCode);    // Inserisci codice di stato nell'array risultante
            return finalResult;
        }
        
    }
    
    // Metodo per cercare i post che contengono la parola inserita dall'utente
    @Override
    public ArrayList getMsgsWithWord(String word)
    {
        NoSQLDatabase db= PostsDB.getIstance();
        Document currentDoc;
        // Testo del messaggio
        String content;
        // Array risultante
        ArrayList finalResult= new ArrayList();
        // Codice di esito
        int statusCode= 0;
        try
        {
            db.connect();   // Connessione al database
            // Recupera tutti i documenti dalla collezioneWi
            ArrayList<Document> results= db.find("Posts");
            for(int i=0; i<results.size(); i++) // Per ogni post...
            {
                // Ricava il documento attuale
                currentDoc= results.get(i);
                // Estrai il contenuto del messaggio
                content= currentDoc.getString("body");
                if(content.contains(word))  // Se presenta la parola chiave inserita dall'utente...
                    finalResult.add(currentDoc);    // Aggiungi il doc. attuale all'array risultate
            }
        }
        catch(Exception e) // Gestisci eccezioni
        {
            System.out.println("Errore: " + e.getMessage());
            statusCode= 1;
        }
        finally
        {
            db.closeSafely();   // Chiudi connessione al database
            finalResult.add(statusCode);    // Aggiungi il codice di esito all'array
            return finalResult; // Restituisci l'array risultante
        }
    }
    
    // Metodo per ottenere la lista di utenti e il numero di post pubblicati
    @Override
    public ArrayList getUserList()
    {
        // Ottieni istanza di PostsDB per accedere al database MongoDB
        NoSQLDatabase noSqlDB= PostsDB.getIstance();
        /* Array per inserire oggetti di tipo Bson contenenti le impostazioni per aggregare i risultati
        delle query MongoDB */
        ArrayList<Bson> optionsPipeline= new ArrayList<Bson>();
        // Array per i risultati effettivi
        ArrayList results= null;
        // Codice di esito
        int statusCode= 0;
        // Imposta gli oggetti Bson con le impostazioni per contare il n. di post e aggregare in base agli utenti
        Bson groupSettings= Aggregates.group("$Author", Accumulators.sum("numero_post", 1));
        Bson sortSettings= Aggregates.sort(descending("numero_post"));
        /* Aggiungi le impostazioni nell'array  */
        optionsPipeline.add(groupSettings);
        optionsPipeline.add(sortSettings);
        try
        {
            noSqlDB.connect();  // Connessione al db
            // Ottieni i risultati passando il nome della collezione e le impostazioni di aggregazione
            results= noSqlDB.getAggregatedResults("Posts", optionsPipeline);
        }
        catch(Exception e)  // Cattura eccezioni
        {
            System.out.println("Errore: " + e.getMessage());
            statusCode= 1;
        }
        finally
        {
            noSqlDB.closeSafely();  // Chiudi connessione
            results.add(statusCode);    // Aggiungi il codice di esito ai risultati
            return results;
        }
    }
}
