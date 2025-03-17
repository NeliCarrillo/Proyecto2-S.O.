/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EDD;

import Objetos.Archivo;
import Objetos.Directorio;

/**
 *
 * @author nelsoncarrillo
 */
public class Lista<T> {
    private Nodo<T> primero; // Apuntador al primer nodo
    private Nodo<T> ultimo;  // Apuntador al último nodo

    // Constructor
    public Lista() {
        this.primero = null;
        this.ultimo = null;
    }
    
    public Nodo getPrimero(){
        return primero;
    }
    
    public Nodo getUltimo(){
        return ultimo;
    }
    
    public void setUltimo(Nodo l){
        this.ultimo=l;
    }

    // Método para verificar si la lista está vacía
    public boolean estaVacia() {
        return primero == null;
    }

    // Método para agregar un elemento al final de la lista
    public void agregar(T dato) {
        Nodo<T> nuevoNodo = new Nodo<>(dato); // Crear un nuevo nodo
        if (estaVacia()) {
            // Si la lista está vacía, el nuevo nodo es el primero y el último
            primero = nuevoNodo;
            ultimo = nuevoNodo;
        } else {
            // Si no está vacía, el nuevo nodo se agrega al final
            ultimo.setSiguiente(nuevoNodo);
            ultimo = nuevoNodo;
        }
    }

    // Método para eliminar el primer elemento de la lista
    public void eliminarPrimero() {
        if (estaVacia()) {
            System.out.println("La lista está vacía, no se puede eliminar.");
            return;
        }
        // Si solo hay un elemento, se vacía la lista
        if (primero == ultimo) {
            primero = null;
            ultimo = null;
        } else {
            // Si hay más de un elemento, se elimina el primero
            primero = primero.getSiguiente();
        }
    }

    // Método para mostrar todos los elementos de la lista
    public void mostrar() {
        if (estaVacia()) {
            System.out.println("La lista está vacía.");
            return;
        }
        Nodo<T> actual = primero;
        while (actual != null) {
            Directorio actus = (Directorio) actual.getDato();
            System.out.print( actus.getNombre()+ " -> ");
            actual = actual.getSiguiente();
        }
        System.out.println("null");
    }

    // Método para buscar un elemento en la lista
    public boolean buscar(T dato) {
        Nodo<T> actual = primero;
        while (actual != null) {
            if (actual.getDato().equals(dato)) {
                return true; // Elemento encontrado
            }
            actual = actual.getSiguiente();
        }
        return false; // Elemento no encontrado
    }

    // Método para obtener el tamaño de la lista
    public int tamaño() {
        int contador = 0;
        Nodo<T> actual = primero;
        while (actual != null) {
            contador++;
            actual = actual.getSiguiente();
        }
        return contador;
    }
    
    // Método para eliminar un archivo por su nombre
    public void eliminarArchivo(String nombre,String direct) {
        if (estaVacia()) {
            System.out.println("La lista está vacía, no se puede eliminar.");
            return;
        }

        Nodo<T> actual = primero;
        Nodo<T> anterior = null;

        // Recorrer la lista para encontrar el archivo con el nombre especificado
        while (actual != null) {
            Archivo archivo = (Archivo) actual.getDato();
            if (archivo.getNombre().equals(nombre)&&archivo.getDirectorio().equals(direct)) {
                // Si el archivo es el primero
                if (anterior == null) {
                    primero = actual.getSiguiente();
                    // Si el archivo es también el último
                    if (primero == null) {
                        ultimo = null;
                    }
                } else {
                    // Si el archivo no es el primero
                    anterior.setSiguiente(actual.getSiguiente());
                    // Si el archivo es el último
                    if (actual.getSiguiente() == null) {
                        ultimo = anterior;
                    }
                }
                System.out.println("Archivo eliminado: " + archivo.getNombre());
                return;
            }
            anterior = actual;
            actual = actual.getSiguiente();
        }

        System.out.println("Archivo no encontrado: " + nombre);
    }
    
