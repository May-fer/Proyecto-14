/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author LESLEE NICOLE
 */
public class LectorArchivo {
    
     public static void cargarCSV(String ruta, GrafoPonderado grafo) {

        String linea;

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {

            while ((linea = br.readLine()) != null) {

                String[] datos = linea.split(",");

                int origen = Integer.parseInt(datos[0]);
                int destino = Integer.parseInt(datos[1]);
                double peso = Double.parseDouble(datos[2]);

                grafo.agregarArista(origen, destino, peso);
            }

            System.out.println("Archivo cargado correctamente.");

        } catch (IOException e) {
            System.out.println("Error al leer archivo: " + e.getMessage());
        }
    }
}
