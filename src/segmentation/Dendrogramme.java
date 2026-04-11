/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segmentation;

/**
 *
 * @author THINKPAD
 */
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Dendrogramme extends JPanel {

    private List<Node> clusters;

    public Dendrogramme(Node root, int k) {
        this.clusters = DendrogrammeCutter.cut(root, k);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE};

        int i = 0;
        for (Node cluster : clusters) {
            drawNode(g, cluster, 50, getHeight() / 2, 100, colors[i % colors.length]);
            i++;
        }
    }

    private void drawNode(Graphics g, Node node, int x, int y, int offset, Color color) {
        if (node == null) return;

        g.setColor(color);

        if (node.left != null && node.right != null) {

            int y1 = y - offset;
            int y2 = y + offset;

            g.drawLine(x, y1, x, y2);
            g.drawLine(x, y1, x + 50, y1);
            g.drawLine(x, y2, x + 50, y2);

            drawNode(g, node.left, x + 50, y1, offset / 2, color);
            drawNode(g, node.right, x + 50, y2, offset / 2, color);

        } else {
            g.drawString(node.name, x, y);
        }
    }
}
/*public class Dendrogramme extends JPanel {

    private List<Node> clusters;
    private Color[] colors = {
        new Color(231,76,60),
        new Color(52,152,219),
        new Color(46,204,113),
        new Color(155,89,182),
        new Color(241,196,15)
    };

    public Dendrogramme(Node root, int k) {
        this.clusters = DendrogrammeCutter.cut(root, k);
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));

        int i = 0;
        for (Node cluster : clusters) {
            drawNode(g2, cluster, 50, getHeight() / 2, 120, colors[i % colors.length]);
            i++;
        }
    }

    private void drawNode(Graphics2D g, Node node, int x, int y, int offset, Color color) {
        if (node == null) return;

        g.setColor(color);

        if (node.left != null && node.right != null) {

            int y1 = y - offset;
            int y2 = y + offset;

            g.drawLine(x, y1, x, y2);
            g.drawLine(x, y1, x + 70, y1);
            g.drawLine(x, y2, x + 70, y2);

            drawNode(g, node.left, x + 70, y1, offset / 2, color);
            drawNode(g, node.right, x + 70, y2, offset / 2, color);

        } else {
            g.fillOval(x - 3, y - 3, 6, 6);
            g.drawString(node.name, x + 5, y);
        }
    }
}*/