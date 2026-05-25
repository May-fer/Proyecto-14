/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poo.grafo;

/**
 *
 * @author LESLEE NICOLE
 */
public class Subestacion {
    
    private int id;
    private String nombre;
    private String region;
    private double x;
    private double y;

    public Subestacion(int id, String nombre, String region, double x, double y) {
        this.id = id;
        this.nombre = nombre;
        this.region = region;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRegion() {
        return region;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return id + " - " + nombre + " (" + region + ")";
    }
}
