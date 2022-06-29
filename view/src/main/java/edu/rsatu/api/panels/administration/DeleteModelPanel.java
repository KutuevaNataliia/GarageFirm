package edu.rsatu.api.panels.administration;

import edu.rsatu.api.MainFrame;
import edu.rsatu.api.panels.MainPanel;
import edu.rsatu.garage.entities.Car;
import edu.rsatu.garage.entities.Model;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;

public class DeleteModelPanel extends MainPanel {

    public DeleteModelPanel(MainFrame parent) {
        super(parent);

        setLayout(new GridBagLayout());

        JLabel caption = new JLabel("Прекращение обслуживания марки", SwingConstants.CENTER);
        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 2;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.anchor = GridBagConstraints.NORTH;
        add(caption, cc);

        JLabel chooseModel = new JLabel("Выберите марку из списка", SwingConstants.CENTER);
        GridBagConstraints cmc = new GridBagConstraints();
        cmc.weightx = 1;
        cmc.weighty = 1;
        cmc.gridx = 0;
        cmc.gridy = 1;
        cmc.fill = GridBagConstraints.HORIZONTAL;
        add(chooseModel, cmc);

        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        JComboBox<String> models = new JComboBox<>(comboBoxModel);
        GridBagConstraints mc = new GridBagConstraints();
        mc.weightx = 0.6;
        mc.weighty = 5;
        mc.gridx = 0;
        mc.gridy = 2;
        mc.fill = GridBagConstraints.CENTER;
        mc.anchor = GridBagConstraints.NORTH;
        add(models, mc);

        //добавление в список
        List<Model> modelsG = informationController.getAllModels();
        for (Model model : modelsG) {
            models.addItem(model.getName());
        }

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 20));
        GridBagConstraints bpc = new GridBagConstraints();
        bpc.weightx = 1;
        bpc.weighty = 1;
        bpc.gridx = 0;
        bpc.gridy = 3;
        bpc.fill = GridBagConstraints.HORIZONTAL;
        bpc.anchor = GridBagConstraints.SOUTH;
        add(buttonsPanel, bpc);


        JButton stopService = new JButton("Прекратить обслуживание");
        buttonsPanel.add(stopService);
        stopService.addActionListener(e -> {
            int index = models.getSelectedIndex();
            Model model = modelsG.get(index);
            List<Car> cars = informationController.getCarsForModel(model);
            if (!cars.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Невозможно прекрытить обслуживание марки, т.к. она обслуживается в одном из занятых боксов");
            } else {
                administrationController.deleteModel(model);
                models.removeItem(models.getSelectedItem());
                JOptionPane.showMessageDialog(this, "Обслуживание марки прекращено");
            }
        });
        JButton cancel = new JButton("Отмена");
        buttonsPanel.add(cancel);

        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
    }

}
