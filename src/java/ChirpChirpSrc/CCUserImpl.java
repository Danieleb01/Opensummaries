/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChirpChirpSrc;
import java.sql.*;
import java.util.ArrayList;
import org.bson.Document;
import com.google.gson.Gson;


/**
 * Classe che implementa CCUser
 * @author Daniele
 */
public class CCUserImpl implements CCUser {
    private String username;    // Username dell'utente
    private Date signUpDate;    // Data di iscrizione
    private Date birthdate; // Data di nascita
    private boolean dataFound= false;   // Segnala se i dati precedenti sono stati reperiti
    
    public CCUserImpl(String username)  // Costruttore
    {
        this.username= username;
        loadUserInfo(); // Reperisci dal database i dati dell'utente
    }
    
    // Metodo per effettuare il login
    @Override
    public int logIn(String password)
    {
        SQLDatabase db= UserDB.getIstance("CCAdmin", "admin123");
        // Query per ottenere la password dell'utente che sta effettuando l'accesso
        String query= "SELECT password FROM utente WHERE username= ?";
        PreparedStatement stm= null;
        ResultSet result= null;
        String correctPwd= null;    // Var. per memorizzare la password corretta
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
                System.out.println("Utente non esistente o username scritto erroneamente");
                statusCode= 3;
        }
        // Eccezione per gli errori relativi al database
        catch(SQLException e) 
        {
            System.out.println("Si e' verificato un errore: " + e.getErrorCode() + " - " + e.getMessage());
            statusCode= 4;
        } 
        // Eccezioni generiche
        catch (Exception ex) 
        {
            System.out.println("Si e' verificato un errore: " + ex.getMessage());
            statusCode= 5;
        }
        finally
        {
            // Chiusura elementi relativi al database, compresa connessione
            db.closeEverything(stm, result);
            // Restituisci il codice di esito
            return statusCode;   
        }
    }

    // Metodo per reperire dal database i dati dell'utente (data di nascita e iscrizione)
    private void loadUserInfo()
    {
        SQLDatabase db= UserDB.getIstance("CCAdmin", "admin123");
        String query= "SELECT username, data_nascita, data_iscrizione FROM utente WHERE username= ?";
        boolean found= false;
        PreparedStatement stm= null;
        ResultSet result= null;
        try {
                db.connect();   // Connessione al database
                /* Ottieni lo statement impostandolo con la query opportuna, imposta i parametri della query e ottieni
                i risultati */
                stm= db.getStatement(query);
                stm.setString(1, this.username);
                result= db.executeStatement(stm);
                if(result.next())   // Condizione per esplorare il risultato della query
                {
                    // Estrai dal risultato la data di iscrizione e quella di nascita
                    this.signUpDate= result.getDate("data_iscrizione");
                    this.birthdate= result.getDate("data_nascita");
                }
                found= true;    // Segnala che i dati sono stati ottenuti
        }
        catch(SQLException e)   // Gestisci le varie eccezioni
        {
            System.out.println("Errore verificato nella inizializzazione della classe CCUser: " + e.getErrorCode() + " - " + e.getMessage() + "\n");
        }
        catch(Exception e)
        {
            System.out.println("Errore verificato nella inizializzazione della classe CCUser: " + e.getMessage() + "\n");
        }
        finally {
            db.closeEverything(stm, result);    // Chiudi tutto (statement, result e connessione)
            this.dataFound= found;
        }
    }
    
    // Metodo per postare un nuovo messaggio sulla piattaforma
    @Override
  public int sendMessage(CCMessage message)
    {
        // Ottieni l'istanza di PostsDB
        NoSQLDatabase db= PostsDB.getIstance();
        // Converti in JSON l'oggetto message e memorizzalo in document
        Gson gson= new Gson();
        String json= gson.toJson(message);
        Document document= Document.parse(json);
        document.append("Author", username);  // Aggiungi la voce riguardante l'autore del post
        int statusCode= 0;  // Inizializza la statusCode a 0
        try
        {
            db.connect();   // Connessione al database MongoDB
            db.insert(document, "Posts");   // Inserisci documento(il messaggio) nella collezione 'Posts'
        }
        catch(Exception e)  // Gestione eccezioni
        {
            System.out.println("Errore avvenuto: " + e.getMessage());
            statusCode= 1;
        }
        finally
        {
            db.closeSafely();
            return statusCode;
        }
    }

    // Metodo per seguire un altro utente
    @Override
    public int followUser(String username)
    {
        SQLDatabase db= UserDB.getIstance("CCAdmin", "admin123");
        // Query per ottenere username dell'utente che si vuole seguire
        String query1= "SELECT username FROM utente WHERE username= ?";
        // Query per vedere se l'utente segue già l'utene che vuole seguire
        String query2= "SELECT * FROM seguiti WHERE username= ? AND utente_seguito= ?";
        // Query per inserire dati nella tabella seguiti
        String query3= "INSERT INTO Seguiti VALUES(?,?)";
        PreparedStatement stm= null;
        ResultSet result= null;
        int i=0, statusCode=0;
        try
        {
            db.connect();   // Connetti al database
            // Ricava l'username dell'utente che si vuole seguire
            stm= db.getStatement(query1);
            stm.setString(1, username);
            result= db.executeStatement(stm);
            // Se la query non restituisce risultati...
            if(!result.isBeforeFirst())
                throw new NotExistingUserException();   // Lancia eccezione
            // Controlla se già si sta seguendo tale utente
            stm= db.getStatement(query2);
            stm.setString(1, this.username);
            stm.setString(2, username);
            result= db.executeStatement(stm);
            // Se la query restituisce risultati...
            if(result.isBeforeFirst())
                throw new AlreadyFollowingUserException();  // Lancia eccezione
            // Altrimenti inserisci dati nella tabella seguiti
            stm= db.getStatement(query3);
            stm.setString(1, this.username);
            stm.setString(2, username);
            i= db.insertOrUpdate(stm);
            System.out.println("Utente " + this.username + " ha seguito " + username);
        }
        catch(SQLException e)   // Gestione eccezioni
        {
            System.out.println("Si e' verificato un errore: " + e.getErrorCode() + " - " + e.getMessage());
            statusCode= 1;
        }
        catch(NotExistingUserException e) 
        {
                System.out.println("Utente non esistente o username scritto erroneamente");
                statusCode= 2;
        }
        catch(AlreadyFollowingUserException e)  // Eccezione che segnala un utente che si segue già
        {
                System.out.println("Utente " + this.username + " gia' segue " + username);
                statusCode= 3;
        }
        catch(Exception e)
        {
            System.out.println("Si e' verificato un errore: " + e.getMessage());
            statusCode= 4;
        }
        finally
        {
            db.closeEverything(stm);    // Chiudi connessione
            return statusCode;
        }
    }
    
    @Override
    public ArrayList getUserInfo()  // Metodo che restituisce le informazioni dell'utente
    {
        ArrayList info= new ArrayList();    // Crea array di oggetti
        if(dataFound)   // Se i dati sono stati caricati...
        {
            // Inserisci i dati all'interno dell'array
            info.add(this.username);
            info.add(this.signUpDate);
            info.add(this.birthdate);   
            info.add(0);    // Dati ottenuti correttamente
        }
        else    // Dati non presenti
        {
            info.add(1);    // Dati non presenti
        }
        return info;    // Restituisci l'array di dati
    }
    
    @Override
    public ArrayList getPosts()   // Metodo per ottenere i post dell'utente dal database
    {
        NoSQLDatabase db= PostsDB.getIstance(); // Ottieni unica istanza della classe PostsDB
        ArrayList posts= new ArrayList<Document>();   // Array di document risultante
        Document searchQuery= new Document();   // Oggetto di tipo document per impostare la query
        int statusCode= 0;  // Codice di esito
        // Imposta la query, in modo da trovare i post pubblicati dall'utente
        searchQuery.append("Author", username);
        try
        {
            db.connect();   // Connetti al database
            // Ottieni dalla collezione Posts i documenti in base alla query impostata
            posts= db.find("Posts", searchQuery);
            System.out.println("Post dell'utente: " + username + " reperiti;");
        }
        catch(Exception e)  // Gestisci eccezioni
        {
            System.out.println("Si e' verificato un errore: " + e.getMessage());
            statusCode= 1;
        }
        finally
        {
           db.closeSafely();    // Chiudi connessione al database
           posts.add(statusCode);   // Aggiungi il codice di stato alla fine dell'array
           return posts;    // Restituisci array
        }
    }
    
    @Override
    public ArrayList getFollowingPosts()
    {
        // Ricava le istanze per comunicare con i DB di Oracle e MongoDB
        SQLDatabase sqlDB= UserDB.getIstance("CCAdmin", "admin123");
        NoSQLDatabase noSQLDB= PostsDB.getIstance();
        // Query per ottenere gli username degli utenti seguiti
        String query= "SELECT utente_seguito FROM seguiti WHERE username= ?";
        // Elementi necessari per effettuare le query in Oracle
        PreparedStatement stm= null;
        ResultSet result= null;
        // Oggetto di tipo document per impostare la query di ricerca
        Document searchQuery= new Document();
        // Codice di esito
        int statusCode= 0;
        // Array di stringhe per memorizzare gli username
        ArrayList<String> userList= new ArrayList<String>();
        // Array che conterrà i post
        ArrayList posts= new ArrayList();
        try
        {
            sqlDB.connect();    // Connetti al database Oracle
            // Recupera i dati riguardanti gli utenti seguiti
            stm= sqlDB.getStatement(query);
            stm.setString(1, username);
            result= sqlDB.executeStatement(stm);
            while(result.next())
                userList.add(result.getString("utente_seguito"));
        }
        catch(SQLException e)   // Gestisci eccezioni
        {
            System.out.println("Si e' verificato un errore: " + e.getErrorCode() + " - " + e.getMessage());
            statusCode= 1;
        }
        catch(Exception e)
        {
            System.out.println("Si e' verificato un errore: " + e.getMessage());
            statusCode= 1;
        }
        finally
        {
            sqlDB.closeEverything(stm, result);
        }
        if(statusCode != 1) // Se fino ad ora non si sono verificati errori...
        {
            try
            {
                noSQLDB.connect();  // Connetti al database mongoDB
                // Per ogni utente seguito...
                for(int i=0; i<userList.size(); i++)
                {
                    // Ricava i suoi post in base al suo username
                    searchQuery.append("Author", userList.get(i));
                    posts.add(noSQLDB.find("Posts", searchQuery));
                }
            }
            catch(Exception e)  // Gestisci eccezioni
            {
                System.out.println("Si e' verificato un errore: " + e.getMessage());
                statusCode= 1;
            }
            finally
            {
                noSQLDB.closeSafely();  // Chiudi connessione a MongoDB
            }
        }
        posts.add(statusCode);  // Aggiungi codice di stato alla fine
        return posts;   // Restituisci array
    }
}
