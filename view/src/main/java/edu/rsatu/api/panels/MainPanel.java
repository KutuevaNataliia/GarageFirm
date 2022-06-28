package edu.rsatu.api.panels;

import edu.rsatu.api.MainFrame;
import edu.rsatu.garage.controller.AdministrationController;
import edu.rsatu.garage.controller.InformationController;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainPanel extends JPanel {

    protected MainFrame parent;

    protected AdministrationController administrationController = new AdministrationController();
    protected InformationController informationController = new InformationController();

    public MainPanel(MainFrame parent) {
        this.parent = parent;
    }
}
