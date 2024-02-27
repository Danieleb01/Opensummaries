/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChirpChirpSrc;
/**
 * Eccezione che segnala un tentativo di accesso all'area riservata agli amministratori da parte di un utente che non
 * ha tali permessi
 * @author Daniele
 */
public class NotAnAdminException extends Exception {
    public NotAnAdminException()
    {
        super("Un utente non amministratore ha tentato l'accesso come tale");
    }
    
}
