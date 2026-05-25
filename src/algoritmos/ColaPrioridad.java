package algoritmos;

public class ColaPrioridad {
    private int[] heap;
    private double[] clave;
    private int[] posicion;
    private int tamanio;
    private int capacidad;

    public ColaPrioridad(int capacidad) {
        this.capacidad = capacidad;
        this.tamanio = 0;
        this.heap = new int[capacidad];
        this.clave = new double[capacidad];
        this.posicion = new int[capacidad];
        for (int i = 0; i < this.capacidad; i++) {
            clave[i] = Double.MAX_VALUE;
            posicion[i] = -1;
        }
    }

    private int padre(int i) {
        return (i - 1) / 2;
    }

    private int izquierdo(int i) {
        return 2 * i + 1;
    }

    private int derecho(int i) {
        return 2 * i + 2;
    }

    private void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;

        posicion[heap[i]] = i;
        posicion[heap[j]] = j;
    }

    public void insertar(int nodo, double k) {
        heap[tamanio] = nodo;
        clave[nodo] = k;
        posicion[nodo] = tamanio;
        int i = tamanio;
        tamanio++;

        while(i > 0 && clave[heap[i]] < clave[heap[padre(i)]]) {
            swap(i, padre(i));
            i = padre(i);
        }
    }

    public int extraerMin() {
        if (estaVacia()) {
            return -1;
        }

        int minimo = heap[0];
        heap[0] = heap[tamanio - 1];
        posicion[heap[0]] = 0;
        tamanio--;
        heapify(0);
        posicion[minimo] = -1;
        return minimo;
    }

    private void heapify(int i) {
        int izq = izquierdo(i);
        int der = derecho(i);
        int menor = i;

        if(izq < tamanio && clave[heap[izq]] < clave[heap[menor]]) {
            menor = izq;
        }

        if(der < tamanio && clave[heap[der]] < clave[heap[menor]]) {
            menor = der;
        }

        if(menor != i) {
            swap(i, menor);
            heapify(menor);
        }
    }

    public void decreaseClave(int nodo, double nuevaClave) {
        if(nuevaClave >= clave[nodo]) {
            return;
        }
        clave[nodo] = nuevaClave;
        int i = posicion[nodo];

        while(i > 0 && clave[heap[i]] < clave[heap[padre(i)]]) {
            swap(i, padre(i));
            i = padre(i);
        }
    }

    public boolean estaVacia() {
        return tamanio == 0;
    }

}
