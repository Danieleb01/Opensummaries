/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChirpChirpSrc;
import java.sql.*;
import oracle.jdbc.OracleDriver;

/**
 * CLasse che implementa l'interfaccia SQLDatabase
 * @author Daniele
 */
public class UserDB implements SQLDatabase {
    private Connection con;
    private static UserDB istance;  // Variabile per memorizzare l'unica istanza di UserDB
    private final String dbUrl= "jdbc:oracle:thin:@localhost:1521/XEPDB1";  // URL per la connessione al DB
    private static String user;
    private static String password;    
    
    // Costruttore che inizializza semplicemente l'user e la password per connettersi al db
    private UserDB(String user, String pass)
    {
        this.user= user;
        this.password= pass;
    }
    
    // Metodo per ottenere l'unica istanza di UserDB (Singleton)
    public static UserDB getIstance(String user, String pass)
    {
        if(istance == null) // Se non esiste ancora una istanza di UserDB...
        {
            istance= new UserDB(user, pass);    // Creala sul momento e memorizzala in istance
        }
        return istance; // Restituisci il contenuto di istance
    }
    
    // Metodo per connettersi al database
    @Override
    public void connect()throws SQLException
    {
        DriverManager.registerDriver(new OracleDriver());   // Carica il driver JDBC
        con= DriverManager.getConnection(dbUrl, user, password);    // Ottieni la connessione al database
        System.out.println("Connessione effettuata al database Oracle");
    }
    
    // Metodo per chiudere la connessione al db
    @Override
    public void close() throws SQLException
    {
        con.close();
        System.out.println("Connessione chiusa");
    }
    
    // Metodo per ottenere un oggetto di tipo PreparedStatement da impostare poi opportunamente
    @Override
    public PreparedStatement getStatement(String SQLStatement) throws Exception
    {
        PreparedStatement stm= con.prepareStatement(SQLStatement);
        return stm;
    }
    
    // Esegui query SQL 'SELECT'
    @Override
    public ResultSet executeStatement(PreparedStatement stm) throws Exception
    {
        ResultSet result;
        result= stm.executeQuery(); // Memorizza i risultati in result
        return result;  // Restituisci i risultati
    }
    
    // Esegui query SQL 'INSERT' o 'UPDATE'
    @Override
    public int insertOrUpdate(PreparedStatement stm) throws Exception
    {
        return stm.executeUpdate();
    }
    
    // Metodo per chiudere la connessione e tutti gli elementi utilizzati in modo sicuro
    @Override
    public void closeEverything(PreparedStatement stm, ResultSet result)
    {
        try // Chiusura elementi gestendo eventuali eccezioni
        {
            stm.close();
            result.close();
            close();
        }
        catch(SQLException e)
        {
            System.out.println("Errore nella chiusura connessione: " + e.getErrorCode() + " - " + e.getMessage());
        }
    }
    
    // Metodo per chiudere la connessione e un statement in modo sicuro
    @Override
    public void closeEverything(PreparedStatement stm)
    {
        try 
        {
            stm.close();
            close();
        }
        catch(SQLException e)
        {
            System.out.println("Errore nella chiusura connessione: " + e.getErrorCode() + " - " + e.getMessage());
        }
    }
}
