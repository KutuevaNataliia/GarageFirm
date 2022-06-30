package edu.rsatu.api.panels;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;

public class DefaultPanel extends JPanel {

    public DefaultPanel() {
        setLayout(new FlowLayout());

        JLabel hello = new JLabel("<html>Вы можете добавить или удалить из списка обслуживаемых бокс или марку,<br/>" +
                "а также оформить или закрыть аренду, или получить справку.<br/>" +
                "Все функции доступны в меню.</html>");
        add(hello);
    }
}
