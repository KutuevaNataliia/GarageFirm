package edu.rsatu.api.panels;

import edu.rsatu.api.MainFrame;
import edu.rsatu.garage.entities.Box;
import edu.rsatu.garage.entities.Car;

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

public class CloseBoxPanel extends MainPanel {

    public CloseBoxPanel(MainFrame parent) {
        super(parent);

        setLayout(new GridBagLayout());
        JLabel caption = new JLabel("Закрытие бокса", SwingConstants.CENTER);
        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 2;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.anchor = GridBagConstraints.NORTH;
        add(caption, cc);

        JLabel chooseBox = new JLabel("Выберите номер бокса из списка", SwingConstants.CENTER);
        add(chooseBox);
        GridBagConstraints cbc = new GridBagConstraints();
        cbc.weightx = 1;
        cbc.weighty = 1;
        cbc.gridx = 0;
        cbc.gridy = 1;
        cbc.fill = GridBagConstraints.HORIZONTAL;
        add(chooseBox, cbc);

        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        JComboBox<String> boxes = new JComboBox<>(comboBoxModel);
        GridBagConstraints bc = new GridBagConstraints();
        bc.weightx = 0.6;
        bc.weighty = 5;
        bc.gridx = 0;
        bc.gridy = 2;
        bc.fill = GridBagConstraints.CENTER;
        bc.anchor = GridBagConstraints.NORTH;
        add(boxes, bc);

        //заполнить список боксов при заходе на форму
        List<Box> boxesG = informationController.getAllBoxes();
        for (Box box : boxesG) {
            boxes.addItem(box.getId().toString());
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

        JButton stopService = new JButton("Закрыть бокс");
        buttonsPanel.add(stopService);

        //закрытие бокса - контроллер
        stopService.addActionListener(e -> {
            int index = boxes.getSelectedIndex();
            Box box = boxesG.get(index);
            List<Car> cars = informationController.getCarsForBox(box);
            if (!cars.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Невозможно закрыть бокс, т.к. он занят");
            } else {
                administrationController.deleteBox(box);
                boxes.removeItem(boxes.getSelectedItem());
                JOptionPane.showMessageDialog(parent,
                        "<html><i>Выбранный бокс был успешно закрыт</i>");
            }
        });

        /*
        JButton cancel = new JButton("Отмена");
        buttonsPanel.add(cancel);
        */
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
    }
}
