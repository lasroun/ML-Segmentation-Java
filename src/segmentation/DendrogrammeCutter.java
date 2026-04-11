package segmentation;

import java.util.ArrayList;
import java.util.List;

/**
 * Coupe un arbre Newick en plusieurs racines en fendant de façon itérative le nœud interne de plus grande distance de fusion.
 */
public class DendrogrammeCutter {

    /**
     * Retourne jusqu'à {@code k} sous-arbres ; s'arrête plus tôt si aucun nœud interne n'est plus divisible.
     */
    public static List<Node> cut(Node root, int k) {
        List<Node> clusters = new ArrayList<>();
        clusters.add(root);

        while (clusters.size() < k) {

            Node maxNode = null;
            double maxDist = -1;

            for (Node n : clusters) {
                if (n.left != null && n.right != null && n.distance > maxDist) {
                    maxDist = n.distance;
                    maxNode = n;
                }
            }

            if (maxNode == null) break;

            clusters.remove(maxNode);
            clusters.add(maxNode.left);
            clusters.add(maxNode.right);
        }

        return clusters;
    }
}