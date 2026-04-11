package segmentation;

import java.util.Arrays;
import javax.swing.JFrame;
import weka.clusterers.HierarchicalClusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;


public class ClusteringService {

    public static class ClusteringResult {

        private final String algorithm;
        private final int[] assignments;
        private final int[] clusterSizes;
        private final Instances centroids; // Ajouté ici

        public ClusteringResult(String algorithm, int[] assignments, int[] clusterSizes, Instances centroids) {
            this.algorithm = algorithm;
            this.assignments = assignments;
            this.clusterSizes = clusterSizes;
            this.centroids = centroids; // Initialisé ici
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

        //On ajoute un getter pour récupérer les centroïdes plus tard
        public Instances getCentroids() {
            return centroids;
        }
    }

    public Instances getDataWithCluster(Instances data, int[] assignments) {

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

    public ClusteringResult runKMeans(JFrame frame, Instances data, int k, int seed) throws Exception {
        SimpleKMeans kMeans = new SimpleKMeans();
        kMeans.setNumClusters(k);
        kMeans.setSeed(seed);
        kMeans.setPreserveInstancesOrder(true);
        kMeans.buildClusterer(data);

        int[] assignments = kMeans.getAssignments();
        // Récupérer les centroïdes
        Instances centroids = kMeans.getClusterCentroids();
        this.getDataWithCluster(data, assignments);
        Instances clusteredData = getDataWithCluster(data, assignments);

        double[] weightedSizes = kMeans.getClusterSizes();
        int[] sizes = new int[weightedSizes.length];
        for (int i = 0; i < weightedSizes.length; i++) {
            sizes[i] = (int) Math.round(weightedSizes[i]);
        }
        tracerKMEans(frame, clusteredData, kMeans);

        return new ClusteringResult("K-Means", assignments, sizes, centroids); //On passe centroids au constructeur
    }

    public void tracerKMEans(JFrame frame, Instances data, SimpleKMeans kMeans) throws Exception {
        WekaView view = new WekaView(data, kMeans);
        String plotName = "Cluster", titre = "Visualisation KMEANS";
        view.afficher(frame, data, plotName, titre);
    }

    public void tracerDendogramme(String graph, int k){
        
        String newick = graph.replace("Newick:", "").trim();

        NewickParser parser = new NewickParser();
        Node root = parser.parse(newick);

        JFrame frame = new JFrame("Dendrogramme");
        frame.setSize(800, 600);

        frame.add(new Dendrogramme(root, k)); // 🔥 IMPORTANT

        frame.setVisible(true);
        
    }
    /*public void tracerDendogramme(String graph, int k,JFrame frame) {

        String newick = graph.replace("Newick:", "").trim();

        NewickParser parser = new NewickParser();
        Node root = parser.parse(newick);

        List<Node> clusters = DendrogrammeCutter.cut(root, k);

       frame = new JFrame("Dendrogramme PRO");
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());

        // 🎨 dendrogramme
        Dendrogramme panel = new Dendrogramme(root, k);
        frame.add(panel, BorderLayout.CENTER);

        // 📋 panneau clusters
        JTextArea clusterArea = new JTextArea();
        clusterArea.setEditable(false);

        StringBuilder sb = new StringBuilder();

        int i = 1;
        for (Node cluster : clusters) {
            List<String> leaves = new ArrayList<>();
            ClusterUtils.collectLeaves(cluster, leaves);

            sb.append("Cluster ").append(i).append(":\n");
            sb.append(leaves).append("\n\n");
            i++;
        }

        clusterArea.setText(sb.toString());

        frame.add(new JScrollPane(clusterArea), BorderLayout.EAST);

        frame.setVisible(true);
    }*/

    public ClusteringResult runHierarchical(JFrame frame, Instances data, int k) throws Exception {
        HierarchicalClusterer hierarchical = new HierarchicalClusterer();
        hierarchical.setNumClusters(k);
        hierarchical.setLinkType(new SelectedTag(1, HierarchicalClusterer.TAGS_LINK_TYPE));
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

        //  IMPORTANT : ajouter colonne cluster
        Instances clusteredData = getDataWithCluster(data, assignments);
        String graph = hierarchical.graph();
        System.out.println("coucou  " + graph);
        tracerDendogramme(graph, k);

        return new ClusteringResult("Hierarchical", assignments, sizes, null); //Le clustering hiérarchique ne génère pas de centroïdes par défaut
    }

    public void printSummary(ClusteringResult result, int sampleSize) {
        System.out.println("\n=== Resultats " + result.getAlgorithm() + " ===");
        System.out.println("Tailles clusters: " + Arrays.toString(result.getClusterSizes()));
        // Vérification si les centroïdes existent (K-Means)
        if (result.getCentroids() != null) {
            System.out.println("Centroides :\n" + result.getCentroids());
        } else {
            System.out.println("Centroides : Non applicable pour cet algorithme.");
        }

        int[] assignments = result.getAssignments();
        int limit = Math.min(sampleSize, assignments.length);
        System.out.println("Affectations (echantillon " + limit + "):");
        for (int i = 0; i < limit; i++) {
            System.out.println("  Ligne " + i + " -> cluster " + assignments[i]);
        }
    }
}
