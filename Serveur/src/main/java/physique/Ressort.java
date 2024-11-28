package physique;

import org.dyn4j.collision.Filter;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import physique.framework.SimulationBody;

import java.awt.*;

public class Ressort extends SimulationBody{
    private double force; //indique la force du rebond du ressort :
                          // force < 1 : fera rebondir avec une vitesse inférieur à la vitesse d'entrer
                          // force > 1 : fera rebondir avec une vitesse supérieur à la vitesse d'entrer
    public static final int IMMOBILE = 0;
    public static final int MOBILE = 1;

    public Ressort(double posX, double posY, double rayon, double force, int type) {
        int couleur;

        //Vérification sur la force donné
        if(force < 1) //Minimum 1
            this.force = 1;
        if(force > 5) //Maximum 5
            this.force = 5;
        else
            this.force = force;
        if(type == MOBILE) {
            this.force = this.force/10;
        }

        super.color = new Color(255, 10, 100-(int)(this.force*20)); //couleur en fonction de la force du ressort

        BodyFixture fixture = this.addFixture(Geometry.createCircle(rayon), 100, 0, this.force);
        fixture.setRestitutionVelocity(0.0);
        this.translate(posX, posY);
        if(type == IMMOBILE) {
            this.setMass(MassType.INFINITE);
        }
        else{
            this.setMass(MassType.NORMAL);
        }

        this.setAtRestDetectionEnabled(false);
        this.setBullet(true);
    }

    public double getForceRessort() {
        return force;
    }

    public void setForceRessort(double force) {
        this.force = force;
    }
}

