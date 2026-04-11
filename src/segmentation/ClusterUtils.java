/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segmentation;

import java.util.List;

/** Utilitaires sur la structure d'arbre utilisée pour le dendrogramme. */
public class ClusterUtils {

    /** Ajoute récursivement les noms des feuilles du sous-arbre {@code node} dans {@code leaves}. */
    public static void collectLeaves(Node node, List<String> leaves) {
        if (node == null) return;

        if (node.left == null && node.right == null) {
            leaves.add(node.name);
        } else {
            collectLeaves(node.left, leaves);
            collectLeaves(node.right, leaves);
        }
    }
}