package edu.rsatu.api;

import edu.rsatu.garage.DocsHelper;
import edu.rsatu.garage.controller.AdministrationController;
import edu.rsatu.garage.controller.InformationController;
import edu.rsatu.garage.controller.RentController;
import edu.rsatu.garage.db.entitiesDao.ModelsDao;
import edu.rsatu.garage.entities.*;
import edu.rsatu.garage.entities.Box;
import edu.rsatu.garage.exceptions.DateException;


import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;

public class MainFrame extends JFrame {

    private JPanel mainPanel;
    private JPanel rentIssueMutablePanel;
    private ArrayDeque<JPanel> rentIssuePanels = new ArrayDeque<>();

    AdministrationController administrationController = new AdministrationController();
    InformationController informationController = new InformationController();

    ModelsDao modelsDao = new ModelsDao();

    public MainFrame() {


        getContentPane().setLayout(new GridBagLayout());

        JLabel caption = new JLabel("Гаражная фирма", SwingConstants.CENTER);
        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 1;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.gridheight = 2;
        cc.fill = GridBagConstraints.BOTH;
        getContentPane().add(caption, cc);

        GridBagConstraints mbc = new GridBagConstraints();
        mbc.weightx = 1;
        mbc.weighty = 1;
        mbc.gridx = 0;
        mbc.gridy = 2;
        mbc.fill = GridBagConstraints.HORIZONTAL;
        JMenuBar menuBar = new JMenuBar();

        JMenu administration = new JMenu("Администрирование боксов");
        menuBar.add(administration);
        JMenuItem newBox = new JMenuItem("Новый бокс");
        administration.add(newBox);
        newBox.addActionListener(e -> setMainPanel(getNewBoxPanel()));
        JMenuItem closeBox = new JMenuItem("Закрыть бокс");
        administration.add(closeBox);
        closeBox.addActionListener(e -> setMainPanel(getCloseBoxPanel()));
        JMenuItem changePrice = new JMenuItem("Изменить стоимость аренды");
        administration.add(changePrice);
        changePrice.addActionListener(e -> setMainPanel(getChangePricePanel()));
        administration.add(new JSeparator());
        JMenuItem newBrand = new JMenuItem("Новая марка");
        administration.add(newBrand);
        newBrand.addActionListener(e -> setMainPanel(getNewModelJPanel()));
        JMenuItem deleteModel = new JMenuItem("Прекратить обслуживание марки");
        administration.add(deleteModel);
        deleteModel.addActionListener(e -> setMainPanel(getDeleteModelPanel()));
        JMenu services = new JMenu("Обслуживание клиентов");
        menuBar.add(services);
        JMenuItem startRenting = new JMenuItem("Оформить аренду");
        services.add(startRenting);
        startRenting.addActionListener(e -> setMainPanel(getRentIssuePanel(null)));
        JMenuItem finishRenting = new JMenuItem("Закрыть аренду");
        services.add(finishRenting);
        finishRenting.addActionListener(e -> setMainPanel(getRentClosePanel()));

        JMenu selection = new JMenu("Справки");
        menuBar.add(selection);
        JMenuItem clients = new JMenuItem("Клиенты");
        selection.add(clients);
        clients.addActionListener(e -> setMainPanel(getClientsInfoPanel()));
        JMenuItem boxes = new JMenuItem("Боксы");
        selection.add(boxes);
        boxes.addActionListener(e -> setMainPanel(getBoxesInfoPanel()));
        JMenuItem models = new JMenuItem("Марки");
        selection.add(models);
        models.addActionListener(e -> setMainPanel(getModelsInfoPanel()));

        JMenu exit = new JMenu("Выход");
        menuBar.add(exit);
        exit.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                setVisible(false);
                dispose();
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });
        getContentPane().add(menuBar, mbc);

        mainPanel = new JPanel(new GridLayout(1, 1));
        GridBagConstraints mpc = new GridBagConstraints();
        mpc.weightx = 1;
        mpc.weighty = 30;
        mpc.gridx = 0;
        mpc.gridy = 3;
        mpc.gridheight = 30;
        mpc.fill = GridBagConstraints.BOTH;
        getContentPane().add(mainPanel, mpc);

        setSize(800, 600);
    }

    private void setMainPanel(JPanel newPanel) {
        mainPanel.removeAll();
        mainPanel.add(newPanel);
        getContentPane().validate();
        getContentPane().repaint();
    }

    private JPanel getNewBoxPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());

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
        mainPanel.add(caption, cc);

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
        mainPanel.add(upperPanel, uc);

        JLabel chooseModels = new JLabel("Выберите обслуживаемые марки автомобилей", SwingConstants.CENTER);
        GridBagConstraints cmc = new GridBagConstraints();
        cmc.weightx = 1;
        cmc.weighty = 1;
        cmc.gridx = 0;
        cmc.gridy = 2;
        cmc.fill = GridBagConstraints.HORIZONTAL;
        cmc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(chooseModels, cmc);

        JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.LINE_AXIS));
        GridBagConstraints lc = new GridBagConstraints();
        lc.weightx = 1;
        lc.weighty = 5;
        lc.gridx = 0;
        lc.gridy = 3;
        lc.fill = GridBagConstraints.HORIZONTAL;
        lc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(lowerPanel, lc);

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
        mainPanel.add(buttonsPanel, bpc);

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
                        JOptionPane.showMessageDialog(MainFrame.this,
                                "<html><i>Номер бокса должен быть целым числом</i>");
                        cont = false;
                    }
                    if (cont) {
                        cont = true;
                        try {
                            pr = Double.parseDouble(price.getText().trim());
                            System.out.println("->>>>" + pr);
                        } catch (NumberFormatException nfe) {
                            JOptionPane.showMessageDialog(MainFrame.this,
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
                                    JOptionPane.showMessageDialog(MainFrame.this,
                                            "<html><i>Бокс успешно добавлен</i>");
                                } else {
                                    JOptionPane.showMessageDialog(MainFrame.this,
                                            "<html><i>Выберите хотя бы одну модель</i>");
                                }

                            } else {
                                JOptionPane.showMessageDialog(MainFrame.this,
                                        "<html><i>Бокс с таким id уже есть в базе данных</i>");
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "<html><i>Цена не может быть пустой</i>");
                }
            } else {
                JOptionPane.showMessageDialog(MainFrame.this,
                        "<html><i>Номер бокса не может быть пустым</i>");
            }

            System.out.println("End");
        });




        /*
        JButton cancel = new JButton("Отмена");
        buttonsPanel.add(cancel);
        */
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        return mainPanel;
    }

    private JPanel getNewModelJPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());

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
        mainPanel.add(caption, cc);

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
        mainPanel.add(upperPanel, uc);

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 20));
        GridBagConstraints bpc = new GridBagConstraints();
        bpc.weightx = 1;
        bpc.weighty = 1;
        bpc.gridx = 0;
        bpc.gridy = 2;
        bpc.fill = GridBagConstraints.HORIZONTAL;
        bpc.anchor = GridBagConstraints.SOUTH;
        mainPanel.add(buttonsPanel, bpc);

        JButton save = new JButton("Добавить марку");
        buttonsPanel.add(save);
        save.addActionListener(e -> addModel(nameM.getText()));
        /*JButton cancel = new JButton("Отмена");
        buttonsPanel.add(cancel);
        cancel.addActionListener(e -> canselAddModel());*/
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        return mainPanel;
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
                JOptionPane.showMessageDialog(MainFrame.this,
                        "<html><i>Модель успешно добавлена в базу данных</i>");
            } else {
                JOptionPane.showMessageDialog(MainFrame.this,
                        "<html><i>Такая модель уже имеется в базе данных</i>");
            }
        } else {
            JOptionPane.showMessageDialog(MainFrame.this,
                    "<html><i>Название модели не может быть пустым</i>");
        }
    }


    private JPanel getDeleteModelPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());

        JLabel caption = new JLabel("Прекращение обслуживания марки", SwingConstants.CENTER);
        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 2;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(caption, cc);

        JLabel chooseModel = new JLabel("Выберите марку из списка", SwingConstants.CENTER);
        GridBagConstraints cmc = new GridBagConstraints();
        cmc.weightx = 1;
        cmc.weighty = 1;
        cmc.gridx = 0;
        cmc.gridy = 1;
        cmc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(chooseModel, cmc);

        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        JComboBox<String> models = new JComboBox<>(comboBoxModel);
        GridBagConstraints mc = new GridBagConstraints();
        mc.weightx = 0.6;
        mc.weighty = 5;
        mc.gridx = 0;
        mc.gridy = 2;
        mc.fill = GridBagConstraints.CENTER;
        mc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(models, mc);

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
        mainPanel.add(buttonsPanel, bpc);


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

        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        return mainPanel;
    }

    //удаление модели
    private void stopService(Object item, String nameModel, JComboBox<String> models) {
        System.out.println(nameModel);
        administrationController.deleteModelByName(nameModel);
        models.removeItem(item);
    }


    private JPanel getCloseBoxPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());

        JLabel caption = new JLabel("Закрытие бокса", SwingConstants.CENTER);
        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 2;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(caption, cc);

        JLabel chooseBox = new JLabel("Выберите номер бокса из списка", SwingConstants.CENTER);
        mainPanel.add(chooseBox);
        GridBagConstraints cbc = new GridBagConstraints();
        cbc.weightx = 1;
        cbc.weighty = 1;
        cbc.gridx = 0;
        cbc.gridy = 1;
        cbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(chooseBox, cbc);

        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        JComboBox<String> boxes = new JComboBox<>(comboBoxModel);
        GridBagConstraints bc = new GridBagConstraints();
        bc.weightx = 0.6;
        bc.weighty = 5;
        bc.gridx = 0;
        bc.gridy = 2;
        bc.fill = GridBagConstraints.CENTER;
        bc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(boxes, bc);

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
        mainPanel.add(buttonsPanel, bpc);

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
                JOptionPane.showMessageDialog(MainFrame.this,
                        "<html><i>Выбранный бокс был успешно закрыт</i>");
            }
        });

        /*
        JButton cancel = new JButton("Отмена");
        buttonsPanel.add(cancel);
        */
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        return mainPanel;
    }

    private JPanel getChangePricePanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());

        JLabel caption = new JLabel("Изменение стоимости аренды", SwingConstants.CENTER);
        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 1;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(caption, cc);

        JPanel optionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        GridBagConstraints opc = new GridBagConstraints();
        opc.weightx = 1;
        opc.weighty = 1;
        opc.gridx = 0;
        opc.gridy = 1;
        opc.fill = GridBagConstraints.CENTER;
        cc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(optionPanel, opc);

        ButtonGroup options = new ButtonGroup();
        JRadioButton increase = new JRadioButton("Увеличить стоимость");
        increase.setSelected(true);
        JRadioButton decrease = new JRadioButton("Уменьшить стоимость");
        options.add(increase);
        options.add(decrease);
        optionPanel.add(increase);
        optionPanel.add(decrease);

        JPanel ratioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        GridBagConstraints rpc = new GridBagConstraints();
        rpc.weightx = 1;
        rpc.weighty = 1;
        rpc.gridx = 0;
        rpc.gridy = 2;
        rpc.fill = GridBagConstraints.CENTER;
        rpc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(ratioPanel, rpc);

        JLabel vLabel = new JLabel("в", SwingConstants.RIGHT);
        ratioPanel.add(vLabel);
        JTextField text = new JTextField();
        text.setColumns(10);
        ratioPanel.add(text);
        JLabel times = new JLabel("раз", SwingConstants.LEFT);
        ratioPanel.add(times);

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 20));
        GridBagConstraints bpc = new GridBagConstraints();
        bpc.weightx = 1;
        bpc.weighty = 1;
        bpc.gridx = 0;
        bpc.gridy = 3;
        bpc.fill = GridBagConstraints.HORIZONTAL;
        bpc.anchor = GridBagConstraints.SOUTH;
        mainPanel.add(buttonsPanel, bpc);

        JButton save = new JButton("Подтвердить изменение стоимости");
        buttonsPanel.add(save);
        save.addActionListener(e -> {

            String str = text.getText();
            if (!str.equals("")) {
                Double mult = null;
                try {
                    mult = Double.parseDouble(str.trim());
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "<html><i>Число должно быть вещественным</i>");
                }
                if (!mult.equals(null)) {
                    if (increase.isSelected()) {
                        administrationController.increaseBoxPrices(mult);

                        JOptionPane.showMessageDialog(MainFrame.this,
                                "<html><i>Цена аренды всех боксов была увеличена в " + str + " раз</i>");
                    } else if (decrease.isSelected()) {
                        administrationController.decreaseBoxPrices(mult);

                        JOptionPane.showMessageDialog(MainFrame.this,
                                "<html><i>Цена аренды всех боксов была уменьшена в " + str + " раз</i>");
                    }

                }
            } else {
                JOptionPane.showMessageDialog(MainFrame.this,
                        "<html><i>Введите значение!</i>");
            }


        });
        /*
        JButton cancel = new JButton("Отмена");
        buttonsPanel.add(cancel);
        */
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        return mainPanel;
    }

    private JPanel getRentIssuePanel(RentController controller) {
        RentController rentController = controller != null ? controller : new RentController();
        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        JLabel caption = new JLabel("Оформление аренды", SwingConstants.CENTER);
        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 1;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.anchor = GridBagConstraints.PAGE_START;
        mainPanel.add(caption, cc);

        rentIssueMutablePanel = new JPanel(new GridLayout(1, 1));
        GridBagConstraints mpc = new GridBagConstraints();
        mpc.weightx = 1;
        mpc.weighty = 29;
        mpc.gridx = 0;
        mpc.gridy = 1;
        mpc.fill = GridBagConstraints.HORIZONTAL;
        mpc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(rentIssueMutablePanel, mpc);

        rentIssuePanels.clear();
        rentIssueMutablePanel.add(getRentFirstPanel(rentController));

        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        return mainPanel;
    }

    private JPanel getRentFirstPanel(RentController rentController) {
        JPanel mutablePanel = new JPanel(new GridBagLayout());

        JLabel caption = new JLabel("Выберите клиента", SwingConstants.CENTER);
        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 1;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.anchor = GridBagConstraints.NORTH;
        mutablePanel.add(caption, cc);

        JPanel upperPanel = new JPanel(new GridLayout(1, 2));
        GridBagConstraints uc = new GridBagConstraints();
        uc.weightx = 1;
        uc.weighty = 5;
        uc.gridx = 0;
        uc.gridy = 1;
        uc.fill = GridBagConstraints.HORIZONTAL;
        uc.anchor = GridBagConstraints.NORTH;
        mutablePanel.add(upperPanel, uc);

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
        mutablePanel.add(newClient, ncc);
        newClient.addActionListener(e -> setMainPanel(getNewClientPanel(rentController)));

        String infoDefaultText = "<html>Клиент: <br/> </html>";
        JLabel info = new JLabel();
        GridBagConstraints ic = new GridBagConstraints();
        ic.weightx = 1;
        ic.weighty = 2;
        ic.gridx = 0;
        ic.gridy = 3;
        ic.fill = GridBagConstraints.HORIZONTAL;
        ic.anchor = GridBagConstraints.NORTH;
        mutablePanel.add(info, ic);
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
        mutablePanel.add(buttonsPanel, bpc);

        JButton nextStep = new JButton("Следующий шаг");
        buttonsPanel.add(nextStep);
        nextStep.addActionListener(e -> {
            if (rentController.getCurrentClient() == null) {
                JOptionPane.showMessageDialog(this, "Не выбран клиент");
            } else {
                rentIssuePanels.add(mutablePanel);
                setRentMutablePanel(getRentSecondPanel(rentController));
            }

        });
        JButton cancel = new JButton("Отмена");
        buttonsPanel.add(cancel);
        cancel.addActionListener(e -> {

        });

        return mutablePanel;
    }

    private JPanel getRentSecondPanel(RentController rentController) {
        JPanel mutablePanel = new JPanel(new GridBagLayout());
        JLabel caption = new JLabel("Выберите марку автомобиля", SwingConstants.CENTER);
        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 1;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.anchor = GridBagConstraints.NORTH;
        mutablePanel.add(caption, cc);

        JPanel upperPanel = new JPanel(new GridLayout(1, 2));
        GridBagConstraints uc = new GridBagConstraints();
        uc.weightx = 1;
        uc.weighty = 5;
        uc.gridx = 0;
        uc.gridy = 1;
        uc.fill = GridBagConstraints.HORIZONTAL;
        uc.anchor = GridBagConstraints.NORTH;
        mutablePanel.add(upperPanel, uc);

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
        mutablePanel.add(info, ic);

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
        mutablePanel.add(buttonsPanel, bpc);

        JButton nextStep = new JButton("Следующий шаг");
        buttonsPanel.add(nextStep);
        nextStep.addActionListener(e -> {
            if (rentController.getCurrentModel() == null) {
                JOptionPane.showMessageDialog(this, "Не выбрана марка автомобиля");
            } else {
                rentIssuePanels.add(mutablePanel);
                setRentMutablePanel(getRentThirdPanel(rentController, defaultInfoText));
            }
        });
        JButton back = new JButton("Назад");
        buttonsPanel.add(back);
        back.addActionListener(e -> {
            rentController.setCurrentModel(null);
            setRentMutablePanel(rentIssuePanels.removeLast());
        });

        return mutablePanel;
    }

    private JPanel getRentThirdPanel(RentController rentController, String prevInfo) {
        JPanel mutablePanel = new JPanel(new GridBagLayout());

        JLabel caption = new JLabel("Выберите бокс", SwingConstants.CENTER);
        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 1;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.anchor = GridBagConstraints.NORTH;
        mutablePanel.add(caption, cc);

        JPanel upperPanel = new JPanel(new GridLayout(1, 2));
        GridBagConstraints uc = new GridBagConstraints();
        uc.weightx = 1;
        uc.weighty = 5;
        uc.gridx = 0;
        uc.gridy = 1;
        uc.fill = GridBagConstraints.HORIZONTAL;
        uc.anchor = GridBagConstraints.NORTH;
        mutablePanel.add(upperPanel, uc);

        JPanel boxesPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        upperPanel.add(boxesPanel);
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        JComboBox<String> boxes = new JComboBox<>(comboBoxModel);
        boxesPanel.add(boxes);
        boxesPanel.add(new JLabel());
        boxesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        List<edu.rsatu.garage.entities.Box> allBoxes = informationController.getFreeBoxesForModel(rentController.getCurrentModel());
        comboBoxModel.addAll(allBoxes.stream().map(box -> "" + box.getId() + " стоимость: " + box.getRentPrice()).collect(Collectors.toList()));

        JPanel selectPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        JButton select = new JButton("Выбрать");
        selectPanel.add(select);
        JButton cancelSelection = new JButton("Отменить выбор");
        selectPanel.add(cancelSelection);
        selectPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        upperPanel.add(selectPanel);

        String defaultInfoText = prevInfo + "<br/>Марка автомобиля: " + rentController.getCurrentModel().getName();
        JLabel info = new JLabel("<html>" + defaultInfoText + "</html>");
        GridBagConstraints ic = new GridBagConstraints();
        ic.weightx = 1;
        ic.weighty = 2;
        ic.gridx = 0;
        ic.gridy = 2;
        ic.fill = GridBagConstraints.HORIZONTAL;
        ic.anchor = GridBagConstraints.NORTH;
        mutablePanel.add(info, ic);

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
        mutablePanel.add(buttonsPanel, bpc);

        JButton nextStep = new JButton("Следующий шаг");
        buttonsPanel.add(nextStep);
        nextStep.addActionListener(e -> {
            if (rentController.getCurrentBox() == null) {
                JOptionPane.showMessageDialog(this, "Не выбран бокс");
            } else {
                rentIssuePanels.add(mutablePanel);
                setRentMutablePanel(getRentFinalPanel(rentController, defaultInfoText));
            }
        });
        JButton back = new JButton("Назад");
        buttonsPanel.add(back);
        back.addActionListener(e -> {
            rentController.setCurrentBox(null);
            setRentMutablePanel(rentIssuePanels.removeLast());
        });

        return mutablePanel;
    }

    private JPanel getRentFinalPanel(RentController rentController, String previousInfo) {
        JPanel mutablePanel = new JPanel(new GridBagLayout());

        String defaultInfoText = previousInfo + "<br/>Бокс: " + rentController.getCurrentBox().getId() + " стоимость: " + rentController.getCurrentBox().getRentPrice();
        JLabel caption = new JLabel("<html>" + defaultInfoText + "</html>");
        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 5;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.anchor = GridBagConstraints.NORTH;
        mutablePanel.add(caption, cc);

        JPanel autoPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        GridBagConstraints apc = new GridBagConstraints();
        apc.weightx = 1;
        apc.weighty = 2;
        apc.gridx = 0;
        apc.gridy = 1;
        apc.fill = GridBagConstraints.HORIZONTAL;
        apc.anchor = GridBagConstraints.NORTH;
        mutablePanel.add(autoPanel, apc);

        JLabel typeAuto = new JLabel("Введите государственный номер автомобиля", SwingConstants.LEFT);
        autoPanel.add(typeAuto);
        JTextField autoText = new JTextField();
        autoPanel.add(autoText);

        JPanel datesPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        GridBagConstraints dpc = new GridBagConstraints();
        dpc.weightx = 1;
        dpc.weighty = 4;
        dpc.gridx = 0;
        dpc.gridy = 2;
        dpc.fill = GridBagConstraints.HORIZONTAL;
        dpc.anchor = GridBagConstraints.NORTH;
        mutablePanel.add(datesPanel, dpc);

        JLabel startDate = new JLabel("Дата начала аренды", SwingConstants.CENTER);
        datesPanel.add(startDate);
        JLabel finishDate = new JLabel("Дата окончания аренды", SwingConstants.CENTER);
        datesPanel.add(finishDate);
        JTextField startText = new JTextField();
        datesPanel.add(startText);
        JTextField finishText = new JTextField();
        datesPanel.add(finishText);

        JLabel sum = new JLabel("Общая сумма:", SwingConstants.LEFT);
        GridBagConstraints sc = new GridBagConstraints();
        sc.weightx = 1;
        sc.weighty = 1;
        sc.gridx = 0;
        sc.gridy = 3;
        sc.fill = GridBagConstraints.HORIZONTAL;
        sc.anchor = GridBagConstraints.NORTH;
        mutablePanel.add(sum, sc);

        startText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                getRentPrice(rentController, sum, startText, finishText);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                getRentPrice(rentController, sum, startText, finishText);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                getRentPrice(rentController, sum, startText, finishText);
            }
        });

        finishText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                getRentPrice(rentController, sum, startText, finishText);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                getRentPrice(rentController, sum, startText, finishText);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                getRentPrice(rentController, sum, startText, finishText);
            }
        });

        JButton back = new JButton("Назад");
        GridBagConstraints bc = new GridBagConstraints();
        bc.weightx = 1;
        bc.weighty = 1;
        bc.gridx = 0;
        bc.gridy = 4;
        bc.fill = GridBagConstraints.CENTER;
        bc.anchor = GridBagConstraints.SOUTH;
        mutablePanel.add(back, bc);
        back.addActionListener(e -> setRentMutablePanel(rentIssuePanels.removeLast()));

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        GridBagConstraints bpc = new GridBagConstraints();
        bpc.weightx = 1;
        bpc.weighty = 1;
        bpc.gridx = 0;
        bpc.gridy = 5;
        bpc.fill = GridBagConstraints.HORIZONTAL;
        bpc.anchor = GridBagConstraints.SOUTH;
        mutablePanel.add(buttonsPanel, bpc);

        JButton save = new JButton("Оформить");
        buttonsPanel.add(save);
        JButton cancel = new JButton("Отмена");
        buttonsPanel.add(cancel);

        save.addActionListener(new AddCarListener(rentController, autoText, startText, finishText));

        return mutablePanel;
    }

    private void getRentPrice(RentController rentController, JLabel priceLabel, JTextField startText, JTextField finishText) {
        Runnable calculate = () -> {
            if (startText.getText() != null && startText.getText().length() == 10 &&
                    finishText.getText() != null && finishText.getText().length() == 10) {
                try {
                    double price = rentController.calculatePrice(startText.getText(), finishText.getText());
                    priceLabel.setText("Общая сумма: " + price);
                } catch (DateException e) {
                    priceLabel.setText("Общая сумма:");
                }
            } else {
                priceLabel.setText("Общая сумма:");
            }
        };
        SwingUtilities.invokeLater(calculate);
    }

    private void setRentMutablePanel(JPanel newMutable) {
        rentIssueMutablePanel.removeAll();
        rentIssueMutablePanel.add(newMutable);
        getContentPane().validate();
        getContentPane().repaint();
    }

    private JPanel getRentClosePanel() {
        RentController rentController = new RentController();
        JPanel mainPanel = new JPanel(new GridBagLayout());
        JLabel caption = new JLabel("Закрытие аренды", SwingConstants.CENTER);
        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 1;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.anchor = GridBagConstraints.PAGE_START;
        mainPanel.add(caption, cc);

        JPanel autoPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        GridBagConstraints apc = new GridBagConstraints();
        apc.weightx = 1;
        apc.weighty = 1;
        apc.gridx = 0;
        apc.gridy = 1;
        apc.fill = GridBagConstraints.HORIZONTAL;
        apc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(autoPanel, apc);

        JLabel typeAutoNumber = new JLabel("Введите номер автомобиля", SwingConstants.LEFT);
        autoPanel.add(typeAutoNumber);
        JTextField autoNumber = new JTextField();
        autoPanel.add(autoNumber);

        JButton findAuto = new JButton("Найти автомобиль");
        GridBagConstraints fac = new GridBagConstraints();
        fac.weightx = 1;
        fac.weighty = 1;
        fac.gridx = 0;
        fac.gridy = 2;
        fac.fill = GridBagConstraints.CENTER;
        fac.anchor = GridBagConstraints.NORTH;
        mainPanel.add(findAuto, fac);

        JLabel info = new JLabel("", SwingConstants.LEFT);
        GridBagConstraints ic = new GridBagConstraints();
        ic.weightx = 1;
        ic.weighty = 5;
        ic.gridx = 0;
        ic.gridy = 3;
        ic.fill = GridBagConstraints.HORIZONTAL;
        ic.anchor = GridBagConstraints.NORTH;
        mainPanel.add(info, ic);

        findAuto.addActionListener(e -> {
            String number = autoNumber.getText();
            if (RentController.checkNumber2(number)) {
                Car car = informationController.getCarByNumber(number);
                rentController.setCurrentCar(car);
                Client client = informationController.getClientById(car.getClientId());
                rentController.setCurrentClient(client);
                Model model = informationController.getModelById(car.getModelId());
                rentController.setCurrentModel(model);
                Box box = informationController.getBoxByNumber(car.getBoxId());
                rentController.setCurrentBox(box);
                info.setText("<html>Клиент: " + client.getSurname() + " Адрес: " + client.getAddress() +
                        "<br/>Автомобиль марки: " + model.getName() +
                        "<br/>находится в боксе: " + box.getId() +
                        "<br/>с " + car.getRentStartDate().toString() +
                        "<br/>Срок окончания аренды: " + car.getRentEndDate().toString() + "</html>");
            } else {
                JOptionPane.showMessageDialog(this, "Неправильный формат номера автомобиля");
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
        mainPanel.add(buttonsPanel, bpc);

        JButton save = new JButton("Закрыть аренду");
        buttonsPanel.add(save);
        JButton cancel = new JButton("Отмена");
        buttonsPanel.add(cancel);

        save.addActionListener(new DeleteCarListener(rentController));

        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        return mainPanel;
    }

    private JPanel getNewClientPanel(RentController rentController) {
        JPanel mainPanel = new JPanel(new GridBagLayout());

        JLabel caption = new JLabel("Добавление нового клиента", SwingConstants.CENTER);
        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 1;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(caption, cc);

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
        mainPanel.add(upperPanel, uc);

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 20));
        GridBagConstraints bpc = new GridBagConstraints();
        bpc.weightx = 1;
        bpc.weighty = 1;
        bpc.gridx = 0;
        bpc.gridy = 2;
        bpc.fill = GridBagConstraints.HORIZONTAL;
        bpc.anchor = GridBagConstraints.SOUTH;
        mainPanel.add(buttonsPanel, bpc);

        JButton save = new JButton("Добавить клиента");
        buttonsPanel.add(save);
        save.addActionListener(e -> {
            Client client = new Client(surname.getText(), address.getText());
            rentController.getClientsWithoutCar().add(client);
            setMainPanel(getRentIssuePanel(rentController));
            JOptionPane.showMessageDialog(this, "Клиент успешно добавлен");
        });
        JButton cancel = new JButton("Отмена");
        buttonsPanel.add(cancel);
        cancel.addActionListener(e -> setMainPanel(getRentIssuePanel(rentController)));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        return mainPanel;
    }

    private JPanel getBoxesInfoPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());

        JLabel caption = new JLabel("Справки по боксам", SwingConstants.CENTER);
        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 1;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.anchor = GridBagConstraints.PAGE_START;
        mainPanel.add(caption, cc);

        JPanel optionPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        GridBagConstraints opc = new GridBagConstraints();
        opc.weightx = 1;
        opc.weighty = 1;
        opc.gridx = 0;
        opc.gridy = 1;
        opc.fill = GridBagConstraints.HORIZONTAL;
        opc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(optionPanel, opc);

        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        JComboBox<String> models = new JComboBox<>(comboBoxModel);

        List<Model> modelsG = informationController.getAllModels();
        for (Model model : modelsG) {
            models.addItem(model.getName());
        }
        //надо бы делать невидимым по хорошему, но как?
        models.setEnabled(false);


        JCheckBox all = new JCheckBox("Все боксы");
        all.setSelected(true);
        JCheckBox free = new JCheckBox("Свободные боксы");
        JCheckBox forModel = new JCheckBox("Боксы, обслуживающие марку:");
        optionPanel.add(all);
        optionPanel.add(free);
        optionPanel.add(forModel);

        JPanel forModelPanel = new JPanel(new GridLayout(1, 2, 10, 20));
        forModelPanel.add(forModel);
        forModelPanel.add(models);

        GridBagConstraints uc = new GridBagConstraints();
        uc.weightx = 1;
        uc.weighty = 5;
        uc.gridx = 0;
        uc.gridy = 2;
        uc.fill = GridBagConstraints.HORIZONTAL;
        uc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(forModelPanel, uc);

        all.addActionListener(e -> {
            if (all.isSelected()) {
                free.setSelected(false);
                forModel.setSelected(false);
                models.setEnabled(false);
            }
        });
        free.addActionListener(e -> {
            if (free.isSelected()) {
                all.setSelected(false);
            }
        });
        forModel.addActionListener(e -> {
            if (forModel.isSelected()) {
                all.setSelected(false);
                models.setEnabled(true);
            } else {
                models.setEnabled(false);
            }
        });


        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 20));
        GridBagConstraints bpc = new GridBagConstraints();
        bpc.weightx = 1;
        bpc.weighty = 1;
        bpc.gridx = 0;
        bpc.gridy = 3;
        bpc.fill = GridBagConstraints.HORIZONTAL;
        bpc.anchor = GridBagConstraints.SOUTH;
        mainPanel.add(buttonsPanel, bpc);

        JButton save = new JButton("Получить справку");
        buttonsPanel.add(save);
        //
        save.addActionListener(e -> {
            if (all.isSelected() || free.isSelected() || forModel.isSelected()) {
                if (forModel.isSelected()) {
                    setMainPanel(BoxesInfo(all.isSelected(), free.isSelected(), forModel.isSelected(), models.getSelectedItem().toString()));
                } else {
                    setMainPanel(BoxesInfo(all.isSelected(), free.isSelected(), forModel.isSelected(), ""));
                }
            }
        });
        /*
        JButton cancel = new JButton("Отмена");
        buttonsPanel.add(cancel);
        */
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        return mainPanel;
    }

    //панель информации по боксам
    private JPanel BoxesInfo(boolean all, boolean free, boolean forModel, String modelName) {
        JPanel mainPanel = new JPanel(new GridBagLayout());

        JLabel caption1 = new JLabel("Все боксы", SwingConstants.CENTER);
        JLabel caption2 = new JLabel("Свободные боксы", SwingConstants.CENTER);
        JLabel caption3 = new JLabel("Боксы обслуживающие марку" + modelName, SwingConstants.CENTER);
        JLabel caption4 = new JLabel("Свободные боксы обслуживающие марку " + modelName, SwingConstants.CENTER);

        String text = "";
        String text1 = "Все боксы";
        String text2 = "Свободные боксы";
        String text3 = "Боксы обслуживающие марку " + modelName;
        String text4 = "Свободные боксы обслуживающие марку " + modelName;

        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 2;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.anchor = GridBagConstraints.PAGE_START;
        int r = 0;

        if (all) {
            text = text1;
            r = 1;
        } else {
            if (free) {
                if (forModel) {
                    text = text4;
                    r = 4;
                } else {
                    text = text2;
                    r = 2;
                }

            } else if (forModel) {
                text = text3;
                r = 3;
            }
        }

        JLabel chooseBox = new JLabel(text, SwingConstants.CENTER);
        mainPanel.add(chooseBox);
        GridBagConstraints cbc = new GridBagConstraints();
        cbc.weightx = 1;
        cbc.weighty = 1;
        cbc.gridx = 0;
        cbc.gridy = 1;
        cbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(chooseBox, cbc);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> boxes = new JList<>(listModel);
        GridBagConstraints bc = new GridBagConstraints();
        bc.weightx = 0.6;
        bc.weighty = 5;
        bc.gridx = 0;
        bc.gridy = 2;
        bc.fill = GridBagConstraints.CENTER;
        bc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(boxes, bc);

        List<Box> boxesX = new ArrayList<>();

        if (r == 1) {
            boxesX = informationController.getAllBoxes();
        } else if (r == 2) {
            boxesX = informationController.getFreeBoxes();
        } else if (r == 3) {
            boxesX = informationController.getBoxesForModel(informationController.getModelByName(modelName));
        } else if (r == 4) {
            boxesX = informationController.getFreeBoxesForModel(informationController.getModelByName(modelName));
        }
        if (boxesX.size() == 0) {
            listModel.add(listModel.getSize(), "отсутствуют");
        }


        for (Box box : boxesX) {
            Client client = informationController.getClientForBox(box);
            String cliName = "-";
            if (client != null) {
                cliName = client.getSurname();
            }

            listModel.add(listModel.getSize(), "Бокс №" + box.getId() + "       " + "цена аренды: " + box.getRentPrice() +
                    " Р" + "       " + "владелец: " + cliName);
        }

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 20));
        GridBagConstraints bpc = new GridBagConstraints();
        bpc.weightx = 1;
        bpc.weighty = 1;
        bpc.gridx = 0;
        bpc.gridy = 3;
        bpc.fill = GridBagConstraints.HORIZONTAL;
        bpc.anchor = GridBagConstraints.SOUTH;
        mainPanel.add(buttonsPanel, bpc);

        JButton cancel = new JButton("Назад");
        buttonsPanel.add(cancel);
        cancel.addActionListener(e -> setMainPanel(getBoxesInfoPanel()));

        JButton getDoc = new JButton("Получить спраку в формате docx");
        buttonsPanel.add(getDoc);

        return mainPanel;
    }


    private JPanel getModelsInfoPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());

        JLabel caption = new JLabel("Справки по маркам", SwingConstants.CENTER);
        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 1;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.anchor = GridBagConstraints.PAGE_START;
        mainPanel.add(caption, cc);

        JPanel optionPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        GridBagConstraints opc = new GridBagConstraints();
        opc.weightx = 1;
        opc.weighty = 1;
        opc.gridx = 0;
        opc.gridy = 1;
        opc.fill = GridBagConstraints.HORIZONTAL;
        opc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(optionPanel, opc);


        JCheckBox all = new JCheckBox("Все марки");
        all.setSelected(true);

        optionPanel.add(all);


        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        JComboBox<String> boxes = new JComboBox<>(comboBoxModel);
        GridBagConstraints bc = new GridBagConstraints();
        bc.weightx = 1;
        bc.weighty = 1;
        bc.gridx = 0;
        bc.gridy = 2;
        bc.fill = GridBagConstraints.CENTER;
        bc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(boxes, bc);

        List<Box> boxesG = informationController.getAllBoxes();
        for (Box box : boxesG) {
            boxes.addItem(box.getId().toString());
        }
        //надо бы делать невидимым по хорошему, но как?
        boxes.setEnabled(false);

        JPanel forBoxPanel = new JPanel(new GridLayout(1, 2, 10, 20));
        JCheckBox forBox = new JCheckBox("Марки, которые могут быть помещены в бокс:");
        forBoxPanel.add(forBox);
        forBoxPanel.add(boxes);

        GridBagConstraints uc = new GridBagConstraints();
        uc.weightx = 1;
        uc.weighty = 5;
        uc.gridx = 0;
        uc.gridy = 2;
        uc.fill = GridBagConstraints.HORIZONTAL;
        uc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(forBoxPanel, uc);

        all.addActionListener(e ->{
            if(all.isSelected()){
                forBox.setSelected(false);
                boxes.setEnabled(false);
            }
        });

        forBox.addActionListener(e ->{
            if(forBox.isSelected()){
                all.setSelected(false);
                boxes.setEnabled(true);
            }
        });

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 20));
        GridBagConstraints bpc = new GridBagConstraints();
        bpc.weightx = 1;
        bpc.weighty = 1;
        bpc.gridx = 0;
        bpc.gridy = 3;
        bpc.fill = GridBagConstraints.HORIZONTAL;
        bpc.anchor = GridBagConstraints.SOUTH;
        mainPanel.add(buttonsPanel, bpc);

        JButton save = new JButton("Получить справку");
        buttonsPanel.add(save);
        save.addActionListener(e -> {
            if (all.isSelected() || forBox.isSelected())  {
                if (forBox.isSelected()) {
                    setMainPanel(ModelsInfo(all.isSelected(), forBox.isSelected(), boxes.getSelectedItem().toString()));
                } else {
                    setMainPanel(ModelsInfo(all.isSelected(), forBox.isSelected(), "-"));
                }
            }
        });

        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        return mainPanel;
    }

    //панель информации по маркам
    private JPanel ModelsInfo(boolean all, boolean forBox, String boxID) {
        JPanel mainPanel = new JPanel(new GridBagLayout());

        String text = "";
        String text1 = "Все марки";
        String text2 = "Марки, которые могут быть помещены в бокс №"+ boxID;

        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 2;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.anchor = GridBagConstraints.PAGE_START;
        int r = 0;

        if (all) {
            text = text1;
            r = 1;
        } else {
            text = text2;
            r = 2;
        }

        JLabel chooseBox = new JLabel(text, SwingConstants.CENTER);
        mainPanel.add(chooseBox);
        GridBagConstraints cbc = new GridBagConstraints();
        cbc.weightx = 1;
        cbc.weighty = 1;
        cbc.gridx = 0;
        cbc.gridy = 1;
        cbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(chooseBox, cbc);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> boxes = new JList<>(listModel);
        GridBagConstraints bc = new GridBagConstraints();
        bc.weightx = 0.6;
        bc.weighty = 5;
        bc.gridx = 0;
        bc.gridy = 2;
        bc.fill = GridBagConstraints.CENTER;
        bc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(boxes, bc);

        Integer id = -1;
        try {
            id = Integer.parseInt(boxID.trim());
        } catch (NumberFormatException nfe) {
        }

        List<Model> modelsX = new ArrayList<>();

        if (r == 1) {
            modelsX = informationController.getAllModels();
        } else {
            modelsX = informationController.getModelsForBox(informationController.getBoxByNumber(id));
        }
        if (modelsX.size() == 0) {
            listModel.add(listModel.getSize(), "отсутствуют");
        }

        for (Model model : modelsX) {

            listModel.add(listModel.getSize(), model.getName());
        }

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 20));
        GridBagConstraints bpc = new GridBagConstraints();
        bpc.weightx = 1;
        bpc.weighty = 1;
        bpc.gridx = 0;
        bpc.gridy = 3;
        bpc.fill = GridBagConstraints.HORIZONTAL;
        bpc.anchor = GridBagConstraints.SOUTH;
        mainPanel.add(buttonsPanel, bpc);

        JButton cancel = new JButton("Назад");
        buttonsPanel.add(cancel);
        cancel.addActionListener(e -> setMainPanel(getModelsInfoPanel()));

        JButton getDoc = new JButton("Получить спраку в формате docx");
        buttonsPanel.add(getDoc);
        getDoc.addActionListener(new ModelNotesListener(modelsX, text));

        return mainPanel;
    }



    private JPanel getClientsInfoPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());

        JLabel caption = new JLabel("Справки по клиентам", SwingConstants.CENTER);
        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 1;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.gridwidth = 2;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.anchor = GridBagConstraints.PAGE_START;
        mainPanel.add(caption, cc);

        JCheckBox all = new JCheckBox("Все");
        GridBagConstraints alc = new GridBagConstraints();
        alc.weightx = 1;
        alc.weighty = 1;
        alc.gridx = 0;
        alc.gridy = 1;
        alc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(all, alc);

        JCheckBox forBox = new JCheckBox("Занимающий бокс");
        GridBagConstraints fbc = new GridBagConstraints();
        fbc.weightx = 1;
        fbc.weighty = 1;
        fbc.gridx = 1;
        fbc.gridy = 1;
        fbc.fill = GridBagConstraints.HORIZONTAL;
        fbc.anchor = GridBagConstraints.PAGE_START;
        mainPanel.add(forBox, fbc);

        DefaultComboBoxModel<String> boxesModel = new DefaultComboBoxModel<>();
        JComboBox<String> boxes = new JComboBox<>(boxesModel);
        GridBagConstraints bc = new GridBagConstraints();
        bc.weightx = 1;
        bc.weighty = 1;
        bc.gridx = 1;
        bc.gridy = 2;
        bc.fill = GridBagConstraints.CENTER;
        bc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(boxes, bc);

        JCheckBox forAuto = new JCheckBox("Владеющие автомобилем марки");
        GridBagConstraints fac = new GridBagConstraints();
        fac.weightx = 1;
        fac.weighty = 1;
        fac.gridx = 0;
        fac.gridy = 3;
        fac.fill = GridBagConstraints.HORIZONTAL;
        fac.anchor = GridBagConstraints.NORTH;
        mainPanel.add(forAuto, fac);

        JCheckBox rentTill = new JCheckBox("Аренда до");
        GridBagConstraints rtc = new GridBagConstraints();
        rtc.weightx = 1;
        rtc.weighty = 1;
        rtc.gridx = 1;
        rtc.gridy = 3;
        rtc.fill = GridBagConstraints.HORIZONTAL;
        rtc.anchor = GridBagConstraints.PAGE_START;
        mainPanel.add(rentTill, rtc);

        all.setSelected(true);
        all.addActionListener(e -> {
            if (all.isSelected()) {
                forBox.setSelected(false);
                forAuto.setSelected(false);
                rentTill.setSelected(false);
            }
        });
        forBox.addActionListener(e -> {
            if (forBox.isSelected()) {
                all.setSelected(false);
                forAuto.setSelected(false);
                rentTill.setSelected(false);
            }
        });
        forAuto.addActionListener(e -> {
            if (forAuto.isSelected()) {
                all.setSelected(false);
                forBox.setSelected(false);
            }
        });
        rentTill.addActionListener(e -> {
            if (rentTill.isSelected()) {
                all.setSelected(false);
                forBox.setSelected(false);
            }
        });

        DefaultComboBoxModel<String> modelsModel = new DefaultComboBoxModel<>();
        JComboBox<String> models = new JComboBox<>(modelsModel);
        GridBagConstraints mc = new GridBagConstraints();
        mc.weightx = 1;
        mc.weighty = 1;
        mc.gridx = 0;
        mc.gridy = 4;
        mc.fill = GridBagConstraints.CENTER;
        mc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(models, mc);

        JTextField rentDate = new JTextField();
        GridBagConstraints rdc = new GridBagConstraints();
        rdc.weightx = 1;
        rdc.weighty = 1;
        rdc.gridx = 1;
        rdc.gridy = 4;
        rdc.fill = GridBagConstraints.HORIZONTAL;
        rdc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(rentDate, rdc);

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 20));
        GridBagConstraints bpc = new GridBagConstraints();
        bpc.weightx = 1;
        bpc.weighty = 1;
        bpc.gridx = 0;
        bpc.gridy = 5;
        bpc.gridwidth = 2;
        bpc.fill = GridBagConstraints.HORIZONTAL;
        bpc.anchor = GridBagConstraints.SOUTH;
        mainPanel.add(buttonsPanel, bpc);

        JButton save = new JButton("Получить справку");
        buttonsPanel.add(save);
        JButton cancel = new JButton("Отмена");
        buttonsPanel.add(cancel);

        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        return mainPanel;
    }

    private class ModelNotesListener implements ActionListener {
        private List<Model> models;
        private String title;

        public ModelNotesListener(List<Model> models, String title) {
            this.models = models;
            this.title = title;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            List<List<String>> records = models.stream().map(m -> List.of(m.getName())).collect(Collectors.toList());
            try {
                DocsHelper.generateNote(title, records, "Справка_марки.docx");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private class AddCarListener implements ActionListener {
        private RentController rentController;
        private JTextField autoText;
        private JTextField startText;
        private JTextField finishText;

        public AddCarListener(RentController rentController, JTextField autoText, JTextField startText, JTextField finishText) {
            this.rentController = rentController;
            this.autoText = autoText;
            this.startText = startText;
            this.finishText = finishText;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String number = autoText.getText().trim();
            if (!RentController.checkNumber2(number)) {
                JOptionPane.showMessageDialog(null, "Неправильный формат номера автомобиля");
            } else {
                Car oldCar = informationController.getCarByNumber(number);
                if (oldCar != null) {
                    JOptionPane.showMessageDialog(null, "Данный автомобиль уже находится в одном из боксов");
                } else {
                    LocalDate startRentDate = rentController.getDateFromString(startText.getText().trim());
                    LocalDate endRentDate = rentController.getDateFromString(finishText.getText().trim());
                    if (startRentDate == null || endRentDate == null) {
                        JOptionPane.showMessageDialog(null, "Неправильный формат даты, должно быть дд.мм.гггг");
                    } else {
                        try {
                            rentController.getInterval(startRentDate, endRentDate);
                            Client client = rentController.getCurrentClient();
                            if (rentController.getClientsWithoutCar().contains(client)) {
                                //Вот здесь нам может понадобиться транзакция, чтобы не было клиента без автомобиля,
                                //т.к. это ограничение нашей предметной области
                                Long id = rentController.addClient(client);
                                client.setId(id);
                            }
                            long receiptNumber = rentController.addCar(client, rentController.getCurrentModel(),
                                    rentController.getCurrentBox(), number, startRentDate, endRentDate);
                            double sum = rentController.calculatePrice(rentController.getInterval(startRentDate, endRentDate));
                            DocsHelper.generateReceipt(receiptNumber, number, client, sum, rentController.getCurrentBox().getId(),
                                    startRentDate, endRentDate);
                            rentController = new RentController();
                            JOptionPane.showMessageDialog(null, "Аренда успешно оформлена");
                        } catch (DateException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage());
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "Не удалось сформировать квитанцию");
                        }
                    }
                }
            }
        }
    }

    private class DeleteCarListener implements ActionListener {
        private RentController rentController;

        public DeleteCarListener(RentController rentController) {
            this.rentController = rentController;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Car car = rentController.getCurrentCar();
            if (car != null) {
                //и здесь тоже нужна транзакция
                Client client = rentController.getCurrentClient();
                List<Car> allCars = informationController.getCarsForClient(client);
                rentController.deleteCar(car.getNumber());
                if (allCars.size() == 1) {
                    rentController.deleteClient(client);
                }
                JOptionPane.showMessageDialog(null, "Аренда закрыта");
            }
        }
    }

}
