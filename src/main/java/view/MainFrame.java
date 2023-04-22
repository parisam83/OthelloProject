package view;

import Loaders.ConfigLoader;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static MainFrame instance;
    public MainFrame(){
        if (instance != null) return;
        instance = this;

        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        int windowWidth = ConfigLoader.getInstance().getProperty(Integer.class, "windowWidth");
        int windowHeight = ConfigLoader.getInstance().getProperty(Integer.class, "windowHeight");

        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(windowWidth, windowHeight));
        this.setLocation(screenWidth/2 - windowWidth/2, screenHeight/2 - windowHeight/2);
        this.setVisible(true);
    }

    public MainFrame getInstance(){
        if (instance == null) instance = new MainFrame();
        return instance;
    }
}
