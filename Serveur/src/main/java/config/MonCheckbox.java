package config;

import javax.swing.*;
import java.awt.*;

public class MonCheckbox extends JCheckBox {
    private Color checkedColor = Color.GREEN;
    private Color uncheckedColor = Color.RED;
    public MonCheckbox() {
        super();
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isSelected()) {
            g.setColor(checkedColor);
        } else {
            g.setColor(uncheckedColor);
        }
        g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);
        super.paintComponent(g);
    }



}
