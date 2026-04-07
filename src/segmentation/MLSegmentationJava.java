package segmentation;

public class MLSegmentationJava 
{
    public static void main(String[] args) 
    {
        // On demande à Java de lancer l'interface graphique
        java.awt.EventQueue.invokeLater(() -> 
        {
            // On crée une instance de ta vue et on la rend visible
            new MLSegmentationJavaView().setVisible(true);
        });
    }
}
