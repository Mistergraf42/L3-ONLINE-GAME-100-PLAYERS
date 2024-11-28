package physique;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Shape;
import org.dyn4j.geometry.Vector2;
import physique.framework.SimulationBody;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;

public class Bombe extends SimulationBody  {
    private final double expulseForce;
    private final Vector2 position; // Ajout de la position de la bombe

    public static final int IMMOBILE = 0;
    public static final int MOBILE = 1;


    public Bombe(Color couleur, double rayon, double expulseForce, Vector2 position) {
        super(couleur);
        this.expulseForce = expulseForce;
        this.position = position; // Initialisation de la position
        BodyFixture fixture = this.addFixture(Geometry.createCircle(rayon), 100.0, 0.1, 0.0);
        fixture.setRestitution(0.5);
        this.setBullet(true);
    }

    public void expulserJoueurs(HashMap<Integer, Personnage> joueurEnVie, Vector2 bombPosition) {
        for (Personnage perso : joueurEnVie.values()) {
            Vector2 playerPosition = perso.getWorldCenter();
            double distance = bombPosition.distance(playerPosition);
            if (distance < 1) {
                Vector2 direction = playerPosition.subtract(bombPosition).getNormalized();
                Vector2 force = direction.product(expulseForce);
                perso.applyForce(force);
            }
        }
    }
    public void render(Graphics2D g, double scale, Color color) {
        // point radius
        final int pr = 4;

        AffineTransform ot = g.getTransform();

        AffineTransform lt = new AffineTransform();
        lt.translate(this.transform.getTranslationX() * scale, this.transform.getTranslationY() * scale);
        lt.rotate(this.transform.getRotationAngle());

        // apply the transform
        g.transform(lt);

        // loop over all the body fixtures for this body
        for (BodyFixture fixture : this.fixtures) {
            this.renderFixture(g, scale, fixture, color);
        }

        g.scale(1,-1);

        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 16));
            g.drawString("Bombe", (float) (this.getLocalCenter().x)-30, (float) (this.getLocalCenter().y)+7);


        // set the original transform
        g.setTransform(ot);
    }


}