   public void eliminarDirectorioDeLista(String nombre, String direct) {
        if (estaVacia()) {
            System.out.println("La lista está vacía, no se puede eliminar.");
            return;
        }

        Nodo<T> actual = primero;
        Nodo<T> anterior = null;

        // Recorrer la lista para encontrar el directorio con el nombre y padre especificados
        while (actual != null) {
            Directorio directorio = (Directorio) actual.getDato();
            if (directorio.getNombre().equals(nombre) && directorio.getPadre().equals(direct)) {
                // Eliminar todos los subdirectorios de este directorio
                eliminarSubdirectoriosRecursivo(directorio.getNombre());

                // Eliminar el directorio actual
                if (anterior == null) {
                    // Si es el primer nodo
                    primero = actual.getSiguiente();
                    if (primero == null) {
                        ultimo = null;
                    }
                } else {
                    // Si no es el primer nodo
                    anterior.setSiguiente(actual.getSiguiente());
                    if (actual.getSiguiente() == null) {
                        ultimo = anterior;
                    }
                }
                System.out.println("Directorio eliminado: " + directorio.getNombre());
                return;
            }
            anterior = actual;
            actual = actual.getSiguiente();
        }

        System.out.println("Directorio no encontrado: " + nombre);
    }

    private void eliminarSubdirectoriosRecursivo(String nombrePadre) {
        if (estaVacia()) {
            return;
        }

        Nodo<T> actual = primero;
        Nodo<T> anterior = null;

        // Recorrer la lista para encontrar y eliminar todos los subdirectorios
        while (actual != null) {
            Directorio directorio = (Directorio) actual.getDato();
            if (directorio.getPadre().equals(nombrePadre)) {
                // Eliminar recursivamente los subdirectorios de este directorio
                eliminarSubdirectoriosRecursivo(directorio.getNombre());

                // Eliminar el subdirectorio actual
                if (anterior == null) {
                    // Si es el primer nodo
                    primero = actual.getSiguiente();
                    if (primero == null) {
                        ultimo = null;
                    }
                } else {
                    // Si no es el primer nodo
                    anterior.setSiguiente(actual.getSiguiente());
                    if (actual.getSiguiente() == null) {
                        ultimo = anterior;
                    }
                }
                System.out.println("Subdirectorio eliminado: " + directorio.getNombre());
            } else {
                anterior = actual;
            }
            actual = actual.getSiguiente();
        }
    }
    
    public Archivo encontrarArchivo(String nombreArchivo, String nombreDirectorio) {
        if (estaVacia()) {
            System.out.println("La lista está vacía.");
            return null;
        }

        Nodo<T> actual = primero;

        // Recorrer la lista para encontrar el archivo
        while (actual != null) {
            Archivo archivo = (Archivo) actual.getDato();

            // Verificar si el nombre y el directorio coinciden
            if (archivo.getNombre().equals(nombreArchivo) && archivo.getDirectorio().equals(nombreDirectorio)) {
                System.out.println("Archivo encontrado: " + archivo.getNombre() + " en el directorio: " + archivo.getDirectorio());
                return archivo; // Devolver el archivo encontrado
            }

            // Avanzar al siguiente nodo
            actual = actual.getSiguiente();
        }

        System.out.println("Archivo no encontrado: " + nombreArchivo + " en el directorio: " + nombreDirectorio);
        return null; // Si no se encuentra el archivo, devolver null
    }
    public Directorio encontrarDirectorio(String nombreDirectorio) {
        if (estaVacia()) {
            System.out.println("La lista está vacía.");
            return null;
        }

        Nodo<T> actual = primero;

        // Recorrer la lista para encontrar el archivo
        while (actual != null) {
            Directorio archivo = (Directorio) actual.getDato();

            // Verificar si el nombre y el directorio coinciden
            if (archivo.getNombre().equals(nombreDirectorio) ) {
                return archivo; // Devolver el archivo encontrado
            }

            // Avanzar al siguiente nodo
            actual = actual.getSiguiente();
        }

        return null; // Si no se encuentra el archivo, devolver null
    }
    
    public void encontrarArchivoYCambiar(String nuevoNombre,String nombreArchivo, String nombreDirectorio) {
        if (estaVacia()) {
            System.out.println("La lista está vacía.");
            return;
        }

        Nodo<T> actual = primero;

        // Recorrer la lista para encontrar el archivo
        while (actual != null) {
            Archivo archivo = (Archivo) actual.getDato();

            // Verificar si el nombre y el directorio coinciden
            if (archivo.getNombre().equals(nombreArchivo) && archivo.getDirectorio().equals(nombreDirectorio)) {
                System.out.println("Archivo encontrado: " + archivo.getNombre() + " en el directorio: " + archivo.getDirectorio());
                archivo.setNombre(nuevoNombre);
                return; // Devolver el archivo encontrado
            }

            // Avanzar al siguiente nodo
            actual = actual.getSiguiente();
        }

        System.out.println("Archivo no encontrado: " + nombreArchivo + " en el directorio: " + nombreDirectorio);
        return; // Si no se encuentra el archivo, devolver null
    }
}