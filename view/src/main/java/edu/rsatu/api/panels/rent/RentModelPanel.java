package edu.rsatu.api.panels.rent;

import edu.rsatu.api.MainFrame;
import edu.rsatu.garage.controller.RentController;
import edu.rsatu.garage.entities.Model;

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

public class RentModelPanel extends RentIssuesPanel {

    public RentModelPanel(MainFrame parent, RentCommonPanel rentCommonPanel, RentController rentController) {
        super(parent, rentCommonPanel, rentController);
        setLayout(new GridBagLayout());

        JLabel caption = new JLabel("Выберите марку автомобиля", SwingConstants.CENTER);
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

        JPanel modelsPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        upperPanel.add(modelsPanel);
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        JComboBox<String> models = new JComboBox<>(comboBoxModel);
        modelsPanel.add(models);
        modelsPanel.add(new JLabel());
        modelsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        List<Model> allModels = informationController.getAllModels();
        comboBoxModel.addAll(allModels.stream().map(Model::getName).collect(Collectors.toList()));

        JPanel selectPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        JButton select = new JButton("Выбрать");
        selectPanel.add(select);
        JButton cancelSelection = new JButton("Отменить выбор");
        selectPanel.add(cancelSelection);
        selectPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        upperPanel.add(selectPanel);

        String defaultInfoText = "Клиент:<br/>" + "Фамилия: " + rentController.getCurrentClient().getSurname() + "<br/> Адрес: "
                + rentController.getCurrentClient().getAddress();
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
            int index = models.getSelectedIndex();
            if (index >= 0) {
                rentController.setCurrentModel(allModels.get(index));
                info.setText("<html>" + defaultInfoText + "<br/> Марка автомобиля: " +
                        rentController.getCurrentModel().getName() + "</html>");
            } else {
                JOptionPane.showMessageDialog(this, "Выберите марку из списка");
            }
        });
        cancelSelection.addActionListener(e -> {
            rentController.setCurrentModel(null);
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
            if (rentController.getCurrentModel() == null) {
                JOptionPane.showMessageDialog(this, "Не выбрана марка автомобиля");
            } else {
                rentCommonPanel.rentIssuePanels.add(this);
                rentCommonPanel.setRentMutablePanel(new RentBoxPanel(parent, rentCommonPanel, rentController));
            }
        });
        JButton back = new JButton("Назад");
        buttonsPanel.add(back);
        back.addActionListener(e -> {
            rentController.setCurrentModel(null);
            rentCommonPanel.setRentMutablePanel(rentCommonPanel.rentIssuePanels.removeLast());
        });
    }
}
