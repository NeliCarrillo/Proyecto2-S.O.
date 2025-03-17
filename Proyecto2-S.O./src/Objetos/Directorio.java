package Objetos;

import EDD.Lista;

/**
 *
 * @author nelsoncarrillo
 */
public class Directorio {
    private String padre;
    private String nombre;
    private Lista<Archivo> archivos;
    private Lista<Directorio> subdirectorios;

    public Directorio(String padre, String nombre) {
        this.padre = padre;
        this.nombre = nombre;
        this.archivos = new Lista<>();
        this.subdirectorios = new Lista<>();
    }
    
    public Directorio(String nombre) {
        this.padre = "FileSystem";
        this.nombre = nombre;
        this.archivos = new Lista<>();
        this.subdirectorios = new Lista<>();
    }

    public String getPadre() {
        return padre;
    }

    public void setPadre(String padre) {
        this.padre = padre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Lista<Archivo> getArchivos() {
        return archivos;
    }

    public void setArchivos(Lista<Archivo> archivos) {
        this.archivos = archivos;
    }

    public Lista<Directorio> getSubdirectorios() {
        return subdirectorios;
    }

    public void setSubdirectorios(Lista<Directorio> subdirectorios) {
        this.subdirectorios = subdirectorios;
    }
 
}
