package edu.rsatu.api.panels.info;

import edu.rsatu.api.MainFrame;
import edu.rsatu.api.panels.MainPanel;
import edu.rsatu.garage.controller.RentController;
import edu.rsatu.garage.entities.Box;
import edu.rsatu.garage.entities.Client;
import edu.rsatu.garage.entities.Model;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

public class ClientsInfoPanel extends MainPanel {

    private RentController rentController = new RentController();

    public ClientsInfoPanel(MainFrame parent) {
        super(parent);
        setLayout(new GridBagLayout());

        JLabel caption = new JLabel("Справки по клиентам", SwingConstants.CENTER);
        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 1;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.gridwidth = 2;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.anchor = GridBagConstraints.PAGE_START;
        add(caption, cc);

        JCheckBox all = new JCheckBox("Все клиенты");
        GridBagConstraints alc = new GridBagConstraints();
        alc.weightx = 1;
        alc.weighty = 1;
        alc.gridx = 0;
        alc.gridy = 1;
        alc.anchor = GridBagConstraints.NORTHWEST;
        add(all, alc);

        JCheckBox forBox = new JCheckBox("Клиент занимающий бокс:");
        GridBagConstraints fbc = new GridBagConstraints();
        fbc.weightx = 1;
        fbc.weighty = 1;
        fbc.gridx = 1;
        fbc.gridy = 1;
        fbc.fill = GridBagConstraints.HORIZONTAL;
        fbc.anchor = GridBagConstraints.PAGE_START;
        add(forBox, fbc);

        //список боксов
        DefaultComboBoxModel<String> boxesModel = new DefaultComboBoxModel<>();
        JComboBox<String> boxes = new JComboBox<>(boxesModel);
        GridBagConstraints bc = new GridBagConstraints();
        bc.weightx = 1;
        bc.weighty = 1;
        bc.gridx = 1;
        bc.gridy = 2;
        bc.fill = GridBagConstraints.CENTER;
        bc.anchor = GridBagConstraints.NORTHWEST;
        add(boxes, bc);

        List<Box> boxesG = informationController.getAllBoxes();
        for (Box box : boxesG) {
            boxes.addItem(box.getId().toString());
        }

        JCheckBox forAuto = new JCheckBox("Клиенты, владеющие автомобилем марки:");
        GridBagConstraints fac = new GridBagConstraints();
        fac.weightx = 1;
        fac.weighty = 1;
        fac.gridx = 0;
        fac.gridy = 3;
        fac.fill = GridBagConstraints.HORIZONTAL;
        fac.anchor = GridBagConstraints.NORTH;
        add(forAuto, fac);

        JCheckBox rentTill = new JCheckBox("Клиенты, аренда которых кончается:");
        GridBagConstraints rtc = new GridBagConstraints();
        rtc.weightx = 1;
        rtc.weighty = 1;
        rtc.gridx = 1;
        rtc.gridy = 3;
        rtc.fill = GridBagConstraints.HORIZONTAL;
        rtc.anchor = GridBagConstraints.PAGE_START;
        add(rentTill, rtc);


        //список моделей
        DefaultComboBoxModel<String> modelsModel = new DefaultComboBoxModel<>();
        JComboBox<String> models = new JComboBox<>(modelsModel);
        GridBagConstraints mc = new GridBagConstraints();
        mc.weightx = 1;
        mc.weighty = 1;
        mc.gridx = 0;
        mc.gridy = 4;
        mc.fill = GridBagConstraints.CENTER;
        mc.anchor = GridBagConstraints.NORTHWEST;
        add(models, mc);

        List<Model> modelsG = informationController.getAllModels();
        for (Model model : modelsG) {
            models.addItem(model.getName());
        }

        //строка для даты
        JTextField rentDate = new JTextField();
        GridBagConstraints rdc = new GridBagConstraints();
        rdc.weightx = 1;
        rdc.weighty = 1;
        rdc.gridx = 1;
        rdc.gridy = 4;
        rdc.fill = GridBagConstraints.HORIZONTAL;
        rdc.anchor = GridBagConstraints.NORTH;
        add(rentDate, rdc);

        boxes.setEnabled(false);
        models.setEnabled(false);
        rentDate.setEnabled(false);

        all.setSelected(true);
        all.addActionListener(e -> {
            if (all.isSelected()) {
                forBox.setSelected(false);
                forAuto.setSelected(false);
                rentTill.setSelected(false);
                //список боксов выключается
                //список марок выключается
                //строка для даты выключается
                boxes.setEnabled(false);
                models.setEnabled(false);
                rentDate.setEnabled(false);
            }
        });
        forBox.addActionListener(e -> {
            if (forBox.isSelected()) {
                all.setSelected(false);
                forAuto.setSelected(false);
                rentTill.setSelected(false);
                //список боксов включается
                //список марок выключается
                //строка для даты выключается
                boxes.setEnabled(true);
                models.setEnabled(false);
                rentDate.setEnabled(false);

            }
            else{
                //список боксов выключается
                boxes.setEnabled(false);
            }
        });
        forAuto.addActionListener(e -> {
            if (forAuto.isSelected()) {
                all.setSelected(false);
                forBox.setSelected(false);
                //список марок включается
                //список боксов выключается
                models.setEnabled(true);
                boxes.setEnabled(false);
            }else{
                //список марок выключается
                models.setEnabled(false);
            }
        });
        rentTill.addActionListener(e -> {
            if (rentTill.isSelected()) {
                all.setSelected(false);
                forBox.setSelected(false);
                //строка для даты включается
                //список боксов выключается
                rentDate.setEnabled(true);
                boxes.setEnabled(false);
            }else{
                //строка для даты выключается
                rentDate.setEnabled(false);
            }
        });

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 20));
        GridBagConstraints bpc = new GridBagConstraints();
        bpc.weightx = 1;
        bpc.weighty = 1;
        bpc.gridx = 0;
        bpc.gridy = 5;
        bpc.gridwidth = 2;
        bpc.fill = GridBagConstraints.HORIZONTAL;
        bpc.anchor = GridBagConstraints.SOUTH;
        add(buttonsPanel, bpc);

        JButton save = new JButton("Получить справку");
        buttonsPanel.add(save);

        save.addActionListener(e ->{
            if(all.isSelected()){
                //Все клиенты
                parent.setMainPanel(getClientsInfoResults(1,"-","-","-" ));
            }else if(forBox.isSelected()){
                //Клиент занимающий бокс
                parent.setMainPanel(getClientsInfoResults(2,boxes.getSelectedItem().toString(),"-","-" ));
            }else if(forAuto.isSelected()){

                if(rentTill.isSelected()){
                    if(rentController.checkDate(rentDate.getText().trim())){
                        //Клиенты владеющие автомобилем марки, аренда которых кончается <date>
                        parent.setMainPanel(getClientsInfoResults(5,"-",models.getSelectedItem().toString(),rentDate.getText()));
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Некорректно введена дата");
                    }
                }else{
                    //Клиенты владеющие автомобилем марки
                    parent.setMainPanel(getClientsInfoResults(3,"-",models.getSelectedItem().toString(),"-" ));
                }
            }else if(rentTill.isSelected()){

                if(rentController.checkDate(rentDate.getText().trim())){
                    //Клиенты аренда которых кончается <date>
                    parent.setMainPanel(getClientsInfoResults(4,"-","-",rentDate.getText() ));
                }
                else{
                    JOptionPane.showMessageDialog(null, "Некорректно введена дата");
                }
            }

        });
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
    }

    //панель информации по клиентам
    private JPanel getClientsInfoResults(int r, String boxID, String modelName, String rentDate) {

        JPanel mainPanel = new JPanel(new GridBagLayout());

        String text = "";
        String text1 = "Все клиенты";
        String text2 = "Клиент занимающий бокс №" + boxID;
        String text3 = "Клиенты, владеющие автомобилем марки " + modelName;
        String text4 = "Клиенты, аренда которых кончается " + rentDate;
        String text5 = "Клиенты, владеющие автомобилем марки " + modelName + ", аренда которых кончается " + rentDate;


        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 2;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.anchor = GridBagConstraints.PAGE_START;


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
        JList<String> clients = new JList<>(listModel);
        GridBagConstraints bc = new GridBagConstraints();
        bc.weightx = 0.6;
        bc.weighty = 5;
        bc.gridx = 0;
        bc.gridy = 2;
        bc.fill = GridBagConstraints.CENTER;
        bc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(clients, bc);

        List<Client> clientsX = new ArrayList<>();


        if (r == 1) {
            chooseBox.setText(text1);
            clientsX = informationController.getAllClients();
        } else if (r == 2) {
            chooseBox.setText(text2);
            Integer id = -1;
            try {
                id = Integer.parseInt(boxID.trim());
            } catch (NumberFormatException nfe) {
            }
            Box box = informationController.getBoxByNumber(id);
            Client client = informationController.getClientForBox(box);
            if (client != null) clientsX = List.of(client);

        } else if (r == 3) {
            chooseBox.setText(text3);
            Model model = informationController.getModelByName(modelName);
            if (model != null)
                clientsX = informationController.getClientsForModel(model);
        } else if (r == 4) {
            chooseBox.setText(text4);
            clientsX = informationController.getClientsForDate(rentController.getDateFromString(rentDate));
        } else if (r == 5) {
            chooseBox.setText(text5);
            Model model = informationController.getModelByName(modelName);
            if (model != null)
                clientsX = informationController.getClientsForModelDate(model,
                        rentController.getDateFromString(rentDate));
        }

        if (clientsX.size() == 0) {
            listModel.add(listModel.getSize(), "-");
        }


        for (Client client : clientsX) {

            listModel.add(listModel.getSize(), client.getSurname() + "     адрес: " + client.getAddress());
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

        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        return mainPanel;
    }
}
