/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segmentation;

/**
 * Noeud d'un arbre Newick parsé : feuille si {@code left} et {@code right} sont nuls, sinon paire de fils et {@code distance} de fusion.
 */
class Node {
    String name;
    double distance;
    Node left;
    Node right;
}