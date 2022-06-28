package edu.rsatu.api.panels;

import edu.rsatu.api.MainFrame;
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
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

public class BoxesInfoPanel extends MainPanel {

    public BoxesInfoPanel(MainFrame parent) {
        super(parent);
        setLayout(new GridBagLayout());

        JLabel caption = new JLabel("Справки по боксам", SwingConstants.CENTER);
        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 1;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.anchor = GridBagConstraints.PAGE_START;
        add(caption, cc);

        JPanel optionPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        GridBagConstraints opc = new GridBagConstraints();
        opc.weightx = 1;
        opc.weighty = 1;
        opc.gridx = 0;
        opc.gridy = 1;
        opc.fill = GridBagConstraints.HORIZONTAL;
        opc.anchor = GridBagConstraints.NORTH;
        add(optionPanel, opc);

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
        add(forModelPanel, uc);

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
        add(buttonsPanel, bpc);

        JButton save = new JButton("Получить справку");
        buttonsPanel.add(save);
        //
        save.addActionListener(e -> {
            if (all.isSelected() || free.isSelected() || forModel.isSelected()) {
                if (forModel.isSelected()) {
                    parent.setMainPanel(getBoxesInfoResultsPanel(all.isSelected(), free.isSelected(), forModel.isSelected(), models.getSelectedItem().toString()));
                } else {
                    parent.setMainPanel(getBoxesInfoResultsPanel(all.isSelected(), free.isSelected(), forModel.isSelected(), ""));
                }
            }
        });
        /*
        JButton cancel = new JButton("Отмена");
        buttonsPanel.add(cancel);
        */
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
    }

    //панель информации по боксам
    private JPanel getBoxesInfoResultsPanel(boolean all, boolean free, boolean forModel, String modelName) {
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
        cancel.addActionListener(e -> parent.setMainPanel(this));

        JButton getDoc = new JButton("Получить спраку в формате docx");
        buttonsPanel.add(getDoc);

        return mainPanel;
    }
}
