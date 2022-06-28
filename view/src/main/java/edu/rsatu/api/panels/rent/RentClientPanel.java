package edu.rsatu.api.panels.rent;

import edu.rsatu.api.MainFrame;
import edu.rsatu.garage.controller.RentController;
import edu.rsatu.garage.entities.Client;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.stream.Collectors;

public class RentClientPanel extends RentIssuesPanel {

    public RentClientPanel(MainFrame parent, RentCommonPanel rentCommonPanel, RentController rentController) {
        super(parent, rentCommonPanel, rentController);

        setLayout(new GridBagLayout());

        JLabel caption = new JLabel("Выберите клиента", SwingConstants.CENTER);
        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 1;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.anchor = GridBagConstraints.NORTH;
        add(caption, cc);

        JPanel upperPanel = new JPanel(new GridLayout(1, 2));
        GridBagConstraints uc = new GridBagConstraints();
        uc.weightx = 1;
        uc.weighty = 5;
        uc.gridx = 0;
        uc.gridy = 1;
        uc.fill = GridBagConstraints.HORIZONTAL;
        uc.anchor = GridBagConstraints.NORTH;
        add(upperPanel, uc);

        JPanel clientsPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        upperPanel.add(clientsPanel);
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        JComboBox<String> clients = new JComboBox<>(comboBoxModel);
        List<Client> allClients = informationController.getAllClients();
        allClients.addAll(rentController.getClientsWithoutCar());
        comboBoxModel.addAll(allClients.stream().map(Client::getSurname).collect(Collectors.toList()));
        clientsPanel.add(clients);
        clientsPanel.add(new JLabel());
        clientsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel selectPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        JButton select = new JButton("Выбрать");
        selectPanel.add(select);
        JButton cancelSelection = new JButton("Отменить выбор");
        selectPanel.add(cancelSelection);
        selectPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        upperPanel.add(selectPanel);

        JButton newClient = new JButton("Новый клиент");
        GridBagConstraints ncc = new GridBagConstraints();
        ncc.weightx = 1;
        ncc.weighty = 1;
        ncc.gridx = 0;
        ncc.gridy = 2;
        ncc.fill = GridBagConstraints.CENTER;
        ncc.anchor = GridBagConstraints.NORTH;
        add(newClient, ncc);
        newClient.addActionListener(e -> parent.setMainPanel(new NewClientPanel(parent, rentCommonPanel, rentController)));

        String infoDefaultText = "<html>Клиент: <br/> </html>";
        JLabel info = new JLabel();
        GridBagConstraints ic = new GridBagConstraints();
        ic.weightx = 1;
        ic.weighty = 2;
        ic.gridx = 0;
        ic.gridy = 3;
        ic.fill = GridBagConstraints.HORIZONTAL;
        ic.anchor = GridBagConstraints.NORTH;
        add(info, ic);
        if (rentController.getCurrentClient() != null) {
            info.setText("<html>Клиент: " + rentController.getCurrentClient().getSurname() + "<br/> Адрес: " +
                    rentController.getCurrentClient().getAddress() + "</html>");
        } else {
            info.setText(infoDefaultText);
        }

        select.addActionListener(e -> {
            int index = clients.getSelectedIndex();
            if (index >= 0) {
                rentController.setCurrentClient(allClients.get(index));
                info.setText("<html>Клиент: " + rentController.getCurrentClient().getSurname() + "<br/> Адрес: " +
                        rentController.getCurrentClient().getAddress() + "</html>");
            } else {
                JOptionPane.showMessageDialog(this, "Выберите клиента из списка");
            }
        });
        cancelSelection.addActionListener(e -> {
            rentController.setCurrentClient(null);
            info.setText(infoDefaultText);
        });

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 20));
        GridBagConstraints bpc = new GridBagConstraints();
        bpc.weightx = 1;
        bpc.weighty = 1;
        bpc.gridx = 0;
        bpc.gridy = 4;
        bpc.fill = GridBagConstraints.HORIZONTAL;
        bpc.anchor = GridBagConstraints.SOUTH;
        add(buttonsPanel, bpc);

        JButton nextStep = new JButton("Следующий шаг");
        buttonsPanel.add(nextStep);
        nextStep.addActionListener(e -> {
            if (rentController.getCurrentClient() == null) {
                JOptionPane.showMessageDialog(this, "Не выбран клиент");
            } else {
                rentCommonPanel.rentIssuePanels.add(this);
                rentCommonPanel.setRentMutablePanel(new RentModelPanel(parent, rentCommonPanel, rentController));
            }

        });
        JButton cancel = new JButton("Отмена");
        buttonsPanel.add(cancel);
        cancel.addActionListener(e -> {

        });
    }
}
