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
import java.util.ArrayList;
import org.bson.Document;
import com.google.gson.Gson;

/**
 * Servlet per reperire i post dell'utente (connesso)
 * @author Daniele
 */
@WebServlet(name = "reperimento_PostUtente", urlPatterns = {"/reperimento_PostUtente"})
public class reperimento_PostUtente extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param req servlet request
     * @param res servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        HttpSession session= req.getSession();
        PrintWriter out= res.getWriter();
        ArrayList posts;    // Array per ottenere i post
        int statusCode; // Codice di esito
        UserFactory fc= new CCUserFactory();
        String connectedUser= (String) session.getAttribute("connectedUser");
        CCUser user= (CCUser) fc.createUser(connectedUser);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        posts= user.getPosts(); // Ottieni i post dell'utente
        statusCode= (int) posts.get(posts.size() - 1);  // Ricava codice di esito
        if(statusCode == 1) // Gestisci il caso in cui il reperimento non sia andato a buon fine
        {
            res.setStatus(500);
            out.println("errore nel reperimento dei post");
            out.flush();
        }
        posts.remove(posts.size() - 1); // Altrimenti rimuovi il codice di esito dai risultati
        // Converti in JSON
        Gson gson= new Gson();
        String json= gson.toJson(posts);
        // Stampa nel corpo della risposta e invia al client
        out.println(json);
        out.flush();
    }
}
