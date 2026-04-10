package segmentation;

import java.util.Arrays;
import javax.swing.JFrame;
import weka.clusterers.HierarchicalClusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class ClusteringService {

    public static class ClusteringResult {
        private final String algorithm;
        private final int[] assignments;
        private final int[] clusterSizes;
    

        public ClusteringResult(String algorithm, int[] assignments, int[] clusterSizes) {
            this.algorithm = algorithm;
            this.assignments = assignments;
            this.clusterSizes = clusterSizes;
        }

        public String getAlgorithm() {
            return algorithm;
        }

        public int[] getAssignments() {
            return assignments;
        }

        public int[] getClusterSizes() {
            return clusterSizes;
        }
    }
    
    public Instances getDataWithCluster(Instances data, int[] assignments){
        
        Instances newData = new Instances(data); // copie

        Attribute clusterAttr = new Attribute("cluster");
        newData.insertAttributeAt(clusterAttr, newData.numAttributes());

        // remplir
        for (int i = 0; i < data.numInstances(); i++) {
            newData.instance(i).setValue(newData.numAttributes() - 1, assignments[i]);
        }
        // définir comme classe (pour couleur)
        newData.setClassIndex(newData.numAttributes() - 1);
        return newData;
    }

    public ClusteringResult runKMeans(JFrame frame,Instances data, int k, int seed) throws Exception {
        SimpleKMeans kMeans = new SimpleKMeans();
        kMeans.setNumClusters(k);
        kMeans.setSeed(seed);
        kMeans.setPreserveInstancesOrder(true);
        kMeans.buildClusterer(data);

        int[] assignments = kMeans.getAssignments();
        this.getDataWithCluster(data, assignments);
        Instances clusteredData = getDataWithCluster(data, assignments);
        double[] weightedSizes = kMeans.getClusterSizes();
        int[] sizes = new int[weightedSizes.length];
        for (int i = 0; i < weightedSizes.length; i++) {
            sizes[i] = (int) Math.round(weightedSizes[i]);
        }
        TracerKMEans(frame, clusteredData, kMeans);
        return new ClusteringResult("K-Means", assignments, sizes);
    }
    
    public void TracerKMEans(JFrame frame,Instances data,SimpleKMeans kMeans) throws Exception{
        WekaView view = new WekaView(data, kMeans);
        String plotName ="",titre="";
        view.afficher(frame, data, plotName, titre);
    }

    public ClusteringResult runHierarchical(Instances data, int k) throws Exception {
        HierarchicalClusterer hierarchical = new HierarchicalClusterer();
        hierarchical.setNumClusters(k);
        hierarchical.buildClusterer(data);

        int[] assignments = new int[data.numInstances()];
        int[] sizes = new int[k];
        
        for (int i = 0; i < data.numInstances(); i++) {
            Instance instance = data.instance(i);
            int cluster = hierarchical.clusterInstance(instance);
            assignments[i] = cluster;
            if (cluster >= 0 && cluster < sizes.length) {
                sizes[cluster]++;
            }
        }

        return new ClusteringResult("Hierarchical", assignments, sizes);
    }

    public void printSummary(ClusteringResult result, int sampleSize) {
        System.out.println("\n=== Resultats " + result.getAlgorithm() + " ===");
        System.out.println("Tailles clusters: " + Arrays.toString(result.getClusterSizes()));

        int[] assignments = result.getAssignments();
        int limit = Math.min(sampleSize, assignments.length);
        System.out.println("Affectations (echantillon " + limit + "):");
        for (int i = 0; i < limit; i++) {
            System.out.println("  Ligne " + i + " -> cluster " + assignments[i]);
        }
    }
}
