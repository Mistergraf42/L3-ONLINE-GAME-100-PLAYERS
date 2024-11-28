package physique;

import config.BombeConfig;
import config.Frame;
import config.RessortConfig;
import config.TourniquetConfig;
import jdk.jfr.BooleanFlag;
import org.dyn4j.dynamics.BodyFixture;

import javax.imageio.ImageIO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.geometry.decompose.Bayazit;
import org.dyn4j.geometry.decompose.Decomposer;
import org.dyn4j.geometry.decompose.SweepLine;
import org.dyn4j.world.World;

import java.io.File;

import org.dyn4j.dynamics.joint.RevoluteJoint;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import jogamp.graph.font.typecast.ot.table.ID;
import physique.framework.Camera;
import physique.framework.SimulationBody;
import physique.framework.SimulationFrame;
import physique.framework.input.BooleanStateKeyboardInputHandler;

import java.net.Socket;
import java.io.DataOutputStream;
import java.io.IOException;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.awt.geom.AffineTransform;
import java.util.Timer;


public class Fenetre extends SimulationFrame {
    String song1 = "./Serveur/ressource_tuto/piege_audio/aimant.wav";
    String song2 = "./Serveur/ressource_tuto/piege_audio/bombe.wav";
    String song3 = "./Serveur/ressource_tuto/piege_audio/ressort.wav";

    AudioObstacle audio1 = new AudioObstacle(song1);
    AudioObstacle audio2 = new AudioObstacle(song2);
    AudioObstacle audio3 = new AudioObstacle(song3);


    private int song_test = 0;

    private final BooleanStateKeyboardInputHandler left;
    private final BooleanStateKeyboardInputHandler right;
    private final BooleanStateKeyboardInputHandler up;
    private final BooleanStateKeyboardInputHandler down;

    private final BooleanStateKeyboardInputHandler vote;
    private final BooleanStateKeyboardInputHandler vote1;
    private final BooleanStateKeyboardInputHandler vote2;
    private final BooleanStateKeyboardInputHandler vote3;
    private final BooleanStateKeyboardInputHandler vote4;
    private final BooleanStateKeyboardInputHandler startGame;
    private final BooleanStateKeyboardInputHandler startGameLibre;
    private final BooleanStateKeyboardInputHandler partieEnCoursLibreStop;

    // Les onkeys

    private final BooleanStateKeyboardInputHandler J3;

    // Liste pour le fonctionnement du jeu
    private ArrayList<Boule> listBouleVote1 = new ArrayList<Boule>();
    private ArrayList<Boule> listBouleVote2 = new ArrayList<Boule>();
    private ArrayList<Boule> listBouleVote3 = new ArrayList<Boule>();
    private ArrayList<Boule> listBouleVote4 = new ArrayList<Boule>();

    // Variable pour les listes d'objets //
    private LinkedList<Mur> listeMurs = new LinkedList<>();
    private HashMap<Integer, Personnage> joueurEnVie = new HashMap<Integer, Personnage>();
    private HashMap<Integer, Personnage> listeJoueurs = new HashMap<Integer, Personnage>();
    private ArrayList<Bombe> listBombe = new ArrayList<Bombe>();
    private ArrayList<Piece> listPiece = new ArrayList<Piece>();
    private ArrayList<Ressort> listRessort = new ArrayList<Ressort>();
    private ArrayList<Tourniquet> listTourniquet = new ArrayList<Tourniquet>();
    private LinkedList<MurTroue> listeMursTroues = new LinkedList<>();
    private LinkedHashMap<Integer, Integer> JoueursRiches = new LinkedHashMap<>(); // Affichage des 5 joueurs les plus riches

    config.Frame fenetreDebug;

    // Variable pour la gestion des murs
    int cooldown = 0; // utilisé pour gérer le temps entre deux murs d'une même rafale
    double posTrou = 0; // position du trou du mur créé
    int typeMur = 0; //type du mur à créer
    int rafale = 0; //combien de mur nous allons créer de suite

    /****************************************************************/

    // a mettre Iconfig ?
    private double largeur_map;
    private double longueur_map;
    private double epaisseur_mur;

    private boolean partieEnCours = false;
    private boolean partieEnCoursLibre = false;
    private Serveur serveur;
    private boolean affiche_resultat = false;
    private javax.swing.Timer chronoBombe;


    // Les votes //
    private int total_vote = 0;
    private int vote_choisi = -1;
    private boolean vote_affichage = false;


    // Variable pour les images des votes et du qr code
    BufferedImage imageVote1;
    BufferedImage imageVote2;
    BufferedImage imageVote3;
    BufferedImage imageQR;
    BufferedImage imageVote4;

    // Variable pour les emplacements des obstacles (pseudo-aléatoire) //
    public static int[] emplacementTourniquet = {0, 0, 0, 0};
    public static int[] emplacementBombe = {0, 0, 0, 0};
    public static int[] emplacementRessort = {0, 0};


    // Variable utile pour la pipeline //
    private int seconds = 0;
    private int nextStep = 1; // va contenir les nombres de secondes pour la prochaine pipeline
    private int actuelStep = 0; // Va contenir l'étape sur laquelle on est (sur la pipeline)
    private Timer minuteur;


