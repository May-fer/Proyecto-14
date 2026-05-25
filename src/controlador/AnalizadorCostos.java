package controlador;

public class AnalizadorCostos {
    public void analizar() {
    
    }
    public void mostrarTablaComparativa(String algoritmo, long tiempo, int comparaciones, int costo) {
    System.out.printf("| %-10s | %-12d | %-15d | %-9d |%n",
            algoritmo, tiempo, comparaciones, costo);
    }
    
}
