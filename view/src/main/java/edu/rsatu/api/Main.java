package edu.rsatu.api;

import edu.rsatu.api.panels.DefaultPanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setMainPanel(new DefaultPanel());
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }
}
