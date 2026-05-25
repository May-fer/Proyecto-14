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
     * Encuentra el representante (raíz) del conjunto que contiene el nodo x.
     *
     * Aplica COMPRESIÓN DE CAMINOS: durante la búsqueda de la raíz,
     * hace que todos los nodos intermedios apunten directamente a la raíz.
     * Esto "aplana" el árbol y acelera consultas futuras.
     *
     * Ejemplo visual:
     *   Antes de find(3):  3 → 2 → 1 → 0(raíz)
     *   Después de find(3): 3 → 0, 2 → 0, 1 → 0
     *
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
     * Une los conjuntos que contienen los nodos x e y.
     *
     * Aplica UNIÓN POR RANGO: el árbol de menor rango se cuelga bajo
     * el de mayor rango. Si los rangos son iguales, uno se cuelga del otro
     * y ese incrementa su rango en 1.
     *
     * Regla clave para Kruskal:
     *   - Retorna TRUE  → x e y estaban en componentes distintas → arista válida → agregar al MST.
     *   - Retorna FALSE → x e y ya estaban conectados → agregar la arista formaría un ciclo → DESCARTAR.
     *
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

    /**
     * Imprime el estado interno completo: arrays parent[] y rank[].
     * Útil para depurar y verificar que la estructura funciona correctamente.
     *
     * Ejemplo de salida:
     *   ── Estado UnionFind (5 nodos) ──
     *   Nodo  :   0   1   2   3   4
     *   Padre :   0   0   0   0   0   ← todos apuntan a raíz 0
     *   Rango :   2   0   0   0   0
     *   Componentes activas: 1
     */
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

    /**
     * Valida que el id de nodo esté dentro del rango [0, n).
     * @param x nodo a validar
     */
    private void validarNodo(int x) {
        if (x < 0 || x >= n) {
            throw new ArrayIndexOutOfBoundsException(
                "Nodo inválido: " + x + ". Rango válido: [0, " + (n-1) + "]"
            );
        }
    }
}