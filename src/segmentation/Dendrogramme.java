package segmentation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;
import javax.swing.JPanel;

/**
 * Dessine les sous-arbres obtenus par coupe du Newick Weka.
 * La coupe heuristique (plus grande distance de fusion) peut différer
 * légèrement des libellés de cluster renvoyés par {@code clusterInstance}.
 */
public class Dendrogramme extends JPanel {

    private static final int H_STEP = 50;

    private final List<Node> clusters;
    private static final Color[] COLORS = {
        Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE,
        new Color(155, 89, 182), new Color(241, 196, 15)
    };

    /** Calcule les {@code k} sous-arbres racines via {@link DendrogrammeCutter} et configure le fond blanc. */
    public Dendrogramme(Node root, int k) {
        this.clusters = DendrogrammeCutter.cut(root, k);
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
}
