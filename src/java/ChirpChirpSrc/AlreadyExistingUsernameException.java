/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChirpChirpSrc;

/**
 * Eccezione nel caso in cui il nome utente esista già.
 * @author Daniele
 */
public class AlreadyExistingUsernameException extends Exception {
    public AlreadyExistingUsernameException()
    {
        super("L'username inserito e' gia' esistente! Digitane un altro");
    }
    
}
