package physique;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Random;

import org.dyn4j.collision.Fixture;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import physique.framework.SimulationBody;

public class Personnage extends SimulationBody{
    private Vector2 direction;
    private Color couleur;
    private int id;
    private int score;
    private boolean vote = false;
    private boolean tir;
    private int numero;
    private double vitesse;
    private double taille;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }

    public void setCouleur(Color couleur){
        this.couleur = couleur;
    }
    public Color getCouleur(){
        return this.couleur;
    }
    public void setDirection(Vector2 direction){
        this.direction = direction;
    }

    public Vector2 getDirection(){
        return this.direction;
    }

    public Personnage(Color couleur, double taille,double vitesse,double densite, double friction, double restitution){
        super(couleur);
        this.taille = taille;
        BodyFixture fixture = this.addFixture(Geometry.createCircle(this.taille), densite, friction, restitution);
        fixture.setRestitutionVelocity(0.001);
        this.setLinearDamping(1); //resistance a la translation
        this.setAngularDamping(0.8); //resistance a la rotation
        this.setMass(MassType.NORMAL);
        this.setBullet(true);
        this.tir = false;
        this.vitesse = vitesse;
    }

    public int getScore(){
        return this.score;
    }
    public void setScore(int score){
        this.score = score;
    }
    public void addScore(int score){
        this.score+= score;
    }

    public void setVote(boolean bool){
        this.vote = bool;
    }
    public boolean getVote(){
        return this.vote;
    }

    public void setNumero(int numero){
        this.numero = numero;
    }
    public int getNumero(){
        return this.numero;
    }
    
    public void setTir(boolean b){
        this.tir = b;
    }

    public boolean getTir(){
        return this.tir;
    }

    public void setVitesse(double vitesse){
        this.vitesse = vitesse;
    }
    public double getVitesse(){
        return this.vitesse;
    }

    public void setTaille(double taille){
        this.taille = taille;
    }

    public double getTaille(){
        return this.taille;
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

        BodyFixture f = new BodyFixture(Geometry.createCircle(this.taille));
        this.fixtures.set(0,f);

        // loop over all the body fixtures for this body
        for (BodyFixture fixture : this.fixtures) {
            this.renderFixture(g, scale, fixture, color);
        }

        g.scale(1,-1);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 20));
        if (this.numero<10){
            g.drawString(""+this.numero, (float) (this.getLocalCenter().x)-6, (float) (this.getLocalCenter().y)+7);
        }
        else {
            g.drawString(""+this.numero, (float) (this.getLocalCenter().x)-10, (float) (this.getLocalCenter().y)+7);
        }

        // set the original transform
        g.setTransform(ot);
    }
}

