/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Objetos;

import EDD.Lista;

/**
 *
 * @author nelsoncarrillo
 */
public class Contenedor {
    private Lista<Archivo> archivos;
    private Lista<Directorio> directorios;

    // Constructor
    public Contenedor() {
        this.archivos = new Lista<>();
        this.directorios = new Lista<>();
    }

    // Métodos para agregar archivos y directorios
    public void agregarArchivo(Archivo archivo) {
        archivos.agregar(archivo);
    }

    public void agregarDirectorio(Directorio directorio) {
        directorios.agregar(directorio);
    }

    // Getters y setters
    public Lista<Archivo> getArchivos() { return archivos; }
    public Lista<Directorio> getDirectorios() { return directorios; }

    public void setArchivos(Lista<Archivo> archivos) { this.archivos = archivos; }
    public void setDirectorios(Lista<Directorio> directorios) { this.directorios = directorios; }
}
