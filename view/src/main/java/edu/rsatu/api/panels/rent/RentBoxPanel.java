package edu.rsatu.api.panels.rent;

import edu.rsatu.api.MainFrame;
import edu.rsatu.garage.controller.RentController;
import edu.rsatu.garage.entities.Box;

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

public class RentBoxPanel extends RentIssuesPanel {
    public RentBoxPanel(MainFrame parent, RentCommonPanel rentCommonPanel, RentController rentController) {
        super(parent, rentCommonPanel, rentController);
        setLayout(new GridBagLayout());

        JLabel caption = new JLabel("Выберите бокс", SwingConstants.CENTER);
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

        JPanel boxesPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        upperPanel.add(boxesPanel);
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        JComboBox<String> boxes = new JComboBox<>(comboBoxModel);
        boxesPanel.add(boxes);
        boxesPanel.add(new JLabel());
        boxesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        List<Box> allBoxes = informationController.getFreeBoxesForModel(rentController.getCurrentModel());
        comboBoxModel.addAll(allBoxes.stream().map(box -> "" + box.getId() + " стоимость: " + box.getRentPrice()).collect(Collectors.toList()));

        JPanel selectPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        JButton select = new JButton("Выбрать");
        selectPanel.add(select);
        JButton cancelSelection = new JButton("Отменить выбор");
        selectPanel.add(cancelSelection);
        selectPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        upperPanel.add(selectPanel);

        String prevInfo = "Клиент:<br/>" + "Фамилия: " + rentController.getCurrentClient().getSurname() + "<br/> Адрес: "
                + rentController.getCurrentClient().getAddress();
        String defaultInfoText = prevInfo + "<br/>Марка автомобиля: " + rentController.getCurrentModel().getName();
        JLabel info = new JLabel("<html>" + defaultInfoText + "</html>");
        GridBagConstraints ic = new GridBagConstraints();
        ic.weightx = 1;
        ic.weighty = 2;
        ic.gridx = 0;
        ic.gridy = 2;
        ic.fill = GridBagConstraints.HORIZONTAL;
        ic.anchor = GridBagConstraints.NORTH;
        add(info, ic);

        select.addActionListener(e -> {
            int index = boxes.getSelectedIndex();
            if (index >= 0) {
                rentController.setCurrentBox(allBoxes.get(index));
                info.setText("<html>" + defaultInfoText + "<br/> Бокс:" + rentController.getCurrentBox().getId() +
                        " стоимость: " + rentController.getCurrentBox().getRentPrice() + "</html>");
            } else {
                JOptionPane.showMessageDialog(this, "Выберите бокс из списка");
            }
        });
        cancelSelection.addActionListener(e -> {
            rentController.setCurrentBox(null);
            info.setText("<html>" + defaultInfoText + "</html>");
        });


        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 20));
        GridBagConstraints bpc = new GridBagConstraints();
        bpc.weightx = 1;
        bpc.weighty = 1;
        bpc.gridx = 0;
        bpc.gridy = 3;
        bpc.fill = GridBagConstraints.HORIZONTAL;
        bpc.anchor = GridBagConstraints.SOUTH;
        add(buttonsPanel, bpc);

        JButton nextStep = new JButton("Следующий шаг");
        buttonsPanel.add(nextStep);
        nextStep.addActionListener(e -> {
            if (rentController.getCurrentBox() == null) {
                JOptionPane.showMessageDialog(this, "Не выбран бокс");
            } else {
                rentCommonPanel.rentIssuePanels.add(this);
                rentCommonPanel.setRentMutablePanel(new RentCarPanel(parent, rentCommonPanel, rentController));
            }
        });
        JButton back = new JButton("Назад");
        buttonsPanel.add(back);
        back.addActionListener(e -> {
            rentController.setCurrentBox(null);
            rentCommonPanel.setRentMutablePanel(rentCommonPanel.rentIssuePanels.removeLast());
        });
    }
}
