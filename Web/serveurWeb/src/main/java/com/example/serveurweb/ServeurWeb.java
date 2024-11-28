package com.example.serveurweb;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServeurWeb implements ServletContextListener {
    public static Client  client;
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Connexion au serveur de jeu...");
        client = new Client();
        client.initierConnexion();
        System.out.println("Connectée !");
    }

    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Fermeture de la connexion au serveur de jeu...");
    }
}
