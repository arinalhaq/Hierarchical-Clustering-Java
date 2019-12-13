/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clustering;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChartBuilder;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Windows
 */
public class Clustering {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
//        KMeans k = new KMeans();
        Hierarchical h = new Hierarchical();
        int[][] dataset = h.readFile("C:\\Users\\Windows\\Downloads\\Documents\\pak ali\\ruspini.csv");
//        double[][] data = k.readFile("C:\\Users\\Windows\\Downloads\\Documents\\pak ali\\ruspini.csv");
//        k.kMeans(data, 4);
        
        System.out.println(Arrays.deepToString(dataset));
        Scanner sc = new Scanner(System.in);
        System.out.print("Masukkan k : ");
        int k = sc.nextInt();
        System.out.println("1. Single Linkage");
        System.out.println("2. Complete Linkage");
        System.out.println("3. Average Linkage");
        System.out.print("Masukkan pilihan : ");
        int[][] data = h.hierarchicalCluster(dataset, k, sc.nextInt());
        Integer[] cluster = h.getCluster(data);
        for(int i=0; i<cluster.length; i++){
            System.out.println("\ncluster " + cluster[i] + " : ");
            System.out.println(Arrays.deepToString(h.getDataCluster(data, cluster[i])));
            System.out.println(h.getDataCluster(data, cluster[i]).length+ " data");
        }
        System.out.print("\nError Ratio : ");
        System.out.println(h.getErrorRatio(data, dataset));
        
        //System.out.println(Arrays.deepToString(data));
    }
    
}
