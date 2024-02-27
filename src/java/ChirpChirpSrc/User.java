/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ChirpChirpSrc;
import jakarta.servlet.http.HttpSession;


/**
 * Interfaccia che rappresenta un generico utente. I metodi definiti sono login e logout
 * @author Daniele
 */
public interface User {
    public abstract int logIn(String password);    
    default public void logOut(HttpSession session)
    {
        session.invalidate();
    }
}
