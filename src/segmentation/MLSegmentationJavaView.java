/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package segmentation;


/** Fenêtre principale : choix du CSV, de l'algorithme et de k, lancement du pipeline via {@link SegmentationController}. */
public class MLSegmentationJavaView extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MLSegmentationJavaView.class.getName());
    private final SegmentationController segmentationController = new SegmentationController();

    /** Construit la fenêtre et initialise les composants Swing (générés par le Form Editor). */
    public MLSegmentationJavaView() {
        initComponents();
    }

    /**
     * Code généré par le Form Editor ; ne pas modifier manuellement (régénéré à chaque sauvegarde du .form).
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jFrame2 = new javax.swing.JFrame();
        jFrame3 = new javax.swing.JFrame();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jRbtn1 = new javax.swing.JRadioButton();
        jRbtn2 = new javax.swing.JRadioButton();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 15), new java.awt.Dimension(0, 15), new java.awt.Dimension(32767, 15));

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jFrame2Layout = new javax.swing.GroupLayout(jFrame2.getContentPane());
        jFrame2.getContentPane().setLayout(jFrame2Layout);
        jFrame2Layout.setHorizontalGroup(
            jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame2Layout.setVerticalGroup(
            jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jFrame3Layout = new javax.swing.GroupLayout(jFrame3.getContentPane());
        jFrame3.getContentPane().setLayout(jFrame3Layout);
        jFrame3Layout.setHorizontalGroup(
            jFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame3Layout.setVerticalGroup(
            jFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Lancer");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jLabel1.setText("Dataset");

        jButton2.setText("...");
        jButton2.setToolTipText("");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jRbtn1.setText("K Means");
        jRbtn1.addActionListener(this::jRbtn1ActionPerformed);

        jRbtn2.setText("Clustering hiérarchique");
        jRbtn2.addActionListener(this::jRbtn2ActionPerformed);

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(2, 2, null, 1));

        jLabel2.setText("Valeur de K(>1)");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                        .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(165, 165, 165))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(112, 112, 112)
                                .addComponent(jRbtn2)
                                .addGap(82, 82, 82)
                                .addComponent(jRbtn1)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRbtn2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jRbtn1))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Valide le chemin CSV, lit k et l'algorithme, appelle {@link SegmentationController#process} et affiche le résumé ou une erreur.
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String chemin = jTextField1.getText().trim(); // .trim() enlève les espaces inutiles au début/fin du string

        // Créer un objet File avec le chemin entré
        java.io.File fichier = new java.io.File(chemin);

        // Vérifications successives
        if (chemin.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Le champ est vide. Veuillez choisir un fichier.", "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
        } 
        else if (!fichier.exists()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Le fichier n'existe pas !\nVérifiez le chemin saisi.", "Chemin invalide", javax.swing.JOptionPane.ERROR_MESSAGE);
        } 
        else if (!chemin.toLowerCase().endsWith(".csv")) {
            javax.swing.JOptionPane.showMessageDialog(this, "Le fichier doit être au format .csv", "Format incorrect", javax.swing.JOptionPane.WARNING_MESSAGE);
        } 
        else {
            if (!jRbtn1.isSelected() && !jRbtn2.isSelected()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Selectionnez un algorithme (K Means ou Clustering hierarchique).", "Algorithme manquant", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int k = (Integer) jSpinner1.getValue();
                SegmentationController.AlgorithmChoice choice = jRbtn1.isSelected()
                    ? SegmentationController.AlgorithmChoice.K_MEANS
                    : SegmentationController.AlgorithmChoice.HIERARCHICAL;

                SegmentationResult result = segmentationController.process(jFrame2,fichier, choice, k);
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    result.toUiSummary(),
                    "Succes",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
                );
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Erreur pendant le traitement:\n" + e.getMessage(),
                    "Erreur",
                    javax.swing.JOptionPane.ERROR_MESSAGE
                );
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /** Ouvre un {@link javax.swing.JFileChooser} et recopie le chemin absolu dans le champ texte. */
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Créer la fenêtre de choix de fichier
        javax.swing.JFileChooser fc = new javax.swing.JFileChooser();

        // Afficher la fenêtre et stocker le résultat (ouvrir ou annuler)
        int resultat = fc.showOpenDialog(this);

        // Si l'utilisateur a choisi un fichier
        if (resultat == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File fichier = fc.getSelectedFile();
    
        // On écrit le chemin dans le champ texte
        jTextField1.setText(fichier.getAbsolutePath());
        }
        
    }//GEN-LAST:event_jButton2ActionPerformed

    /** Sélection K-means : libellé du spinner pour le nombre de groupes, désélectionne l'autre radio. */
    private void jRbtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRbtn1ActionPerformed
        // TODO add your handling code here:
        if (jRbtn1.isSelected()) {
            jLabel2.setText("Nbre de Groupes");
            jRbtn2.setSelected(false);
        }
    }//GEN-LAST:event_jRbtn1ActionPerformed

    /** Sélection hiérarchique : libellé du spinner pour le niveau de coupure, désélectionne K-means. */
    private void jRbtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRbtn2ActionPerformed
        if (jRbtn2.isSelected()) {
            jRbtn1.setSelected(false);
            
            jLabel2.setText("Niveau de coupure Arbre");
        }
    }//GEN-LAST:event_jRbtn2ActionPerformed

    /**
     * Point d'entrée alternatif : applique le look Nimbus si disponible puis affiche cette vue.
     *
     * @param args non utilisés
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new MLSegmentationJavaView().setVisible(true));
       
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JFrame jFrame2;
    private javax.swing.JFrame jFrame3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JRadioButton jRbtn1;
    private javax.swing.JRadioButton jRbtn2;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
