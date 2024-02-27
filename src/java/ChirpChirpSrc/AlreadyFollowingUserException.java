/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChirpChirpSrc;
/**
 *  Eccezione che segnala un utente già seguito.
 * @author Daniele
 */
public class AlreadyFollowingUserException extends Exception{
    public AlreadyFollowingUserException()
    {
        super("Stai già seguendo quest'utente");
    }
}
