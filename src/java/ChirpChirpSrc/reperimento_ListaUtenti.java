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
 * Servlet per reperire la lista degli utenti e il numero di messaggi inviati
 * @author Daniele
 */
@WebServlet(name = "reperimento_ListaUtenti", urlPatterns = {"/reperimento_ListaUtenti"})
public class reperimento_ListaUtenti extends HttpServlet {

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
        String connectedAdmin= (String) session.getAttribute("connectedAdmin");
        ArrayList results;  // Array contenente i risultati
        int statusCode;
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        UserFactory fc= new CCAdminFactory();
        CCAdmin admin= (CCAdmin) fc.createUser(connectedAdmin);
        results= admin.getUserList();   // Ottieni la lista utenti con il numero di post inviati
        statusCode= (int) results.get(results.size() - 1);  // Ricava codice di stato
        if(statusCode == 1) // Gestisci errore
        {
            res.setStatus(500);
            out.print("Errore interno");
            out.flush();
        }
        results.remove(results.size() - 1); // Altrimenti rimuovi il codice di esito dall'array
        // Converti in JSON
        Gson gson= new Gson();
        String json= gson.toJson(results);
        res.setStatus(200);
        out.print(json);    // Stampa i risultati nel corpo della response
        out.flush();    // Invia al client
    }
}
