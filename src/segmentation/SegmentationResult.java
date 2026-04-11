package segmentation;

import java.util.Arrays;

/** Valeurs agrégées renvoyées à l'interface après un run de segmentation. */
public class SegmentationResult {

    private final String algorithm;
    private final int[] clusterSizes;
    private final int[] assignmentSample;
    private final String tempOutputPath;
    private final String message;
    private String centroids;

    /**
     * Construit le résultat affichable (algo, tailles, échantillon d'affectations, chemins et texte centroïdes).
     */
    public SegmentationResult(
        String algorithm,
        int[] clusterSizes,
        int[] assignmentSample,
        String tempOutputPath,
        String message,
        String centroids
    ) {
        this.algorithm = algorithm;
        this.clusterSizes = clusterSizes;
        this.assignmentSample = assignmentSample;
        this.tempOutputPath = tempOutputPath;
        this.message = message;
        this.centroids = centroids;
    }

    /** Nom de l'algorithme utilisé (ex. K-Means, Hierarchical). */
    public String getAlgorithm() {
        return algorithm;
    }

    /** Effectifs par cluster après clustering. */
    public int[] getClusterSizes() {
        return clusterSizes;
    }

    /** Premiers indices de cluster par instance (aperçu pour l'UI). */
    public int[] getAssignmentSample() {
        return assignmentSample;
    }

    /** Chemin absolu du CSV nettoyé produit pendant le pipeline. */
    public String getTempOutputPath() {
        return tempOutputPath;
    }

    /** Message de statut destiné à l'utilisateur. */
    public String getMessage() {
        return message;
    }

    /** Représentation textuelle des centroïdes, ou libellé si non applicable. */
    public String getCentroids() {
        return centroids;
    }

    /** Assemble un résumé multi-lignes pour boîte de dialogue ou zone de texte. */
    public String toUiSummary() {
        return "Algo: " + algorithm
            + "\nTailles clusters: " + Arrays.toString(clusterSizes)
            + "\nExtrait affectations: " + Arrays.toString(assignmentSample)
            + "\nCentroides : " + centroids
            + "\nCSV nettoye: " + tempOutputPath
            + "\n" + message;
    }
}
