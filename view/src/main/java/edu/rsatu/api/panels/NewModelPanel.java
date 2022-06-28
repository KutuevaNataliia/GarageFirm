package edu.rsatu.api.panels;

import edu.rsatu.api.MainFrame;
import edu.rsatu.garage.controller.AdministrationController;
import edu.rsatu.garage.controller.InformationController;
import edu.rsatu.garage.entities.Model;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;

public class NewModelPanel extends MainPanel {


    public NewModelPanel(MainFrame parent) {

        super(parent);
        setLayout(new GridBagLayout());

        JPanel captionPanel = new JPanel(new GridLayout(1, 1));
        JLabel caption = new JLabel("Добавление новой марки автомобилей", SwingConstants.CENTER);
        captionPanel.add(caption);
        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 1;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.anchor = GridBagConstraints.NORTH;
        add(caption, cc);

        JPanel upperPanel = new JPanel(new GridLayout(2, 2, 10, 20));
        JLabel typeName = new JLabel("Введите название", SwingConstants.LEFT);
        upperPanel.add(typeName);
        JTextField nameM = new JTextField();
        upperPanel.add(nameM);
        /*
        JLabel typeIdentifier = new JLabel("Введите идентификатор", SwingConstants.LEFT);
        upperPanel.add(typeIdentifier);
        JTextField identifier = new JTextField();
        upperPanel.add(identifier);
        */
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

        JButton save = new JButton("Добавить марку");
        buttonsPanel.add(save);
        save.addActionListener(e -> addModel(nameM.getText()));
        /*JButton cancel = new JButton("Отмена");
        buttonsPanel.add(cancel);
        cancel.addActionListener(e -> canselAddModel());*/
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

    }

    //обработчик добавления модели
    private void addModel(String name) {
        System.out.println("Модель: " + name);
        if (!name.isEmpty()) {
            List<Model> models = informationController.getAllModels();
            boolean check = true;
            for (Model model : models) {
                if (model.getName().equals(name)) {
                    check = false;
                    break;
                }
            }
            if (check) {
                administrationController.addModel(name);
                JOptionPane.showMessageDialog(parent,
                        "<html><i>Модель успешно добавлена в базу данных</i>");
            } else {
                JOptionPane.showMessageDialog(parent,
                        "<html><i>Такая модель уже имеется в базе данных</i>");
            }
        } else {
            JOptionPane.showMessageDialog(parent,
                    "<html><i>Название модели не может быть пустым</i>");
        }
    }
}
