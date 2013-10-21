/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package auxiliary;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 *
 * @author daq
 */
public class Kmeans {

    public Kmeans() {
    }

    /*
     * Input double[numIns][numAtt] features, int K
     * Output double[K][numAtt] clusterCenters, int[numIns] clusterIndex
     * 
     * clusterCenters[k] should store the kth cluster center
     * clusterIndex[i] should store the cluster index which the ith sample belongs to
     */
    public void train(double[][] features, int K, double[][] clusterCenters, int[] clusterIndex) {
    	double currentfunction = 0.0;
    	double lastfunction = 0.0;
    	double[][]classcount = new double[K][clusterCenters[0].length];
    	int[] count = new int[K];
    	int loop = 0;
    	
        handleMissData(features);
        
        for(int i = 0; i< K; i++){
        	for(int j = 0; j< features[0].length; j++){
        		clusterCenters[i][j] = features[i][j];
        		clusterIndex[i] = i;
        	}
        }
        
        do{
        	clear(classcount,count);
        	lastfunction = currentfunction;
        	for(int i = 0; i< features.length; i++){
        		double distance = Double.MAX_VALUE;
        		for(int j = 0; j< clusterCenters.length; j++){
        			double currentdistance = getDistance(features[i],clusterCenters[j]);
        			if(currentdistance < distance){
        				clusterIndex[i] = j;
        				distance = currentdistance;
        			}
        		}
        		for(int j = 0; j< features[0].length; j++){
        			classcount[clusterIndex[i]][j] = classcount[clusterIndex[i]][j]+features[i][j];
        		}
        		count[clusterIndex[i]] ++;
        	}
        	
        	for(int i = 0; i< clusterCenters.length; i++){
        		for(int j = 0; j< clusterCenters[0].length; j++){
        			clusterCenters[i][j] = classcount[i][j]/count[i];
        		}
        	}
        	
        	currentfunction = 0;
        	for(int i = 0; i< features.length; i++){
        		currentfunction += getDistance(features[i],clusterCenters[clusterIndex[i]]);
        	}
        	//System.out.println(currentfunction);
        	if(Math.abs(currentfunction - lastfunction) > 0.1 ){
        		loop = 0;
        	}
        	else{
        		loop ++;
        	}
        }
        while(loop < 5);
    }
    
    public void clear(double[][] classcount, int[] count){
    	for(int i = 0; i < classcount.length; i++){
    		for(int j = 0; j< classcount[0].length; j++){
    			classcount[i][j] = 0;
    		}
    		count[i] = 0;
    	}
    }
    public double getDistance(double[] features1, double[] features2){
    	double distance = 0.0;
    	for(int i = 0; i< features1.length; i++){
    		distance = distance + (features1[i]-features2[i])*(features1[i]-features2[i]);
    	}
    	distance = Math.sqrt(distance);
    	return distance;
    }
    public void handleMissData(double[][] features){
       	if(features == null){
       		return;
       	}
       	List<ArrayList> missIndex = new ArrayList<ArrayList>();
       	for(int i=0; i<features[0].length; i++){
       		missIndex.add(new ArrayList());
       	}
       	double[] sumToAvg = new double[features[0].length];
       	for(int i=0; i<features.length; i++){
       		for(int j=0; j<features[0].length; j++){
       			if(features[i][j] >=0 || features[i][j]<=0)
       				sumToAvg[j] += features[i][j];
       			else{
       				missIndex.get(j).add(i);
       			}
       		}
       	}
       	for(int i=0; i<features[0].length; i++){
       		if(missIndex.get(i).size() > 0){
       			sumToAvg[i] = Math.round((sumToAvg[i]/(features.length-missIndex.get(i).size())));
   	    		for(int j=0; j<missIndex.get(i).size(); j++){
   	    			features[(int)missIndex.get(i).get(j)][i] = sumToAvg[i];
   	    		}
       		}
       	}
    }
}
