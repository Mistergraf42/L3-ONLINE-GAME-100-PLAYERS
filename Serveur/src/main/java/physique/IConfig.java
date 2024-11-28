package physique;

import java.awt.*;

public class IConfig {

    public static Color couleurMurCadre = new Color(138, 6, 6);

    /////////////////////////
    // STATISTIQUE DE GAME // 
    /////////////////////////

    // Deplacement section de carte 
    public static int DEPLACEMENT_VERTICALE_VOTE = 25;
    public static int DEPLACEMENT_VERTICALE_ACCUEIL = -25;
    public static int DEPLACEMENT_VERTICALE_SCORE = -50;



    // Couleurs boules votes 
    public static Color couleurVote1 = new Color(200,0,0);
    public static Color couleurVote2 = new Color(0,200,0);
    public static Color couleurVote3 = new Color(0,0,200);
    public static Color couleurVote4 = new Color(200,100,10);


    // temps des jeux 
    public static int tempsVote = 15;
    public static int tempsJeu = 10;
    public static int tempsScore = 3;
    public static int tempsResultatVote = 5;

    // Size des objets 
    public static double rayonPiece = 0.07;
    public static double rayonPersonnage = 0.1;


    public static final int GAUCHE = 0;
    public static final int HAUT = 1;
    public static final int DROITE = 2;
    public static final int BAS = 3;

    public static final double MD = 4;
    public static final double MG = -4;
    public static final double MH = 2;
    public static final double MB = -2;

    // Couleurs des pièces
    public static Color couleurPiece = new Color(255 ,215 ,0);

    ////////////////////////////
    // STATISTIQUE D'INSTANCE //
    ////////////////////////////
    // Nombre d'obstacle 
    public static int nombrePiece = 15;
    public static int nombreTourniquet = 0;
    public static int nombreRessort = 0;
    public static int nombreBombe = 0;

    // Changement gameplay
    public static int temps_attente_mur = 0;
    public static int tempsExplosionBombe = 4;
    public static double coeff_vitesse_mur = 0.12;
    public static double coeff_taille_trou_mur = 1.5;


    public static int LONGUEUR_FENETRE = 1800;
    public static int LARGEUR_FENETRE = 900;

    public static double vitesseDeplacement = 0.005;
}
