package edu.rsatu.api.panels.rent;

import edu.rsatu.api.MainFrame;
import edu.rsatu.api.panels.MainPanel;
import edu.rsatu.garage.controller.RentController;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.util.ArrayDeque;

public class RentCommonPanel extends MainPanel {

    public JPanel rentIssueMutablePanel;
    public ArrayDeque<JPanel> rentIssuePanels = new ArrayDeque<>();
    RentController rentController = new RentController();

    public RentCommonPanel(MainFrame parent) {
        super(parent);
        setLayout(new GridLayout(2, 1, 10, 10));

        JLabel caption = new JLabel("Оформление аренды", SwingConstants.CENTER);
        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 1;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.anchor = GridBagConstraints.PAGE_START;
        add(caption, cc);

        rentIssueMutablePanel = new JPanel(new GridLayout(1, 1));
        GridBagConstraints mpc = new GridBagConstraints();
        mpc.weightx = 1;
        mpc.weighty = 29;
        mpc.gridx = 0;
        mpc.gridy = 1;
        mpc.fill = GridBagConstraints.HORIZONTAL;
        mpc.anchor = GridBagConstraints.NORTH;
        add(rentIssueMutablePanel, mpc);

        rentIssuePanels.clear();
        rentIssueMutablePanel.add(new RentClientPanel(parent, this, rentController));

        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
    }

    public void setRentMutablePanel(JPanel newMutable) {
        rentIssueMutablePanel.removeAll();
        rentIssueMutablePanel.add(newMutable);
        parent.getContentPane().validate();
        parent.getContentPane().repaint();
    }
}
