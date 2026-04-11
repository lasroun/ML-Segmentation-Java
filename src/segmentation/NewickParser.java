/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segmentation;


public class NewickParser {

    private int index = 0;

    public Node parse(String s) {
        if (s.charAt(index) == '(') {
            index++; // skip '('

            Node left = parse(s);

            index++; // skip ','

            Node right = parse(s);

            index++; // skip ')'

            Node parent = new Node();
            parent.left = left;
            parent.right = right;

            // distance
            if (index < s.length() && s.charAt(index) == ':') {
                index++;
                parent.distance = readNumber(s);
            }

            return parent;
        } else {
            // feuille
            Node leaf = new Node();
            leaf.name = readLabel(s);

            if (index < s.length() && s.charAt(index) == ':') {
                index++;
                leaf.distance = readNumber(s);
            }

            return leaf;
        }
    }

    private String readLabel(String s) {
        StringBuilder sb = new StringBuilder();
        while (index < s.length() &&
              s.charAt(index) != ':' &&
              s.charAt(index) != ',' &&
              s.charAt(index) != ')') {
            sb.append(s.charAt(index++));
        }
        return sb.toString();
    }

    private double readNumber(String s) {
        StringBuilder sb = new StringBuilder();
        while (index < s.length() &&
              (Character.isDigit(s.charAt(index)) || s.charAt(index) == '.')) {
            sb.append(s.charAt(index++));
        }
        return Double.parseDouble(sb.toString());
    }
}