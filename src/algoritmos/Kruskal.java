package algoritmos;

import modelo.Arista;
import modelo.GrafoPonderado;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Michael
 * @version 2.0 (adaptada al grafo real de Antony)
 */
public class Kruskal {

    private static class AristaCompleta implements Comparable<AristaCompleta> {

        int    origen;
        int    destino;
        double peso;

        AristaCompleta(int origen, int destino, double peso) {
            this.origen  = origen;
            this.destino = destino;
            this.peso    = peso;
        }

        @Override
        public int compareTo(AristaCompleta otra) {
            return Double.compare(this.peso, otra.peso);
        }

        @Override
        public String toString() {
            return String.format("(%d -- %d, $%.2f)", origen, destino, peso);
        }
    }


    private GrafoPonderado      grafo;
    private List<AristaCompleta> mst;          // aristas del árbol resultante
    private double              costoTotal;    // suma de pesos del MST
    private int                 comparaciones; // contador para análisis O()
    private long                tiempoNs;      // tiempo de ejecución (nanosegundos)
    private boolean             ejecutado;     // flag de control


    /**
     * @param grafo el GrafoPonderado construido por Antony
     */
    public Kruskal(GrafoPonderado grafo) {
        if (grafo == null)
            throw new IllegalArgumentException("El grafo no puede ser null.");

        this.grafo        = grafo;
        this.mst          = new ArrayList<>();
        this.costoTotal   = 0.0;
        this.comparaciones = 0;
        this.tiempoNs     = 0;
        this.ejecutado    = false;
    }

