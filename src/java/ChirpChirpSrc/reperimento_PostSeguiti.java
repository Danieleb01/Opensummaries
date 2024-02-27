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
 *
 * @author Daniele
 */
@WebServlet(name = "reperimento_PostSeguiti", urlPatterns = {"/reperimento_PostSeguiti"})
public class reperimento_PostSeguiti extends HttpServlet {

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
        String connectedUser= (String) session.getAttribute("connectedUser");
        UserFactory fc= new CCUserFactory();
        CCUser user= (CCUser) fc.createUser(connectedUser);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        ArrayList posts= user.getFollowingPosts();  // Ottieni i posts 
        statusCode= (int) posts.get(posts.size() - 1);  // Ricava codice di esito
        if(statusCode == 1) // Gestisci il caso in cui è avvenuto un errore
        {
            res.setStatus(500);
            out.flush();
        }
        // Altrimenti invia al client i risultati
        posts.remove(posts.size() - 1);
        Gson gson= new Gson();
        String json= gson.toJson(posts);
        out.println(json);
        res.setStatus(200);
        out.flush();
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
