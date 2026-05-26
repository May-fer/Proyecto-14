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
    private double costoTotal;
    private long tiempoNs;
    private boolean ejecutando;

    public Prim(GrafoPonderado grafo) {
        if (grafo == null) {
            throw new IllegalArgumentException("El grafo no puede ser nulo");
        }
        this.grafo = grafo;
        this.comparaciones = 0;
        this.costoTotal = 0;
        this.tiempoNs = 0;
        this.ejecutando = false;
    }

    //Se ejecuta Prim desde un nodo inicial y retorna un list de aristas del mst
    public List<Arista> ejecutar(int inicio){
        if(inicio < 0 || inicio >= grafo.getNumNodos()) {
            throw new IllegalArgumentException("Nodo inicial invalido");
        }
        costoTotal = 0;
        int n = grafo.getNumNodos();
        key = new double[n];
        parent = new int[n];
        inMST = new boolean[n];
        comparaciones = 0;

        //Inicializacion
        for(int i = 0; i < n; i++) {
            key[i] = Double.MAX_VALUE;
            parent[i] = -1;
            inMST[i] = false;
        }

        //Se insertan los nodos en la Cola de Prioridad
        ColaPrioridad cola = new ColaPrioridad(n);
        for(int i = 0; i < n; i++) {
            cola.insertar(i, key[i]);
        }

        key[inicio] = 0; //Nodo inicial
        cola.decreaseClave(inicio,0);
        long inicioTiempo = System.nanoTime();

        //Bucle principal de Prim
        while(!cola.estaVacia()) {
            int u = cola.extraerMin(); //Se trabaja con el nodo con menor clave
            inMST[u] = true;

            List<Arista> vecinos = grafo.getVecinos(u); //Vecinos del nodo u
            for(Arista arista : vecinos) {
                int v = arista.getDestino();
                double peso = arista.getPeso();
                comparaciones++;

                //Relajacion
                if(!inMST[v] && peso < key[v]) {
                    key[v] = peso;
                    parent[v] = u;
                    cola.decreaseClave(v,peso);
                }
            }
        }
        tiempoNs = System.nanoTime() - inicioTiempo;//Calcula el tiempo en ns

        //Construye el MST resultante
        List<Arista> resultado = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            if(parent[i] != -1) {
                resultado.add(new Arista(i, key[i]));
                costoTotal += key[i];
            }
        }
        ejecutando = true;
        return resultado;
    }

    public void mostrarResultados() {
        verificarEjecucion();
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║             RESULTADO — ALGORITMO PRIM              ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        if(!esMSTCompleto()) {
            System.out.println("\n    Advertencia: el grafo NO es conexo.");
            System.out.printf("    Aristas encontradas: %d | Necesarias: %d%n",
                    getCantidadAristasMST(),
                    grafo.getNumNodos() - 1);
        }

        System.out.println("\n  Árbol de Expansión Mínimo (MST):");
        System.out.println("  ┌────────┬────────────┬────────────┬──────────────┐");
        System.out.printf("  │ %-6s │ %-10s │ %-10s │ %-12s │%n",
                "#",
                "Origen",
                "Destino",
                "Costo ($)");
        System.out.println("  ├────────┼────────────┼────────────┼──────────────┤");
        int contador = 1;

        for(int nodo = 0; nodo < parent.length; nodo++) {
            if(parent[nodo] != -1) {
                System.out.printf("  │ %-6d │ Nodo %-5d │ Nodo %-5d │ %12.2f │%n",
                        contador++,
                        parent[nodo],
                        nodo,
                        key[nodo]);
            }
        }
        System.out.println("  └────────┴────────────┴────────────┴──────────────┘");

        System.out.printf("%n  Aristas en MST    : %d%n", getCantidadAristasMST());
        System.out.printf("  Costo total MST   : $ %.2f%n", costoTotal);

        //Metricas
        System.out.println("\n  ── Métricas de Rendimiento ──────────────────────────");
        System.out.printf("  Comparaciones     : %,d%n", comparaciones);
        System.out.printf("  Tiempo ejecución  : %,d ns (%.4f ms)%n", tiempoNs,
                tiempoNs / 1_000_000.0);
        System.out.println("  ─────────────────────────────────────────────────────");
    }

    //Imprime los analisis de complejidad
    public void mostrarAnalisisComplejidad() {
        verificarEjecucion();
        int V = grafo.getNumNodos();
        int E = comparaciones;
        double eLogV = E * (Math.log(V) / Math.log(2));

        System.out.println("\n  ── Análisis de Complejidad — Prim ───────────────────");
        System.out.printf("  Nodos (V)                    : %d%n", V);
        System.out.println("  Complejidad teórica          : O(E log V)");
        System.out.printf("  Operaciones teóricas E·log₂V : %.0f%n", eLogV);
        System.out.printf("  Comparaciones reales         : %,d%n", comparaciones);
        System.out.printf("  Tiempo ejecución             : %,d ns%n", tiempoNs);
        System.out.println("  ─────────────────────────────────────────────────────");

        System.out.println("  Pasos del algoritmo:");
        System.out.println("    1. Inicializar heap      : O(V)");
        System.out.println("    2. Extraer mínimo        : O(log V)");
        System.out.println("    3. Relajar aristas       : O(E log V)");
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

    //Metodo para comparativa
    public long getTiempoNs() {
        verificarEjecucion();
        return tiempoNs;
    }

    private int getCantidadAristasMST() {
        int cantidad = 0;
        for(int i = 0; i < parent.length; i++) {
            if(parent[i] != -1) {
                cantidad++;
            }
        }
        return cantidad;
    }

    public boolean esMSTCompleto() {
        verificarEjecucion();
        return getCantidadAristasMST() == grafo.getNumNodos() - 1;
    }

    private void verificarEjecucion() {
        if(!ejecutando) {
            throw new IllegalStateException("Primero se debe ejecutar Prim antes de consultar resultados");
        }
    }
}