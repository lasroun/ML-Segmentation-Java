package segmentation;

import java.awt.BorderLayout;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import weka.clusterers.HierarchicalClusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;

/**
 * Encapsule Weka pour K-means et clustering hiérarchique, plus affichages associés.
 */
public class ClusteringService {

    /** Résultat numérique d'un run de clusterer (affectations, tailles, centroïdes éventuels). */
    public static class ClusteringResult {

        private final String algorithm;
        private final int[] assignments;
        private final int[] clusterSizes;
        private final Instances centroids;

        /** Crée un résultat immuable pour l'algo indiqué. */
        public ClusteringResult(String algorithm, int[] assignments, int[] clusterSizes, Instances centroids) {
            this.algorithm = algorithm;
            this.assignments = assignments;
            this.clusterSizes = clusterSizes;
            this.centroids = centroids;
        }

        /** Libellé de l'algorithme (K-Means ou Hierarchical). */
        public String getAlgorithm() {
            return algorithm;
        }

        /** Index du cluster par position d'instance (même ordre que les données d'entrée). */
        public int[] getAssignments() {
            return assignments;
        }

        /** Nombre d'instances par identifiant de cluster. */
        public int[] getClusterSizes() {
            return clusterSizes;
        }

        /** Centroïdes Weka pour K-means ; {@code null} pour le hiérarchique. */
        public Instances getCentroids() {
            return centroids;
        }
    }

    /**
     * Duplique les données et ajoute un attribut nominal {@code cluster} renseigné puis défini comme classe.
     */
    public Instances getDataWithCluster(Instances data, int[] assignments) {

        Instances newData = new Instances(data);

        Attribute clusterAttr = new Attribute("cluster");
        newData.insertAttributeAt(clusterAttr, newData.numAttributes());

        for (int i = 0; i < data.numInstances(); i++) {
            newData.instance(i).setValue(newData.numAttributes() - 1, assignments[i]);
        }
        newData.setClassIndex(newData.numAttributes() - 1);
        return newData;
    }

    /**
     * Lance {@link SimpleKMeans}, met à jour la visualisation sur {@code frame} et renvoie affectations et centroïdes.
     */
    public ClusteringResult runKMeans(JFrame frame, Instances data, int k, int seed) throws Exception {
        SimpleKMeans kMeans = new SimpleKMeans();
        kMeans.setNumClusters(k);
        kMeans.setSeed(seed);
        kMeans.setPreserveInstancesOrder(true);
        kMeans.buildClusterer(data);

        int[] assignments = kMeans.getAssignments();
        Instances centroids = kMeans.getClusterCentroids();
        Instances clusteredData = getDataWithCluster(data, assignments);

        double[] weightedSizes = kMeans.getClusterSizes();
        int[] sizes = new int[weightedSizes.length];
        for (int i = 0; i < weightedSizes.length; i++) {
            sizes[i] = (int) Math.round(weightedSizes[i]);
        }
        tracerKMEans(frame, clusteredData, kMeans);

        return new ClusteringResult("K-Means", assignments, sizes, centroids);
    }

    /** Affiche les instances clusterisées et les centroïdes dans un panneau Weka sur la fenêtre donnée. */
    public void tracerKMEans(JFrame frame, Instances data, SimpleKMeans kMeans) throws Exception {
        WekaView view = new WekaView(kMeans);
        view.afficher(frame, data, "Cluster", "Visualisation K-Means");
    }

    /**
     * Parse la chaîne Newick {@code graph}, ouvre une fenêtre et affiche le dendrogramme découpé pour {@code k} groupes.
     */
    public void tracerDendogramme(String graph, int k) {

        String newick = graph.replace("Newick:", "").trim();

        NewickParser parser = new NewickParser();
        Node root = parser.parse(newick);

        JFrame frame = new JFrame("Dendrogramme");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(new Dendrogramme(root, k), BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    /**
     * Construit un {@link HierarchicalClusterer} Weka, calcule les affectations, trace le dendrogramme Newick.
     */
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

        String graph = hierarchical.graph();
        tracerDendogramme(graph, k);

        return new ClusteringResult("Hierarchical", assignments, sizes, null);
    }

    /** Écrit sur la console tailles, centroïdes éventuels et un échantillon des affectations. */
    public void printSummary(ClusteringResult result, int sampleSize) {
        System.out.println("\n=== Resultats " + result.getAlgorithm() + " ===");
        System.out.println("Tailles clusters: " + Arrays.toString(result.getClusterSizes()));
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
