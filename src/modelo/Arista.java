/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;


/**
 *
 * @author LESLEE NICOLE
 */
public class Arista implements Comparable<Arista>{
    
     private int destino;
    private double peso;

    public Arista(int destino, double peso) {
        this.destino = destino;
        this.peso = peso;
    }

    public int getDestino() {
        return destino;
    }

    public double getPeso() {
        return peso;
    }

    @Override
    public int compareTo(Arista otra) {
        return Double.compare(this.peso, otra.peso);
    }

    @Override
    public String toString() {
        return "Destino: " + destino + " Peso: " + peso;
    }
}
