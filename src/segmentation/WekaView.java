package segmentation;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.Instances;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.VisualizePanel;

public class WekaView {

    private Instances data;
    private SimpleKMeans kmeans;

    public WekaView(Instances data, SimpleKMeans kmeans) {
        this.data = data;
        this.kmeans = kmeans;
    }

    /*
    public void afficher(JFrame frame, Instances data, String plotName, String titre) throws Exception {

        VisualizePanel vp = new VisualizePanel();
        vp.setName(titre);

        //  IMPORTANT : définir le plot AVANT les axes
        PlotData2D plot = new PlotData2D(data);
        plot.setPlotName(plotName);

        vp.setMasterPlot(plot); 

        // maintenant seulement
        int xIndex = 0;
        int yIndex = (data.numAttributes() > 1) ? 1 : 0;

        vp.setXIndex(xIndex);
        vp.setYIndex(yIndex);

        // ====== Frame ======
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

            Instances centroids = kmeans.getClusterCentroids();

            // 🔴 Plot principal centroid
            PlotData2D centroidPlot = new PlotData2D(centroids);
            centroidPlot.setPlotName("Centroids");

            centroidPlot.m_useCustomColour = true;
            centroidPlot.m_customColour = java.awt.Color.RED;

            Attribute clusterAttr = new Attribute("cluster");
            centroids.insertAttributeAt(clusterAttr, centroids.numAttributes());

            // remplir valeur cluster
            for (int i = 0; i < centroids.numInstances(); i++) {
                centroids.instance(i).setValue(centroids.numAttributes() - 1, i);
            }

            // définir comme classe
            centroids.setClassIndex(centroids.numAttributes() - 1);

            vp.addPlot(centroidPlot);

            // 🔥 ASTUCE : dupliquer pour grossir visuellement
            PlotData2D centroidPlot2 = new PlotData2D(centroids);
            centroidPlot2.m_useCustomColour = true;
            centroidPlot2.m_customColour = java.awt.Color.MAGENTA;

            vp.addPlot(centroidPlot2);
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
