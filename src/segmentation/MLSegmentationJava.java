package segmentation;

/** Point d'entrée de l'application : lance la fenêtre principale sur le thread Swing. */
public class MLSegmentationJava {

    /**
     * Affiche {@link MLSegmentationJavaView} de façon thread-safe via la file d'événements AWT.
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new MLSegmentationJavaView().setVisible(true));
    }
}
