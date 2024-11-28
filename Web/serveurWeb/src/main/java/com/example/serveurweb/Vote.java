package com.example.serveurweb;

import java.io.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/vote")
public class Vote extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //HttpSession session = request.getSession();

        //session.setAttribute("idUser",(int)(Math.random() * 100000));
        //ServeurWeb.client.getSortie().writeUTF("addJoueur;"+(int) session.getAttribute("idUser"));
        System.out.println("En get");


        response.setContentType("text/html");
        //http://localhost:8080/serveurWeb_war_exploded/hello-servlet
        RequestDispatcher dispatcher = request.getRequestDispatcher("/hello.jsp");
        dispatcher.include(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        int idUser = (int) session.getAttribute("idUser");

        String vote = request.getParameter("target");
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            ServeurWeb.client.getSortie().writeUTF("vote;"+idUser+";"+vote);
        }
    }

    public void destroy() {
    }
}