    /**
     * Constructor.
     * <p>
     * By default creates a 800x600 canvas.
     *
     * @param name the frame name
     */
    public Fenetre(String name) {
        super(name);
        // parametrage de la fenetre de config
        fenetreDebug = new Frame("Test");
        // Initialisation du serveur
        serveur = new Serveur();
        new Thread(() -> ecouter()).start();
        // Initialisation des array de pseudo aleatoire
        // Initialisation du timer
        chronoBombe = new javax.swing.Timer(1000, new ActionListener() {
            int temps = -1;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (temps == 0) {
                    if (exploseUneBombe()) {
                        javax.swing.Timer timerAjout = new javax.swing.Timer(1000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                ajoutBombe();
                                ((javax.swing.Timer) e.getSource()).stop();
                            }
                        });
                        timerAjout.start();
                        temps = IConfig.tempsExplosionBombe;
                    }

                } else if (temps == -1) {

                    temps = IConfig.tempsExplosionBombe;

                } else {
                    temps--;
                }
            }
        });
        fenetreDebug.getPanel().getGlobal().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VerifEtAppliqueChoixConsole();
            }
        });

        // On lance le thread d'attente de message
        // Instanciation des Key Listeners
        this.left = new BooleanStateKeyboardInputHandler(this.canvas, KeyEvent.VK_LEFT);
        this.right = new BooleanStateKeyboardInputHandler(this.canvas, KeyEvent.VK_RIGHT);
        this.up = new BooleanStateKeyboardInputHandler(this.canvas, KeyEvent.VK_UP);
        this.down = new BooleanStateKeyboardInputHandler(this.canvas, KeyEvent.VK_DOWN);
        // Mise en place des key listeners
        this.left.install();
        this.right.install();
        this.up.install();
        this.down.install();
        // Pour controler les joueurs 1 et 2
        this.J3 = new BooleanStateKeyboardInputHandler(this.canvas, KeyEvent.VK_E);
        this.J3.install();


        this.vote = new BooleanStateKeyboardInputHandler(this.canvas, KeyEvent.VK_V);
        this.vote.install();

        this.vote1 = new BooleanStateKeyboardInputHandler(this.canvas, KeyEvent.VK_P);
        this.vote2 = new BooleanStateKeyboardInputHandler(this.canvas, KeyEvent.VK_I);
        this.vote3 = new BooleanStateKeyboardInputHandler(this.canvas, KeyEvent.VK_O);
        this.vote4 = new BooleanStateKeyboardInputHandler(this.canvas, KeyEvent.VK_U);

        this.vote1.install();
        this.vote2.install();
        this.vote3.install();
        this.vote4.install();

        this.startGame = new BooleanStateKeyboardInputHandler(this.canvas, KeyEvent.VK_ENTER);

        this.startGame.install();
        this.startGameLibre = new BooleanStateKeyboardInputHandler(this.canvas , KeyEvent.VK_SPACE);
        this.startGameLibre.install();
        this.partieEnCoursLibreStop = new BooleanStateKeyboardInputHandler(this.canvas, KeyEvent.VK_N);
        this.partieEnCoursLibreStop.install();

        // Paramètres de construction de la map
        largeur_map = 4;
        longueur_map = 8;
        epaisseur_mur = 1;
    }

    @Override
    protected void initializeWorld() {
        this.afficheAccueil();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // //
        // CARTE DE JEU //
        // //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        this.world.setGravity(World.ZERO_GRAVITY);
        Musique.playMusic("./Serveur/ressource_tuto/ressource_menu/musique_de_fonds.wav");
        // Création des murs (cadre)
        Mur murHaut = new Mur(IConfig.couleurMurCadre, longueur_map, epaisseur_mur, 0, largeur_map / 2);
        this.world.addBody(murHaut);
        Mur murBas = new Mur(IConfig.couleurMurCadre, longueur_map, epaisseur_mur, 0, -largeur_map / 2);
        this.world.addBody(murBas);
        Mur murGauche = new Mur(IConfig.couleurMurCadre, epaisseur_mur, largeur_map, -longueur_map / 2, 0);
        this.world.addBody(murGauche);
        Mur murDroit = new Mur(IConfig.couleurMurCadre, epaisseur_mur, largeur_map, longueur_map / 2, 0);
        this.world.addBody(murDroit);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // //
        // CARTE DE VOTE //
        // //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Création des murs (cadre)
        Mur murHautVote = new Mur(IConfig.couleurMurCadre, 8, 1, 0 - IConfig.DEPLACEMENT_VERTICALE_VOTE, 2);
        this.world.addBody(murHautVote);
        Mur murBasVote = new Mur(IConfig.couleurMurCadre, 8, 1, 0 - IConfig.DEPLACEMENT_VERTICALE_VOTE, -2);
        this.world.addBody(murBasVote);
        Mur murGaucheVote = new Mur(IConfig.couleurMurCadre, 1, 5, -3.8 - IConfig.DEPLACEMENT_VERTICALE_VOTE, 0);
        this.world.addBody(murGaucheVote);
        Mur murDroitVote = new Mur(IConfig.couleurMurCadre, 1, 5, 3.8 - IConfig.DEPLACEMENT_VERTICALE_VOTE, 0);
        this.world.addBody(murDroitVote);

        // création des murs (séparation)
        Mur murp1_2 = new Mur(IConfig.couleurMurCadre, 0.2, 2, 0 - IConfig.DEPLACEMENT_VERTICALE_VOTE, -0.5);
        this.world.addBody(murp1_2);
        Mur murp2_3 = new Mur(IConfig.couleurMurCadre, 0.2, 2, -2 - IConfig.DEPLACEMENT_VERTICALE_VOTE, -0.5);
        this.world.addBody(murp2_3);
        Mur murp3_4 = new Mur(IConfig.couleurMurCadre, 0.2, 2, 2 - IConfig.DEPLACEMENT_VERTICALE_VOTE, -0.5);
        this.world.addBody(murp3_4);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // //
        // CARTE D'ACCUEIL //
        // //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // création des murs (séparation)
        Mur murAccueil1 = new Mur(IConfig.couleurMurCadre, 8, 1, 0 - IConfig.DEPLACEMENT_VERTICALE_ACCUEIL, 2);
        this.world.addBody(murAccueil1);
        Mur murAccueil2 = new Mur(IConfig.couleurMurCadre, 8, 1, 0 - IConfig.DEPLACEMENT_VERTICALE_ACCUEIL, -2);
        this.world.addBody(murAccueil2);
        Mur murAccueil3 = new Mur(IConfig.couleurMurCadre, 1, 5, -3.8 - IConfig.DEPLACEMENT_VERTICALE_ACCUEIL, 0);
        this.world.addBody(murAccueil3);
        Mur murAccueil4 = new Mur(IConfig.couleurMurCadre, 1, 5, 3.8 - IConfig.DEPLACEMENT_VERTICALE_ACCUEIL, 0);
        this.world.addBody(murAccueil4);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // //
        // CARTE SCORE //
        // //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // création des murs (séparation)
        Mur murScore1 = new Mur(IConfig.couleurMurCadre, 8, 1, 0 - IConfig.DEPLACEMENT_VERTICALE_SCORE, 2);
        this.world.addBody(murScore1);
        Mur murScore2 = new Mur(IConfig.couleurMurCadre, 8, 1, 0 - IConfig.DEPLACEMENT_VERTICALE_SCORE, -2);
        this.world.addBody(murScore2);
        Mur murScore3 = new Mur(IConfig.couleurMurCadre, 1, 5, -3.8 - IConfig.DEPLACEMENT_VERTICALE_SCORE, 0);
        this.world.addBody(murScore3);
        Mur murScore4 = new Mur(IConfig.couleurMurCadre, 1, 5, 3.8 - IConfig.DEPLACEMENT_VERTICALE_SCORE, 0);
        this.world.addBody(murScore4);
    }

    protected synchronized void render(Graphics2D g, double elapsedTime) {

        super.render(g, elapsedTime);
        g.scale(1, -1);
        g.translate(-this.getWidth() * 0.5 - this.getCameraOffsetX(),
                -this.getHeight() * 0.5 + this.getCameraOffsetY());
        Dimension d = this.getSize();
        int largeur = d.height;
        int longueur = d.width;

        if (partieEnCours) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.PLAIN, 30));
            if (seconds == 0) {
                g.drawString("Temps : " + String.format("%02d:%02d", 0, seconds), longueur-(longueur-20), largeur-100);
            } else {
                g.setColor(Color.BLACK);
                g.drawString("Temps : " + String.format("%02d:%02d", seconds / 60, seconds % 60), longueur-(longueur-20), largeur-100);

            }
            g.drawString("Etape : " + actuelStep + "/11 (" + nextStep + " avant la prochaine étape)", longueur-(longueur-20), largeur-50);

        }
        g.drawString("Joueurs : " + listeJoueurs.size(), longueur-(longueur-20), largeur-75);

        if (this.J3.isActive()) {
            Personnage perso = new Personnage(new Color(151, 151, 151),IConfig.rayonPersonnage, IConfig.vitesseDeplacement,150, 0.08, 0);
            perso.setDirection(new Vector2(0, 0));
            perso.translate(-2, 1);
            this.world.addBody(perso);
        }
        if (this.startGame.isActive() && partieEnCours == false) {
            partieEnCours = true;
            joueurEnVie.clear();
            listBouleVote1.clear();
            listBouleVote2.clear();
            listBouleVote3.clear();
            listBouleVote4.clear();
            startGame();
        }
        if(this.startGameLibre.isActive() && partieEnCoursLibre == false){
                partieEnCoursLibre = true;
                joueurEnVie.clear();
                listBouleVote1.clear();
                listBouleVote2.clear();
                listBouleVote3.clear();
                listBouleVote4.clear();
                afficheGame();
                chronoBombe.start();

        }
        if(this.partieEnCoursLibreStop.isActive() && partieEnCoursLibre == true){
            joueurEnVie.clear();
            listBouleVote1.clear();
            listBouleVote2.clear();
            listBouleVote3.clear();
            listBouleVote4.clear();
            afficheAccueil();
            partieEnCoursLibre = false;
        }

        // AFFICHAGE VOTE
        if (vote_affichage) {
            if (this.vote1.isActive() && total_vote < 100) {
                genererNouveauVote(1);
            }
            if (this.vote2.isActive() && total_vote < 100) {
                genererNouveauVote(2);
            }
            if (this.vote3.isActive() && total_vote < 100) {
                genererNouveauVote(3);
            }
            if (this.vote4.isActive() && total_vote < 100) {
                genererNouveauVote(4);
            }
            g.setColor(Color.BLACK);
            g.setFont(new Font("SansSerif", Font.PLAIN, 30));
            g.drawString("Vote 1: " + listBouleVote1.size(), (longueur/4)-300, (largeur/5));
            g.drawImage(imageVote1, (longueur/4)-300, (largeur/4), 200, 200, null);

            g.setFont(new Font("SansSerif", Font.PLAIN, 30));
            g.drawString("Vote 2: " + listBouleVote2.size(), 2*(longueur/4)-300, (largeur/5));
            g.drawImage(imageVote2, 2*(longueur/4)-300, (largeur/4), 200, 200, null);

            g.setFont(new Font("SansSerif", Font.PLAIN, 30));
            g.drawString("Vote 3: " + listBouleVote3.size(), 3*(longueur/4)-300, (largeur/5));
            g.drawImage(imageVote3, 3*(longueur/4)-300, (largeur/4), 200, 200, null);

            g.setFont(new Font("SansSerif", Font.PLAIN, 30));
            g.drawString("Vote 4: " + listBouleVote4.size(), longueur-300, (largeur/5));
            g.drawImage(imageVote4, longueur-300, (largeur/4), 200, 200, null);
        }
        if (affiche_resultat) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("SansSerif", Font.PLAIN, 30));
            if (listBouleVote1.size() > listBouleVote2.size() && listBouleVote1.size() > listBouleVote3.size()
                    && listBouleVote1.size() > listBouleVote4.size()) {
                vote_choisi = 1;
                g.drawString("Le choix n°1 a été selectionné", (longueur/4), (largeur/6));
            } else if (listBouleVote2.size() > listBouleVote1.size() && listBouleVote2.size() > listBouleVote3.size()
                    && listBouleVote2.size() > listBouleVote4.size()) {
                vote_choisi = 2;
                g.drawString("Le choix n°2 a été selectionné", (longueur/4), (largeur/6));
            } else if (listBouleVote3.size() > listBouleVote1.size() && listBouleVote3.size() > listBouleVote2.size()
                    && listBouleVote3.size() > listBouleVote4.size()) {
                vote_choisi = 3;
                g.drawString("Le choix n°3 a été selectionné",(longueur/4), (largeur/6));
            } else if (listBouleVote4.size() > listBouleVote1.size() && listBouleVote4.size() > listBouleVote3.size()
                    && listBouleVote4.size() > listBouleVote2.size()) {
                vote_choisi = 4;
                g.drawString("Le choix n°4 a été selectionné", (longueur/4), (largeur/6));
            } else {
                if (vote_choisi == -1) {
                    Random rand = new Random();
                    vote_choisi = rand.nextInt(4) + 1;
                }
                g.drawString("Aucun vote a été majoritaire ! Le vote N°" + vote_choisi + " a été choisi !", (longueur/4), (largeur/6));
            }
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // //
        // CONTROLE JOUEURS //
        // //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        StringBuilder tabScore = new StringBuilder("score;");
        for (Map.Entry<Integer, Personnage> entry : joueurEnVie.entrySet()) {
            Personnage persoJoueurActuel = entry.getValue();

            MajPlusRiches(persoJoueurActuel.getNumero(), persoJoueurActuel.getScore());

            Vector2 dir = persoJoueurActuel.getDirection();

            //pour faire un rendu plus lisse
            for (int i = 0; i < 2; i++) {
                persoJoueurActuel.translate(dir);
            }

            //Fonction de tir autour du joueur
            if (persoJoueurActuel.getTir() && persoJoueurActuel.getScore() >= 10){
                for (Personnage perso : joueurEnVie.values()) {
                    if (perso != persoJoueurActuel){
                        Vector2 playerPosition = perso.getWorldCenter();
                        double distance = persoJoueurActuel.getWorldCenter().distance(playerPosition);
                        if (distance < 0.5) {
                            Vector2 direction = playerPosition.subtract(persoJoueurActuel.getWorldCenter()).getNormalized();
                            Vector2 force = direction.product(0.08);
                            perso.applyImpulse(force);
                        }
                    }
                }
                persoJoueurActuel.setScore(persoJoueurActuel.getScore()-10);
                persoJoueurActuel.setTir(false);
            }
            else if (persoJoueurActuel.getTir()){
                persoJoueurActuel.setTir(false);
            }

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // //
            // GESTION COLISION MURS //
            // //
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            if (partieEnCours){
                if (persoJoueurActuel.getWorldCenter().x < -longueur_map / 2 + epaisseur_mur / 2 + persoJoueurActuel.getTaille()
                        || persoJoueurActuel.getWorldCenter().x > longueur_map / 2 - epaisseur_mur / 2 - persoJoueurActuel.getTaille()) {
                    this.world.removeBody(persoJoueurActuel);
                }
                if (persoJoueurActuel.getWorldCenter().y < -largeur_map / 2 + epaisseur_mur / 2 + persoJoueurActuel.getTaille()
                        || persoJoueurActuel.getWorldCenter().y > largeur_map / 2 - epaisseur_mur / 2 - persoJoueurActuel.getTaille()) {
                    this.world.removeBody(persoJoueurActuel);
                }
            }
            /*GESTION DES PIECES*/
            for (Piece piece : listPiece) {
                // le calcul de la distance entre 2 cercles pour savoir si ca croise
                double distanceCentres = Math.sqrt(Math.pow(piece.getWorldCenter().x - persoJoueurActuel.getWorldCenter().x, 2)
                                + Math.pow(piece.getWorldCenter().y - persoJoueurActuel.getWorldCenter().y, 2));
                if (distanceCentres < IConfig.rayonPiece + persoJoueurActuel.getTaille()) {
                    joueurEnVie.get(persoJoueurActuel.getId()).addScore(piece.getValeur());
                    listeJoueurs.get(persoJoueurActuel.getId()).addScore(piece.getValeur());
                    // on reactualise le score et on l'envoi aux clients
                    String message = String.valueOf(tabScore.append(persoJoueurActuel.getId()).append(";").append(persoJoueurActuel.getScore()).append(";"));
                    envoyerMessage(message);
                    this.ajoutPiece();
                    this.world.removeBody(piece);
                    listPiece.remove(piece);
                    break;
                }
            }
        }
        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.PLAIN, 30));
        int posXAffichage = longueur-(longueur-700);
        for (Map.Entry<Integer, Integer> entry : JoueursRiches.entrySet()) {
            g.drawString("n°" + entry.getKey() + " : " + entry.getValue() + "$ |", posXAffichage, largeur-50);
            posXAffichage += 120;
        }

        gereMur();
    }

    public String printScore(){
        StringBuilder tabScore = new StringBuilder("score;");
        for(Personnage perso : listeJoueurs.values()){
            tabScore.append(perso.getId()).append(";").append(perso.getScore()).append(";");
        }
        return tabScore.toString();
    }


    protected void initializeCameraAccueil(Camera camera) {
        super.initializeCamera(camera);
        camera.scale = 230.0;
        camera.offsetX = IConfig.DEPLACEMENT_VERTICALE_ACCUEIL * 230;
    }

    protected void initializeCamera(Camera camera) {
        super.initializeCamera(camera);
        camera.scale = 250.0;
    }

    protected void initializeCameraVote(Camera camera) {
        super.initializeCamera(camera);
        camera.scale = 230.0;
        camera.offsetX = IConfig.DEPLACEMENT_VERTICALE_VOTE * 230;
    }

    protected void initializeCameraScore(Camera camera) {
        super.initializeCamera(camera);
        camera.scale = 230.0;
        camera.offsetX = IConfig.DEPLACEMENT_VERTICALE_SCORE * 230;
    }


    private void afficheAccueil() {
        this.partieEnCours = false;
        initializeCameraAccueil(getCamera());
    }

    private synchronized void afficheGame() {
        initializeCamera(this.getCamera());
        this.tuerJoueur();
        this.ajoutJoueur();
        ajoutObstacle();
        this.world.setGravity(new Vector2(0.0, 0.0));
        vote_affichage = false;
    }


    private void afficheScore() {
        initializeCameraScore(this.getCamera());
        // afficher les scores
    }

    private void afficheVote() {
        initializeCameraVote(this.getCamera());
        tuerJoueur();
        resetAllVote();
        this.world.setGravity(new Vector2(0.0, -5));
        //////////////////////////////////
        // //
        // ON RESET LES ATTRIBUTS //
        // //
        //////////////////////////////////
        total_vote = 0;
        vote_affichage = true;
        for (int i = 0; i < listBouleVote1.size(); i++) {
            this.world.removeBody(listBouleVote1.get(i));
        }
        for (int i = 0; i < listBouleVote2.size(); i++) {
            this.world.removeBody(listBouleVote2.get(i));
        }
        for (int i = 0; i < listBouleVote3.size(); i++) {
            this.world.removeBody(listBouleVote3.get(i));
        }
        for (int i = 0; i < listBouleVote4.size(); i++) {
            this.world.removeBody(listBouleVote4.get(i));
        }
        listBouleVote1.clear();
        listBouleVote2.clear();
        listBouleVote3.clear();
        listBouleVote4.clear();
        repaint();
    }


    public void ecouter() {
        try {
            while (!serveur.getFin()) {
                Socket socketAttente = serveur.getServSocket().accept();
                if (socketAttente != null) {
                    System.out.println("Nouveau client connecté: " + socketAttente.getInetAddress().getHostAddress());
                    serveur.getListClientSocket().add(socketAttente);
                    // ouvrir le flux pour le socketATtente
                    DataOutputStream dOut = serveur.ouvrirFlux(socketAttente);
                    serveur.getListdIn().add(new DataInputStream(socketAttente.getInputStream()));
                    serveur.getListdOut().add(new DataOutputStream(dOut));
                    if (dOut != null) {
                        serveur.envoyerConfirm(dOut);
                    }
                    lancerThread(serveur.getListClientSocket().indexOf(socketAttente));
                }
            }
        } catch (IOException e) {
            if (!serveur.getFin()) {
                e.printStackTrace();
            }
        }
    }
    // envoi une confirm
    public  void envoyerMessage(String message) {
        for (DataOutputStream out : serveur.getListdOut()) {
            try {
                out.writeUTF(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public synchronized void lancerThread(int acc) {
        // lancement d'un thread pour chaque joueur
        new Thread(() -> {
            while (true) {
                String message = serveur.getMessageFromSocket(acc);
                this.gererMessage(message);
            }
        }).start();
    }

    /**
     * Gere les messages envoyé depuis le serveur
     *
     * @param message
     */
    private synchronized void gererMessage(String message) {
        try {
            StringTokenizer strTok = new StringTokenizer(message, ";");
            String demande = strTok.nextToken();
            String ID_JOUEUR = strTok.nextToken();
            int idJoueur;
            Personnage p;
            String messageScore;
            switch (demande) {
                case "deplacement":
                    while (strTok.hasMoreTokens()) {
                        p = joueurEnVie.get(Integer.valueOf(ID_JOUEUR));
                        double dX = Double.parseDouble(strTok.nextToken()) * p.getVitesse();
                        double dY = Double.parseDouble(strTok.nextToken()) * p.getVitesse();
                        p.setDirection(new Vector2(dX, dY));
                    }
                    break;
                case "addJoueur":
                    idJoueur = Integer.parseInt(ID_JOUEUR);
                    int r = Integer.parseInt(strTok.nextToken());
                    int g = Integer.parseInt(strTok.nextToken());
                    int b = Integer.parseInt(strTok.nextToken());
                    int numJoueur = Integer.parseInt(strTok.nextToken());
                    Personnage perso = new Personnage(new Color(r, g, b),IConfig.rayonPersonnage, IConfig.vitesseDeplacement,1, 0, 1);
                    perso.translate(-IConfig.DEPLACEMENT_VERTICALE_ACCUEIL, 0);
                    perso.setId(Integer.parseInt(ID_JOUEUR));

                    perso.setCouleur(new Color(r,g,b));
                    perso.setDirection(new Vector2(0,0));
                    perso.setNumero(numJoueur);
                    this.world.addBody(perso);
                    this.joueurEnVie.put(Integer.valueOf(ID_JOUEUR), perso);
                    // On ajoute le joueur dans la liste (instanciation)
                    this.listeJoueurs.put(idJoueur, perso);
                    System.out.println("On a ajouter le joueur");
                    messageScore = printScore();
                    envoyerMessage(messageScore);
                    break;
                case "tir":
                    idJoueur = Integer.parseInt(ID_JOUEUR);
                    p = joueurEnVie.get(idJoueur);
                    p.setTir(true);
                    break;
                case "vote":
                    String choixvote = strTok.nextToken();
                    if (this.listeJoueurs.containsKey(Integer.valueOf(ID_JOUEUR))) {
                        if (!this.listeJoueurs.get(Integer.valueOf(ID_JOUEUR)).getVote()) {
                            this.listeJoueurs.get(Integer.valueOf(ID_JOUEUR)).setVote(true);
                            genererNouveauVote(Integer.parseInt(choixvote));
                        }
                    }
                    break;
                case "shop":
                    int numAchat = Integer.parseInt(strTok.nextToken());
                    idJoueur = Integer.parseInt(ID_JOUEUR);
                    Personnage pListeJ = listeJoueurs.get(idJoueur);
                    switch(numAchat) {
                        case 1: //Vitesse ++
                            if (pListeJ.getScore()>=5 && pListeJ.getVitesse()<0.017){
                                pListeJ.setVitesse(pListeJ.getVitesse() + 0.0005);
                                pListeJ.setScore(pListeJ.getScore()-5);
                                if (partieEnCours){
                                    p = joueurEnVie.get(idJoueur);
                                    if (p!=null){
                                        p.setVitesse(p.getVitesse() + 0.0005);
                                        p.setScore(p.getScore()-5);
                                    }
                                }
                            }
                            break;
                        case 2: //Vitesse --
                            if (pListeJ.getScore()>=5 && pListeJ.getVitesse()>0.002) {
                                pListeJ.setVitesse(pListeJ.getVitesse() - 0.0005);
                                pListeJ.setScore(pListeJ.getScore()-5);
                                if (partieEnCours){
                                    p = joueurEnVie.get(idJoueur);
                                    if (p!=null){
                                        p.setVitesse(p.getVitesse() - 0.0005);
                                        p.setScore(p.getScore()-5);
                                    }
                                }
                            }
                            break;
                        case 3: //Taille ++
                            if (pListeJ.getScore()>=5 && pListeJ.getTaille()<0.18) {
                                pListeJ.setTaille(pListeJ.getTaille() + 0.005);
                                pListeJ.setScore(pListeJ.getScore()-5);
                                if (partieEnCours){
                                    p = joueurEnVie.get(idJoueur);
                                    if (p!=null){
                                        p.setTaille(p.getTaille() + 0.005);
                                        p.setScore(p.getScore()-5);
                                    }
                                }
                            }
                            break;
                        case 4: //Taille --
                            if (pListeJ.getScore()>=5 && pListeJ.getTaille()>0.07) {
                                pListeJ.setTaille(pListeJ.getTaille() - 0.005);
                                pListeJ.setScore(pListeJ.getScore()-5);
                                if (partieEnCours){
                                    p = joueurEnVie.get(idJoueur);
                                    if (p!=null){
                                        p.setTaille(p.getTaille() - 0.005);
                                        p.setScore(p.getScore()-5);
                                    }
                                }
                            }
                            break;
                    }
                    messageScore = printScore();
                    envoyerMessage(messageScore);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Fonction permettant de voter
     *
     * @param numeroVote
     */
    private void genererNouveauVote(int numeroVote) {
        total_vote++;
        Random random = new Random();
        double decalage = random.nextDouble() - 0.5;
        Boule belleBoule = new Boule(IConfig.couleurVote1, 0.1, 150, 0.08, 0);
        switch (numeroVote) {
            case 1:
                belleBoule.setColor(IConfig.couleurVote1);
                listBouleVote1.add(belleBoule);
                belleBoule.translate(-2 - IConfig.DEPLACEMENT_VERTICALE_VOTE + decalage, 0.1);
                world.addBody(belleBoule);
                break;
            case 2:
                belleBoule.setColor(IConfig.couleurVote2);
                listBouleVote2.add(belleBoule);
                belleBoule.translate(-0.5 - IConfig.DEPLACEMENT_VERTICALE_VOTE + decalage, 0.1);
                world.addBody(belleBoule);
                break;
            case 3:
                belleBoule.setColor(IConfig.couleurVote3);
                listBouleVote3.add(belleBoule);
                belleBoule.translate(2 - IConfig.DEPLACEMENT_VERTICALE_VOTE + decalage, 0.1);
                world.addBody(belleBoule);
                break;
            case 4:
                belleBoule.setColor(IConfig.couleurVote4);
                listBouleVote4.add(belleBoule);
                belleBoule.translate(3.5 - IConfig.DEPLACEMENT_VERTICALE_VOTE + decalage, 0.1);
                world.addBody(belleBoule);
                break;
            default:
        }
    }

    private void startGame() {
        this.minuteur = new Timer();
        this.minuteur.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateTimer();
            }
        }, 0, 1000);
    }

    /**
     * Squelette de la pipeline. Cette fonction va faire la transition de chaque scene
     */
    private void updateTimer() {
        seconds++;
        // Jeu Vote Result Jeu Vote Result Jeu Vote Result Jeu Score Accueil
        nextStep--;
        if (nextStep == 0) {
            if (actuelStep == 0 || actuelStep == 3 || actuelStep == 6 || actuelStep == 9) { // Jeu
                afficheGame();
                chronoBombe.start();
                nextStep = IConfig.tempsJeu;
                affiche_resultat = false;
                if(actuelStep != 0){
                    faireVote();
                }
                vote_choisi = -1;
            } else if (actuelStep == 1 || actuelStep == 4 || actuelStep == 7) { // Vote
                if(actuelStep == 1){
                    changerImage1();
                }else if(actuelStep == 4){
                    changerImage2();
                }else {
                    changerImage3();
                }
                nextStep = IConfig.tempsVote;
                chronoBombe.stop();
                afficheVote();
            } else if (actuelStep == 10) { // Score
                afficheScore();
                nextStep = IConfig.tempsScore;
            } else if (actuelStep == 2 || actuelStep == 5 || actuelStep == 8) {
                affiche_resultat = true;
                nextStep = IConfig.tempsResultatVote;
            } else if (actuelStep == 11) { // Fin jeu
                nextStep = 1;
                actuelStep = 0;
                tuerJoueur();
                deleteObstacle();
                chronoBombe.stop();
                afficheAccueil();
                resetScore();
                this.minuteur.cancel();
                return;
            }
            actuelStep++;
        }
    }

    /**
     * Tuer les joueus
     */
    private synchronized void tuerJoueur() {
        for (Personnage joueur : joueurEnVie.values()) {
            this.world.removeBody(joueur);
        }
        for (Personnage joueur : listeJoueurs.values()) {
            this.world.removeBody(joueur);
        }
        joueurEnVie.clear();
    }


    /**
     * Ajoute chaque joueur au monde lors du changement d'instance
     */
    private synchronized void ajoutJoueur() {
        for (Map.Entry<Integer, Personnage> entry : listeJoueurs.entrySet()) {
            Personnage perso = new Personnage(entry.getValue().getCouleur(),entry.getValue().getTaille(),entry.getValue().getVitesse(),1, 0, 1);
            perso.setId(entry.getValue().getId());
            perso.setScore(entry.getValue().getScore());
            perso.setNumero(entry.getValue().getNumero());
            perso.setDirection(new Vector2(0, 0));
            this.world.addBody(perso);
            this.joueurEnVie.put(perso.getId(), perso);
        }
    }

    /**
     * Ajoute une piece au monde
     */
    public void ajoutPiece() {
        Random rand = new Random();
        double x;
        double y;
        do {
            x = (-3.2 + rand.nextDouble() * (3.2 - -3.2));
            y = (-1 + rand.nextDouble() * (1 - -1));
        } while (!verifSpawnPiece(x, y));

        Piece piece = new Piece(new Vector2(x, y));
        this.listPiece.add(piece);
        this.world.addBody(piece);
    }

    /**
     * Fonction permettant de faire la verif de spawn des pieces a des endroits ok
     *
     * @param x
     * @param y
     * @return
     */
    public boolean verifSpawnPiece(double x, double y) {
        for (Tourniquet tourni : listTourniquet) {
            if (cerclesSeTouchent(x, y, IConfig.rayonPiece, tourni.getWorldCenter().x, tourni.getWorldCenter().y, 0.5 /* longeuru ntourniquet */)) {
                return false;
            }
        }
        for (Ressort ressort : listRessort) {
            if (cerclesSeTouchent(x, y, IConfig.rayonPiece, ressort.getWorldCenter().x, ressort.getWorldCenter().y, 0.3 /* diametre ressort */)) {
                return false;
            }
        }
        // les bombes explosent donc pas besoin
        return true;
    }


    /**
     * Renvoi un boolean suivant si 2 cercles se touchent
     *
     * @param x1
     * @param y1
     * @param r1
     * @param x2
     * @param y2
     * @param r2
     * @return
     */
    public boolean cerclesSeTouchent(double x1, double y1, double r1, double x2, double y2, double r2) {
        double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        return distance <= (r1 + r2);
    }

    public void ajoutBombe() {
        Random rand = new Random();
        int x;
        // Verif pour pas faire de boucle infinie
        if (Arrays.stream(emplacementBombe).sum() == emplacementBombe.length) {
            System.out.println("Le nombre maximal de bombe a été atteint");
            return;
        }
        do {
            x = rand.nextInt(4);
        } while (emplacementBombe[x] == 1);
        Bombe bombe = new Bombe(Color.RED, 0.2, 1.5, new Vector2(0, 0));
        int xx = 0;
        int yy = 0;
        switch (x) {
            case 0:
                xx = -2;
                yy = -1;
                break;
            case 1:
                xx = -2;
                yy = +1;
                break;
            case 2:
                xx = +2;
                yy = -1;
                break;
            case 3:
                xx = +2;
                yy = +1;
                break;
        }
        emplacementBombe[x] = 1;
        bombe.translate(xx, yy);
        this.world.addBody(bombe);
        listBombe.add(bombe);
    }

    public void ajoutBombe(int emplacement) {
        if (emplacementBombe[emplacement - 1] == 1) {
            return;
        }
        Bombe bombe = new Bombe(Color.RED, 0.2, 1.5, new Vector2(0, 0));
        int xx = 0;
        int yy = 0;
        switch (emplacement-1) {
            case 0:
                xx = -2;
                yy = -1;
                break;
            case 1:
                xx = -2;
                yy = +1;
                break;
            case 2:
                xx = +2;
                yy = -1;
                break;
            case 3:
                xx = +2;
                yy = +1;
                break;
        }
        emplacementBombe[emplacement-1] = 1;
        bombe.translate(xx, yy);
        this.world.addBody(bombe);
        listBombe.add(bombe);
    }
    public void removeBombe(int emplacement) {
        boolean broke = false;
        for (Bombe bombe : listBombe) {
            switch (emplacement) {
                case 1:
                    if (bombe.getWorldCenter().x == -2 && bombe.getWorldCenter().y == -1) {
                        this.world.removeBody(bombe);
                        listBombe.remove(bombe);
                        emplacementBombe[0] = 0;
                        broke = true;
                    }
                    break;
                case 2:
                    if (bombe.getWorldCenter().x == (2) && bombe.getWorldCenter().y == 1) {
                        this.world.removeBody(bombe);
                    listBombe.remove(bombe);
                    emplacementBombe[1] = 0;
                        broke = true;
                    }
                    break;
                case 3:
                    if (bombe.getWorldCenter().x == 2 && bombe.getWorldCenter().y == -1) {
                        this.world.removeBody(bombe);
                        listBombe.remove(bombe);
                        emplacementBombe[2] = 0;
                        broke = true;
                    }
                    break;
                case 4:
                    if (bombe.getWorldCenter().x == 2 && bombe.getWorldCenter().y == 1) {
                        this.world.removeBody(bombe);
                        listBombe.remove(bombe);
                        emplacementBombe[3] = 0;
                        broke = true;
                    }
                    break;

            }
            if (broke) {
                break;
            }
        }
    }
    public void ajoutTourniquet(double longueur, int sens, double vitesse) {
        // sens = -1 (sens trigonométrique)
        // = 1 (sens horaire)
        // 500 <= vitesse <= 1000
        Random rand = new Random();
        int x;
        // Verif pour pas faire de boucle infinie
        if (Arrays.stream(emplacementTourniquet).sum() == emplacementTourniquet.length) {
            System.out.println("Le nombre maximal de Tourniquet a été atteint");
            return;
        }
        do {
            x = rand.nextInt(4);
        } while (emplacementTourniquet[x] == 1);
        Tourniquet tourniquet;
        int xx = 0;
        int yy = 0;
        switch (x) {
            case 0: // -1 0
                xx = -3;
                break;
            case 1: // 0 0
                xx = -1;
                break;
            case 2: // 1 0
                xx = 1;
                break;
            case 3: // 2 0
                xx = 3;

                break;
        }

        emplacementTourniquet[x] = 1;
        tourniquet = new Tourniquet(xx, yy, longueur, 0.1);
        tourniquet.setCentre(new Mur(new Color(100, 42, 42), 0.01, 0.01, xx, yy));
        this.world.addBody(tourniquet.getCentre());
        this.world.addBody(tourniquet);
        tourniquet.setJoint(new RevoluteJoint<SimulationBody>(tourniquet.getCentre(), tourniquet, new Vector2(xx, yy)));
        if (vitesse < 500)
            vitesse = 500;
        if (vitesse > 1000)
            vitesse = 1000;

        tourniquet.getJoint().setMotorEnabled(true);
        tourniquet.getJoint().setMotorSpeed(Math.toRadians(vitesse * sens));
        tourniquet.getJoint().setMaximumMotorTorque(10000.0);
        listTourniquet.add(tourniquet);

        this.world.addJoint(tourniquet.getJoint());
    }

    public void ajoutTourniquet(double longueur, int sens, double vitesse, int emplacement) {
        if (emplacementTourniquet[emplacement - 1] == 1) {
            return;
        }
        Tourniquet tourniquet;
        int xx = 0;
        int yy = 0;
        switch (emplacement - 1) {
            case 0: // -1 0
                xx = -3;
                break;
            case 1: // 0 0
                xx = -1;
                break;
            case 2: // 1 0
                xx = 1;
                break;
            case 3: // 2 0
                xx = 3;

                break;
        }

        emplacementTourniquet[emplacement - 1] = 1;
        tourniquet = new Tourniquet(xx, yy, longueur, 0.1);
        tourniquet.setCentre(new Mur(new Color(100, 42, 42), 0.01, 0.01, xx, yy));
        this.world.addBody(tourniquet.getCentre());
        this.world.addBody(tourniquet);
        tourniquet.setJoint(new RevoluteJoint<SimulationBody>(tourniquet.getCentre(), tourniquet, new Vector2(xx, yy)));
        if (vitesse < 500)
            vitesse = 500;
        if (vitesse > 1000)
            vitesse = 1000;

        tourniquet.getJoint().setMotorEnabled(true);
        tourniquet.getJoint().setMotorSpeed(Math.toRadians(vitesse * sens));
        tourniquet.getJoint().setMaximumMotorTorque(10000.0);
        listTourniquet.add(tourniquet);

        this.world.addJoint(tourniquet.getJoint());
    }

    public void removeTourniquet(int emplacement) {
        boolean broke = false;
        for (Tourniquet tourni : listTourniquet) {
            switch (emplacement) {
                case 1:
                    if (tourni.getWorldCenter().x == -3 && tourni.getWorldCenter().y == 0) {
                        this.world.removeBody(tourni);
                        listTourniquet.remove(tourni);
                        emplacementTourniquet[0] = 0;
                        broke = true;
                    }
                    break;
                case 2:
                    if (tourni.getWorldCenter().x == -1 && tourni.getWorldCenter().y == 0) {
                        this.world.removeBody(tourni);
                        listTourniquet.remove(tourni);
                        emplacementTourniquet[1] = 0;
                        broke = true;
                    }
                    break;
                case 3:
                    if (tourni.getWorldCenter().x == 1 && tourni.getWorldCenter().y == 0) {
                        this.world.removeBody(tourni);
                        listTourniquet.remove(tourni);
                        emplacementTourniquet[2] = 0;
                        broke = true;
                    }
                    break;
                case 4:
                    if (tourni.getWorldCenter().x == 3 && tourni.getWorldCenter().y == 0) {
                        this.world.removeBody(tourni);
                        listTourniquet.remove(tourni);
                        emplacementTourniquet[3] = 0;
                        broke = true;
                    }
                    break;

            }
            if (broke) {
                break;
            }
        }
    }

    public void resetScore() {
        for (HashMap.Entry<Integer, Personnage> entry : listeJoueurs.entrySet()) {
            entry.getValue().setScore(0);
        }
    }

    public boolean exploseUneBombe() {
        if (!listBombe.isEmpty()) {
            Bombe bombe = listBombe.get(0);
            if (bombe != null) {
                Vector2 positionBombe = new Vector2(bombe.getWorldCenter().x, bombe.getWorldCenter().y);
                bombe.expulserJoueurs(joueurEnVie, positionBombe);
                this.world.removeBody(bombe);
                if (bombe.getWorldCenter().x == -2 && bombe.getWorldCenter().y == -1) {
                    emplacementBombe[0] = 0;
                } else if (bombe.getWorldCenter().x == -2 && bombe.getWorldCenter().y == 1) {
                    emplacementBombe[1] = 0;
                } else if (bombe.getWorldCenter().x == 2 && bombe.getWorldCenter().y == -1) {
                    emplacementBombe[2] = 0;

                } else if (bombe.getWorldCenter().x == 2 && bombe.getWorldCenter().y == 1) {
                    emplacementBombe[3] = 0;
                }
                Musique.playSound("./Serveur/src/main/java/son/explosion.wav");
                listBombe.remove(0);
                return true;
            }
        }
        return false;
    }


    public void ajoutRessort(double rayon, double force, int type) {
        Random rand = new Random();
        int x;
        // Verif pour pas faire de boucle infinie
        if (Arrays.stream(emplacementRessort).sum() == emplacementRessort.length) {
            System.out.println("Le nombre maximal de Ressort a été atteint");
            return;
        }
        do {
            x = rand.nextInt(2);
        } while (emplacementRessort[x] == 1);
        int xx = 0;
        int yy = 0;
        switch (x) {
            case 0:
                yy = -1;
                break;
            case 1:
                yy = 1;
                break;
        }
        emplacementRessort[x] = 1;
        Ressort ressort = new Ressort(xx, yy, rayon, force, type);
        listRessort.add(ressort);
        this.world.addBody(ressort);
    }
    public void ajoutRessort(double rayon, double force, int type,int emplacement) {
        if (emplacementRessort[emplacement - 1] == 1) {
            return;
        }
        int xx = 0;
        int yy = 0;
        switch (emplacement-1) {
            case 0:
                yy = -1;
                break;
            case 1:
                yy = 1;
                break;
        }
        emplacementRessort[emplacement-1] = 1;
        Ressort ressort = new Ressort(xx, yy, rayon, force, type);
        listRessort.add(ressort);
        this.world.addBody(ressort);
    }
    public void removeRessort(int emplacement) {
        boolean broke = false;
        for (Ressort ressort : listRessort) {
            switch (emplacement) {
                case 1:
                    if (ressort.getWorldCenter().x == 0 && ressort.getWorldCenter().y == -1) {
                        this.world.removeBody(ressort);
                        listRessort.remove(ressort);
                        emplacementRessort[0] = 0;
                        broke = true;
                    }
                    break;
                case 2:
                    if (ressort.getWorldCenter().x == 0 && ressort.getWorldCenter().y == 1) {
                        this.world.removeBody(ressort);
                        listRessort.remove(ressort);
                        emplacementRessort[1] = 0;
                        broke = true;
                    }
                    break;


            }
            if (broke) {
                break;
            }
        }
    }
    public void resetAllVote() {
        for (HashMap.Entry<Integer, Personnage> entry : listeJoueurs.entrySet()) {
            entry.getValue().setVote(false);
        }
    }

    /**
     * Permet de delete tous les obstacles sur le world
     */
    public void deleteObstacle() {
        for (Tourniquet tourni : listTourniquet) {
            this.world.removeBody(tourni);
            this.world.removeBody(tourni.getCentre());
        }
        emplacementTourniquet = new int[]{0, 0, 0, 0};
        for (Ressort res : listRessort) {
            this.world.removeBody(res);
        }
        emplacementRessort = new int[]{0, 0, 0, 0};
        for (Piece piece : listPiece) {
            this.world.removeBody(piece);
        }
        for (Bombe bombaAfrica : listBombe) {
            this.world.removeBody(bombaAfrica);
        }
        emplacementBombe = new int[]{0, 0, 0, 0};
        listBombe.clear();
        listPiece.clear();
        listRessort.clear();
        listTourniquet.clear();
    }

    /**
     * Fonction pour ajouter les obstacles a chaque instance de la pipeline
     */
    public void ajoutObstacle() {
        deleteObstacle();
        // Tourniquet
        System.out.println("Il y a : "+ IConfig.nombreTourniquet);
        for (int i = 0; i < IConfig.nombreTourniquet; i++) {
            ajoutTourniquet(1, -1, 500);
        }
        // Ressort
        for (int i = 0; i < IConfig.nombreRessort; i++) {
            ajoutRessort(0.3, 1, Ressort.IMMOBILE);
        }
        // Piece
        for (int i = 0; i < IConfig.nombrePiece; i++) {
            ajoutPiece();
        }
        // Bombe
        for (int i = 0; i < IConfig.nombreBombe; i++) {
            ajoutBombe();
        }
    }

    /**
     * Fonction permettant d'effectuer le resultat du vote suivant l'étape de la pipeline
     */
    public void faireVote() {
        // vote_choisi contient le resultat du vote
        if (actuelStep == 3) {
            // vote 1
            switch (vote_choisi) {
                case 1:
                    IConfig.nombreBombe++;
                    break;
                case 2:
                    IConfig.nombreRessort++;
                    break;
                case 3:
                    IConfig.nombreTourniquet++;
                    break;
                case 4:
                    IConfig.nombrePiece += 5;
                    break;

                default:
                    break;
            }
        } else if (actuelStep == 6) {
            // vote 2
            switch (vote_choisi) {
                case 1:
                    IConfig.nombreBombe++;
                    IConfig.nombreRessort++;
                    break;
                case 2:
                    IConfig.nombreRessort++;
                    break;
                case 3:
                    IConfig.nombreTourniquet++;
                    break;
                case 4:
                    IConfig.nombrePiece += 5;
                    break;

                default:
                    break;
            }
        } else if (actuelStep == 9) {
            // vote 3
            switch (vote_choisi) {
                case 1:
                    for(Personnage perso : listeJoueurs.values()){
                        perso.setVitesse(perso.getVitesse()+0.0005);
                    }
                    break;
                case 2:
                    IConfig.coeff_vitesse_mur += 0.15;
                    break;
                case 3:
                    IConfig.temps_attente_mur += 1000;
                    break;
                case 4:
                    IConfig.coeff_taille_trou_mur -= 0.25;
                    break;
                default:
                    break;
            }
        }
        afficheGame();
    }

    public double ajoutMurTroue(int type, double taille, double vitesseMur, double vitesseTrou) {
        Random random;
        double posTrou;
        random = new Random();
        switch (type) {
            case MurTroue.GAUCHE:
                posTrou = random.nextDouble() * (MurTroue.MH - 0.5 - (MurTroue.MB + 0.5)) + MurTroue.MB + 0.5;
                break;
            case MurTroue.HAUT:
                posTrou = random.nextDouble() * (MurTroue.MD - 0.5 - (MurTroue.MG + 0.5)) + MurTroue.MG + 0.5;
                break;
            case MurTroue.DROITE:
                posTrou = random.nextDouble() * (MurTroue.MH - 0.5 - (MurTroue.MB + 0.5)) + MurTroue.MB + 0.5;
                break;
            case MurTroue.BAS:
                posTrou = random.nextDouble() * (MurTroue.MD - 0.5 - (MurTroue.MG + 0.5)) + MurTroue.MG + 0.5;
                break;
            default:
                posTrou = random.nextDouble() * (MurTroue.MH - 0.5 - (MurTroue.MB + 0.5)) + MurTroue.MB + 0.5;
                break;
        }
        MurTroue mur1 = new MurTroue(0, posTrou, taille, vitesseMur, vitesseTrou, type);
        MurTroue mur2 = new MurTroue(1, posTrou, taille, vitesseMur, vitesseTrou, type);
        this.world.addBody(mur1);
        this.world.addBody(mur2);
        this.listeMursTroues.add(mur1);
        this.listeMursTroues.add(mur2);

        return posTrou;
    }

    /**
     * Cette fonction permet d'ajouter un mur
     *
     * @param type
     * @param taille
     * @param vitesseMur
     * @param vitesseTrou
     * @param posTrou
     */
    public void ajoutMurTroue(int type, double taille, double vitesseMur, double vitesseTrou, double posTrou) {
        //version ou la position du trou y est décidée
        MurTroue mur1 = new MurTroue(0, posTrou, taille, vitesseMur, vitesseTrou, type);
        MurTroue mur2 = new MurTroue(1, posTrou, taille, vitesseMur, vitesseTrou, type);
        this.world.addBody(mur1);
        this.world.addBody(mur2);
        this.listeMursTroues.add(mur1);
        this.listeMursTroues.add(mur2);
    }

    /**
     * @bref Permet de gerer le deplacement des murs
     */
    public void gereMur() {
        Random random = new Random(); // utilisé pour des calculs aléatoires
        MurTroue mt;
        // ajout d'un mur lorsqu'il n'y en a pas sur le terrain de jeu
        if (listeMursTroues.isEmpty() && rafale == 0) {
            try {
                Thread.sleep(IConfig.temps_attente_mur);
            } catch (Exception e) {
                e.printStackTrace();
            }
            typeMur = random.nextInt(4);
            rafale = random.nextInt(5);
            posTrou = ajoutMurTroue(typeMur, 1, 0.03, 0.05);
            cooldown = random.nextInt(60 - 20) + 20; //donne un temps de "recharge" entre chaque mur d'une rafale
        }

        if (rafale > 0) {
            if (cooldown <= 0) {
                ajoutMurTroue(typeMur, 1, 0.03, 0.05, posTrou);
                cooldown = random.nextInt(60 - 20) + 20;
                ; //donne un temps de "recharge" entre chaque mur d'une rafale
                rafale--;
            } else {
                cooldown--;
            }
        }

        for (int i = 0; i < listeMursTroues.size(); i++) {
            mt = listeMursTroues.get(i);
            switch (mt.getType()) {
                case MurTroue.GAUCHE:
                    // Si le mur est en dehors de la map on l'enlève
                    if (mt.getWorldCenter().x > longueur_map / 2 + 1) {
                        this.world.removeBody(mt);
                        listeMursTroues.remove(mt);
                        i--;
                    }
                    // Si le mur à atteint le plafond ou le fond, on inverse son déplacement Y
                    if (mt.getWorldCenter().y + mt.getHauteur() / 2 < MurTroue.MB || mt.getWorldCenter().y - mt.getHauteur() / 2 > MurTroue.MH) {
                        mt.setChangement(true);
                        if (mt.getPosition() == 0) { //mur haut / droit
                            if (i < listeMursTroues.size())
                                listeMursTroues.get(i + 1).setChangement(true);
                        } else { //mur bas / gauche
                            if (i > 0)
                                listeMursTroues.get(i - 1).setChangement(true);
                        }
                    }
                    // On déplace le mur
                    if (mt.getChangement() == true) {
                        mt.setTranslationY(-mt.getTranslationY());
                        mt.setChangement(false);
                    }
                    mt.translate(mt.getTranslationX() * IConfig.coeff_vitesse_mur, mt.getTranslationY() * IConfig.coeff_vitesse_mur);
                    break;
                case MurTroue.DROITE:
                    // Si le mur est en dehors de la map on l'enlève
                    if (mt.getWorldCenter().x < -longueur_map / 2 - 1) {
                        this.world.removeBody(mt);
                        listeMursTroues.remove(mt);
                        i--;
                    }
                    // Si le mur à atteint le plafond ou le fond, on inverse son déplacement Y
                    else if (mt.getWorldCenter().y + mt.getHauteur() / 2 < MurTroue.MB || mt.getWorldCenter().y - mt.getHauteur() / 2 > MurTroue.MH) {
                        mt.setChangement(true);
                        if (mt.getPosition() == 0) { //mur haut / droit
                            if (i < listeMursTroues.size())
                                listeMursTroues.get(i + 1).setChangement(true);
                        } else { //mur bas / gauche
                            if (i > 0)
                                listeMursTroues.get(i - 1).setChangement(true);
                        }
                    }
                    // On déplace le mur
                    if (mt.getChangement() == true) {
                        mt.setTranslationY(-mt.getTranslationY());
                        mt.setChangement(false);
                    }
                    // On déplace le mur
                    mt.translate(mt.getTranslationX() * IConfig.coeff_vitesse_mur, mt.getTranslationY() * IConfig.coeff_vitesse_mur);
                    break;
                case MurTroue.HAUT:
                    // Si le mur est en dehors de la map on l'enlève
                    if (mt.getWorldCenter().y < -largeur_map / 2 - 1) {
                        this.world.removeBody(mt);
                        listeMursTroues.remove(mt);
                        i--;
                    }
                    // Si le mur à atteint le coté droit ou le coté gauche, on inverse son déplacement X
                    if (mt.getWorldCenter().x + mt.getLongueur() / 2 < MurTroue.MG || mt.getWorldCenter().x - mt.getLongueur() / 2 > MurTroue.MD) {
                        mt.setChangement(true);
                        if (mt.getPosition() == 0) { //mur haut / droit
                            if (i < listeMursTroues.size())
                                listeMursTroues.get(i + 1).setChangement(true);
                        } else { //mur bas / gauche
                            if (i > 0)
                                listeMursTroues.get(i - 1).setChangement(true);
                        }
                    }
                    // On déplace le mur
                    if (mt.getChangement()) {
                        mt.setTranslationX(-mt.getTranslationX());
                        mt.setChangement(false);
                    }
                    // On déplace le mur
                    mt.translate(mt.getTranslationX() * IConfig.coeff_vitesse_mur, mt.getTranslationY() * IConfig.coeff_vitesse_mur);
                    break;
                case MurTroue.BAS:
                    // Si le mur est en dehors de la map on l'enlève
                    if (mt.getWorldCenter().y > largeur_map / 2 + 1) {
                        this.world.removeBody(mt);
                        listeMursTroues.remove(mt);
                        i--;
                    }
                    // Si le mur à atteint le coté droit ou le coté gauche, on inverse son déplacement X
                    if (mt.getWorldCenter().x + mt.getLongueur() / 2 < MurTroue.MG || mt.getWorldCenter().x - mt.getLongueur() / 2 > MurTroue.MD) {
                        mt.setChangement(true);
                        if (mt.getPosition() == 0) { //mur haut / droit
                            if (i < listeMursTroues.size())
                                listeMursTroues.get(i + 1).setChangement(true);
                        } else { //mur bas / gauche
                            if (i > 0)
                                listeMursTroues.get(i - 1).setChangement(true);
                        }
                    }
                    // On déplace le mur
                    if (mt.getChangement() == true) {
                        mt.setTranslationX(-mt.getTranslationX());
                        mt.setChangement(false);
                    }
                    // On déplace le mur
                    mt.translate(mt.getTranslationX() * IConfig.coeff_vitesse_mur, mt.getTranslationY() * IConfig.coeff_vitesse_mur);
                    break;
            }
        }
    }

    /**
     * permet de rafraichir le tableau des joueurs les plus riches
     *
     * @param numJoueur
     * @param richesseJoueur
     */
    private void MajPlusRiches(int numJoueur, int richesseJoueur) {
        JoueursRiches.put(numJoueur, richesseJoueur);
        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(JoueursRiches.entrySet());
        list.sort((o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));
        int count = 0;
        LinkedHashMap<Integer, Integer> top5 = new LinkedHashMap<>();
        for (Map.Entry<Integer, Integer> entry : list) {
            if (count >= 5) break;
            top5.put(entry.getKey(), entry.getValue());
            count++;
        }
        JoueursRiches.clear();
        JoueursRiches.putAll(top5);
    }


    private void VerifEtAppliqueChoixConsole() {

        // Gestion des tourniquets :
        TourniquetConfig tourniPanel = fenetreDebug.getPanel().getTourniPanel();
        if (tourniPanel != null) {
            System.out.println("On check tourniquet");
            if (tourniPanel.getCB1().isSelected()) {
                //
                if (emplacementTourniquet[0] == 1) {
                    // on fait rien
                } else {
                    ajoutTourniquet(1, -1, 500, 1);
                }
            } else {
                if (emplacementTourniquet[0] == 1) {
                    removeTourniquet(1);
                }
            }
            if (tourniPanel.getCB2().isSelected()) {
                //
                if (emplacementTourniquet[1] == 1) {
                    // on fait rien
                } else {
                    ajoutTourniquet(1, -1, 500, 2);
                }
            } else {
                if (emplacementTourniquet[1] == 1) {
                    removeTourniquet(2);
                }
            }
            if (tourniPanel.getCB3().isSelected()) {
                //
                if (emplacementTourniquet[2] == 1) {
                    // on fait rien
                } else {
                    ajoutTourniquet(1, -1, 500, 3);
                }
            } else {
                if (emplacementTourniquet[2] == 1) {
                    removeTourniquet(3);
                }
            }
            if (tourniPanel.getCB4().isSelected()) {

                if (emplacementTourniquet[3] == 1) {
                    // on fait rien
                } else {
                    ajoutTourniquet(1, -1, 500, 4);
                }
            } else {
                if (emplacementTourniquet[3] == 1) {
                    removeTourniquet(4);
                }
            }
        }
        BombeConfig bombePanel  = fenetreDebug.getPanel().getBombePanel();
        if(bombePanel != null){
            System.out.println("On check bombe");
            if (bombePanel.getCB1().isSelected()) {
                if (emplacementBombe[0] == 1) {
                    // on fait rien
                } else {
                    ajoutBombe( 1);
                }
            } else {
                if (emplacementBombe[0] == 1) {
                    removeBombe(1);
                }
            }

            if (bombePanel.getCB2().isSelected()) {
                if (emplacementBombe[1] == 1) {
                    // on fait rien
                } else {
                    ajoutBombe( 2);
                }
            } else {
                if (emplacementBombe[1] == 1) {
                    removeBombe(2);
                }
            }
            if (bombePanel.getCB3().isSelected()) {
                if (emplacementBombe[2] == 1) {
                    // on fait rien
                } else {
                    ajoutBombe( 3);
                }
            } else {
                if (emplacementBombe[2] == 1) {
                    removeBombe(3);
                }
            }
            if (bombePanel.getCB4().isSelected()) {
                if (emplacementBombe[3] == 1) {
                    // on fait rien
                } else {
                    ajoutBombe( 4);
                }
            } else {
                if (emplacementBombe[3] == 1) {
                    removeBombe(4);
                }
            }
        }

        RessortConfig ressortConfig = fenetreDebug.getPanel().getRessortPanel();

        if(ressortConfig != null){
            System.out.println("On check bombe");
            if (ressortConfig.getCB1().isSelected()) {
                if (emplacementRessort[0] == 1) {
                    // on fait rien
                } else {
                    ajoutRessort(0.3, 1, Ressort.IMMOBILE,1);
                }
            } else {
                if (emplacementRessort[0] == 1) {
                    removeRessort(1);
                }
            }

            if (ressortConfig.getCB2().isSelected()) {
                if (emplacementRessort[1] == 1) {
                    // on fait rien
                } else {
                    ajoutRessort(0.3, 1, Ressort.IMMOBILE,2);
                }
            } else {
                if (emplacementRessort[1] == 1) {
                    removeRessort(2);
                }
            }
        }
    }

    private void refreshConsole(){
        TourniquetConfig tourniPanel = fenetreDebug.getPanel().getTourniPanel();
        if(tourniPanel != null){
            if(emplacementTourniquet[0] == 1 && !tourniPanel.getCB1().isSelected()){
                tourniPanel.getCB1().setSelected(true);
            }
            else if(emplacementTourniquet[0] == 0 && tourniPanel.getCB1().isSelected()){
                tourniPanel.getCB1().setSelected(false);
            }
            if(emplacementTourniquet[1] == 1 && !tourniPanel.getCB2().isSelected()){
                tourniPanel.getCB2().setSelected(true);
            }
            else if(emplacementTourniquet[1] == 0 && tourniPanel.getCB2().isSelected()){
                tourniPanel.getCB2().setSelected(false);
            }
            if(emplacementTourniquet[2] == 1 && !tourniPanel.getCB3().isSelected()){
                tourniPanel.getCB3().setSelected(true);
            }
            else if(emplacementTourniquet[2] == 0 && tourniPanel.getCB3().isSelected()){
                tourniPanel.getCB3().setSelected(false);
            }
            if(emplacementTourniquet[3] == 1 && !tourniPanel.getCB4().isSelected()){
                tourniPanel.getCB4().setSelected(true);
            }
            else if(emplacementTourniquet[3] == 0 && tourniPanel.getCB4().isSelected()){
                tourniPanel.getCB4().setSelected(false);
            }
        }
        BombeConfig bombePanel  = fenetreDebug.getPanel().getBombePanel();
        if(bombePanel != null){

        }
        RessortConfig ressortConfig = fenetreDebug.getPanel().getRessortPanel();
        if(ressortConfig != null){

        }
    }

    private void changerImage1(){
        {
            try {
                imageVote1 = ImageIO.read(new File("./image2.0/phase_une/bombe.png"));
                imageVote2 = ImageIO.read(new File("./image2.0/phase_une/ressort.png"));
                imageVote3 = ImageIO.read(new File("./image2.0/phase_une/rotation.png"));
                imageVote4 = ImageIO.read(new File("./image2.0/phase_deux/piece_plus_deux.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private void changerImage2(){
        {
            try {
                imageVote1 = ImageIO.read(new File("./image2.0/phase_deux/bombe_ressort.png"));
                imageVote2 = ImageIO.read(new File("./image2.0/phase_deux/ressort.png"));
                imageVote3 = ImageIO.read(new File("./image2.0/phase_deux/rotation.png"));
                imageVote4 = ImageIO.read(new File("./image2.0/phase_deux/piece_plus_deux.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void changerImage3(){
        {
            try {
                imageVote1 = ImageIO.read(new File("./image2.0/phase_trois/vitesse_accru.png"));
                imageVote2 = ImageIO.read(new File("./image2.0/phase_trois/murs_rapide.png"));
                imageVote3 = ImageIO.read(new File("./image2.0/phase_trois/temps_supp.png"));
                imageVote4 = ImageIO.read(new File("./image2.0/phase_trois/mur_plus_petit.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
