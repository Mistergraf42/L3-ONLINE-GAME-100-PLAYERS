package config;

import physique.Fenetre;

import javax.swing.*;
import java.awt.*;

public class RessortConfig extends JPanel {
    MonCheckbox checkBox1 = new MonCheckbox();
    MonCheckbox checkBox2 = new MonCheckbox();

    public MonCheckbox getCB1(){
        return this.checkBox1;
    }

    public MonCheckbox getCB2(){
        return this.checkBox2;
    }


    RessortConfig(){
        this.setBackground(Color.BLACK);
        checkBox1.setBounds(550,100,100,100);
        if(physique.Fenetre.emplacementRessort[0] == 1){
            checkBox1.setSelected(true);
        }
        this.add(checkBox1);
        repaint();

        checkBox2.setBounds(550,500,100,100);
        if(Fenetre.emplacementRessort[1] == 1){
            checkBox2.setSelected(true);
        }
        this.add(checkBox2);
        repaint();


        repaint();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.fillRect(100,100,1000,500);
    }
}
