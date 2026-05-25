package vista;
import java.util.Scanner;
public class Menu {
    private Scanner scanner = new Scanner(System.in);

    public void mostrarMenu() {
        int key;
        do{
        System.out.println("=== Menú ===");
        
        System.out.println("1. Comparar Kruskal y Prim");
        System.out.println("2. Mostrar tabla comparativa");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");

        key = scanner.nextInt();

    }while (key != 0);

    }
    
    private void opciones(int key){
        switch (key) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            default: System.out.println("Opción no válida.");
                break;
    }
}
}

