package algoritmos;

import modelo.Arista;
import modelo.GrafoPonderado;

import java.util.ArrayList;
import java.util.List;

public class Prim {
    private GrafoPonderado grafo;
    private double[] key;
    private int[] parent;
    private boolean[] inMST;
    private int comparaciones;

    public Prim(GrafoPonderado grafo) {
        this.grafo = grafo;
    }

    public List<Arista> ejecutar(int inicio){
        int n = grafo.getNumNodos;
        key = new double[n];
        parent = new int[n];
        inMST = new boolean[n];
        comparaciones = 0;

        //Inicializacion
        for(int i = 0; i < n; i++) {
            key[i] = Double.MAX_VALUE;
            parent[i] = -1;
        }

        ColaPrioridad cola = new ColaPrioridad(n);
        for(int i = 0; i < n; i++) {
            cola.insertar(i, key[i]);
        }

        key[inicio] = 0;
        cola.decreaseClave(inicio,0);

        //Bucle principal de Prim
        while(!cola.estaVacia()) {
            int u = cola.extraerMin();
            inMST[u] = true;

            List<Arista> vecinos = grafo.getVecinos(u);
            for(Arista arista : vecinos) {
                int v = arista.getDestino();
                double peso = arista.getPeso();
                comparaciones++;

                if(!inMST[v] && peso < key[v]) {
                    key[v] = peso;
                    parent[v] = u;
                    cola.decreaseClave(v,peso);
                }
            }
        }

        //MST resultante
        List<Arista> mst = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            if(parent[i] != -1) {
                mst.add(new Arista(parent[i], i, key[i]));
            }
        }
        return mst;
    }

    public double getCostoTotal() {
        double total = 0;
        for(double peso : key) {
            if(peso != Double.MAX_VALUE) {
                total += peso;
            }
        }
        return total;
    }

    public int getNumNodos() {
        return comparaciones;
    }

    public void mostrarResultados() {
        System.out.println("=== MST PRIM ===");
        for (int i = 0; i < parent.length; i++) {
            if(parent[i] != -1) {
                System.out.println(parent[i]
                        + " - "
                        + i
                        + " | Peso: " + key[parent[i]]
                        + " | Costo: " + key[i]);
            }
        }
        System.out.println("-------------------");

        System.out.println("Costo Total: " + getCostoTotal());

        System.out.println("Comparaciones: " + comparaciones);
    }
}
