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
 * Servlet che si occupa di servire richieste all'url /Profilo_Utente
 * @author Daniele
 */
@WebServlet(name = "Profilo_Utente", urlPatterns = {"/Profilo_Utente"})
public class Profilo_Utente extends HttpServlet {


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
        
        HttpSession session= req.getSession();  // Ottieni oggetto HttpSession dalla richiesta
        String username= (String) session.getAttribute("connectedUser");    // Estrai il contenuto dell'oggetto
        if(username == null)    // Se l'oggetto session non ha valori (utente non autenticato)
        {
            res.sendRedirect("/Twitter2_Prog3/");   // Reindirizza alla pagina di login
        }
        else
        {
            // Altrimenti visualizza il profilo
            req.getRequestDispatcher("Profilo-Utente.html").forward(req, res);
        }
    }
}
