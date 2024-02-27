/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ChirpChirpSrc;
import java.util.ArrayList;

/**
 * Interfaccia che estende User e definisce i metodi per effettuare le operazioni destinate all'amministratore
 * @author Daniele
 */
public interface CCAdmin extends User {
    public abstract ArrayList categoriseMsgsByHashtags();
    public abstract ArrayList getMsgsWithWord(String word);
    public abstract ArrayList getUserList();
    
}
