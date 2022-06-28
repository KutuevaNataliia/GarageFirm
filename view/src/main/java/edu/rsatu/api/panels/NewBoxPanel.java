package edu.rsatu.api.panels;

import edu.rsatu.api.MainFrame;
import edu.rsatu.garage.controller.AdministrationController;
import edu.rsatu.garage.controller.InformationController;
import edu.rsatu.garage.entities.Box;
import edu.rsatu.garage.entities.Model;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;

public class NewBoxPanel extends MainPanel {

    public NewBoxPanel(MainFrame parent) {

        super(parent);
        setLayout(new GridBagLayout());

        JPanel captionPanel = new JPanel(new GridLayout(1, 1));
        JLabel caption = new JLabel("Добавление нового бокса", SwingConstants.CENTER);
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
        JLabel typeNumber = new JLabel("Введите номер", SwingConstants.LEFT);
        upperPanel.add(typeNumber);
        JTextField number = new JTextField();
        upperPanel.add(number);
        JLabel typePrice = new JLabel("<html>Введите стоимость<br/>суточной аренды</html>", SwingConstants.LEFT);
        upperPanel.add(typePrice);
        JTextField price = new JTextField();
        upperPanel.add(price);
        GridBagConstraints uc = new GridBagConstraints();
        uc.weightx = 1;
        uc.weighty = 2;
        uc.gridx = 0;
        uc.gridy = 1;
        uc.fill = GridBagConstraints.HORIZONTAL;
        uc.anchor = GridBagConstraints.NORTH;
        add(upperPanel, uc);

        JLabel chooseModels = new JLabel("Выберите обслуживаемые марки автомобилей", SwingConstants.CENTER);
        GridBagConstraints cmc = new GridBagConstraints();
        cmc.weightx = 1;
        cmc.weighty = 1;
        cmc.gridx = 0;
        cmc.gridy = 2;
        cmc.fill = GridBagConstraints.HORIZONTAL;
        cmc.anchor = GridBagConstraints.NORTH;
        add(chooseModels, cmc);

        JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.LINE_AXIS));
        GridBagConstraints lc = new GridBagConstraints();
        lc.weightx = 1;
        lc.weighty = 5;
        lc.gridx = 0;
        lc.gridy = 3;
        lc.fill = GridBagConstraints.HORIZONTAL;
        lc.anchor = GridBagConstraints.NORTH;
        add(lowerPanel, lc);

        JPanel allModelsPanel = new JPanel(new GridBagLayout());
        lowerPanel.add(allModelsPanel);
        lowerPanel.add(javax.swing.Box.createRigidArea(new Dimension(15, 0)));

        JLabel allModels = new JLabel("Все марки", SwingConstants.CENTER);
        GridBagConstraints amc = new GridBagConstraints();
        amc.weightx = 1;
        amc.weighty = 1;
        amc.gridx = 0;
        amc.gridy = 0;
        amc.fill = GridBagConstraints.HORIZONTAL;
        amc.anchor = GridBagConstraints.NORTH;
        allModelsPanel.add(allModels, amc);

        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        JComboBox<String> models = new JComboBox<>(comboBoxModel);
        GridBagConstraints mc = new GridBagConstraints();
        mc.weightx = 1;
        mc.weighty = 5;
        mc.gridx = 0;
        mc.gridy = 1;
        mc.fill = GridBagConstraints.HORIZONTAL;
        mc.anchor = GridBagConstraints.NORTH;
        allModelsPanel.add(models, mc);

        //добавление в список
        List<Model> modelsG = informationController.getAllModels();
        for (Model model : modelsG) {
            models.addItem(model.getName());
        }
        //****************************************************************************************************************************************************
        JPanel selectButtons = new JPanel(new GridLayout(3, 1, 10, 10));
        JButton add = new JButton("Добавить в выбранные");
        selectButtons.add(add);
        JButton delete = new JButton("Убрать из выбранных");
        selectButtons.add(delete);

        /*
        JButton newModel = new JButton("Новая марка");
        selectButtons.add(newModel);
        */
        lowerPanel.add(selectButtons);
        lowerPanel.add(javax.swing.Box.createRigidArea(new Dimension(15, 0)));

        JPanel selectedModelsPanel = new JPanel(new GridBagLayout());
        lowerPanel.add(selectedModelsPanel);

        JLabel selectedModels = new JLabel("Выбранные марки", SwingConstants.CENTER);
        GridBagConstraints smc = new GridBagConstraints();
        smc.weightx = 1;
        smc.weighty = 1;
        smc.gridx = 0;
        smc.gridy = 0;
        smc.fill = GridBagConstraints.HORIZONTAL;
        smc.anchor = GridBagConstraints.NORTH;
        selectedModelsPanel.add(selectedModels, smc);

        //список справа
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> selected = new JList<>(listModel);
        selectedModelsPanel.add(selected);
        GridBagConstraints sc = new GridBagConstraints();
        sc.weightx = 1;
        sc.weighty = 5;
        sc.gridx = 0;
        sc.gridy = 1;
        sc.fill = GridBagConstraints.HORIZONTAL;
        sc.anchor = GridBagConstraints.NORTH;
        selectedModelsPanel.add(selected, sc);

        selected.setSelectionMode(MULTIPLE_INTERVAL_SELECTION);
        //добавление в список
        add.addActionListener(e -> {

            List<String> modelsT = new ArrayList<>();
            for (int i = 0; i < selected.getModel().getSize(); i++) {
                modelsT.add(String.valueOf(selected.getModel().getElementAt(i)));
            }

            String model = models.getSelectedItem().toString();
            List<String> modelsC = selected.getSelectedValuesList();
            if (!modelsT.contains(models.getSelectedItem())) {
                listModel.add(listModel.getSize(), model);
            }

        });

        delete.addActionListener(e -> {

            String model = models.getSelectedItem().toString();
            List<String> modelsT = new ArrayList<>();
            for (int i = 0; i < selected.getModel().getSize(); i++) {
                modelsT.add(String.valueOf(selected.getModel().getElementAt(i)));
            }
            if (modelsT.contains(models.getSelectedItem())) {
                listModel.removeElement(models.getSelectedItem().toString());
            }
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

        JButton save = new JButton("Добавить бокс");
        buttonsPanel.add(save);


        //добавление бокса обработчик
        save.addActionListener(e -> {
            System.out.println("Start");
            int id = 1;
            Double pr = 1.0;
            List<Box> boxes = informationController.getAllBoxes();
            if (!number.getText().isEmpty()) {
                if (!price.getText().isEmpty()) {
                    boolean cont = true;
                    try {
                        id = Integer.parseInt(number.getText().trim());
                        System.out.println("->>>>" + id);
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(parent,
                                "<html><i>Номер бокса должен быть целым числом</i>");
                        cont = false;
                    }
                    if (cont) {
                        cont = true;
                        try {
                            pr = Double.parseDouble(price.getText().trim());
                            System.out.println("->>>>" + pr);
                        } catch (NumberFormatException nfe) {
                            JOptionPane.showMessageDialog(parent,
                                    "<html><i>Суточная аренда должна быть вещественным числом</i>");
                            cont = false;
                        }
                        if (cont) {
                            boolean check = true;
                            for (Box box : boxes) {
                                if (box.getId() == id) {
                                    check = false;
                                }
                            }
                            if (check) {

                                //получение названий выбранных моделей
                                List<String> names = new ArrayList<>();
                                for (int i = 0; i < selected.getModel().getSize(); i++) {
                                    names.add(String.valueOf(selected.getModel().getElementAt(i)));
                                }
                                if (names.size() != 0) {
                                    //можно записывать в БД
                                    //формирование списка выбранных моделей из БД
                                    List<Model> modelsZ = new ArrayList<>();
                                    // List<Model> modelsT = informationController.getAllModels();
                                    for (String name : names) {
                                        modelsZ.add(informationController.getModelByName(name));
                                    }
                                    administrationController.addBox(id, pr, modelsZ);
                                    JOptionPane.showMessageDialog(parent,
                                            "<html><i>Бокс успешно добавлен</i>");
                                } else {
                                    JOptionPane.showMessageDialog(parent,
                                            "<html><i>Выберите хотя бы одну модель</i>");
                                }

                            } else {
                                JOptionPane.showMessageDialog(parent,
                                        "<html><i>Бокс с таким id уже есть в базе данных</i>");
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(parent,
                            "<html><i>Цена не может быть пустой</i>");
                }
            } else {
                JOptionPane.showMessageDialog(parent,
                        "<html><i>Номер бокса не может быть пустым</i>");
            }

            System.out.println("End");
        });

        /*
        JButton cancel = new JButton("Отмена");
        buttonsPanel.add(cancel);
        */
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
    }
}
