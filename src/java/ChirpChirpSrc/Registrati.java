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
import java.sql.*;
import java.time.LocalDate;
/**
 * Servlet che si occupa sia di servire richieste all'url /Registrati, sia di effettuare 
 * l'operazione di registrazione
 * @author Daniele
 */
@WebServlet(name = "Registrati", urlPatterns = {"/Registrati"})
public class Registrati extends HttpServlet {

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
        req.getRequestDispatcher("Registrazione.html").forward(req,res);

    }

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
        String username= req.getParameter("username");  // Ricava username dalla richiesta
        String password= req.getParameter("password");  // Ricava password dalla richiesta
         // Ricava email dalla richiesta
        String email= req.getParameter("email");
        // Ricava data di nascita trasformandola in un oggetto di tipo LocalDate
        LocalDate birthdate= LocalDate.parse(req.getParameter("birthdate"));
        int result;
        res.setContentType("text/plain");
        res.setCharacterEncoding("UTF-8"); // Imposta la codifica caratteri
        // Invoca metodo 'register' di CCUser passando i dati necessari per effettuare la registrazione
        result= CCUser.register(username, password, email, java.sql.Date.valueOf(birthdate));
        switch (result) {   // Gestisci esito
            case 0:
                res.setStatus(200); // Registrazione effettuata
                break;
            case 2:
                res.setStatus(404); // Username già esistente
                out.println("Username già usato! Utilizzane un altro");
                break;
            default:
                res.setStatus(500); // Errore interno
                out.println("si è verificato un errore interno. Riprova tra qualche istante");
                break;
        }
        out.flush();    // Invia al client
    }
}
