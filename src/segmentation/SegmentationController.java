package segmentation;

import java.io.File;
import java.util.Arrays;
import javax.swing.JFrame;
import weka.core.Instances;

/**
 * Orchestre le pipeline segmentation : nettoyage CSV, préparation Weka, clustering, résumé.
 */
public class SegmentationController {

    public enum AlgorithmChoice {
        K_MEANS,
        HIERARCHICAL
    }

    private final ReaderService readerService;
    private final WekaPipelineService pipelineService;
    private final ClusteringService clusteringService;
    private int[] cluster;

    /** Initialise les services de lecture, pipeline Weka et clustering. */
    public SegmentationController() {
        this.readerService = new ReaderService();
        this.pipelineService = new WekaPipelineService();
        this.clusteringService = new ClusteringService();
    }

    /**
     * Exécute le traitement complet depuis le CSV utilisateur jusqu'au {@link SegmentationResult} affichable.
     */
    public SegmentationResult process(JFrame frame, File inputCsv, AlgorithmChoice choice, int k) throws Exception {
        File tmpDir = new File("build/tmp");
        File cleanedCsv = readerService.donneFichierNettoye(inputCsv, ",", tmpDir);
        if (cleanedCsv == null || !cleanedCsv.exists()) {
            throw new IllegalStateException("Le fichier nettoye n'a pas pu etre genere.");
        }

        Instances preparedData = pipelineService.prepareData(cleanedCsv);

        ClusteringService.ClusteringResult result;
        if (choice == AlgorithmChoice.K_MEANS) {
            result = clusteringService.runKMeans(frame, preparedData, k, 1);
        } else {
            result = clusteringService.runHierarchical(frame, preparedData, k);
        }
        cluster = result.getClusterSizes();

        clusteringService.printSummary(result, 10);

        int[] sample = Arrays.copyOf(result.getAssignments(), Math.min(10, result.getAssignments().length));
        return new SegmentationResult(
            result.getAlgorithm(),
            cluster,
            sample,
            cleanedCsv.getAbsolutePath(),
            "Traitement termine avec succes. Details complets dans la console.",
            result.getCentroids() != null ? result.getCentroids().toString() : "Non applicable pour cet algorithme"
        );
    }
}