    /**
     * Ejecuta Kruskal y retorna las aristas del MST.
     *
     * PASOS:
     *   1. Recorrer la lista de adyacencia y recolectar aristas sin duplicados.
     *   2. Ordenar aristas de menor a mayor peso.
     *   3. Inicializar UnionFind.
     *   4. Agregar aristas al MST si no forman ciclo.
     *   5. Parar cuando MST tenga V-1 aristas.
     *
     * @return lista de aristas que forman el Árbol de Expansión Mínimo
     */
    public List<AristaCompleta> ejecutar() {

        mst.clear();
        costoTotal    = 0.0;
        comparaciones = 0;

        Set<Integer>  nodos = grafo.getNodos();
        int           V     = grafo.getNumNodos();

        List<AristaCompleta> todasAristas = new ArrayList<>();

        for (int origen : nodos) {
            for (Arista a : grafo.getVecinos(origen)) {
                comparaciones++; 
                if (origen < a.getDestino()) {  
                    todasAristas.add(new AristaCompleta(origen, a.getDestino(), a.getPeso()));
                }
            }
        }

        long inicio = System.nanoTime();

        Collections.sort(todasAristas); // O(E log E)

        List<Integer>  listaOrdenada = new ArrayList<>(nodos);
        Collections.sort(listaOrdenada);
        Map<Integer, Integer> indice = new HashMap<>();
        for (int i = 0; i < listaOrdenada.size(); i++) {
            indice.put(listaOrdenada.get(i), i);
        }

        UnionFind uf = new UnionFind(V);

        for (AristaCompleta arista : todasAristas) {

            comparaciones++; 

            int idxOrigen  = indice.get(arista.origen);
            int idxDestino = indice.get(arista.destino);

            if (uf.union(idxOrigen, idxDestino)) {
                mst.add(arista);
                costoTotal += arista.peso;
                comparaciones++; 

                if (mst.size() == V - 1) {
                    break;
                }
            }
        }

        tiempoNs  = System.nanoTime() - inicio;
        ejecutado = true;

        return new ArrayList<>(mst); 
    }

    

   
    public void mostrarResultado() {
        verificarEjecucion();

        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║          RESULTADO — ALGORITMO DE KRUSKAL            ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        if (mst.isEmpty()) {
            System.out.println("\n  ⚠  El grafo no tiene aristas. MST vacío.");
            return;
        }

        if (!esMSTCompleto()) {
            System.out.println("\n  ⚠  ADVERTENCIA: el grafo NO es conexo.");
            System.out.printf( "     Aristas encontradas: %d | Necesarias: %d%n",
                               mst.size(), grafo.getNumNodos() - 1);
        }

        // ── Tabla de aristas ──────────────────────────────────────────────
        System.out.println("\n  Arbol de Expansion Minimo (MST):");
        System.out.println("  ┌────────┬────────────┬────────────┬──────────────┐");
        System.out.printf( "  │ %-6s │ %-10s │ %-10s │ %-12s │%n",
                           "#", "Origen", "Destino", "Costo ($)");
        System.out.println("  ├────────┼────────────┼────────────┼──────────────┤");

        int i = 1;
        for (AristaCompleta a : mst) {
            System.out.printf("  │ %-6d │ Nodo %-5d │ Nodo %-5d │ %12.2f │%n",
                              i++, a.origen, a.destino, a.peso);
        }

        System.out.println("  └────────┴────────────┴────────────┴──────────────┘");
        System.out.printf( "%n  Aristas en MST    : %d%n", mst.size());
        System.out.printf( "  Costo total MST   : $ %.2f%n", costoTotal);

        // ── Métricas ──────────────────────────────────────────────────────
        System.out.println("\n  ── Metricas de Rendimiento ──────────────────────────");
        System.out.printf( "  Comparaciones     : %,d%n",        comparaciones);
        System.out.printf( "  Tiempo ejecucion  : %,d ns  (%.4f ms)%n",
                           tiempoNs, tiempoNs / 1_000_000.0);
        System.out.println("  ─────────────────────────────────────────────────────");
    }

    
    public void mostrarAnalisisComplejidad() {
        verificarEjecucion();

        int    E    = mst.size() + comparaciones; 
        int    V    = grafo.getNumNodos();
        double eLog = E > 0 ? E * (Math.log(E) / Math.log(2)) : 0;

        System.out.println("\n  ── Analisis de Complejidad — Kruskal ─────────────────");
        System.out.printf( "  Nodos (V)                    : %d%n",     V);
        System.out.println("  Complejidad teorica          : O(E log E)");
        System.out.printf( "  Operaciones teoricas E·log₂E : %.0f%n",   eLog);
        System.out.printf( "  Comparaciones reales         : %,d%n",    comparaciones);
        System.out.printf( "  Tiempo de ejecucion          : %,d ns%n", tiempoNs);
        System.out.println("  ──────────────────────────────────────────────────────");
        System.out.println("  Pasos del algoritmo:");
        System.out.println("    1. Recolectar aristas  : O(V + E)");
        System.out.println("    2. Ordenar aristas     : O(E log E)  ← dominante");
        System.out.println("    3. UnionFind (E veces) : O(E · α(V)) ≈ O(E)");
        System.out.println("  ──────────────────────────────────────────────────────");
        System.out.println("  α = inversa de Ackermann (menor que 5 en la practica)");
    }


    /** @return lista de aristas del MST (copia) */
    public List<AristaCompleta> getMST() {
        verificarEjecucion();
        return new ArrayList<>(mst);
    }

    /** @return costo total del MST (debe coincidir con Prim si el grafo es conexo) */
    public double getCostoTotal() {
        verificarEjecucion();
        return costoTotal;
    }

    /** @return número de comparaciones realizadas (para tabla comparativa) */
    public int getComparaciones() {
        verificarEjecucion();
        return comparaciones;
    }

    /** @return tiempo de ejecución en nanosegundos (para tabla comparativa) */
    public long getTiempoNs() {
        verificarEjecucion();
        return tiempoNs;
    }

    /** @return true si el MST cubre todos los nodos del grafo (grafo conexo) */
    public boolean esMSTCompleto() {
        verificarEjecucion();
        return mst.size() == grafo.getNumNodos() - 1;
    }

    private void verificarEjecucion() {
        if (!ejecutado) {
            throw new IllegalStateException(
                "Llama a ejecutar() antes de consultar resultados."
            );
        }
    }
}