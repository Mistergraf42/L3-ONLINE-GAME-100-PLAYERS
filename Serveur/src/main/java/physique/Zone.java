package physique;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import physique.framework.SimulationBody;
public class Zone extends SimulationBody{
    private int x1;
    private int y1;
    private int x2;
    private int Y2;
    private double friction;

    public Zone(int x1, int y1, int x2, int y2, double friction) {
        double longueur = x2-x1;
        double largeur = y2-y1;
        BodyFixture fixture = this.addFixture(Geometry.createRectangle(longueur, largeur), 10, 0, 0);
        fixture.setRestitutionVelocity(0.0);
        this.setMass(MassType.INFINITE);
        this.translate(x1, y1);
        this.setAtRestDetectionEnabled(false);
        this.setBullet(false);
    }
}

