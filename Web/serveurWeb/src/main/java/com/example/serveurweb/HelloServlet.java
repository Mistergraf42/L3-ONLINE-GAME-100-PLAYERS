package com.example.serveurweb;

import java.io.*;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private static int nombreJoueurs = 0;
    private String message;


    public void init() {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();

        if(session.getAttribute("idUser") == null) {
            // Pour la couleur du joueur et du fond d'écran
            Random couleur = new Random();
            int rouge = couleur.nextInt(255);
            int vert = couleur.nextInt(255);
            int bleu = couleur.nextInt(255);
            int numJoueur = ++nombreJoueurs;
            // Envoi du message
            session.setAttribute("idUser",(int)(Math.random() * 100000));
            ServeurWeb.client.getSortie().writeUTF("addJoueur;" + session.getAttribute("idUser")+";"+rouge+";"+vert+";"+bleu+";"+numJoueur);
            session.setAttribute("rouge", rouge);
            session.setAttribute("vert", vert);
            session.setAttribute("bleu", bleu);
            session.setAttribute("numJoueur", numJoueur);

        }

        response.setContentType("text/html");
        //http://localhost:8080/serveurWeb_war_exploded/hello-servlet
        RequestDispatcher dispatcher = request.getRequestDispatcher("/hello.jsp");
        dispatcher.include(request, response);
    }

    public void destroy() {
    }
}