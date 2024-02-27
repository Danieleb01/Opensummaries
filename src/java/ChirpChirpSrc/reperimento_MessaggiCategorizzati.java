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
 *  Servlet per reperire messaggi categorizzati in base agli hashtag.
 * @author Daniele
 */
@WebServlet(name = "reperimento_MessaggiCategorizzati", urlPatterns = {"/reperimento_MessaggiCategorizzati"})
public class reperimento_MessaggiCategorizzati extends HttpServlet {

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
         PrintWriter out= res.getWriter();
         HttpSession session= req.getSession();
         ArrayList results; // Array finale
         int statusCode;    // Codice di esito
         res.setContentType("application/json");
         res.setCharacterEncoding("UTF-8");
         String connectedAdmin= (String) session.getAttribute("connectedAdmin");
         UserFactory fc= new CCAdminFactory();
         CCAdmin admin= (CCAdmin) fc.createUser(connectedAdmin);
         results= admin.categoriseMsgsByHashtags(); // Ottieni i post
         statusCode= (int) results.get(results.size() - 1); // Ricava il codice di esito e gestisci in caso di errore
         if(statusCode == 1)
         {
             res.setStatus(500);
             out.print("Errore interno");
             out.flush();
         }
         results.remove(results.size() - 1);    // Rimuovi codice di esito
         // Converti in JSON
         Gson gson= new Gson();
         String json= gson.toJson(results);
         res.setStatus(200);
         out.print(json);
         out.flush();
    }
}
