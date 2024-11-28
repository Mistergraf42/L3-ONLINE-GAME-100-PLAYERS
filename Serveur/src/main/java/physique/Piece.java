package physique;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Vector2;
import physique.framework.SimulationBody;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Piece extends SimulationBody {

    private final Vector2 position; // Utile pour les collisions
    
    public int getValeur(){
        return 1;
    }
    public Piece(Vector2 position) {
        super(IConfig.couleurPiece);
        this.position = position; // Initialisation de la position
        BodyFixture fixture = this.addFixture(Geometry.createCircle(IConfig.rayonPiece), 100.0, 0.1, 0.0);
        fixture.setRestitution(0);
        this.translate(position.x, position.y);

        this.setBullet(true);
    }

    public Vector2 getPosition(){
        return this.position;
    }

    public void render(Graphics2D g, double scale, Color color) {
        // point radius
        final int pr = 4;

        // save the original transform
        AffineTransform ot = g.getTransform();

        // transform the coordinate system from world coordinates to local coordinates
        AffineTransform lt = new AffineTransform();
        lt.translate(this.transform.getTranslationX() * scale, this.transform.getTranslationY() * scale);
        lt.rotate(this.transform.getRotationAngle());

        // apply the transform
        g.transform(lt);

        // loop over all the body fixtures for this body
        for (BodyFixture fixture : this.fixtures) {
            this.renderFixture(g, scale, fixture, color);
        }

        // affichage du numéro du joueur
        g.scale(1,-1);
        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 18));
            g.drawString("$", (float) (this.getLocalCenter().x)-6, (float) (this.getLocalCenter().y)+5);


        // set the original transform
        g.setTransform(ot);
    }


}
