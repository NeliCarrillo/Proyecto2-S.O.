/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Objetos;

import java.awt.Color;

/**
 *
 * @author nelsoncarrillo
 */
public class Archivo {
    // Atributos
    private String nombre;
    private int tamaño; // Tamaño en bloques
    private int direccionPrimerBloque;
    private Color color;
    private String directorio;
    
    
    // Constructor
    public Archivo(String nombre, int tamaño,String directori) {
        this.nombre = nombre;
        this.tamaño = tamaño;
        this.directorio=directori;
    }
    
    // Constructor
    public Archivo(String nombre, int tamaño, int direccionPrimerBloque) {
        this.nombre = nombre;
        this.tamaño = tamaño;
        this.direccionPrimerBloque = direccionPrimerBloque;
    }

    // Constructor
    public Archivo(String nombre, int tamaño, int direccionPrimerBloque, Color color) {
        this.nombre = nombre;
        this.tamaño = tamaño;
        this.direccionPrimerBloque = direccionPrimerBloque;
        this.color = color;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTamaño() {
        return tamaño;
    }

    public String getDirectorio() {
        return directorio;
    }

    public void setDirectorio(String directorio) {
        this.directorio = directorio;
    }

    public void setTamaño(int tamaño) {
        this.tamaño = tamaño;
    }

    public int getDireccionPrimerBloque() {
        return direccionPrimerBloque;
    }

    public void setDireccionPrimerBloque(int direccionPrimerBloque) {
        this.direccionPrimerBloque = direccionPrimerBloque;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    // Método para calcular y actualizar la asignación de bloques
    public void actualizarAsignacion(int nuevaDireccionPrimerBloque) {
        this.direccionPrimerBloque = nuevaDireccionPrimerBloque;
        // Aquí podrías agregar lógica adicional para actualizar la asignación de bloques
    }

    @Override
    public String toString() {
        return "Archivo{" +
                "nombre='" + nombre + '\'' +
                ", tamaño=" + tamaño +
                ", direccionPrimerBloque=" + direccionPrimerBloque +
                ", color='" + color + '\'' +
                '}';
    }
}
