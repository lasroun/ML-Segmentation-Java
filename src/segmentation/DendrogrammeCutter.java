package segmentation;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.util.*;
/**
 *
 * @author THINKPAD
 */
public class DendrogrammeCutter {

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