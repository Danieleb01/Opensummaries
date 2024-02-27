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
 * Servlet che effettua il login degli amministratori. La logica è simile alla servlet che si occupa del login 
 * degli utenti normali, con l'aggiunta di un nuovo codice di stato.
 * @author Daniele
 */
@WebServlet(name = "AdminLogin", urlPatterns = {"/AdminLogin"})
public class AdminLogin extends HttpServlet {

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
        String username= req.getParameter("username");
        String password= req.getParameter("password");
        HttpSession session= req.getSession();
        UserFactory fc= new CCAdminFactory();
        int statusCode;
        CCAdmin admin= (CCAdmin) fc.createUser(username);
        res.setContentType("text/plain");
        res.setCharacterEncoding("UTF-8"); // Imposta la codifica caratteri
        statusCode= admin.logIn(password);  // Effettua login dell'admin
        switch(statusCode) // Gestisci codice di stato
        {
            case 1:
                session.setAttribute("connectedAdmin", username);
                res.setStatus(200);
            break;
            
            case 2:
                res.setStatus(401);
                out.println("Password errata. Riprova");
                break;
            case 3:
                res.setStatus(401);
                out.println("Utente non esistente o username scritto erroneamente");
                break;
            case 5: // Codice uguale a 5 (Accesso negato)
                res.setStatus(401);
                out.println("non hai i permessi di amministratore. Accesso negato!");
                break;
            default:
                res.setStatus(500);
                out.println("Errore interno. Riprova tra qualche istante");
                break;
        }
        out.flush();
    }

}
