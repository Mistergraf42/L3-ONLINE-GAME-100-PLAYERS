package physique;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import physique.framework.SimulationBody;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.geometry.Vector2;

import java.awt.*;
public class Tourniquet extends SimulationBody  {

    Mur centre;
    RevoluteJoint<SimulationBody> joint;

    public void setCentre(Mur centre){
        this.centre = centre;
    }
    public Mur getCentre(){
        return this.centre;
    }

    public void setJoint(RevoluteJoint<SimulationBody> joint){
        this.joint = joint;
    }
    public RevoluteJoint<SimulationBody> getJoint(){
        return this.joint;
    }
    public Tourniquet(){}
    public Tourniquet(double posX, double posY, double hauteur, double longueur) {


        super.color = new Color(100, 42, 42);

        BodyFixture fixture = this.addFixture(Geometry.createRectangle(longueur, hauteur), 100, 0, 0);
        fixture.setRestitutionVelocity(0.0);
        this.translate(posX, posY);
        this.setMass(MassType.NORMAL);

    }
}
