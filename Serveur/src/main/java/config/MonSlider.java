package config;
import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.util.Dictionary;
import java.util.Hashtable;

public class MonSlider extends JSlider {

    public MonSlider(int min, int max) {
        super(min, max);

        // Définir les étiquettes des paliers
        setLabelTable(echelleSlider(min, max));
        // Afficher les étiquettes
        setPaintLabels(true);

        setUI(new CustomSliderUI(this));
    }



    /**
     * Fonction permettant d'afficher les sliders pour le menu Option 
     * @param minValue valeur minimum du slider (souvent 0)
     * @param maxValue valeur maximap du slider (souvent 5)
     * @return Dictionnary
     */
    private Dictionary<Integer, JLabel> echelleSlider(int minValue, int maxValue) {
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(minValue, new JLabel(String.valueOf(minValue))); 
        labelTable.put(maxValue, new JLabel(String.valueOf(maxValue))); // en gros tableau 

        int step = (maxValue - minValue) / 4; // Intervalle 
        // affichage 
        for (int i = minValue + step; i < maxValue; i += step) {
            labelTable.put(i, new JLabel(String.valueOf(i)));
        }

        return labelTable;
    }



    private static class CustomSliderUI extends BasicSliderUI {

        public CustomSliderUI(JSlider b) {
            super(b);
        }
        @Override
        public void paintThumb(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(new Color(50, 150, 255));
            g2d.fillRect(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);

            g2d.dispose();
        }

        @Override
        public void paintTrack(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(new Color(200, 200, 200));
            g2d.fillRect(trackRect.x, trackRect.y, trackRect.width, trackRect.height);
            g2d.dispose();
        }
    }
}