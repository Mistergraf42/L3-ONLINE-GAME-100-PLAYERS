package com.example.serveurweb;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Objects;
import java.util.StringTokenizer;

public class Client {
    private String hote = "127.0.0.1";
    private int port = 2121;
    private Socket cliSocket;
    private DataInputStream entree;
    private DataOutputStream sortie;
    public String getHote() {
        return hote;
    }

    public int getPort() {
        return port;
    }

    public Socket getCliSocket() {
        return cliSocket;
    }

    public DataInputStream getEntree() {
        return entree;
    }

    public DataOutputStream getSortie() {
        return sortie;
    }

    public void initierConnexion() {
        try {
            cliSocket = new Socket(hote, port);
            System.out.println("C : connecté au serveur");

            entree = new DataInputStream(cliSocket.getInputStream());
            sortie = new DataOutputStream(cliSocket.getOutputStream());
            lancerThread();
            System.out.println("C : flux binaires ouverts");
        } catch (UnknownHostException e) {
            System.err.println("C : erreur de connection au serveur, hôte inconnu");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("C : erreur d'ouverture des flux");
            e.printStackTrace();
        }


    }


    public synchronized void lancerThread() {
        // lancement d'un thread pour chaque joueur
        new Thread(() -> {
            while (true) {
                if(cliSocket.isClosed()){
                    break;
                }
                String message = attendreMessage();

                // gestion ici du message pour envoyer aux iencli
                if(!message.equals( "S : Confirmation de connexion")){
                   updateScore(message);
                }
            }
        }).start();
    }



    // Emplacement de code a la Tolga (pardonn :))) )
    public static HashMap<Integer, Integer> hashScore = new HashMap<Integer, Integer>();
    public static void updateScore(String message){
        StringTokenizer strtok = new StringTokenizer(message,";");
        String dem;
        if(strtok.hasMoreTokens()){
             dem = strtok.nextToken();
        }else{
            return ;
        }
        if(dem.equals("score")){

        while(strtok.hasMoreTokens()){
            String idjoueur = strtok.nextToken();
            String scorejoueur = strtok.nextToken();
            if (hashScore.containsKey(Integer.parseInt(idjoueur))) {
                hashScore.remove(Integer.parseInt(idjoueur));
                hashScore.put(Integer.parseInt(idjoueur), Integer.parseInt(scorejoueur));
            } else {
                // Ajouter le joueur avec son score
                hashScore.put(Integer.parseInt(idjoueur), Integer.parseInt(scorejoueur));
            }
        }
        }
    }
    public static int getScore(int id){
        return hashScore.getOrDefault(id, 0);

    }
    public synchronized String attendreMessage() {
        while (true) {

            String message = "";
            try {
                message = entree.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!message.isEmpty()) {
                return message;
            }
        }
    }

    public void seDeconnecter() {
        try {
            entree.close();
            sortie.close();
            cliSocket.close();
            System.out.println("C : déconnexion du serveur");
        } catch (IOException e) {
            System.err.println("C : erreur lors de la déconnexion");
            e.printStackTrace();
        }
    }

    public void gauche() {

    }

    public void droite() {

    }

    public void haut() {

    }

    public void bas() {

    }


}
