package edu.rsatu.api.panels.administration;

import edu.rsatu.api.MainFrame;
import edu.rsatu.api.panels.MainPanel;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

public class ChangePricePanel extends MainPanel {

    public ChangePricePanel(MainFrame parent) {
        super(parent);

        setLayout(new GridBagLayout());

        JLabel caption = new JLabel("Изменение стоимости аренды", SwingConstants.CENTER);
        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 1;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.anchor = GridBagConstraints.NORTH;
        add(caption, cc);

        JPanel optionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        GridBagConstraints opc = new GridBagConstraints();
        opc.weightx = 1;
        opc.weighty = 1;
        opc.gridx = 0;
        opc.gridy = 1;
        opc.fill = GridBagConstraints.CENTER;
        cc.anchor = GridBagConstraints.NORTH;
        add(optionPanel, opc);

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
        add(ratioPanel, rpc);

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
        add(buttonsPanel, bpc);

        JButton save = new JButton("Подтвердить изменение стоимости");
        buttonsPanel.add(save);
        save.addActionListener(e -> {

            String str = text.getText();
            if (!str.equals("")) {
                Double mult = null;
                try {
                    mult = Double.parseDouble(str.trim());
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(parent,
                            "<html><i>Число должно быть вещественным</i>");
                }
                if (!mult.equals(null)) {
                    if (increase.isSelected()) {
                        administrationController.increaseBoxPrices(mult);

                        JOptionPane.showMessageDialog(parent,
                                "<html><i>Цена аренды всех боксов была увеличена в " + str + " раз</i>");
                    } else if (decrease.isSelected()) {
                        administrationController.decreaseBoxPrices(mult);

                        JOptionPane.showMessageDialog(parent,
                                "<html><i>Цена аренды всех боксов была уменьшена в " + str + " раз</i>");
                    }

                }
            } else {
                JOptionPane.showMessageDialog(parent,
                        "<html><i>Введите значение!</i>");
            }


        });
        /*
        JButton cancel = new JButton("Отмена");
        buttonsPanel.add(cancel);
        */
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
    }
}
