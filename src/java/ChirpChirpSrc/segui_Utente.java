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
 * Servlet che permette ad un utente di seguirne un altro
 * @author Daniele
 */
@WebServlet(name = "segui_Utente", urlPatterns = {"/segui_Utente"})
public class segui_Utente extends HttpServlet {

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
       HttpSession session= req.getSession();
       PrintWriter out= res.getWriter();
       UserFactory fc= new CCUserFactory();
       String connectedUser= (String) session.getAttribute("connectedUser");
       // Ricava dalla request l'username dell'utente da seguire
       String username= req.getParameter("username");
       int result; // Codice di esito
       CCUser user= (CCUser) fc.createUser(connectedUser);
       result= user.followUser(username);   // Segui utente
       switch(result) // Gestisci i vari codici
       {
           case 0:  // Successo
               res.setStatus(200);
               break;
           case 2:  // Utente non trovato
               res.setStatus(404);
               out.println("utente non trovato. Controlla se l'username è corretto");
               break;
           case 3:  // Utente già seguito
               res.setStatus(409);
               out.println("già segui quest'utente");
               break;
           default: // Altri codici di errore
               res.setStatus(500);
               out.println("si è verificato un errore interno. Riprovare tra un istante");
               break;
       }
       out.flush(); // Invia al client
    }
}
