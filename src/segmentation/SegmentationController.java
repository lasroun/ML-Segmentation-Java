package segmentation;

import java.io.File;
import java.util.Arrays;
import weka.core.Instances;

public class SegmentationController {

    public enum AlgorithmChoice {
        K_MEANS,
        HIERARCHICAL
    }

    private final ReaderService readerService;
    private final WekaPipelineService pipelineService;
    private final ClusteringService clusteringService;

    public SegmentationController() {
        this.readerService = new ReaderService();
        this.pipelineService = new WekaPipelineService();
        this.clusteringService = new ClusteringService();
    }

    public SegmentationResult process(File inputCsv, AlgorithmChoice choice, int k) throws Exception {
        File tmpDir = new File("build/tmp");
        File cleanedCsv = readerService.donneFichierNettoye(inputCsv, ",", tmpDir);
        if (cleanedCsv == null || !cleanedCsv.exists()) {
            throw new IllegalStateException("Le fichier nettoye n'a pas pu etre genere.");
        }

        Instances preparedData = pipelineService.prepareData(cleanedCsv);

        ClusteringService.ClusteringResult result;
        if (choice == AlgorithmChoice.K_MEANS) {
            result = clusteringService.runKMeans(preparedData, k, 1);
        } else {
            result = clusteringService.runHierarchical(preparedData, k);
        }

        clusteringService.printSummary(result, 10);

        int[] sample = Arrays.copyOf(result.getAssignments(), Math.min(10, result.getAssignments().length));
        return new SegmentationResult(
            result.getAlgorithm(),
            result.getClusterSizes(),
            sample,
            cleanedCsv.getAbsolutePath(),
            "Traitement termine avec succes. Details complets dans la console."
        );
    }
}
