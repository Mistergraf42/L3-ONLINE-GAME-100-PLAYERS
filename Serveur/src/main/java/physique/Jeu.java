package physique;

import javax.swing.JFrame;
import config.*;
public class Jeu {
    public static void main(String[] args){
        Fenetre fen = new Fenetre("Jeu");
        fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fen.setVisible(true);
        fen.run();

    }
}
