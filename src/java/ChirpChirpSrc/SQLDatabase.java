/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ChirpChirpSrc;
import java.sql.*;

/**
 * Interfaccia che estende Database e definisce metodi che effettuano operazioni su database di tipo sql
 * @author Daniele
 */
public interface SQLDatabase extends Database {
    public abstract PreparedStatement getStatement(String SQLStatement) throws Exception;
    public abstract ResultSet executeStatement(PreparedStatement stm) throws Exception;
    public abstract int insertOrUpdate(PreparedStatement stm) throws Exception;
    public abstract void closeEverything(PreparedStatement stm, ResultSet result);
    public abstract void closeEverything(PreparedStatement stm);
}
