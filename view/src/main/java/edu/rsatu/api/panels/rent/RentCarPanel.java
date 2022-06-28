package edu.rsatu.api.panels.rent;

import edu.rsatu.api.MainFrame;
import edu.rsatu.garage.DocsHelper;
import edu.rsatu.garage.controller.RentController;
import edu.rsatu.garage.entities.Car;
import edu.rsatu.garage.entities.Client;
import edu.rsatu.garage.exceptions.DateException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;

public class RentCarPanel extends RentIssuesPanel {

    public RentCarPanel(MainFrame parent, RentCommonPanel rentCommonPanel, RentController rentController) {
        super(parent, rentCommonPanel, rentController);
        setLayout(new GridBagLayout());
        String previousInfo = "Клиент:<br/>" + "Фамилия: " + rentController.getCurrentClient().getSurname() + "<br/> Адрес: "
                + rentController.getCurrentClient().getAddress() + "<br/>Марка автомобиля: " + rentController.getCurrentModel().getName();
        String defaultInfoText = previousInfo + "<br/>Бокс: " + rentController.getCurrentBox().getId() + " стоимость: " + rentController.getCurrentBox().getRentPrice();
        JLabel caption = new JLabel("<html>" + defaultInfoText + "</html>");
        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 5;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.anchor = GridBagConstraints.NORTH;
        add(caption, cc);

        JPanel autoPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        GridBagConstraints apc = new GridBagConstraints();
        apc.weightx = 1;
        apc.weighty = 2;
        apc.gridx = 0;
        apc.gridy = 1;
        apc.fill = GridBagConstraints.HORIZONTAL;
        apc.anchor = GridBagConstraints.NORTH;
        add(autoPanel, apc);

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
        add(datesPanel, dpc);

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
        add(sum, sc);

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
        add(back, bc);
        back.addActionListener(e -> rentCommonPanel.setRentMutablePanel(rentCommonPanel.rentIssuePanels.removeLast()));

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        GridBagConstraints bpc = new GridBagConstraints();
        bpc.weightx = 1;
        bpc.weighty = 1;
        bpc.gridx = 0;
        bpc.gridy = 5;
        bpc.fill = GridBagConstraints.HORIZONTAL;
        bpc.anchor = GridBagConstraints.SOUTH;
        add(buttonsPanel, bpc);

        JButton save = new JButton("Оформить");
        buttonsPanel.add(save);
        JButton cancel = new JButton("Отмена");
        buttonsPanel.add(cancel);

        save.addActionListener(new AddCarListener(rentController, autoText, startText, finishText));
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
}
