package edu.rsatu.api.panels.rent;

import edu.rsatu.api.MainFrame;
import edu.rsatu.api.panels.MainPanel;
import edu.rsatu.garage.controller.RentController;

public class RentIssuesPanel extends MainPanel {

    protected RentCommonPanel rentCommonPanel;
    protected RentController rentController;

    public RentIssuesPanel(MainFrame parent, RentCommonPanel rentCommonPanel, RentController rentController) {
        super(parent);
        this.rentController = new RentController();
        this.rentCommonPanel = rentCommonPanel;
    }
}
