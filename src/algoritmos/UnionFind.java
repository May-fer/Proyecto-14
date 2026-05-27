package algoritmos;

/**
 * @author Michael
 * @version 1.0
 */
public class UnionFind {

    private int[] parent;

    private int[] rank;

    private int numComponentes;

    private final int n;


    /**
     * Inicializa la estructura con n nodos completamente aislados.
     * Cada nodo forma su propia componente: parent[i] = i, rank[i] = 0.
     *
     * @param n número de nodos del grafo (ids esperados: 0 ... n-1)
     * @throws IllegalArgumentException si n <= 0
     */
    public UnionFind(int n) {
        if (n <= 0) throw new IllegalArgumentException("n debe ser mayor que 0. Recibido: " + n);
        this.n = n;
        this.parent         = new int[n];
        this.rank           = new int[n];
        this.numComponentes = n;

        for (int i = 0; i < n; i++) {
            parent[i] = i;  // cada nodo es raíz de sí mismo
            rank[i]   = 0;  // altura inicial cero
        }
    }

    /**
     * @param x el nodo a consultar (0 ≤ x < n)
     * @return la raíz del conjunto de x
     * @throws ArrayIndexOutOfBoundsException si x está fuera del rango [0, n)
     */
    public int find(int x) {
        validarNodo(x);
        if (parent[x] != x) {
            // Compresión de caminos: recursión que aplana el árbol
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    /**
     * @param x primer nodo
     * @param y segundo nodo
     * @return true si se unieron exitosamente (no había ciclo), false si ya estaban en el mismo conjunto
     */
    public boolean union(int x, int y) {
        validarNodo(x);
        validarNodo(y);

        int raizX = find(x);
        int raizY = find(y);

        if (raizX == raizY) {
            return false;
        }

        if (rank[raizX] < rank[raizY]) {
            parent[raizX] = raizY;

        } else if (rank[raizX] > rank[raizY]) {
            parent[raizY] = raizX;

        } else {
            parent[raizY] = raizX;
            rank[raizX]++;
        }

        numComponentes--; 
        return true;
    }

    /**
     * Verifica si dos nodos pertenecen al mismo conjunto (están conectados).
     * Equivalente a preguntar: ¿hay ya un camino entre x e y en el MST parcial?
     *
     * @param x primer nodo
     * @param y segundo nodo
     * @return true si x e y comparten la misma raíz
     */
    public boolean conectados(int x, int y) {
        validarNodo(x);
        validarNodo(y);
        return find(x) == find(y);
    }

    /**
     * Retorna el número actual de componentes conexas.
     * Al inicio = n. Al final de Kruskal en un grafo conexo = 1.
     * Si termina > 1, el grafo no era conexo y el MST es un bosque.
     *
     * @return número de componentes
     */
    public int getNumComponentes() {
        return numComponentes;
    }

    /**
     * @return número total de nodos de esta estructura
     */
    public int getN() {
        return n;
    }

    
    public void mostrarEstado() {
        System.out.println("\n  ── Estado UnionFind (" + n + " nodos) ──");
        System.out.print("  Nodo  : ");
        for (int i = 0; i < n; i++) System.out.printf("%4d", i);
        System.out.print("\n  Padre : ");
        for (int p : parent)         System.out.printf("%4d", p);
        System.out.print("\n  Rango : ");
        for (int r : rank)           System.out.printf("%4d", r);
        System.out.println("\n  Componentes activas: " + numComponentes);

        System.out.println("  Conjuntos actuales:");
        java.util.Map<Integer, java.util.List<Integer>> conjuntos = new java.util.HashMap<>();
        for (int i = 0; i < n; i++) {
            int raiz = find(i);
            conjuntos.computeIfAbsent(raiz, k -> new java.util.ArrayList<>()).add(i);
        }
        for (java.util.Map.Entry<Integer, java.util.List<Integer>> e : conjuntos.entrySet()) {
            System.out.println("    Raíz " + e.getKey() + " → nodos " + e.getValue());
        }
    }

    private void validarNodo(int x) {
        if (x < 0 || x >= n) {
            throw new ArrayIndexOutOfBoundsException(
                "Nodo inválido: " + x + ". Rango válido: [0, " + (n-1) + "]"
            );
        }
    }
}