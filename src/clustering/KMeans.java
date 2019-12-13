/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clustering;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Windows
 */
public class KMeans {
    public double[][] readFile(String name) throws FileNotFoundException, IOException {
        List<double[]> dataset = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(name))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                double[] data = new double[values.length];
                for (int i = 0; i < values.length; i++) {
                    data[i] = Double.parseDouble(values[i]);
                }
                dataset.add(data);
            }
        }

        return dataset.toArray(new double[dataset.size()][]);
    }
    
    public double[] readLabel(double[][] dataset) {
        double[] label = new double[dataset.length];
        for(int i=0; i<label.length; i++){
            label[i] = dataset[i][dataset[i].length-1];
        }

        return label;
    }
    
    public double[][] readData(double[][] dataset) {
        double[][] data = new double[dataset.length][dataset[0].length-1];
        for(int i=0; i<data.length; i++){
            for(int j=0; j<data[i].length; j++){
                data[i][j] = dataset[i][j];
            }
        }

        return data;
    }
    
    public double[] getMax(double[][] data) {
        double[] max = new double[data[0].length];
        for(int i=0; i<max.length; i++){
            for(int j=0; j<data.length; j++){
                if((i==0&&j==0)||data[j][i]>max[i]){
                    max[i]=data[j][i];
                }
            }
        }
        
        return max;
    }
    
    public double[][] getCentroid(int k, double[] max){
        double[][] centroid = new double[k][max.length];
        for(int i=0; i<k; i++){
            for(int j=0; j<max.length; j++)
                centroid[i][j] = (Math.random() * max[j]);
        }
        
        return centroid;
    }
    
    public double getDistance(double[] centroid, double[] data){
        double sum = 0;
        for(int i=0; i<centroid.length; i++){
            sum = sum + (Math.pow((centroid[i]-data[i]), 2));
        }
        return Math.sqrt(sum);
    }
    
    public double[] getDataLabel(double[][] centroid, double[][] data){
        double[] label = new double[data.length];
        double[] distance = new double[data.length];
        for(int i=0; i<data.length; i++){
            for(int j=0; j<centroid.length; j++){
                if(j==0 | getDistance(centroid[j], data[i])<distance[i]){
                    label[i] = j;
                    distance[i] = getDistance(centroid[j], data[i]);
                }
            }
        }

        return label;
    }
   
    public int[] getJumlahLabel(double[] label, double[][] centroid){
        int[] jumlahLabel = new int[centroid.length];
        
        for(int i=0; i<centroid.length; i++){
            jumlahLabel[i] = 0;
            for(int j=0; j<label.length; j++){
                if(label[j]==i){
                    jumlahLabel[i]++;
                }
            }
        }
        
        return jumlahLabel;
    }
    
    public double[][] clusterData(double[][] data, double[] label){
        double[][] clusteredData = new double[data.length][3];
        for(int i=0; i<data.length; i++){
            clusteredData[i][0] = data[i][0];
            clusteredData[i][1] = data[i][1];
            clusteredData[i][2] = label[i];
        }
        System.out.println(Arrays.deepToString(clusteredData));
        return clusteredData;
    }
    
    public double[][] getNewCentroid(double[][] clusteredData, int[] jumlahLabel, double[][] centroid){
        double[][] sum = new double[centroid.length][2];
        for(int i=0; i<centroid.length; i++){
            sum[i][0] = 0;
            sum[i][1] = 0;
            for(int j=0; j<clusteredData.length-1; j++){
                if(clusteredData[j][2]==i){
                    sum[i][0] += clusteredData[j][0];
                    sum[i][1] += clusteredData[j][1];
                }
            }
            sum[i][0] /= jumlahLabel[i];
            sum[i][1] /= jumlahLabel[i];
        }
        
        return sum;
    }
    
    public Double[] getDataLabel(double[][] dataset){
        List<Double> label = new ArrayList<>();
        
        for (double[] d : dataset) {
            if (!label.contains(d[d.length - 1])) {
                label.add(d[d.length - 1]);
            }
        }
        
        return label.toArray(new Double[label.size()]);
    }
    
    public int[] getJumlahLabel(double[][] dataset){
        Double[] label = getDataLabel(dataset);
        int[] jumlahLabel = new int[label.length];

        for (int i=0; i<label.length; i++) {
            for (double[] d : dataset) {
                if (d[d.length - 1] == label[i]) {
                    jumlahLabel[i]++;
                }
            }
        }
        
        return jumlahLabel;
    }
    
    public void kMeans(double[][] data, int k){
        double[][] centroid = getCentroid(k, getMax(readData(data)));
        double[][] newCentroid;
        boolean stop = false;
        for(;;){
            double[] label = getDataLabel(centroid, readData(data));
            int[] jumlahLabel = getJumlahLabel(label, centroid);
            double[][] clusteredData = clusterData(readData(data), label);
            newCentroid = getNewCentroid(clusteredData, jumlahLabel, centroid);
             System.out.println(Arrays.toString(getJumlahLabel(data)));
            System.out.println(Arrays.toString(getJumlahLabel(clusteredData)));
            System.out.println(Arrays.deepToString(centroid));
            System.out.println(Arrays.deepToString(newCentroid));
            System.out.println(getErrorRatio(clusteredData, readLabel(data)));
            for(int i=0; i<centroid.length; i++){
                for(int j=0; j<centroid[i].length; j++){
                    if(centroid[i][j]==newCentroid[i][j] && i == centroid.length-1){
                        stop = true;
                        break;
                    }
                }
            }
            if(stop){
                break;
            }
            centroid = newCentroid;
            
        }
        
    }
    
    public double getErrorRatio(double[][] clusteredData, double[] labelAwal){
        double same1=0, same2=0, error = 0;
        for(int i=0; i<clusteredData.length; i++){
            if(i==0 || clusteredData[i][clusteredData[i].length-1] != clusteredData[i-1][clusteredData[i-1].length-1]){
                same1++;
            }
            if(i==0||labelAwal[i] != labelAwal[i-1]){
                same2++;
            }
            if(same1!=same2){
                error++;
                same1 = 0;
                same2 = 0;
            }
        }
        return error/clusteredData.length * 100;
    }
}
