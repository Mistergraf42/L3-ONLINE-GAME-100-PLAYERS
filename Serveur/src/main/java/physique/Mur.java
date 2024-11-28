package physique;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import physique.framework.SimulationBody;

import java.awt.*;

public class Mur extends SimulationBody {
    //Chaque mur peut avoir sa propre translationX et translationY
    private double translationX;
    private double translationY;
    public Mur (Color couleur, double longueur, double largeur, double posX, double posY){
        super(couleur);
        BodyFixture fixture = this.addFixture(Geometry.createRectangle(longueur, largeur), 0, 0.4, 0);
        fixture.setRestitutionVelocity(0.0);
        this.translate(posX, posY);
        this.setMass(MassType.INFINITE);
        this.setAtRestDetectionEnabled(false);
        this.translationX = 0;
        this.translationY = 0;
        this.setBullet(true);
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
}
