package controlador;

import modelo.*;
import algoritmos.*;


public class AnalizadorCostos {
    private GrafoPonderado grafo;

    public AnalizadorCostos(GrafoPonderado grafo) {
        this.grafo = grafo;
    }
    public void analizar(int nodoInicio) {
        Kruskal kruskal = new Kruskal(grafo);
        kruskal.ejecutar();

        Prim prim = new Prim(grafo);
        prim.ejecutar(nodoInicio);

        System.out.println("\n===== COMPARACIÓN PRIM vs KRUSKAL =====");
        System.out.printf("%-15s %-15s %-15s %-15s%n",
            "Algoritmo", "Tiempo(ns)", "Comparaciones", "Costo MST");
        System.out.println("-".repeat(60));

        mostrarTablaComparativa("Prim", prim.getTiempoNs(), prim.getComparaciones(), prim.getCostoTotal());
        mostrarTablaComparativa("Kruskal", kruskal.getTiempoNs(), kruskal.getComparaciones(), kruskal.getCostoTotal());

    }
    public void mostrarTablaComparativa(String algoritmo, long tiempo, int comparaciones, double costo) {
    System.out.printf("| %-10s | %-12d | %-15d | %-9.2f |%n",
            algoritmo, tiempo, comparaciones, costo);
    }
    

}