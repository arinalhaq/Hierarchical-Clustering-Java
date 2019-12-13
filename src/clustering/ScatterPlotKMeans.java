/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clustering;

/**
 *
 * @author Windows
 */
import java.awt.Color;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * @author imssbora
 */
public class ScatterPlotKMeans extends JFrame {

    private static final long serialVersionUID = 6294689542092367723L;

    public ScatterPlotKMeans(String title, int k, int pilihan) throws IOException {
        super(title);

        // Create dataset
        XYDataset dataset = createDataset(k, pilihan);

        // Create chart
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Ruspini",
                "X-Axis", "Y-Axis", dataset);

        //Changes background color
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(0, 180, 180));

        // Create Panel
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    private XYDataset createDataset(int k, int pilihan) throws IOException {
        XYSeriesCollection dataset = new XYSeriesCollection();
        KMeans h = new KMeans();
        double[][] data = h.readFile("C:\\Users\\Windows\\Downloads\\Documents\\pak ali\\ruspini.csv");

        h.kMeans(data, k);

        return dataset;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            try {
                Scanner sc = new Scanner(System.in);
                System.out.print("Masukkan k : ");
                int k = sc.nextInt();
                System.out.println("1. Single Linkage");
                System.out.println("2. Complete Linkage");
                System.out.println("3. Average Linkage");
                System.out.print("Masukkan pilihan : ");
                ScatterPlotKMeans example = new ScatterPlotKMeans("Scatter Plot", k, sc.nextInt());
                example.setSize(800, 400);
                example.setLocationRelativeTo(null);
                example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                example.setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(ScatterPlotKMeans.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
    }
}
