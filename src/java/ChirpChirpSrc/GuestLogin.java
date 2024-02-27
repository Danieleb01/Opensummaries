/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package ChirpChirpSrc;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet che si occupa di effettuare l'autenticazione di un semplice utente
 * @author Daniele
 */
@WebServlet(name = "GuestLogin", urlPatterns = {"/GuestLogin"})
public class GuestLogin extends HttpServlet {

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param req servlet request
     * @param res servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        PrintWriter out= res.getWriter();   // Ottieni il writer dalla response (per mandare la risposta al client)
        String username= req.getParameter("username");  // Ricava il valore dal campo username
        String password= req.getParameter("password");  // Ricava il valore dal campo password
        UserFactory fc= new CCUserFactory();    // Creazione factory per ottenere l'utente
        CCUser user= (CCUser) fc.createUser(username);  // Crea utente tramite la factory
        int statusCode= 0;  // Codice di esito
        HttpSession session= req.getSession(); // Ottieni oggetto HttpSession
        res.setContentType("text/plain");   // Imposta tipo di contenuto della risposta
        res.setCharacterEncoding("UTF-8"); // Imposta la codifica caratteri
        statusCode= user.logIn(password);   // Effettua il login dell'utente e restituisci l'esito
        switch(statusCode)  // Gestisci i diversi codici di esito
        {
            case 1: // Codice uguale a 1
                // Imposta l'oggetto session e memorizzaci l'username
                session.setAttribute("connectedUser", username);
                res.setStatus(200); // Imposta il codice http a 200 (success)
                break;
            case 2: // Codice uguale a 2 (Password sbagliata) 
                res.setStatus(401); // Imposta codice http a 401 (Unauthorized)
                out.println("Password errata. Riprova");    // Stampa messaggio di errore
                break;
            case 3: // Codice uguale a 3 (Account non esistente)
                res.setStatus(401);
                out.println("Utente non esistente o username scritto erroneamente");
                break;
            default:    // Altri codici 
                res.setStatus(500); // Imposfta codice http a 500 (Internal server error)
                out.println("Errore interno. Riprova tra qualche istante");
                break;
        }
        out.flush();    // Invia la risposta al client
    }
}
