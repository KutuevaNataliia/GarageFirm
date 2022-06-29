package edu.rsatu.api;

import edu.rsatu.api.panels.info.BoxesInfoPanel;
import edu.rsatu.api.panels.administration.ChangePricePanel;
import edu.rsatu.api.panels.info.ClientsInfoPanel;
import edu.rsatu.api.panels.administration.CloseBoxPanel;
import edu.rsatu.api.panels.administration.DeleteModelPanel;
import edu.rsatu.api.panels.info.ModelsInfoPanel;
import edu.rsatu.api.panels.administration.NewBoxPanel;
import edu.rsatu.api.panels.administration.NewModelPanel;
import edu.rsatu.api.panels.info.StatisticPanel;
import edu.rsatu.api.panels.rent.RentClosePanel;
import edu.rsatu.api.panels.rent.RentCommonPanel;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;


public class MainFrame extends JFrame {

    private JPanel mainPanel;

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
        newBox.addActionListener(e -> setMainPanel(new NewBoxPanel(this)));
        JMenuItem closeBox = new JMenuItem("Закрыть бокс");
        administration.add(closeBox);
        closeBox.addActionListener(e -> setMainPanel(new CloseBoxPanel(this)));
        JMenuItem changePrice = new JMenuItem("Изменить стоимость аренды");
        administration.add(changePrice);
        changePrice.addActionListener(e -> setMainPanel(new ChangePricePanel(this)));
        administration.add(new JSeparator());
        JMenuItem newBrand = new JMenuItem("Новая марка");
        administration.add(newBrand);
        newBrand.addActionListener(e -> setMainPanel(new NewModelPanel(this)));
        JMenuItem deleteModel = new JMenuItem("Прекратить обслуживание марки");
        administration.add(deleteModel);
        deleteModel.addActionListener(e -> setMainPanel(new DeleteModelPanel(this)));
        JMenu services = new JMenu("Обслуживание клиентов");
        menuBar.add(services);
        JMenuItem startRenting = new JMenuItem("Оформить аренду");
        services.add(startRenting);
        startRenting.addActionListener(e -> setMainPanel(new RentCommonPanel(this)));
        JMenuItem finishRenting = new JMenuItem("Закрыть аренду");
        services.add(finishRenting);
        finishRenting.addActionListener(e -> setMainPanel(new RentClosePanel(this)));

        JMenu selection = new JMenu("Справки");
        menuBar.add(selection);
        JMenuItem clients = new JMenuItem("Клиенты");
        selection.add(clients);
        clients.addActionListener(e -> setMainPanel(new ClientsInfoPanel(this)));
        JMenuItem boxes = new JMenuItem("Боксы");
        selection.add(boxes);
        boxes.addActionListener(e -> setMainPanel(new BoxesInfoPanel(this)));
        JMenuItem models = new JMenuItem("Марки");
        selection.add(models);
        models.addActionListener(e -> setMainPanel(new ModelsInfoPanel(this)));
        JMenuItem statistics = new JMenuItem("Статистика");
        selection.add(statistics);
        statistics.addActionListener(e -> setMainPanel(new StatisticPanel(this)));

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

    public void setMainPanel(JPanel newPanel) {
        mainPanel.removeAll();
        mainPanel.add(newPanel);
        getContentPane().validate();
        getContentPane().repaint();
    }
}
