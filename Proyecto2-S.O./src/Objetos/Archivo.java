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
    private transient Color color;
    private int colorRGB;
    private String directorio;
     private int r; // Componente rojo
    private int g; // Componente verde
    private int b; // Componente azul
    
    
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
        this.colorRGB = color.getRGB(); // Convertir el color a RGB
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
        return new Color(r, g, b);
    }
    
    public int getR() { return r; }
    public int getG() { return g; }
    public int getB() { return b; }
    
    public void setR(int rr) { this.r=rr; }
    public void setG(int gg) { this.g=gg; }
    public void setB(int bb) { this.b=bb; }

    public void setColor(Color color) {
        this.color = color;
        this.colorRGB = color.getRGB(); // Convertir el color a RGB
    }
    
    // Getters y setters
    public int getColorRGB() {
        return colorRGB;
    }

    public void setColorRGB(int colorRGB) {
        this.colorRGB = colorRGB;
        this.color = new Color(colorRGB); // Actualizar el objeto Color
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
