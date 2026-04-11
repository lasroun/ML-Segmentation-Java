/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segmentation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Lecture et nettoyage de CSV (remplacement des {@code -1} par des moyennes par colonne). */
public class ReaderService {

    /**
     * Nettoie le CSV vers un fichier temporaire à la racine du processus.
     *
     * @return fichier écrit ou {@code null} si erreur
     * @see #donneFichierNettoye(File, String, File)
     */
    public File donneFichierNettoye(File fileIn, String separator) {
        return donneFichierNettoye(fileIn, separator, null);
    }

    /**
     * Lit {@code fileIn}, remplace les valeurs {@code -1} par la moyenne de colonne, écrit {@code output_temp.*} dans {@code outDir} ou le répertoire courant.
     *
     * @return le fichier produit ou {@code null} si le source est absent ou en cas d'I/O
     */
    public File donneFichierNettoye(File fileIn, String separator, File outDir) {
        if (!fileIn.exists()) {
            System.err.println("Erreur : Le fichier source n'existe pas.");
            return null;
        }

        // ÉTAPE 1 : Analyse automatique pour calculer les moyennes des colonnes numeriques
        // On utilise une Map pour stocker [Index de colonne -> Valeur Moyenne]
        Map<Integer, Double> moyennesAuto = calculerToutesLesMoyennes(fileIn);

        // Nom du fichier de sortie
        String nomSortie = "output_temp." + this.getExtension(fileIn.getName());
        File outputFile;
        if (outDir != null) {
            if (!outDir.exists() && !outDir.mkdirs()) {
                System.err.println("Erreur : impossible de creer le dossier de sortie: " + outDir.getAbsolutePath());
                return null;
            }
            outputFile = new File(outDir, nomSortie);
        } else {
            outputFile = new File(nomSortie);
        }

        // ÉTAPE 2 : Seconde lecture pour copier et corriger les donnees
        try (
            BufferedReader bEntree = new BufferedReader(new FileReader(fileIn));
            BufferedWriter bSortie = new BufferedWriter(new FileWriter(outputFile))
        ) {
            // Lecture de l'entête (Header)
            String ligne = bEntree.readLine();
            if (ligne != null) {
                bSortie.write(ligne); // On ecrit l'entête telle quelle
                bSortie.newLine();
            }

            int i = 1;
            while ((ligne = bEntree.readLine()) != null) {
                // On decoupe la ligne par rapport à la virgule du CSV d'origine
                String[] colonnes = ligne.split(separator);
                
                // On traite chaque colonne
                for (int j = 0; j < colonnes.length; j++) {
                    String valeur = colonnes[j].trim();
                    
                    // Si on trouve un -1 et qu'on a une moyenne calculee pour cette colonne
                    if (valeur.equals("-1") && moyennesAuto.containsKey(j)) {
                        // On remplace par la moyenne formatee (ex: 96.12)
                        colonnes[j] = String.format(Locale.US, "%.2f", moyennesAuto.get(j));
                    }
                }

                // On reconstruit la ligne avec le nouveau separateur et en minuscule
                String ligneCorrigee = String.join(separator, colonnes).toLowerCase();
                
                bSortie.write(ligneCorrigee);
                bSortie.newLine();
                
                System.out.println("Ligne " + i + " nettoyee.");
                i++;
            }

            return outputFile;

        } catch (IOException e) {
            System.err.println("Erreur lors de l'ecriture du fichier nettoye : " + e.getMessage());
            return null;
        }
    }

    /** Première passe : moyenne par index de colonne en ignorant {@code -1} et les cellules non numériques. */
    private Map<Integer, Double> calculerToutesLesMoyennes(File file) {
        Map<Integer, Double> moyennes = new HashMap<>();
        Map<Integer, List<Double>> valeursParCol = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // Sauter l'entête
            String ligne;
            
            while ((ligne = br.readLine()) != null) {
                String[] cols = ligne.split(",");
                for (int i = 0; i < cols.length; i++) {
                    try {
                        double val = Double.parseDouble(cols[i].trim());
                        // On ne prend que les valeurs valides pour la moyenne
                        if (val != -1) {
                            valeursParCol.computeIfAbsent(i, k -> new ArrayList<>()).add(val);
                        }
                    } catch (NumberFormatException e) {
                        // Ce n'est pas un nombre (nom de cereale, etc.), on ignore
                    }
                }
            }

            // Calcul de la moyenne pour chaque colonne stockee
            for (Map.Entry<Integer, List<Double>> entry : valeursParCol.entrySet()) {
                double somme = 0;
                List<Double> liste = entry.getValue();
                for (double v : liste) somme += v;
                
                double moyenne = somme / liste.size();
                moyennes.put(entry.getKey(), moyenne);
                
                System.out.println("Colonne index " + entry.getKey() + " -> Moyenne calculee : " + moyenne);
            }

        } catch (IOException e) {
            System.err.println("Erreur lors du calcul des moyennes : " + e.getMessage());
        }
        return moyennes;
    }

    /** Retourne l'extension (sans le point) ou {@code "csv"} si aucune extension détectée. */
    public String getExtension(String nomFile) {
        int index = nomFile.lastIndexOf('.');
        if (index > 0) {
            return nomFile.substring(index + 1);
        }
        return "csv"; // Par defaut
    }

    /** Point d'entrée manuel pour tester le nettoyage sur {@code cereal.csv} à la racine. */
    public static void main(String[] args) {
        ReaderService rds = new ReaderService();
        File f = new File("./cereal.csv");
        if(f.exists()){
            rds.donneFichierNettoye(f, ",");
            System.out.println("Nettoyage termine avec succes !");
        } else {
            System.out.println("Fichier cereal.csv introuvable à la racine.");
        }
    }

}
