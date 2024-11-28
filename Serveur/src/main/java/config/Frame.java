package config;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import java.awt.event.*;

import org.w3c.dom.ranges.RangeException;

import physique.IConfig;


// Si quelqu'un voit ce code je m'excuse d'avance haha #Sinclair 
public class Frame extends JFrame {
    private PanelGlobal panel;
    public PanelGlobal getPanel(){
        return this.panel;
    }
    public Frame(String nom) {
        List<MonSlider> listeSlider = new ArrayList<>();

        this.setSize(1500, 750);
        this.setVisible(true);
        this.setName(nom);
        this.setLayout(null);

        panel = new PanelGlobal();
        panel.setBounds(0, 0, 1500, 750);
        this.add(panel);

        this.repaint();


    }




}
