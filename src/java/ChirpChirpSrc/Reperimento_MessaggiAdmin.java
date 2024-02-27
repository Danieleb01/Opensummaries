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
import com.google.gson.Gson;
/**
 * Servlet che si occupa di restituire i messaggi in base alla parola scelta dall'utente
 * @author Daniele
 */
@WebServlet(name = "Reperimento_MessaggiAdmin", urlPatterns = {"/Reperimento_MessaggiAdmin"})
public class Reperimento_MessaggiAdmin extends HttpServlet {

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
        // Reperisci dalla request la parola con la quale cercare i messaggi
        String word= req.getParameter("word");
        String connectedAdmin= (String) session.getAttribute("connectedAdmin");
        ArrayList result;   // Array contenente i risultati
        int statusCode; // Codice di esito
        // Imposta il tipo di contenuto a json
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        UserFactory fc= new CCAdminFactory();
        CCAdmin admin= (CCAdmin) fc.createUser(connectedAdmin);
        // Ottieni messaggi
        result= admin.getMsgsWithWord(word);
        // Estrai dall'ultima posizione dell'array il codice di esito
        statusCode= (int) result.get(result.size() - 1);
        if(statusCode == 1) // Se è uguale a 1...
        {
            res.setStatus(500); // Imposta il codice http a 500
            out.print("errore interno");    
            out.flush();    // Invia risposta al client
        }
        result.remove(result.size() - 1);   // Altrimenti elimina il codice di esito dai risultati (non più necessario)
        // Converti in JSON
        Gson gson= new Gson();
        String json= gson.toJson(result);
        res.setStatus(200); // Imposta codice http a 200
        out.print(json);    // Stampa i risultati nel corpo della richiesta
        out.flush();    // Invia al client
    }
}
