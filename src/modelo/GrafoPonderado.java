/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poo.grafo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author LESLEE NICOLE
 */
public class GrafoPonderado {
    
    private Map<Integer, List<Arista>> listaAdyacencia;

    public GrafoPonderado() {
        listaAdyacencia = new HashMap<>();
    }

    // Agregar nodo
    public void agregarNodo(int id) {
        listaAdyacencia.putIfAbsent(id, new ArrayList<>());
    }

    // Agregar arista
    public void agregarArista(int origen, int destino, double peso) {

        agregarNodo(origen);
        agregarNodo(destino);

        listaAdyacencia.get(origen).add(new Arista(destino, peso));
    }

    // Obtener vecinos
    public List<Arista> getVecinos(int nodo) {
        return listaAdyacencia.getOrDefault(nodo, new ArrayList<>());
    }

    // Mostrar grafo
    public void mostrarGrafo() {

        for (Integer nodo : listaAdyacencia.keySet()) {

            System.out.print("Nodo " + nodo + " -> ");

            for (Arista arista : listaAdyacencia.get(nodo)) {
                System.out.print("[" + arista.getDestino() +
                        ", peso=" + arista.getPeso() + "] ");
            }

            System.out.println();
        }
    }

    // Mostrar matriz de adyacencia
    public void mostrarMatrizAdyacencia() {

        List<Integer> nodos = new ArrayList<>(listaAdyacencia.keySet());
        Collections.sort(nodos);

        int n = nodos.size();
        double[][] matriz = new double[n][n];

        for (int i = 0; i < n; i++) {

            int origen = nodos.get(i);

            for (Arista arista : getVecinos(origen)) {

                int j = nodos.indexOf(arista.getDestino());
                matriz[i][j] = arista.getPeso();
            }
        }

        System.out.println("\nMatriz de Adyacencia:");

        for (int i = 0; i < n; i++) {

            for (int j = 0; j < n; j++) {
                System.out.print(matriz[i][j] + "\t");
            }

            System.out.println();
        }
    }

    // Generar grafo aleatorio
    public void generarGrafoAleatorio(int nNodos, int nAristas) {

        Random random = new Random();

        for (int i = 0; i < nNodos; i++) {
            agregarNodo(i);
        }

        for (int i = 0; i < nAristas; i++) {

            int origen = random.nextInt(nNodos);
            int destino = random.nextInt(nNodos);

            double peso = 1 + random.nextInt(100);

            agregarArista(origen, destino, peso);
        }
    }
}
