package edu.rsatu.api.panels;

import edu.rsatu.api.MainFrame;
import edu.rsatu.garage.DocsHelper;
import edu.rsatu.garage.entities.Box;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModelsInfoPanel extends MainPanel {

    public ModelsInfoPanel(MainFrame parent) {
        super(parent);
        setLayout(new GridBagLayout());

        JLabel caption = new JLabel("Справки по маркам", SwingConstants.CENTER);
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
        add(boxes, bc);

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
        add(forBoxPanel, uc);

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
        add(buttonsPanel, bpc);

        JButton save = new JButton("Получить справку");
        buttonsPanel.add(save);
        save.addActionListener(e -> {
            if (all.isSelected() || forBox.isSelected())  {
                if (forBox.isSelected()) {
                    parent.setMainPanel(getModelsInfoResultsPanel(all.isSelected(), forBox.isSelected(), boxes.getSelectedItem().toString()));
                } else {
                    parent.setMainPanel(getModelsInfoResultsPanel(all.isSelected(), forBox.isSelected(), "-"));
                }
            }
        });

        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
    }

    //панель информации по маркам
    private JPanel getModelsInfoResultsPanel(boolean all, boolean forBox, String boxID) {
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
        cancel.addActionListener(e -> parent.setMainPanel(this));

        JButton getDoc = new JButton("Получить спраку в формате docx");
        buttonsPanel.add(getDoc);
        getDoc.addActionListener(new ModelNotesListener(modelsX, text));

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
}
