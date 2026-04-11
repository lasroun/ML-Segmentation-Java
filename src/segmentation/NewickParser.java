package segmentation;

/** Parse récursivement une chaîne Newick (arbre binaire) en graphe de {@link Node}. */
public class NewickParser {

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
