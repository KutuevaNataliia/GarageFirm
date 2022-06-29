package edu.rsatu.api.panels.info;

import edu.rsatu.api.MainFrame;
import edu.rsatu.api.panels.MainPanel;
import edu.rsatu.garage.DocsHelper;
import edu.rsatu.garage.db.Pair;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class StatisticPanel extends MainPanel {

    public StatisticPanel(MainFrame parent) {
        super(parent);
        setLayout(new GridBagLayout());

        JLabel caption = new JLabel("Статистика по срокам аренды", SwingConstants.CENTER);
        GridBagConstraints cc = new GridBagConstraints();
        cc.weightx = 1;
        cc.weighty = 1;
        cc.gridx = 0;
        cc.gridy = 0;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.anchor = GridBagConstraints.PAGE_START;
        add(caption, cc);

        double averageInterval = informationController.getAverageRentInterval();
        JLabel avInterval = new JLabel("Средняя продолжительность аренды: " + averageInterval + " дней", SwingConstants.CENTER);
        GridBagConstraints aic = new GridBagConstraints();
        aic.weightx = 1;
        aic.weighty = 1;
        aic.gridx = 0;
        aic.gridy = 1;
        aic.fill = GridBagConstraints.HORIZONTAL;
        aic.anchor = GridBagConstraints.PAGE_START;
        add(avInterval, aic);

        List<Pair<Integer>> pairs = informationController.getIntervalsStatistic();
        Integer[] intervalsBoxed = pairs.stream().map(Pair::getFirst).toArray(Integer[]::new);
        double[] intervals = Arrays.stream(intervalsBoxed).mapToDouble(i -> (double)i).toArray();
        Integer[] countsBoxed = pairs.stream().map(Pair::getSecond).toArray(Integer[]::new);
        int[] counts = Arrays.stream(countsBoxed).mapToInt(Integer::intValue).toArray();

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < intervals.length; i++) {
            dataset.addValue(counts[i], "key", "" + intervals[i]);
        }
        JFreeChart barChart = ChartFactory.createBarChart("Статистика по интервалам",
                "Количество дней", "Количество аренд", dataset, PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(barChart);
        GridBagConstraints cpc = new GridBagConstraints();
        cpc.weightx = 1;
        cpc.weighty = 20;
        cpc.gridx = 0;
        cpc.gridy = 2;
        cpc.fill = GridBagConstraints.BOTH;
        cpc.anchor = GridBagConstraints.NORTH;
        add(chartPanel, cpc);

        JButton getDoc = new JButton("Получить справку в формате docx");
        GridBagConstraints dc = new GridBagConstraints();
        dc.weightx = 1;
        dc.weighty = 1;
        dc.gridx = 0;
        dc.gridy = 3;
        dc.fill = GridBagConstraints.HORIZONTAL;
        dc.anchor = GridBagConstraints.SOUTH;
        add(getDoc, dc);

        getDoc.addActionListener(e -> {
            String chartFileName = "chart_" + LocalDateTime.now().format(DocsHelper.DATE_TIME_FORMATTER) + ".png";
            String filename = "статистика_" + LocalDateTime.now().format(DocsHelper.DATE_TIME_FORMATTER) + ".docx";
            try {
                ChartUtils.saveChartAsPNG(new File(chartFileName), barChart, 450, 400);
                DocsHelper.generateStatisticsNote(filename, chartFileName, averageInterval);
            } catch (IOException | InvalidFormatException ex) {
                ex.printStackTrace();
            }
        });

        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
    }
}
