package physique;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import physique.framework.SimulationBody;

import javax.swing.text.Position;
import java.awt.*;
import java.util.Random;

public class MurTroue extends SimulationBody {
    public static final int GAUCHE = 0;
    public static final int HAUT = 1;
    public static final int DROITE = 2;
    public static final int BAS = 3;

    public static final double MD = 4;
    public static final double MG = -4;
    public static final double MH = 2;
    public static final double MB = -2;

    private double translationX;
    private double translationY;
    private double hauteur;
    private double longueur;
    private int type;
    private int position;
    private boolean changement;
    private double tailleTrou;

    public MurTroue(int pos, double posTrou, double tailleTrou, double vitesseMur, double vitesseTrou, int type){
        double positionX;
        double positionY;
        double vitesseX;
        double vitesseY;
        super.color = new Color(150, 80, 20);

        switch(type){
            case GAUCHE:
                positionX=MG;
                vitesseX = vitesseMur;
                vitesseY = vitesseTrou;
                if(pos == 0) {
                    //positionY = MH;
                    //hauteur = MH*2+posTrou-(tailleTrou/2);
                    positionY = MH + posTrou+((tailleTrou * IConfig.coeff_taille_trou_mur)/2);
                    hauteur = MH*2;
                }
                else {
                    //positionY = MB;
                    //hauteur = MH*2-posTrou-(tailleTrou/2);
                    positionY = MB + posTrou-((tailleTrou * IConfig.coeff_taille_trou_mur)/2);
                    hauteur = MH*2;
                }
                longueur = 0.1;
                break;
            case HAUT:
                positionY=MH;
                vitesseX = vitesseTrou;
                vitesseY = -vitesseMur;
                if(pos == 0) {
                    //positionX = MD;
                    //longueur = MD*2+posTrou-(tailleTrou/2);
                    positionX = MD+posTrou+((tailleTrou * IConfig.coeff_taille_trou_mur)/2);
                    longueur = MD*2;
                }
                else {
                    //positionX = MG;
                    //longueur = MD*2-posTrou-(tailleTrou/2);
                    positionX = MG+posTrou-((tailleTrou * IConfig.coeff_taille_trou_mur)/2);
                    longueur = MD*2;
                }
                hauteur = 0.1;
                break;
            case DROITE:
                positionX=MD;
                vitesseX = -vitesseMur;
                vitesseY = vitesseTrou;
                if(pos == 0) {
                    //positionY = MH;
                    //hauteur = MH*2+posTrou-(tailleTrou/2);
                    positionY = MH+posTrou+((tailleTrou * IConfig.coeff_taille_trou_mur)/2);
                    hauteur = MH*2;
                }
                else {
                    //positionY = MB;
                    //hauteur = MH*2-posTrou-(tailleTrou/2);
                    positionY = MB+posTrou-((tailleTrou * IConfig.coeff_taille_trou_mur)/2);
                    hauteur = MH*2;
                }
                longueur = 0.1;
                break;
            case BAS:
                positionY=MB;
                vitesseX = vitesseTrou;
                vitesseY = vitesseMur;
                if(pos == 0) {
                    //positionX = MD;
                    //longueur = MD*2+posTrou-(tailleTrou/2);
                    positionX = MD+posTrou+((tailleTrou * IConfig.coeff_taille_trou_mur)/2);
                    longueur = MD*2;
                }
                else {
                    //positionX = MG;
                    //longueur = MD*2-posTrou-(tailleTrou/2);
                    positionX = MG+posTrou-((tailleTrou * IConfig.coeff_taille_trou_mur)/2);
                    longueur = MD*2;
                }
                hauteur = 0.1;
                break;
            default:
                positionY = MH + posTrou+((tailleTrou * IConfig.coeff_taille_trou_mur)/2);
                positionX=MG;
                longueur = 0.1;
                hauteur = MH*2;
                vitesseX = vitesseMur;
                vitesseY = vitesseTrou;
                break;
        }
        //Il se peut qu'un mur ai une hauteur/longueur invalide dû à la position du trou et à sa taille
        //on la met au minimum si tel est le cas
        if(longueur < 0.001)
            longueur = 0.001;
        if(hauteur < 0.001)
            hauteur = 0.001;

        BodyFixture fixture = this.addFixture(Geometry.createRectangle(longueur, hauteur), 0, 0.4, 0);
        fixture.setRestitutionVelocity(0.0);
        this.translate(positionX, positionY);
        this.setMass(MassType.INFINITE);
        this.setAtRestDetectionEnabled(false);
        this.translationX = vitesseX;
        this.translationY = vitesseY;
        this.position = pos;
        this.tailleTrou = (tailleTrou * IConfig.coeff_taille_trou_mur);
        this.setBullet(true);
        this.type = type;
    }


    public void setTranslationX(double tX){
        this.translationX = tX;
    }
    public double getTranslationX(){
        return this.translationX;
    }
    public void setTranslationY(double tY){
        this.translationY = tY;
    }
    public double getTranslationY(){
        return this.translationY;
    }
    public int getType() { return type; }
    public void setType(int type) { this.type = type; }
    public double getHauteur() { return hauteur; }
    public void setHauteur(double hauteur) { this.hauteur = hauteur; }
    public double getLongueur() { return longueur; }
    public void setLongueur(double longueur) { this.longueur = longueur; }
    public boolean getChangement() { return changement; }
    public void setChangement(boolean changement) { this.changement = changement; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public double getTailleTrou() { return tailleTrou; }
    public void setTailleTrou(double tailleTrou) { this.tailleTrou = (tailleTrou * IConfig.coeff_taille_trou_mur); }
}

