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
 * Servlet che si occupa dell'elaborazione riguardo l'invio di un messaggio
 * @author Daniele
 */
@WebServlet(name = "MandaMessaggio", urlPatterns = {"/MandaMessaggio"})
public class MandaMessaggio extends HttpServlet {
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
        PrintWriter out= res.getWriter();
        HttpSession session= req.getSession();
        String username= (String) session.getAttribute("connectedUser");
        UserFactory fc= new CCUserFactory();
        CCUser user= (CCUser) fc.createUser(username);
        // Recupera il testo del messaggio dalla request
        String msgBody= req.getParameter("corpoMessaggio");
        // Recupera gli hashtags dalla request
        String hashtags= req.getParameter("hashtags");
        int result; // Esito dell'operazione
        CCMessage message= new CCMessageImpl(msgBody, hashtags); // Crea un nuovo messaggio ChirpChirp!
        result= user.sendMessage(message);  // Posta messaggio
        if(result == 0) // Se è andato tutto a buon fine...
        {
            res.setStatus(200); // Imposta il codice http a 200
            out.println("Messaggio inviato!");  // Stampa messaggio
        }
        else    // Altrimenti
        {
            res.setStatus(500); // Imposta il codice http a 500 
            out.println("Si è verificato un errore! Riprova tra qualche istante");
        }
        out.flush();    // Invia risposta al client
    }
}
