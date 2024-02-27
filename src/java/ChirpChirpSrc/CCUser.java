/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ChirpChirpSrc;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import org.bson.Document;
/**
 * Interfaccia che estende User e definisce i metodi register, sendMessage, followUser, getPosts, getFollowingPosts e
 * getUserInfo
 * @author Daniele
 */
public interface CCUser extends User {
    // Metodo per effettuare la registrazione dell'utente
    public static int register(String username, String password, String email, Date birthdate)
    {
        // Ottieni l'unica istanza di "UserDB"
        SQLDatabase db= UserDB.getIstance("CCAdmin", "admin123");
        // Query per verificare se all'interno del database ci sia un utente con lo stesso username del nuovo arrivato
        String query1= "SELECT username FROM utente WHERE username= ?";
        // Query per inserire i dati del nuovo arrivato nel database
        String query2= "INSERT INTO utente(username, password, data_nascita, data_iscrizione, permessi, email) VALUES(?, ?, ?, ?, ?, ?)";
        // Necessaria per impostare le query e i loro parametri
        PreparedStatement stm= null;
        // Variabile per memorizzare i risultati delle query
        ResultSet result= null;
        int i;
        int statusCode= 0;
        // Data attuale
        java.sql.Date currentDate= java.sql.Date.valueOf(LocalDate.now());
        try{
            db.connect();   // Connessione al database
            /* Ottieni l'oggetto di tipo PreparedStatement impostando come query quella presente in "query1" 
            e memorizzalo in "stm" */
            stm= db.getStatement(query1); 
            stm.setString(1, username); // Imposta i parametri della query precedente
            result= db.executeStatement(stm);   // Esegui la query e memorizza i risultati in "result"
            if(result.isBeforeFirst())  // Se la query precedente restituisce un risultato...
                throw new AlreadyExistingUsernameException();   // Lancia la seguente eccezione
            /* Stessa cosa effettuata precedentemente, impostando come query però quella presente in "query2" */
            stm= db.getStatement(query2);
            // Imposta i parametri della query precedente
            stm.setString(1, username);
            stm.setString(2, password);
            stm.setDate(3, birthdate);
            stm.setDate(4, currentDate);
            stm.setString(5,"G");
            stm.setString(6, email);
            // Esegui la query precedente
            i= db.insertOrUpdate(stm);
            System.out.println("Registrazione utente effettuata!");

        }
        catch(SQLException e) {
            System.out.println("Si e' verificato un errore: /n" + e.getErrorCode() + " - " + e.getMessage());
            statusCode= 1;
        }
        catch(AlreadyExistingUsernameException e) {
            System.out.println(e.getMessage());
            statusCode= 2;
        }
        catch(Exception e) {
            System.out.println("Si e' verificato un errore: " + e.getMessage());
            statusCode= 3;
        }
        finally {
            db.closeEverything(stm, result);
            return statusCode;
        }
        
    }
    public abstract int sendMessage(CCMessage message);
    public abstract int followUser(String username);
    public abstract ArrayList getPosts();
    public abstract ArrayList getFollowingPosts();
    public abstract ArrayList getUserInfo();

}
