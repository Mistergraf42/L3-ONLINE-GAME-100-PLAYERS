package com.example.serveurweb;

import java.io.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/move")
public class Mouvement extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        int idUser = (int) session.getAttribute("idUser");

        String direction = request.getParameter("direction");

        if (direction != null) {
            ServeurWeb.client.getSortie().writeUTF("deplacement;"+idUser+";" + direction);
            ServeurWeb.client.getSortie().flush();
        } else {
            System.out.println("Problème direction");
        }

        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>Page accessible uniquement en utilisant une méthode POST !</h1>");
        out.println("</body></html>");
    }

    public void destroy() {
    }
}
