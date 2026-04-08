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

/**
 *
 * @author THINKPAD
 */
public class ReaderService {

    public File donneFichierNettoye(File fileIn, String separator) {

        if (fileIn.exists()) {
            // Creation de nom temporaire de fichier 
            String fichierSortie = "output_temp." + this.getExtension(fileIn.getName());
            // Creation du fichier
            File outputFile = new File(fichierSortie);

            // 
            try (
                    BufferedReader bEntree = new BufferedReader(new FileReader(fileIn)); BufferedWriter bSortie = new BufferedWriter(new FileWriter(fichierSortie))) {
                String ligne; int i =0;

                while ((ligne = bEntree.readLine()) != null) {
                    System.out.println("ligne "+i+" : "+ligne);
                    String ligneCorrigee = corrigerLigne(ligne, separator);
                    bSortie.write(ligneCorrigee);
                    bSortie.newLine();
                    i++;
                }

                return outputFile;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public String getExtension(String nomFile) {
        String extension = null;
        int index = nomFile.lastIndexOf('.');
        if (index > 0) {
            extension = nomFile.substring(index + 1);
        }
        return extension;
    }

    public static String corrigerLigne(String ligne, String caractere) {

        // Exemple de corrections possibles :
        // 1. supprimer espaces inutiles
        ligne = ligne.trim();

        // 2. remplacer valeurs manquantes
        ligne = ligne.replace("?", "0");
        ligne = ligne.replace("#", "0");

        // 3. uniformiser séparateur  ???
        ligne = ligne.replace(";", caractere);

        // 4. mettre en minuscule
        ligne = ligne.toLowerCase();

        return ligne;
    }

    public static void main(String[] args) {
        ReaderService rds = new ReaderService();
        File fileTestIn = new File("./cerealcsv");
        File fileTemp = rds.donneFichierNettoye(fileTestIn, ",");
    }
}
