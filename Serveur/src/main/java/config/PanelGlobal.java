package config;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelGlobal extends JPanel{


    TourniquetConfig tourniPanel = null;
    BombeConfig bombePanel = null;
    RessortConfig ressortPanel = null;
    JButton global;

    public RessortConfig getRessortPanel(){
        return this.ressortPanel;
    }
    public JButton getGlobal(){
        return this.global;
    }
    public TourniquetConfig getTourniPanel(){
        return this.tourniPanel;
    }
    public BombeConfig getBombePanel(){
        return this.bombePanel;
    }
    PanelGlobal(){
        this.setLayout(null);
        
        global = new JButton("valider");
        global.setBounds(0, 0, 200,50);
        this.add(global);

        JButton tourniquet = new JButton("Tourniquet");
        tourniquet.setBounds(0, 50, 200,50);
        this.add(tourniquet);

        JButton ressort = new JButton("Ressort");
        ressort.setBounds(0, 100, 200,50);
        this.add(ressort);

        JButton bombe = new JButton("Bombe");
        bombe.setBounds(0, 150, 200,50);
        this.add(bombe);





        tourniquet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tourniPanel == null){
                    removeAll();
                    tourniPanel = new TourniquetConfig();
                    tourniPanel.setBounds(200,0,1200,700);
                    add(tourniPanel);
                    repaint();

                }else{
                    // on gros ca reset les attributs de l'affichage
                    tourniPanel = null;
                    tourniPanel = new TourniquetConfig();
                    tourniPanel.setBounds(200,0,1200,700);
                    add(tourniPanel);
                    repaint();
                }

            }
        });


        bombe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeAll();
                if(bombePanel == null){
                    bombePanel = new BombeConfig();
                    bombePanel.setBounds(200,0,1200,700);
                    add(bombePanel);
                    repaint();

                }else{
                    // on gros ca reset les attributs de l'affichage
                    bombePanel = null;
                    bombePanel = new BombeConfig();
                    bombePanel.setBounds(200,0,1200,700);
                    add(bombePanel);
                    repaint();
                }

            }
        });

        ressort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(ressortPanel == null){
                    removeAll();
                    ressortPanel = new RessortConfig();
                    ressortPanel.setBounds(200,0,1200,700);
                    add(ressortPanel);
                    repaint();

                }else{
                    // on gros ca reset les attributs de l'affichage
                    ressortPanel = null;
                    ressortPanel = new RessortConfig();
                    ressortPanel.setBounds(200,0,1200,700);
                    add(ressortPanel);
                    repaint();
                }

            }
        });
    }
    public void removeAll(){
        if(bombePanel != null){
            this.remove(bombePanel);
            bombePanel = null;
        }
        if(ressortPanel != null){
            this.remove(ressortPanel);
            ressortPanel = null;
        }
        if(tourniPanel != null){
            this.remove(tourniPanel);
            tourniPanel = null;
        }
    }

}
