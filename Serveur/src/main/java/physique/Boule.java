package physique;
import java.awt.Color;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import physique.framework.SimulationBody;

public class Boule extends SimulationBody{
    public Boule(Color couleur, double rayon, double densite, double friction, double restitution){
        super(couleur);

        BodyFixture fixture = this.addFixture(Geometry.createCircle(rayon), densite, friction, restitution);
        fixture.setRestitutionVelocity(0.001);
        this.translate(-0.75, 0.0);
        this.setLinearDamping(1); //resistance a la translation
        this.setAngularDamping(0.8); //resistance a la rotation
        this.setMass(MassType.NORMAL);
        this.setBullet(true);
    }

}
