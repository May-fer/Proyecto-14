package algoritmos;

import modelo.Arista;
import modelo.GrafoPonderado;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Kruskal {

    // Clase interna necesaria porque Arista de Antony no guarda el origen
    private static class AristaKruskal implements Comparable<AristaKruskal> {
        int origen;
        int destino;
        double peso;

        AristaKruskal(int origen, int destino, double peso) {
            this.origen  = origen;
            this.destino = destino;
            this.peso    = peso;
        }

        // para ordenar de menor a mayor peso con Collections.sort
        @Override
        public int compareTo(AristaKruskal otra) {
            return Double.compare(this.peso, otra.peso);
        }
    }

    private GrafoPonderado grafo;
    private List<AristaKruskal> mst;
    private double costoTotal;
    private int comparaciones;
    private long tiempoNs;
    private boolean ejecutado;

    public Kruskal(GrafoPonderado grafo) {
        if (grafo == null) {
            throw new IllegalArgumentException("El grafo no puede ser nulo");
        }
        this.grafo         = grafo;
        this.mst           = new ArrayList<>();
        this.costoTotal    = 0;
        this.comparaciones = 0;
        this.tiempoNs      = 0;
        this.ejecutado     = false;
    }

    // Se ejecuta Kruskal y retorna un list de aristas del MST
    public List<AristaKruskal> ejecutar() {

        mst.clear();
        costoTotal    = 0;
        comparaciones = 0;

        Set<Integer> nodos = grafo.getNodos();
        int V = grafo.getNumNodos();

        // Paso 1: recolectar todas las aristas sin duplicados
        // origen < destino evita agregar A->B y B->A dos veces
        List<AristaKruskal> todasAristas = new ArrayList<>();

        for (int origen : nodos) {
            for (Arista a : grafo.getVecinos(origen)) {
                comparaciones++;
                if (origen < a.getDestino()) {
                    todasAristas.add(new AristaKruskal(origen, a.getDestino(), a.getPeso()));
                }
            }
        }

        long inicioTiempo = System.nanoTime();

        // Paso 2: ordenar aristas por peso de menor a mayor
        Collections.sort(todasAristas);

        // Paso 3: mapear nodos a indices para el UnionFind
        List<Integer> listaNodos = new ArrayList<>(nodos);
        Collections.sort(listaNodos);

        Map<Integer, Integer> indice = new HashMap<>();
        for (int i = 0; i < listaNodos.size(); i++) {
            indice.put(listaNodos.get(i), i);
        }

        // Paso 4: inicializar UnionFind con V nodos
        UnionFind uf = new UnionFind(V);

        // Paso 5: recorrer aristas ordenadas y armar el MST
        for (AristaKruskal arista : todasAristas) {
            comparaciones++;

            int idxOrigen  = indice.get(arista.origen);
            int idxDestino = indice.get(arista.destino);

            // union() retorna true  = no hay ciclo, se agrega al MST
            // union() retorna false = ya estaban conectados, se descarta
            if (uf.union(idxOrigen, idxDestino)) {
                mst.add(arista);
                costoTotal += arista.peso;
                comparaciones++;

                // MST completo cuando tiene V-1 aristas
                if (mst.size() == V - 1) {
                    break;
                }
            }
        }

        tiempoNs  = System.nanoTime() - inicioTiempo;
        ejecutado = true;

        return new ArrayList<>(mst);
    }

    public void mostrarResultado() {
        verificarEjecucion();

        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║          RESULTADO — ALGORITMO DE KRUSKAL            ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        if (!esMSTCompleto()) {
            System.out.println("\n    Advertencia: el grafo NO es conexo.");
            System.out.printf("    Aristas encontradas: %d | Necesarias: %d%n",
                    getCantidadAristasMST(),
                    grafo.getNumNodos() - 1);
        }

        System.out.println("\n  Árbol de Expansión Mínimo (MST):");
        System.out.println("  ┌────────┬────────────┬────────────┬──────────────┐");
        System.out.printf("  │ %-6s │ %-10s │ %-10s │ %-12s │%n",
                "#", "Origen", "Destino", "Costo ($)");
        System.out.println("  ├────────┼────────────┼────────────┼──────────────┤");

        int contador = 1;
        for (AristaKruskal a : mst) {
            System.out.printf("  │ %-6d │ Nodo %-5d │ Nodo %-5d │ %12.2f │%n",
                    contador++, a.origen, a.destino, a.peso);
        }

        System.out.println("  └────────┴────────────┴────────────┴──────────────┘");

        System.out.printf("%n  Aristas en MST    : %d%n", getCantidadAristasMST());
        System.out.printf("  Costo total MST   : $ %.2f%n", costoTotal);

        // Metricas
        System.out.println("\n  ── Métricas de Rendimiento ──────────────────────────");
        System.out.printf("  Comparaciones     : %,d%n", comparaciones);
        System.out.printf("  Tiempo ejecución  : %,d ns (%.4f ms)%n",
                tiempoNs, tiempoNs / 1_000_000.0);
        System.out.println("  ─────────────────────────────────────────────────────");
    }

    // Imprime el analisis de complejidad
    public void mostrarAnalisisComplejidad() {
        verificarEjecucion();

        int V = grafo.getNumNodos();
        int E = comparaciones;
        double eLogE = E > 0 ? E * (Math.log(E) / Math.log(2)) : 0;

        System.out.println("\n  ── Análisis de Complejidad — Kruskal ─────────────────");
        System.out.printf("  Nodos (V)                    : %d%n", V);
        System.out.println("  Complejidad teórica          : O(E log E)");
        System.out.printf("  Operaciones teóricas E·log₂E : %.0f%n", eLogE);
        System.out.printf("  Comparaciones reales         : %,d%n", comparaciones);
        System.out.printf("  Tiempo ejecución             : %,d ns%n", tiempoNs);
        System.out.println("  ─────────────────────────────────────────────────────");

        System.out.println("  Pasos del algoritmo:");
        System.out.println("    1. Recolectar aristas    : O(V + E)");
        System.out.println("    2. Ordenar aristas       : O(E log E)  <- dominante");
        System.out.println("    3. UnionFind por arista  : O(E · α(V)) ≈ O(E)");
        System.out.println("  ─────────────────────────────────────────────────────");
    }

    public double getCostoTotal() {
        verificarEjecucion();
        return costoTotal;
    }

    public int getComparaciones() {
        verificarEjecucion();
        return comparaciones;
    }

    // Metodo para comparativa con Prim
    public long getTiempoNs() {
        verificarEjecucion();
        return tiempoNs;
    }

    private int getCantidadAristasMST() {
        return mst.size();
    }

    public boolean esMSTCompleto() {
        verificarEjecucion();
        return getCantidadAristasMST() == grafo.getNumNodos() - 1;
    }

    private void verificarEjecucion() {
        if (!ejecutado) {
            throw new IllegalStateException("Primero se debe ejecutar Kruskal antes de consultar resultados");
        }
    }
}