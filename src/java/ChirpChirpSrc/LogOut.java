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
 * Servlet che si occupa di fare il logout dell'utente
 * @author Daniele
 */
@WebServlet(name = "LogOut", urlPatterns = {"/LogOut"})
public class LogOut extends HttpServlet {

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
        HttpSession session= req.getSession();  // Ricava, dalla request, l'oggetto Session
        // Ottieni il valore dell'attributo connectedUser da session
        String connectedUser= (String) session.getAttribute("connectedUser");
        UserFactory fc= new CCUserFactory();
        User ccuser= fc.createUser(connectedUser);
        System.out.println("Disconnessione dell'utente: " + connectedUser + " ");
        ccuser.logOut(session); // Effettua la disconnessione dell'utente
        System.out.println("effettuata");
        res.sendRedirect("/Twitter2_Prog3/");   // Reindirizza all'index
    }

}
