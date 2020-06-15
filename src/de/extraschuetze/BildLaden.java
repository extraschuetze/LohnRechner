package de.extraschuetze;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BildLaden extends JFrame {

    public BildLaden(String path) {
        JLabel label = new JLabel(showImg(path));
        JPanel panel = new JPanel();
        panel.add(label);
        this.add(panel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(LohnRechner.class.getResource("PB_Discord.png")));
        this.setTitle("Überweisung");
        this.setSize(300, 330);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private ImageIcon showImg(String path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ImageIcon(img);
    }

}