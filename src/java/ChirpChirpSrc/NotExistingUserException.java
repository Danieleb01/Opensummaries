/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChirpChirpSrc;
/**
 * Eccezione che segnala un utente non trovato
 * @author Daniele
 */
public class NotExistingUserException extends Exception{
    public NotExistingUserException()
    {
        super("Utente non trovato. Correggi l'username");
    }
    
}
