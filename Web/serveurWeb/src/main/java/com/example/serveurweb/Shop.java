package com.example.serveurweb;

import java.io.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/shop")
public class Shop extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        int idUser = (int) session.getAttribute("idUser");

        String achat = request.getParameter("target");
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            ServeurWeb.client.getSortie().writeUTF("shop;"+idUser+";"+achat);
        }
    }

    public void destroy() {
    }
}