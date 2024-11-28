package config;

import javax.swing.*;
import java.awt.*;

public class TourniquetConfig extends JPanel {
    MonCheckbox checkBox1 = new MonCheckbox();
    MonCheckbox checkBox2 = new MonCheckbox();
    MonCheckbox checkBox3 = new MonCheckbox();
    MonCheckbox checkBox4 = new MonCheckbox();

    public MonCheckbox getCB1(){
        return this.checkBox1;
    }

    public MonCheckbox getCB2(){
        return this.checkBox2;
    }

    public MonCheckbox getCB3(){
        return this.checkBox3;
    }

    public MonCheckbox getCB4(){
        return this.checkBox4;
    }

    TourniquetConfig(){
        this.setBackground(Color.BLACK);
        checkBox1.setBounds(100,300,100,100);
        if(physique.Fenetre.emplacementTourniquet[0] == 1){
            checkBox1.setSelected(true);
        }
        this.add(checkBox1);
        repaint();

        checkBox2.setBounds(400,300,100,100);
        if(physique.Fenetre.emplacementTourniquet[1] == 1){
            checkBox2.setSelected(true);
        }
        this.add(checkBox2);
        repaint();

        checkBox3.setBounds(700,300,100,100);
        if(physique.Fenetre.emplacementTourniquet[2] == 1){
            checkBox3.setSelected(true);
        }
        this.add(checkBox3);
        repaint();

        checkBox4.setBounds(1000,300,100,100);
        if(physique.Fenetre.emplacementTourniquet[3] == 1){
            checkBox4.setSelected(true);
        }
        this.add(checkBox4);
        repaint();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.fillRect(100,100,1000,500);
    }
}
