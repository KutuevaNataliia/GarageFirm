package edu.rsatu.api.panels.rent;

import edu.rsatu.api.MainFrame;
import edu.rsatu.garage.controller.RentController;
import edu.rsatu.garage.entities.Client;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

public class NewClientPanel extends RentIssuesPanel {

    public NewClientPanel(MainFrame parent, RentCommonPanel rentCommonPanel, RentController rentController) {
        super(parent, rentCommonPanel, rentController);
        setLayout(new GridBagLayout());

        JLabel caption = new JLabel("Добавление нового клиента", SwingConstants.CENTER);
        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 1;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.anchor = GridBagConstraints.NORTH;
        add(caption, cc);

        JPanel upperPanel = new JPanel(new GridLayout(2, 2, 10, 20));
        JLabel typeSurname = new JLabel("Введите фамилию", SwingConstants.LEFT);
        upperPanel.add(typeSurname);
        JTextField surname = new JTextField();
        upperPanel.add(surname);
        JLabel typeAddress = new JLabel("Введите адрес", SwingConstants.LEFT);
        upperPanel.add(typeAddress);
        JTextField address = new JTextField();
        upperPanel.add(address);
        GridBagConstraints uc = new GridBagConstraints();
        uc.weightx = 1;
        uc.weighty = 5;
        uc.gridx = 0;
        uc.gridy = 1;
        uc.fill = GridBagConstraints.HORIZONTAL;
        uc.anchor = GridBagConstraints.NORTH;
        add(upperPanel, uc);

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 20));
        GridBagConstraints bpc = new GridBagConstraints();
        bpc.weightx = 1;
        bpc.weighty = 1;
        bpc.gridx = 0;
        bpc.gridy = 2;
        bpc.fill = GridBagConstraints.HORIZONTAL;
        bpc.anchor = GridBagConstraints.SOUTH;
        add(buttonsPanel, bpc);

        JButton save = new JButton("Добавить клиента");
        buttonsPanel.add(save);
        save.addActionListener(e -> {
            Client client = new Client(surname.getText(), address.getText());
            rentController.getClientsWithoutCar().add(client);
            parent.setMainPanel(rentCommonPanel);
            JOptionPane.showMessageDialog(this, "Клиент успешно добавлен");
        });
        JButton cancel = new JButton("Отмена");
        buttonsPanel.add(cancel);
        cancel.addActionListener(e -> parent.setMainPanel(rentCommonPanel));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
    }
}
