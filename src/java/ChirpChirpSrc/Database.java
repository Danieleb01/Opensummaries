/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ChirpChirpSrc;

/**
 * Interfaccia che definisce un generico database con i due metodi principali: connect e close 
 * @author Daniele
 */
public interface Database {
    public abstract void connect() throws Exception;
    public abstract void close() throws Exception;
}
