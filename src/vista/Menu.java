package vista;

import java.util.Scanner;
import modelo.*;
import algoritmos.*;
import controlador.*;

public class Menu {
    private Scanner scanner = new Scanner(System.in);
    private GrafoPonderado grafo;

    public void mostrarMenu() {
        int key;
        do{
        System.out.println("=== Menú ===");
        System.out.println("1. Generar grafo aleatorio");
        System.out.println("2. Ver grafo");
        System.out.println("3. Ver matriz de adyacencia");
        System.out.println("4. Ejecutar Kruskal");
        System.out.println("5. Ejecutar Prim");
        System.out.println("6. Comparar Prim vs Kruskal");
        System.out.println("0. Salir");
        System.out.print("Escoja una opción: ");
        

        key = scanner.nextInt();
        

        
            switch (key) {
            
                case 1:
                    grafo = new GrafoPonderado();
                    grafo.generarGrafoAleatorio(6, 10);
                    System.out.println("Grafo generado con 6 nodos y 10 aristas.");
                    break;
                case 2:
                    if (grafo != null) grafo.mostrarGrafo();
                    else System.out.println("Primero genera un grafo.");
                    break;
                case 3:
                    if (grafo != null) grafo.mostrarMatrizAdyacencia();
                    else System.out.println("Primero genera un grafo.");
                    break;
                case 4:
                    if (grafo != null) {
                        Kruskal k = new Kruskal(grafo);
                        k.ejecutar();
                        k.mostrarResultado();
                    } else System.out.println("Primero genera un grafo.");
                    break;
                case 5:
                    if (grafo != null) {
                        System.out.print("Nodo de inicio: ");
                        int nodoInicio = scanner.nextInt();
                        Prim p = new Prim(grafo);
                        p.ejecutar(nodoInicio);
                        p.mostrarResultados();
                    } else System.out.println("Primero genera un grafo.");
                    break;
                case 6:
                    if (grafo != null) {
                        AnalizadorCostos ac = new AnalizadorCostos(grafo);
                        System.out.print("Nodo de inicio para Prim: ");
                        int nodoInicio = scanner.nextInt();
                        ac.analizar(nodoInicio);
                    } else System.out.println("Primero genera un grafo.");
                    break;
                case 0:
                    System.out.println("Gracias por usar el sistema.");
                    break;
                default:
                    System.out.println("Opción no válida.");
                  
        }
        
        }while (key != 0);

    



    }
}


            