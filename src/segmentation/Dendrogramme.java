package segmentation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 * Panneau Swing du dendrogramme : modèle {@link Node}, {@link NewickParser}, coupe en {@code k} sous-arbres
 * (plus grande distance de fusion à chaque fente) et dessin.
 * La coupe heuristique peut différer légèrement des libellés de cluster renvoyés par {@code clusterInstance} Weka.
 */
public class Dendrogramme extends JPanel {

    private static final int H_STEP = 50;

    private final List<Node> clusters;
    private static final Color[] COLORS = {
        Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE,
        new Color(155, 89, 182), new Color(241, 196, 15)
    };

    /** Coupe l'arbre en jusqu'à {@code k} racines puis configure le fond blanc. */
    public Dendrogramme(Node root, int k) {
        this.clusters = cut(root, k);
        setBackground(Color.WHITE);
        setOpaque(true);
    }

    /** Dessine chaque sous-arbre dans une bande horizontale avec antialiasing. */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(1.5f));

        int n = clusters.size();
        if (n == 0) {
            return;
        }

        int h = Math.max(getHeight(), 1);
        int w = Math.max(getWidth(), 1);

        for (int i = 0; i < n; i++) {
            int bandTop = i * h / n;
            int bandBottom = (i + 1) * h / n;
            int bandH = Math.max(bandBottom - bandTop, 1);
            int cy = bandTop + bandH / 2;
            int maxOffset = Math.max(bandH / 2 - 24, 20);
            int offset = Math.min(100, maxOffset);
            int margin = Math.min(48, w / 10);
            Color color = COLORS[i % COLORS.length];
            drawNode(g2, clusters.get(i), margin, cy, offset, color);
        }
    }

    /** Dessine récursivement un nœud interne (U inversé) ou une feuille (point + libellé). */
    private void drawNode(Graphics2D g, Node node, int x, int y, int offset, Color color) {
        if (node == null) {
            return;
        }

        g.setColor(color);

        if (node.left != null && node.right != null) {

            int y1 = y - offset;
            int y2 = y + offset;

            g.drawLine(x, y1, x, y2);
            g.drawLine(x, y1, x + H_STEP, y1);
            g.drawLine(x, y2, x + H_STEP, y2);

            drawNode(g, node.left, x + H_STEP, y1, Math.max(offset / 2, 8), color);
            drawNode(g, node.right, x + H_STEP, y2, Math.max(offset / 2, 8), color);

        } else {
            if (node.name != null && !node.name.isEmpty()) {
                g.fillOval(x - 3, y - 3, 6, 6);
                g.drawString(node.name, x + 6, y + 4);
            }
        }
    }

    /**
     * Retourne jusqu'à {@code k} sous-arbres en fendant itérativement le nœud interne de plus grande distance de fusion.
     */
    private static List<Node> cut(Node root, int k) {
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

            if (maxNode == null) {
                break;
            }

            clusters.remove(maxNode);
            clusters.add(maxNode.left);
            clusters.add(maxNode.right);
        }

        return clusters;
    }

    /**
     * Nœud d'un arbre Newick parsé : feuille si {@code left} et {@code right} sont nuls,
     * sinon paire de fils et {@code distance} de fusion.
     */
    public static class Node {
        String name;
        double distance;
        Node left;
        Node right;
    }

    /**
     * Parse récursivement une chaîne Newick (arbre binaire) en graphe de {@link Node}.
     */
    public static class NewickParser {

        private int index = 0;

        /**
         * Remet l'index à zéro puis construit la racine ; attend un format binaire {@code (A,B):dist} ou feuille {@code label:dist}.
         */
        public Node parse(String s) {
            index = 0;
            if (s == null || s.isEmpty()) {
                throw new IllegalArgumentException("Chaine Newick vide.");
            }
            if (s.charAt(index) == '(') {
                index++;

                Node left = parse(s);

                index++;

                Node right = parse(s);

                index++;

                Node parent = new Node();
                parent.left = left;
                parent.right = right;

                if (index < s.length() && s.charAt(index) == ':') {
                    index++;
                    parent.distance = readNumber(s);
                }

                return parent;
            } else {
                Node leaf = new Node();
                leaf.name = readLabel(s);

                if (index < s.length() && s.charAt(index) == ':') {
                    index++;
                    leaf.distance = readNumber(s);
                }

                return leaf;
            }
        }

        /** Lit un libellé de feuille jusqu'à {@code :} , {@code ,} ou {@code )}. */
        private String readLabel(String s) {
            StringBuilder sb = new StringBuilder();
            while (index < s.length()
                && s.charAt(index) != ':'
                && s.charAt(index) != ','
                && s.charAt(index) != ')') {
                sb.append(s.charAt(index++));
            }
            return sb.toString();
        }

        /** Lit un réel (signe, notation scientifique optionnelle) à partir de la position courante. */
        private double readNumber(String s) {
            StringBuilder sb = new StringBuilder();
            if (index < s.length() && (s.charAt(index) == '+' || s.charAt(index) == '-')) {
                sb.append(s.charAt(index++));
            }
            while (index < s.length() && (Character.isDigit(s.charAt(index)) || s.charAt(index) == '.')) {
                sb.append(s.charAt(index++));
            }
            if (index < s.length() && (s.charAt(index) == 'e' || s.charAt(index) == 'E')) {
                sb.append(s.charAt(index++));
                if (index < s.length() && (s.charAt(index) == '+' || s.charAt(index) == '-')) {
                    sb.append(s.charAt(index++));
                }
                while (index < s.length() && Character.isDigit(s.charAt(index))) {
                    sb.append(s.charAt(index++));
                }
            }
            String str = sb.toString();
            if (str.isEmpty() || str.equals("+") || str.equals("-")) {
                return 0.0;
            }
            return Double.parseDouble(str);
        }
    }
}
