/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package ChirpChirpSrc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.*;
import com.google.gson.Gson;    // Classe GSON per convertire oggetti in file JSON

/**
 * Servlet che si occupa di reperire i dati dell'utente da visualizzare nel profilo
 * @author Daniele
 */
@WebServlet(name = "reperimento_DatiUtente", urlPatterns = {"/reperimento_DatiUtente"})
public class reperimento_DatiUtente extends HttpServlet {


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
        int statusCode;
        UserFactory fc= new CCUserFactory();
        String username= (String) session.getAttribute("connectedUser");
        ArrayList userInfo; // Array contenente le informazioni dell'utente
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        CCUser user= (CCUser) fc.createUser(username);
        userInfo= user.getUserInfo();   // Ricava le informazioni dell'utente
        statusCode= (int) userInfo.get(userInfo.size() - 1);    // Ottieni il codice di esito
        if(statusCode == 1) // Se è uguale a 1...
        {
            res.setStatus(500); // Segnala un errore al client
            System.out.println("Errore nel reperimento dei dati");
            out.flush();
        }
        userInfo.remove(userInfo.size() - 1);
        // Converti in JSON
        Gson gson= new Gson();
        String json= gson.toJson(userInfo);
        System.out.println("Dati reperiti!");
        out.print(json);
        out.flush();
    }
}
