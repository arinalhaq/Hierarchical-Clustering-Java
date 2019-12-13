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
import java.util.List;

/**
 *
 * @author Windows
 */
public class Hierarchical {
    public int[][] readFile(String name) throws FileNotFoundException, IOException {
        List<int[]> dataset = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(name))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int[] data = new int[values.length];
                for (int i = 0; i < values.length; i++) {
                    data[i] = Integer.parseInt(values[i]);
                }
                dataset.add(data);
            }
        }

        return dataset.toArray(new int[dataset.size()][]);
    }
    
    public int[][] initCluster(int[][] data){
        int[][] clusteredData = new int[data.length][data[0].length];
        for(int i=0; i<data.length; i++){
            for(int j=0; j<data[i].length-1; j++){
                clusteredData[i][j] = data[i][j];
            }
            clusteredData[i][data[i].length-1] = i;
        }
        
        return clusteredData;
    }
    
    public double getDistance(int[] data1, int[] data2){
        double sum = 0;
        for(int i=0; i<data1.length-1; i++){
            sum = sum + (Math.pow((data1[i]-data2[i]), 2));
        }
        return Math.sqrt(sum);
    }
    
    public double singleLinkage(int[][] cluster1, int[][] cluster2){
        double min = 0;
        for(int i=0; i<cluster1.length; i++){
            for(int j=0; j<cluster2.length; j++){
                double distance = getDistance(cluster1[i], cluster2[j]);
                if(min == 0 || distance<min){
                    min = distance;
                }
            }
        }
        return min;
    }
    
    public double completeLinkage(int[][] cluster1, int[][] cluster2){
        double max = 0;
        for(int i=0; i<cluster1.length; i++){
            for(int j=0; j<cluster2.length; j++){
                double distance = getDistance(cluster1[i], cluster2[j]);
                if(max == 0 || distance>max){
                    max = distance;
                }
            }
        }
        return max;
    }
    
//    public double centroidLinkage(int[][] cluster1, int[][] cluster2){
//        double max = 0;
//        for(int i=0; i<cluster1.length; i++){
//            for(int j=0; j<cluster2.length; j++){
//                double distance = getDistance(cluster1[i], cluster2[j]);
//                
//            }
//        }
//        return max;
//    }
    
    public double averageLinkage(int[][] cluster1, int[][] cluster2){
        double sum = 0;
        for(int i=0; i<cluster1.length; i++){
            for(int j=0; j<cluster2.length; j++){
                sum+=getDistance(cluster1[i], cluster2[j]);
            }
        }
        return (sum/(cluster1.length*cluster2.length));
    }
    
    public double getClusterDistance(int pilihan, int[][] cluster1, int [][] cluster2){
        switch(pilihan){
            case 1 : return singleLinkage(cluster1, cluster2);
            case 2 : return completeLinkage(cluster1, cluster2);
            case 3 : return averageLinkage(cluster1, cluster2);
        }
        return 0;
    }
    
    public Integer[] getCluster(int[][] dataset){
        List<Integer> label = new ArrayList<>();
        
        for (int[] d : dataset) {
            if (!label.contains(d[d.length - 1])) {
                label.add(d[d.length - 1]);
            }
        }
        
        return label.toArray(new Integer[label.size()]);
    }
    
    public int[][] getDataCluster(int[][] data, int cluster){
        List<int[]> clusteredData = new ArrayList<>();
        for(int i=0; i<data.length; i++){
            if(data[i][2] == cluster){
                clusteredData.add(data[i]);
            }
        }
        return clusteredData.toArray(new int[clusteredData.size()][]);
    }
    
    
    public int[][] mergeCluster(int[][] data, int pilihan){
        Integer[] cluster = getCluster(data);
        double min = 0;
        int[] point = new int[data[0].length-1];
        for(int i=0; i<cluster.length; i++){
            for(int j=cluster.length-1; j>i; j--){
                double distance = getClusterDistance(pilihan, getDataCluster(data,cluster[i]), getDataCluster(data,cluster[j]));
                if(min ==0 || distance<min){
                    min = distance;
                    point[0]= cluster[i];
                    point[1] = cluster[j];
                }
            }
        }
        changeDataCluster(data, point);
        return data;
    }

    private void changeDataCluster(int[][] data, int[] point) {
        for(int i=0; i<data.length; i++){
            if(data[i][data[i].length-1] == point[1]){
               data[i][data[i].length-1] = point[0];
            }
        }
    }
    
    public int[][] hierarchicalCluster(int[][] data, int k, int pilihan){
        data = initCluster(data);
        
        while(getCluster(data).length>k){
            data = mergeCluster(data, pilihan);
        }
        return data;
    }
    
    public double getErrorRatio(int[][] data, int[][] dataset){
        Integer[] c_data = getCluster(data);
        Integer[] c_dataset = getCluster(dataset);
        double error = 0;
        for(int i=0; i<c_data.length; i++){
            for(int j=0; j<data.length; j++){
                if(data[j][data[j].length-1]==c_data[i]){
                    data[j][data[j].length-1]=c_dataset[i];
                }
            }
        }
        
        for(int i=0; i<data.length; i++){
            if(data[i][data[i].length-1]!=dataset[i][dataset[i].length-1]){
                error++;
            }
        }
        
        return (error/data.length)*100;
    }
}
