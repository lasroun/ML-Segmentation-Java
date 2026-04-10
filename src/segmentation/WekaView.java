package segmentation;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import weka.clusterers.SimpleKMeans;
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

//    public void afficher(JFrame frame, Instances data, String plotName, String titre) throws Exception {
//
//        // Panel WEKA
//        VisualizePanel vp = new VisualizePanel();
//        vp.setName("Visualisation KMeans");
//
//        // IMPORTANT : axes
//        int xIndex = 0;
//        int yIndex = (data.numAttributes() > 1) ? 1 : 0;
//
//        vp.setXIndex(xIndex);
//        vp.setYIndex(yIndex);
//
//        // ====== Données (points) ======
//        PlotData2D plot = new PlotData2D(data);
//        plot.setPlotName("Clusters");
//        plot.m_useCustomColour = true;
//        vp.setMasterPlot(plot);
//        
//
//        // ====== Centroids  ======
//        if (kmeans != null) {
//            PlotData2D centroidPlot = new PlotData2D(kmeans.getClusterCentroids());
//            centroidPlot.setPlotName("Centroids");
//
//            // option : afficher avec forme différente
//            boolean[] shape = new boolean[kmeans.getClusterCentroids().numInstances()];
//            for (int i = 0; i < shape.length; i++) {
//                shape[i] = true;
//            }
//            //centroidPlot.setShapeType();
//
//            vp.addPlot(centroidPlot);
//        }
//
//        // ====== Nettoyage frame ======
//        frame.getContentPane().removeAll();
//        frame.setLayout(new BorderLayout());
//
//        // ====== Ajout du graphique ======
//        frame.add(vp, BorderLayout.CENTER);
//
//        // ====== Configuration ======
//        frame.setTitle("Visualisation KMeans");
//        frame.setSize(800, 600);
//        frame.setLocationRelativeTo(null);
//
//        // ====== Refresh ======
//        frame.revalidate();
//        frame.repaint();
//
//        // ====== Affichage ======
//        frame.setVisible(true);
//    }
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

}
