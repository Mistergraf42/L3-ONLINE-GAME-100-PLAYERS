package physique;


import java.awt.event.KeyEvent;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JFrame;

public class Serveur implements KeyEventDispatcher  {

	private static final int PORT = 2121;
	private ServerSocket servSocket;
	private List<Socket> listClientSocket = new ArrayList<>();
	private List<DataInputStream> listdIn = new ArrayList<>();
	private List<DataOutputStream> listdOut = new ArrayList<>();
	private boolean fin = false;


    public List<DataInputStream> getListdIn(){
        return this.listdIn;
    }
    public List<DataOutputStream> getListdOut(){
        return this.listdOut;
    }
    public ServerSocket getServSocket(){
        return this.servSocket;
    }
    public boolean getFin(){
        return this.fin;
    }
	public Serveur() {
		try {
			servSocket = new ServerSocket(PORT);
			System.out.println("S : serveur actif");
		} catch (IOException e) {
			System.err.println("S : erreur d'instanciation de la socket du serveur");
			e.printStackTrace();
		}
	}


    public List<Socket> getListClientSocket(){
        return this.listClientSocket;
    }



	// public void ouvrirFlux() {
	// 	try {
	// 		for (Socket clientSocket : listClientSocket) {

	// 		}
	// 		System.out.println("S : Tous les flux ont été instanciés");
	// 	} catch (IOException e) {
	// 		System.err.println("S : erreur d'ouverture des flux");
	// 		e.printStackTrace();
	// 	}
	// }

	public DataOutputStream ouvrirFlux(Socket socketAttente) {
		try {
			DataOutputStream dOut;

			dOut = new DataOutputStream(socketAttente.getOutputStream());
			listdIn.add(new DataInputStream(socketAttente.getInputStream()));
			listdOut.add(dOut);
			return dOut;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	public  void envoyerConfirm() {
		for (DataOutputStream out : listdOut) {
			try {
				out.writeUTF("Serveur send Confirm");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public synchronized void envoyerConfirm(DataOutputStream out) {
			try {
				out.writeUTF("S : Confirmation de connexion");
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	public void deconnecterClient() {
		try {
			for (int i = 0; i < listdIn.size(); i++) {
				listdIn.get(i).close();
				listdOut.get(i).close();
				listClientSocket.get(i).close();
				System.out.println("S : client déconnecté par le serveur");
			}
		} catch (IOException e) {
			System.err.println("S : erreur lors de la déconnexion du client");
			e.printStackTrace();
		}
	}

	public void arreter() {
		fin = true;
		try {
			servSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public  String attendreMessage() {
		while (true) {
			for (int i = 0; i < listdIn.size(); i++) {
				String message = "";
				try {
					message = listdIn.get(i).readUTF();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (!message.isEmpty()) {
					return message;
				}
			}
		}
	}
    @Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if (e.getID() == KeyEvent.KEY_PRESSED) {
			this.arreter();
		}
		return false;
	}
    public String getMessageFromSocket(int i){
        String message = "";
        try {
            message = listdIn.get(i).readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!message.isEmpty()) {
            try {
                listdOut.get(i).writeUTF("S : Demande recu " + message);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return message;
        }
        return null;
    }
}


