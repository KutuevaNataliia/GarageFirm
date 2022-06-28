package edu.rsatu.api.panels.rent;

import edu.rsatu.api.MainFrame;
import edu.rsatu.api.panels.MainPanel;
import edu.rsatu.garage.controller.RentController;
import edu.rsatu.garage.entities.Box;
import edu.rsatu.garage.entities.Car;
import edu.rsatu.garage.entities.Client;
import edu.rsatu.garage.entities.Model;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class RentClosePanel extends MainPanel {

    private RentController rentController = new RentController();

    public RentClosePanel(MainFrame parent) {
        super(parent);
        setLayout(new GridBagLayout());

        JLabel caption = new JLabel("Закрытие аренды", SwingConstants.CENTER);
        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 1;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.anchor = GridBagConstraints.PAGE_START;
        add(caption, cc);

        JPanel autoPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        GridBagConstraints apc = new GridBagConstraints();
        apc.weightx = 1;
        apc.weighty = 1;
        apc.gridx = 0;
        apc.gridy = 1;
        apc.fill = GridBagConstraints.HORIZONTAL;
        apc.anchor = GridBagConstraints.NORTH;
        add(autoPanel, apc);

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
        add(findAuto, fac);

        JLabel info = new JLabel("", SwingConstants.LEFT);
        GridBagConstraints ic = new GridBagConstraints();
        ic.weightx = 1;
        ic.weighty = 5;
        ic.gridx = 0;
        ic.gridy = 3;
        ic.fill = GridBagConstraints.HORIZONTAL;
        ic.anchor = GridBagConstraints.NORTH;
        add(info, ic);

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
        add(buttonsPanel, bpc);

        JButton save = new JButton("Закрыть аренду");
        buttonsPanel.add(save);
        JButton cancel = new JButton("Отмена");
        buttonsPanel.add(cancel);

        save.addActionListener(new DeleteCarListener(rentController));

        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
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
