package segmentation;

import java.util.Arrays;

public class SegmentationResult {

    private final String algorithm;
    private final int[] clusterSizes;
    private final int[] assignmentSample;
    private final String tempOutputPath;
    private final String message;
    private String centroids;

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

    public String getAlgorithm() {
        return algorithm;
    }

    public int[] getClusterSizes() {
        return clusterSizes;
    }

    public int[] getAssignmentSample() {
        return assignmentSample;
    }

    public String getTempOutputPath() {
        return tempOutputPath;
    }

    public String getMessage() {
        return message;
    }

    public String getCentroids() {
        return centroids;
    }

    public String toUiSummary() {
        return "Algo: " + algorithm
            + "\nTailles clusters: " + Arrays.toString(clusterSizes)
            + "\nExtrait affectations: " + Arrays.toString(assignmentSample)
            + "\nCentroides : " + centroids
            + "\nCSV nettoye: " + tempOutputPath
            + "\n" + message;
    }
}
