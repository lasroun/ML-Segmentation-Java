package segmentation;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.Instances;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.VisualizePanel;

/** Affiche un nuage d'instances Weka et superpose les centroïdes K-means lorsque fournis. */
public class WekaView {

    private final SimpleKMeans kmeans;

    /** @param kmeans modèle K-means entraîné utilisé pour récupérer et tracer les centroïdes. */
    public WekaView(SimpleKMeans kmeans) {
        this.kmeans = kmeans;
    }

    /**
     * Remplace le contenu de {@code frame} par un {@link VisualizePanel} : plot principal sur {@code data},
     * second plot pour les centroïdes si {@code kmeans} est non nul.
     */
    public void afficher(JFrame frame, Instances data, String plotName, String titre) throws Exception {

        VisualizePanel vp = new VisualizePanel();
        vp.setName(titre);

        PlotData2D plot = new PlotData2D(data);
        plot.setPlotName(plotName);

        vp.setMasterPlot(plot);

        int xIndex = 0;
        int yIndex = (data.numAttributes() > 1) ? 1 : 0;

        vp.setXIndex(xIndex);
        vp.setYIndex(yIndex);

        if (kmeans != null) {
            Instances centroidsRaw = kmeans.getClusterCentroids();
            Instances centroids = new Instances(centroidsRaw);

            Attribute clusterAttr = new Attribute("cluster");
            centroids.insertAttributeAt(clusterAttr, centroids.numAttributes());

            for (int i = 0; i < centroids.numInstances(); i++) {
                centroids.instance(i).setValue(centroids.numAttributes() - 1, i);
            }
            centroids.setClassIndex(centroids.numAttributes() - 1);

            PlotData2D centroidPlot = new PlotData2D(centroids);
            centroidPlot.setPlotName("Centroids");
            centroidPlot.m_useCustomColour = true;
            centroidPlot.m_customColour = java.awt.Color.RED;

            vp.addPlot(centroidPlot);
        }

        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());
        frame.add(vp, BorderLayout.CENTER);

        frame.setTitle(titre);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }
}
