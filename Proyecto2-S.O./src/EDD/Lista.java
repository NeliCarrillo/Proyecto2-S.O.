/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EDD;

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
            System.out.print(actual.getDato() + " -> ");
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
}