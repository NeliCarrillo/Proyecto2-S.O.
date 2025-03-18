/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUIs;

import EDD.Lista;
import EDD.Nodo;
import Objetos.Archivo;
import Objetos.ColorCellRenderer;
import Objetos.Contenedor;
import Objetos.Directorio;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author nelsoncarrillo
 */
public final class FileSystemSimulator extends javax.swing.JFrame {
    
    
    private String mode="Administrador";
    private DefaultTreeModel model;
    private Lista directorios = new Lista();
    private Lista archivos = new Lista();


    public String getMode() {
        return mode;
    }
    
    public Lista getDirectorios() {
        return directorios;
    }
    
    public Lista getArchivos() {
        return archivos;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * Creates new form Dos
     */
    public FileSystemSimulator() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        initComponents();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        LoadRoot("FileSystem");
        this.Tree.setEditable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Llamar al método guardarEstado antes de cerrar
                guardarEstado();

                // Cerrar la aplicación
                dispose(); // Cierra la ventana
                System.exit(0); // Termina la aplicación
            }
        });
    }
    
    public void LoadRoot(String n){
        Directorio rwy = new Directorio(n);
        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode (n);
        DefaultMutableTreeNode aux = new DefaultMutableTreeNode ("");
        raiz.add(aux);
        model = (DefaultTreeModel)Tree.getModel();
        model.setRoot(raiz);
        Tree.setModel(model);
        directorios.agregar(rwy);
    }
    
    public void nuevoArchivo(String nombreArchivo, String nombrePadre,int tamano){
        boolean seAgrego = this.anadirArchivoJTree(nombrePadre, nombreArchivo,tamano);
        Archivo nuvo = new Archivo(nombreArchivo,tamano,nombrePadre);
        this.anadirArchivoJTable(seAgrego,nuvo );
    }
    
    /**
    * Método para añadir una fila a un JTable con un archivo y un color aleatorio.Si el parámetro `re` es verdadero, se añade una nueva fila al JTable con el nombre del archivo
 y un color aleatorio en la columna correspondiente.
    *
    * @param re           Indica si se debe añadir el archivo a la tabla. Si es `true`, se añade la fila.
     * @param nue
    */ 
    public void anadirArchivoJTable(boolean re, Archivo nue){
        // Generar un color aleatorio
        Random rand = new Random();
        int r = rand.nextInt(256); // Componente rojo (0-255)
        int g = rand.nextInt(256); // Componente verde (0-255)
        int b = rand.nextInt(256); // Componente azul (0-255)
        while(r==255 && g==255 && b ==255){
            r = rand.nextInt(256); // Componente rojo (0-255)
            g = rand.nextInt(256); // Componente verde (0-255)
            b = rand.nextInt(256); // Componente azul (0-255)
        }
        Color colorArchivo = new Color(r, g, b);
        int se=100;
        if(re){
            se = this.addFile(nue.getTamaño(), r, g, b);
        }
        if (re&&(se!=100)) {
            nue.setDireccionPrimerBloque(se);
            DefaultTableModel modeloTabla = (DefaultTableModel) Tabla.getModel();
            Tabla.getColumnModel().getColumn(3).setCellRenderer(new ColorCellRenderer());

            // Crear un arreglo con los datos de la nueva fila
            Object[] nuevaFila = {nue.getNombre(),nue.getDireccionPrimerBloque(), nue.getTamaño(), colorArchivo};

            // Agregar la fila al modelo de la tabla
            modeloTabla.addRow(nuevaFila);
            this.archivos.agregar(nue);
            nue.setR(r);
            nue.setG(g);
            nue.setB(b);
            nue.setColor(colorArchivo);
        }
    }
    
    
    public void anadirArchivoJTableConColor(boolean re, Archivo nue,int r,int g,int b){
        Color colorArchivo = new Color(r,g,b);
        int se=100;
        if(re){
            se = this.addFile(nue.getTamaño(), r, g, b);
        }
        if (re&&(se!=100)) {
            nue.setDireccionPrimerBloque(se);
            DefaultTableModel modeloTabla = (DefaultTableModel) Tabla.getModel();
            Tabla.getColumnModel().getColumn(3).setCellRenderer(new ColorCellRenderer());

            // Crear un arreglo con los datos de la nueva fila
            Object[] nuevaFila = {nue.getNombre(),nue.getDireccionPrimerBloque(), nue.getTamaño(), colorArchivo};

            // Agregar la fila al modelo de la tabla
            modeloTabla.addRow(nuevaFila);
            this.archivos.agregar(nue);
            nue.setColor(colorArchivo);
        }
    }
    
    public void eliminarArchivoJTable(Archivo archivo) {
        DefaultTableModel modeloTabla = (DefaultTableModel) Tabla.getModel();

        // Recorrer las filas de la tabla
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            String nombre = (String) modeloTabla.getValueAt(i, 0); // Columna del nombre
            int tamaño = (int) modeloTabla.getValueAt(i, 2); // Columna del tamaño
            Color color = (Color) modeloTabla.getValueAt(i, 3); // Columna del color

            // Comparar los atributos del archivo con los de la fila actual
            if (nombre.equals(archivo.getNombre()) && 
                tamaño == archivo.getTamaño() && 
                color.equals(archivo.getColor())) {

                // Eliminar la fila del modelo de la tabla
                modeloTabla.removeRow(i);

                break; // Salir del bucle una vez eliminado el archivo
            }
        }
    }
    
    public void eliminarDirectorio(String nombre,String padre){
        
        // Eliminar el nodo correspondiente del árbol (JTree)
         eliminarNodoDelArbol(padre, nombre);
        if(!archivos.estaVacia()){
            Nodo actual = archivos.getPrimero();
            while(actual!=null){
                Archivo este = (Archivo)actual.getDato();
                String directorio = este.getDirectorio();
                Directorio esperado = this.directorios.encontrarDirectorio(nombre,padre);
                if(nombre.equals(este.getDirectorio())&&padre.equals(esperado.getPadre())){
                    this.eliminarArchivo(este.getNombre(), directorio);
                }
                actual=actual.getSiguiente();
            }
        }
        this.directorios.eliminarDirectorioDeLista(nombre, padre);
    }
    
    /**
    * Método para añadir un archivo (hoja) a un nodo padre específico.
    * Los hijos de un mismo padre no pueden tener el mismo nombre.
    *
    * @param nombrePadre El nombre del nodo padre al que se añadirá el archivo.
    * @param nombreArchivo El nombre del archivo que se añadirá como hoja.
    * @return true si el archivo se añadió correctamente, false si ya existe un hijo con el mismo nombre.
    */
   public boolean anadirArchivoJTree(String nombrePadre, String nombreArchivo, int fileSize) {
        
       
       DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
       DefaultMutableTreeNode parentNode = findNodeJTree(root, nombrePadre);

       if (parentNode != null) {
           // Verificar si ya existe un hijo con el mismo nombre
           for (int i = 0; i < parentNode.getChildCount(); i++) {
               DefaultMutableTreeNode child = (DefaultMutableTreeNode) parentNode.getChildAt(i);
               if (child.getUserObject().equals(nombreArchivo)) {
                   System.out.println("Error: Ya existe un archivo con el nombre '" + nombreArchivo + "' en el nodo padre '" + nombrePadre + "'.");
                   return false; // No se puede añadir, ya existe un hijo con ese nombre
               }
           }
           if (this.findFreePanels(fileSize)==-1) {
                JOptionPane.showMessageDialog(this, "No hay suficiente espacio para el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
           }
           
           

           // Si no existe un hijo con el mismo nombre, añadir el nuevo archivo
           DefaultMutableTreeNode newFileNode = new DefaultMutableTreeNode(nombreArchivo);
           parentNode.add(newFileNode);
           model.reload(parentNode); // Actualiza el modelo para reflejar los cambios
           this.eliminarArchivoJTree(nombrePadre,"");
           System.out.println("Archivo '" + nombreArchivo + "' añadido correctamente al nodo padre '" + nombrePadre + "'.");
           return true; // Archivo añadido correctamente
       } else {
           System.out.println("Error: Nodo padre no encontrado: " + nombrePadre);
           return false; // Nodo padre no existe
       }
   }
   
   /**
    * Método para añadir un nuevo directorio (nodo) con un hijo predeterminado vacío.
    *
    * @param nombrePadre El nombre del nodo padre al que se añadirá el nuevo directorio.
    * @param nombreDirectorio El nombre del nuevo directorio que se añadirá.
    */
   public boolean anadirDirectorio(String nombrePadre, String nombreDirectorio) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        DefaultMutableTreeNode parentNode = findNodeJTree(root, nombrePadre);
        Directorio nuevv = new Directorio(nombrePadre, nombreDirectorio);

        if (parentNode != null) {
            // Verificar si ya existe un nodo con el mismo nombre en el mismo nivel
            boolean nombreRepetido = false;
            for (int i = 0; i < parentNode.getChildCount(); i++) {
                DefaultMutableTreeNode hijo = (DefaultMutableTreeNode) parentNode.getChildAt(i);
                if (hijo.getUserObject().equals(nombreDirectorio)) {
                    nombreRepetido = true;
                    return false;
                }
            }

            if (!nombreRepetido) {
                // Crear el nuevo directorio (nodo) con un hijo predeterminado vacío
                DefaultMutableTreeNode nuevoDirectorio = new DefaultMutableTreeNode(nombreDirectorio);
                DefaultMutableTreeNode hijoPredeterminado = new DefaultMutableTreeNode("");
                nuevoDirectorio.add(hijoPredeterminado);

                // Añadir el nuevo directorio como hijo del nodo padre
                parentNode.add(nuevoDirectorio);
                model.reload(parentNode); // Actualizar el modelo para reflejar los cambios
                directorios.agregar(nuevv); // Asume que Lista tiene un método agregar
                System.out.println("Directorio '" + nombreDirectorio + "' añadido correctamente al nodo padre '" + nombrePadre + "'.");
                this.eliminarArchivoJTree(nombrePadre,"");
                return true;
            } else {
                System.out.println("Error: Ya existe un directorio con el nombre '" + nombreDirectorio + "' en el nodo padre '" + nombrePadre + "'.");
                return false;
            }
        } else {
            System.out.println("Error: No se encontró el nodo padre '" + nombrePadre + "'.");
            return false;
        }
    }
   
    private void eliminarNodoDelArbol(String nombrePadre, String nombreDirectorio) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        DefaultMutableTreeNode parentNode = findNodeJTree(root, nombrePadre);

        if (parentNode != null) {
            // Buscar el nodo hijo que coincide con el nombre del directorio a eliminar
            for (int i = 0; i < parentNode.getChildCount(); i++) {
                DefaultMutableTreeNode hijo = (DefaultMutableTreeNode) parentNode.getChildAt(i);
                if (hijo.getUserObject().equals(nombreDirectorio)) {
                    // Eliminar el nodo del árbol
                    parentNode.remove(hijo);
                    model.reload(parentNode); // Actualizar el modelo para reflejar los cambios
                    System.out.println("Directorio '" + nombreDirectorio + "' eliminado del árbol.");

                    // Verificar si el padre se quedó sin hijos
                    if (parentNode.getChildCount() == 0) {
                        // Agregar una hoja predeterminada vacía
                        DefaultMutableTreeNode hojaPredeterminada = new DefaultMutableTreeNode("");
                        parentNode.add(hojaPredeterminada);
                        model.reload(parentNode); // Actualizar el modelo nuevamente
                        System.out.println("Se agregó una hoja predeterminada al nodo padre '" + nombrePadre + "'.");
                    }

                    return;
                }
            }
            System.out.println("Error: No se encontró el directorio '" + nombreDirectorio + "' en el nodo padre '" + nombrePadre + "'.");
        } else {
            System.out.println("Error: No se encontró el nodo padre '" + nombrePadre + "'.");
        }
    }
    
    public void editarArchivo(Archivo archivo, String nuevoNombre) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        DefaultMutableTreeNode parentNode = findNodeJTree(root, archivo.getDirectorio());

        if (parentNode != null) {
            // Verificar si ya existe un hijo con el nuevo nombre
            boolean nombreRepetido = false;
            for (int i = 0; i < parentNode.getChildCount(); i++) {
                DefaultMutableTreeNode hijo = (DefaultMutableTreeNode) parentNode.getChildAt(i);
                if (hijo.getUserObject().equals(nuevoNombre)) {
                    nombreRepetido = true;
                    break;
                }
            }

            if (nombreRepetido) {
                System.out.println("Error: Ya existe un archivo con el nombre '" + nuevoNombre + "' en el nodo padre '" + archivo.getDirectorio() + "'.");
                return; // Salir del método si el nombre ya existe
            }

            // Buscar el nodo hijo que coincide con el nombre del archivo a editar
            for (int i = 0; i < parentNode.getChildCount(); i++) {
                DefaultMutableTreeNode hijo = (DefaultMutableTreeNode) parentNode.getChildAt(i);
                if (hijo.getUserObject().equals(archivo.getNombre())) {
                    // Cambiar el nombre del nodo
                    hijo.setUserObject(nuevoNombre);
                    model.reload(parentNode); // Actualizar el modelo para reflejar los cambios
                    System.out.println("Nombre del archivo cambiado a '" + nuevoNombre + "' en el árbol.");
                    break;
                }
            }
        } else {
            System.out.println("Error: No se encontró el nodo padre '" + archivo.getDirectorio() + "'.");
        }

        DefaultTableModel modeloTabla = (DefaultTableModel) Tabla.getModel();

        // Recorrer las filas de la tabla
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            String nombre = (String) modeloTabla.getValueAt(i, 0); // Columna del nombre
            int tamaño = (int) modeloTabla.getValueAt(i, 2); // Columna del tamaño
            Color color = (Color) modeloTabla.getValueAt(i, 3); // Columna del color

            // Comparar los atributos del archivo con los de la fila actual
            if (nombre.equals(archivo.getNombre()) && 
                tamaño == archivo.getTamaño() && 
                color.equals(archivo.getColor())) {

                // Actualizar el nombre en la fila del modelo de la tabla
                modeloTabla.setValueAt(nuevoNombre, i, 0);

                break; // Salir del bucle una vez editado el archivo
            }
        }
        this.archivos.encontrarArchivoYCambiar(nuevoNombre, archivo.getNombre(), archivo.getDirectorio());
    }
    
    public void editarDirectorio(String nombrePadre, String nombreViejo, String nuevoNombre) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        DefaultMutableTreeNode parentNode = findNodeJTree(root, nombrePadre);

        if (parentNode != null) {
            // Verificar si ya existe un hijo con el nuevo nombre
            boolean nombreRepetido = false;
            for (int i = 0; i < parentNode.getChildCount(); i++) {
                DefaultMutableTreeNode hijo = (DefaultMutableTreeNode) parentNode.getChildAt(i);
                if (hijo.getUserObject().equals(nuevoNombre)) {
                    nombreRepetido = true;
                    break;
                }
            }

            if (nombreRepetido) {
                System.out.println("Error: Ya existe un directorio con el nombre '" + nuevoNombre + "' en el nodo padre '" + nombrePadre + "'.");
                return; // Salir del método si el nombre ya existe
            }

            // Buscar el nodo hijo que coincide con el nombre viejo del directorio
            for (int i = 0; i < parentNode.getChildCount(); i++) {
                DefaultMutableTreeNode hijo = (DefaultMutableTreeNode) parentNode.getChildAt(i);
                if (hijo.getUserObject().equals(nombreViejo)) {
                    // Cambiar el nombre del nodo
                    hijo.setUserObject(nuevoNombre);
                    this.directorios.encontrarDirectorioYCambiar(nuevoNombre, nombreViejo, nombrePadre);
                    model.reload(parentNode); // Actualizar el modelo para reflejar los cambios
                    System.out.println("Nombre del directorio cambiado de '" + nombreViejo + "' a '" + nuevoNombre + "'.");
                    return;
                }
            }
            System.out.println("Error: No se encontró el directorio '" + nombreViejo + "' en el nodo padre '" + nombrePadre + "'.");
        } else {
            System.out.println("Error: No se encontró el nodo padre '" + nombrePadre + "'.");
        }
    }

   /**
    * Método para eliminar un archivo (hoja) del árbol, verificando que pertenezca al nodo padre especificado.
    *
    * @param nombrePadre El nombre del nodo padre del archivo que se desea eliminar.
    * @param nombreArchivo El nombre del archivo que se desea eliminar.
    */
   public void eliminarArchivoJTree(String nombrePadre, String nombreArchivo) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        DefaultMutableTreeNode parentNode = findNodeJTree(root, nombrePadre);

        if (parentNode != null) {
            // Buscar el archivo (hoja) dentro de los hijos del nodo padre
            DefaultMutableTreeNode nodeToDelete = null;
            for (int i = 0; i < parentNode.getChildCount(); i++) {
                DefaultMutableTreeNode child = (DefaultMutableTreeNode) parentNode.getChildAt(i);
                if (child.getUserObject().equals(nombreArchivo) && child.isLeaf()) {
                    nodeToDelete = child;
                    break;
                }
            }

            if (nodeToDelete != null) {
                // Verificar si el nodo padre solo tiene un hijo
                if (parentNode.getChildCount() == 1) {
                    // Agregar un hijo vacío al nodo padre
                    DefaultMutableTreeNode emptyNode = new DefaultMutableTreeNode("");
                    parentNode.add(emptyNode);
                }

                // Eliminar el nodo del árbol
                parentNode.remove(nodeToDelete);
                model.reload(parentNode); // Actualizar el modelo para reflejar los cambios
                System.out.println("Archivo '" + nombreArchivo + "' eliminado correctamente del nodo padre '" + nombrePadre + "'.");
            } else {
                System.out.println("Error: No se encontró el archivo '" + nombreArchivo + "' como hoja del nodo padre '" + nombrePadre + "'.");
            }
        } else {
            System.out.println("Error: No se encontró el nodo padre '" + nombrePadre + "'.");
        }
    }
   
    /**
     * Método auxiliar para encontrar un nodo en el árbol por su nombre.
     *
     * @param root El nodo raíz desde donde comenzar la búsqueda.
     * @param nombre El nombre del nodo a buscar.
     * @return El nodo encontrado o null si no se encuentra.
     */
    private DefaultMutableTreeNode findNodeJTree(DefaultMutableTreeNode root, String nombre) {
        if (root.getUserObject().equals(nombre)) {
            return root;
        }

        for (int i = 0; i < root.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) root.getChildAt(i);
            DefaultMutableTreeNode foundNode = findNodeJTree(child, nombre);
            if (foundNode != null) {
                return foundNode;
            }
        }

        return null;
    }
    
    public int addFile(int fileSize, int r, int g, int b) {
        if (fileSize > 80) {
            JOptionPane.showMessageDialog(this, "El tamaño del archivo excede el límite de 80 paneles.", "Error", JOptionPane.ERROR_MESSAGE);
            return 100; // Código de error
        }

        Color color = new Color(r, g, b);

        // Buscar paneles libres (con fondo blanco)
        int startPanel = findFreePanels(fileSize);
        if (startPanel != -1) {
            // Si se encontraron paneles libres, usarlos
            for (int i = startPanel; i < startPanel + fileSize; i++) {
                setPanelColor(i, color);
            }
            return startPanel;
        } else {
            JOptionPane.showMessageDialog(this, "No hay suficiente espacio para el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
            return 100; // Código de error
        }
    }
    
    // Método para buscar paneles libres consecutivos
    private int findFreePanels(int fileSize) {
        int consecutiveFreePanels = 0;
        for (int i = 1; i < 81; i++) {
            if (isPanelFree(i)) {
                consecutiveFreePanels++;
                if (consecutiveFreePanels == fileSize) {
                    System.out.println(i - fileSize + 1);
                    return i - fileSize + 1; // Devuelve el índice inicial del bloque libre
                }
            } else {
                consecutiveFreePanels = 0;
            }
        }
        return -1; // No se encontró un bloque de paneles libres
    }
    
    // Método para verificar si un panel está libre (fondo blanco)
    private boolean isPanelFree(int panelIndex) {
        try {
            Field field = this.getClass().getDeclaredField("Panel" + panelIndex);
            field.setAccessible(true);
            JPanel panel = (JPanel) field.get(this);
            return panel.getBackground().equals(new Color(255, 255, 255)); // Fondo blanco
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void eliminarArchivo(String nombreArchivo,String nombreDirectorio) {
        Archivo archivo = this.archivos.encontrarArchivo(nombreArchivo, nombreDirectorio);
        this.archivos.eliminarArchivo(nombreArchivo,nombreDirectorio);
        // Obtener la dirección del primer bloque y el tamaño del archivo
        int direccionPrimerBloque = archivo.getDireccionPrimerBloque();
        int tamano = archivo.getTamaño();

        // Cambiar el color de los paneles asociados al archivo a blanco (255, 255, 255)
        for (int i = direccionPrimerBloque; i < direccionPrimerBloque + tamano; i++) {
            setPanelColor(i, new Color(255, 255, 255)); // Fondo blanco
        }
        this.eliminarArchivoJTree(nombreDirectorio, nombreArchivo);
        this.eliminarArchivoJTable(archivo);
        System.out.println("Archivo eliminado y paneles liberados: " + archivo.getNombre());
    }
    
    // Método para cambiar el color de un panel
    private void setPanelColor(int panelIndex, Color color) {
        try {
            Field field = this.getClass().getDeclaredField("Panel" + panelIndex);
            field.setAccessible(true);
            JPanel panel = (JPanel) field.get(this);
            panel.setBackground(color);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetFileSystem() {
        // Reiniciar todos los JPanels a blanco y resetear el contador
        for (int i = 1; i <= 80; i++) {
            try {
                Field field = this.getClass().getDeclaredField("jPanel" + i);
                field.setAccessible(true);
                JPanel panel = (JPanel) field.get(this);
                panel.setBackground(Color.WHITE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Repintar el JFrame para reflejar los cambios
        this.revalidate();
        this.repaint();
    }
    
    public void guardarEstado() {
        // Crear una instancia de Contenedor y llenarla con los datos actuales
        Contenedor contenedor = new Contenedor();
        contenedor.setArchivos(this.archivos); // Asume que this.archivos es una Lista<Archivo>
        contenedor.setDirectorios(this.directorios); // Asume que this.directorios es una Lista<Directorio>

        // Serializar el contenedor a JSON
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("test/simulacion.json")) {
            gson.toJson(contenedor, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void cargarEstado() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader("test/simulacion.json")) {
            // Deserializar el JSON a una instancia de Contenedor
            Contenedor contenedor = gson.fromJson(reader, Contenedor.class);

            // Limpiar la interfaz gráfica y las listas internas antes de cargar el estado
            DefaultTableModel modeloTabla = (DefaultTableModel) Tabla.getModel();
            modeloTabla.setRowCount(0); // Limpiar la tabla
            DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
            root.removeAllChildren(); // Limpiar el JTree
            model.reload(); // Actualizar el modelo del JTree

            // Recorrer los directorios y agregarlos al JTree y a la lista interna
            Nodo<Directorio> actualDirectorio = contenedor.getDirectorios().getPrimero();
            while (actualDirectorio != null) {
                Directorio directorio = actualDirectorio.getDato();
                if(!directorio.getNombre().equals("FileSystem")){
                                    this.anadirDirectorio(directorio.getPadre(), directorio.getNombre());
                }
                actualDirectorio = actualDirectorio.getSiguiente();
            }

            // Recorrer los archivos y agregarlos al JTree, JTable y a la lista interna
            Nodo<Archivo> actualArchivo = contenedor.getArchivos().getPrimero();
            while (actualArchivo != null) {
                Archivo archivo = actualArchivo.getDato();

                // Agregar el archivo al JTree
                boolean seAgrego = this.anadirArchivoJTree(archivo.getDirectorio(), archivo.getNombre(), archivo.getTamaño());

                // Agregar el archivo al JTable y a la lista interna
                if (seAgrego) {
                    this.anadirArchivoJTableConColor(true, archivo, archivo.getR(),archivo.getG(),archivo.getB());
                }

                actualArchivo = actualArchivo.getSiguiente();
            }

            System.out.println("Estado cargado correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1123123 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();
        Panel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Panel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        Panel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        Panel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        Panel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        Panel11 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        Panel12 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        Panel13 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        Panel14 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        Panel15 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        Panel21 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        Panel22 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        Panel23 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        Panel24 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        Panel25 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        Panel31 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        Panel32 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        Panel33 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        Panel34 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        Panel35 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        Panel61 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        Panel42 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        Panel55 = new javax.swing.JPanel();
        jLabel58 = new javax.swing.JLabel();
        Panel41 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        Panel43 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        Panel72 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        Panel62 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        Panel73 = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        Panel64 = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        Panel52 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        Panel71 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        Panel65 = new javax.swing.JPanel();
        jLabel59 = new javax.swing.JLabel();
        Panel53 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        Panel51 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        Panel74 = new javax.swing.JPanel();
        jLabel56 = new javax.swing.JLabel();
        Panel54 = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        Panel75 = new javax.swing.JPanel();
        jLabel60 = new javax.swing.JLabel();
        Panel44 = new javax.swing.JPanel();
        jLabel53 = new javax.swing.JLabel();
        Panel63 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        Panel45 = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        Panel27 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        Panel36 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        Panel48 = new javax.swing.JPanel();
        jLabel66 = new javax.swing.JLabel();
        Panel18 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        Panel76 = new javax.swing.JPanel();
        jLabel64 = new javax.swing.JLabel();
        Panel10 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        Panel67 = new javax.swing.JPanel();
        jLabel70 = new javax.swing.JLabel();
        Panel39 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        Panel59 = new javax.swing.JPanel();
        jLabel75 = new javax.swing.JLabel();
        Panel56 = new javax.swing.JPanel();
        jLabel62 = new javax.swing.JLabel();
        Panel9 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        Panel16 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        Panel78 = new javax.swing.JPanel();
        jLabel74 = new javax.swing.JLabel();
        Panel19 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        Panel46 = new javax.swing.JPanel();
        jLabel61 = new javax.swing.JLabel();
        Panel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        Panel49 = new javax.swing.JPanel();
        jLabel67 = new javax.swing.JLabel();
        Panel50 = new javax.swing.JPanel();
        jLabel68 = new javax.swing.JLabel();
        Panel47 = new javax.swing.JPanel();
        jLabel65 = new javax.swing.JLabel();
        Panel30 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        Panel79 = new javax.swing.JPanel();
        jLabel77 = new javax.swing.JLabel();
        Panel60 = new javax.swing.JPanel();
        jLabel78 = new javax.swing.JLabel();
        Panel58 = new javax.swing.JPanel();
        jLabel72 = new javax.swing.JLabel();
        Panel66 = new javax.swing.JPanel();
        jLabel63 = new javax.swing.JLabel();
        Panel26 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        Panel29 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        Panel69 = new javax.swing.JPanel();
        jLabel76 = new javax.swing.JLabel();
        Panel77 = new javax.swing.JPanel();
        jLabel71 = new javax.swing.JLabel();
        Panel80 = new javax.swing.JPanel();
        jLabel80 = new javax.swing.JLabel();
        Panel17 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        Panel28 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        Panel68 = new javax.swing.JPanel();
        jLabel73 = new javax.swing.JLabel();
        Panel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        Panel38 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        Panel20 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        Panel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        Panel37 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        Panel40 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        Panel57 = new javax.swing.JPanel();
        jLabel69 = new javax.swing.JLabel();
        Panel70 = new javax.swing.JPanel();
        jLabel79 = new javax.swing.JLabel();
        createFile = new javax.swing.JButton();
        createDir = new javax.swing.JButton();
        change = new javax.swing.JButton();
        modo = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        Tree = new javax.swing.JTree();
        deleteFile = new javax.swing.JButton();
        deleteDir = new javax.swing.JButton();
        editFile = new javax.swing.JButton();
        editDir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1123123.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Tabla.setBackground(new java.awt.Color(204, 255, 255));
        Tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Bloque Inicial", "Longitud", "Color"
            }
        ));
        jScrollPane2.setViewportView(Tabla);

        jPanel1123123.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 328, 380, 310));

        Panel1.setBackground(new java.awt.Color(255, 255, 255));
        Panel1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("1");

        javax.swing.GroupLayout Panel1Layout = new javax.swing.GroupLayout(Panel1);
        Panel1.setLayout(Panel1Layout);
        Panel1Layout.setHorizontalGroup(
            Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        Panel1Layout.setVerticalGroup(
            Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 10, 70, 67));

        Panel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setText("2");

        javax.swing.GroupLayout Panel2Layout = new javax.swing.GroupLayout(Panel2);
        Panel2.setLayout(Panel2Layout);
        Panel2Layout.setHorizontalGroup(
            Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        Panel2Layout.setVerticalGroup(
            Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 10, 70, 67));

        Panel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setText("3");

        javax.swing.GroupLayout Panel3Layout = new javax.swing.GroupLayout(Panel3);
        Panel3.setLayout(Panel3Layout);
        Panel3Layout.setHorizontalGroup(
            Panel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        Panel3Layout.setVerticalGroup(
            Panel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel3Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 10, 70, 67));

        Panel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setText("4");

        javax.swing.GroupLayout Panel4Layout = new javax.swing.GroupLayout(Panel4);
        Panel4.setLayout(Panel4Layout);
        Panel4Layout.setHorizontalGroup(
            Panel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        Panel4Layout.setVerticalGroup(
            Panel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel4Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 10, 70, 67));

        Panel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setText("5");

        javax.swing.GroupLayout Panel5Layout = new javax.swing.GroupLayout(Panel5);
        Panel5.setLayout(Panel5Layout);
        Panel5Layout.setHorizontalGroup(
            Panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        Panel5Layout.setVerticalGroup(
            Panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel5Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 10, 70, 67));

        Panel11.setBackground(new java.awt.Color(255, 255, 255));
        Panel11.setForeground(new java.awt.Color(255, 255, 255));

        jLabel10.setText("11");

        javax.swing.GroupLayout Panel11Layout = new javax.swing.GroupLayout(Panel11);
        Panel11.setLayout(Panel11Layout);
        Panel11Layout.setHorizontalGroup(
            Panel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel11Layout.setVerticalGroup(
            Panel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel11Layout.createSequentialGroup()
                .addComponent(jLabel10)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(401, 91, 70, 67));

        Panel12.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setText("12");

        javax.swing.GroupLayout Panel12Layout = new javax.swing.GroupLayout(Panel12);
        Panel12.setLayout(Panel12Layout);
        Panel12Layout.setHorizontalGroup(
            Panel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel12Layout.setVerticalGroup(
            Panel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel12Layout.createSequentialGroup()
                .addComponent(jLabel12)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 90, 70, 67));

        Panel13.setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setText("13");

        javax.swing.GroupLayout Panel13Layout = new javax.swing.GroupLayout(Panel13);
        Panel13.setLayout(Panel13Layout);
        Panel13Layout.setHorizontalGroup(
            Panel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel13Layout.setVerticalGroup(
            Panel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel13Layout.createSequentialGroup()
                .addComponent(jLabel13)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 90, 70, 67));

        Panel14.setBackground(new java.awt.Color(255, 255, 255));

        jLabel14.setText("14");

        javax.swing.GroupLayout Panel14Layout = new javax.swing.GroupLayout(Panel14);
        Panel14.setLayout(Panel14Layout);
        Panel14Layout.setHorizontalGroup(
            Panel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel14Layout.setVerticalGroup(
            Panel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel14Layout.createSequentialGroup()
                .addComponent(jLabel14)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 90, 70, 67));

        Panel15.setBackground(new java.awt.Color(255, 255, 255));

        jLabel15.setText("15");

        javax.swing.GroupLayout Panel15Layout = new javax.swing.GroupLayout(Panel15);
        Panel15.setLayout(Panel15Layout);
        Panel15Layout.setHorizontalGroup(
            Panel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel15Layout.setVerticalGroup(
            Panel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel15Layout.createSequentialGroup()
                .addComponent(jLabel15)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 90, 70, 67));

        Panel21.setBackground(new java.awt.Color(255, 255, 255));

        jLabel21.setText("21");

        javax.swing.GroupLayout Panel21Layout = new javax.swing.GroupLayout(Panel21);
        Panel21.setLayout(Panel21Layout);
        Panel21Layout.setHorizontalGroup(
            Panel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel21Layout.setVerticalGroup(
            Panel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel21Layout.createSequentialGroup()
                .addComponent(jLabel21)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 170, 70, 67));

        Panel22.setBackground(new java.awt.Color(255, 255, 255));

        jLabel22.setText("22");

        javax.swing.GroupLayout Panel22Layout = new javax.swing.GroupLayout(Panel22);
        Panel22.setLayout(Panel22Layout);
        Panel22Layout.setHorizontalGroup(
            Panel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel22Layout.setVerticalGroup(
            Panel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel22Layout.createSequentialGroup()
                .addComponent(jLabel22)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 170, 70, 67));

        Panel23.setBackground(new java.awt.Color(255, 255, 255));

        jLabel23.setText("23");

        javax.swing.GroupLayout Panel23Layout = new javax.swing.GroupLayout(Panel23);
        Panel23.setLayout(Panel23Layout);
        Panel23Layout.setHorizontalGroup(
            Panel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel23Layout.setVerticalGroup(
            Panel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel23Layout.createSequentialGroup()
                .addComponent(jLabel23)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 170, 70, 67));

        Panel24.setBackground(new java.awt.Color(255, 255, 255));

        jLabel24.setText("24");

        javax.swing.GroupLayout Panel24Layout = new javax.swing.GroupLayout(Panel24);
        Panel24.setLayout(Panel24Layout);
        Panel24Layout.setHorizontalGroup(
            Panel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel24Layout.setVerticalGroup(
            Panel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel24Layout.createSequentialGroup()
                .addComponent(jLabel24)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 170, 70, 67));

        Panel25.setBackground(new java.awt.Color(255, 255, 255));

        jLabel25.setText("25");

        javax.swing.GroupLayout Panel25Layout = new javax.swing.GroupLayout(Panel25);
        Panel25.setLayout(Panel25Layout);
        Panel25Layout.setHorizontalGroup(
            Panel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel25Layout.setVerticalGroup(
            Panel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel25Layout.createSequentialGroup()
                .addComponent(jLabel25)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 170, 70, 67));

        Panel31.setBackground(new java.awt.Color(255, 255, 255));

        jLabel31.setText("31");

        javax.swing.GroupLayout Panel31Layout = new javax.swing.GroupLayout(Panel31);
        Panel31.setLayout(Panel31Layout);
        Panel31Layout.setHorizontalGroup(
            Panel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel31)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel31Layout.setVerticalGroup(
            Panel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel31Layout.createSequentialGroup()
                .addComponent(jLabel31)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 250, 70, 67));

        Panel32.setBackground(new java.awt.Color(255, 255, 255));

        jLabel32.setText("32");

        javax.swing.GroupLayout Panel32Layout = new javax.swing.GroupLayout(Panel32);
        Panel32.setLayout(Panel32Layout);
        Panel32Layout.setHorizontalGroup(
            Panel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel32Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel32Layout.setVerticalGroup(
            Panel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel32Layout.createSequentialGroup()
                .addComponent(jLabel32)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 250, 70, 67));

        Panel33.setBackground(new java.awt.Color(255, 255, 255));

        jLabel33.setText("33");

        javax.swing.GroupLayout Panel33Layout = new javax.swing.GroupLayout(Panel33);
        Panel33.setLayout(Panel33Layout);
        Panel33Layout.setHorizontalGroup(
            Panel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel33Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel33Layout.setVerticalGroup(
            Panel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel33Layout.createSequentialGroup()
                .addComponent(jLabel33)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 250, 70, 67));

        Panel34.setBackground(new java.awt.Color(255, 255, 255));

        jLabel34.setText("34");

        javax.swing.GroupLayout Panel34Layout = new javax.swing.GroupLayout(Panel34);
        Panel34.setLayout(Panel34Layout);
        Panel34Layout.setHorizontalGroup(
            Panel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel34Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel34)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel34Layout.setVerticalGroup(
            Panel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel34Layout.createSequentialGroup()
                .addComponent(jLabel34)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 250, 70, 67));

        Panel35.setBackground(new java.awt.Color(255, 255, 255));

        jLabel35.setText("35");

        javax.swing.GroupLayout Panel35Layout = new javax.swing.GroupLayout(Panel35);
        Panel35.setLayout(Panel35Layout);
        Panel35Layout.setHorizontalGroup(
            Panel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel35Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel35)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel35Layout.setVerticalGroup(
            Panel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel35Layout.createSequentialGroup()
                .addComponent(jLabel35)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 250, 70, 67));

        Panel61.setBackground(new java.awt.Color(255, 255, 255));

        jLabel45.setText("61");

        javax.swing.GroupLayout Panel61Layout = new javax.swing.GroupLayout(Panel61);
        Panel61.setLayout(Panel61Layout);
        Panel61Layout.setHorizontalGroup(
            Panel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel61Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel45)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel61Layout.setVerticalGroup(
            Panel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel61Layout.createSequentialGroup()
                .addComponent(jLabel45)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel61, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 490, 70, 67));

        Panel42.setBackground(new java.awt.Color(255, 255, 255));

        jLabel42.setText("42");

        javax.swing.GroupLayout Panel42Layout = new javax.swing.GroupLayout(Panel42);
        Panel42.setLayout(Panel42Layout);
        Panel42Layout.setHorizontalGroup(
            Panel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel42Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel42)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel42Layout.setVerticalGroup(
            Panel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel42Layout.createSequentialGroup()
                .addComponent(jLabel42)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 330, 70, -1));

        Panel55.setBackground(new java.awt.Color(255, 255, 255));

        jLabel58.setText("55");

        javax.swing.GroupLayout Panel55Layout = new javax.swing.GroupLayout(Panel55);
        Panel55.setLayout(Panel55Layout);
        Panel55Layout.setHorizontalGroup(
            Panel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel55Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel58)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel55Layout.setVerticalGroup(
            Panel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel55Layout.createSequentialGroup()
                .addComponent(jLabel58)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 410, 70, 67));

        Panel41.setBackground(new java.awt.Color(255, 255, 255));
        Panel41.setForeground(new java.awt.Color(255, 255, 255));

        jLabel41.setText("41");

        javax.swing.GroupLayout Panel41Layout = new javax.swing.GroupLayout(Panel41);
        Panel41.setLayout(Panel41Layout);
        Panel41Layout.setHorizontalGroup(
            Panel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel41Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel41)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel41Layout.setVerticalGroup(
            Panel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel41Layout.createSequentialGroup()
                .addComponent(jLabel41)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 330, 70, -1));

        Panel43.setBackground(new java.awt.Color(255, 255, 255));

        jLabel49.setText("43");

        javax.swing.GroupLayout Panel43Layout = new javax.swing.GroupLayout(Panel43);
        Panel43.setLayout(Panel43Layout);
        Panel43Layout.setHorizontalGroup(
            Panel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel43Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel49)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel43Layout.setVerticalGroup(
            Panel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel43Layout.createSequentialGroup()
                .addComponent(jLabel49)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 330, 70, 67));

        Panel72.setBackground(new java.awt.Color(255, 255, 255));

        jLabel48.setText("72");

        javax.swing.GroupLayout Panel72Layout = new javax.swing.GroupLayout(Panel72);
        Panel72.setLayout(Panel72Layout);
        Panel72Layout.setHorizontalGroup(
            Panel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel72Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel48)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel72Layout.setVerticalGroup(
            Panel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel72Layout.createSequentialGroup()
                .addComponent(jLabel48)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel72, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 570, 70, 67));

        Panel62.setBackground(new java.awt.Color(255, 255, 255));

        jLabel46.setText("62");

        javax.swing.GroupLayout Panel62Layout = new javax.swing.GroupLayout(Panel62);
        Panel62.setLayout(Panel62Layout);
        Panel62Layout.setHorizontalGroup(
            Panel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel62Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel46)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel62Layout.setVerticalGroup(
            Panel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel62Layout.createSequentialGroup()
                .addComponent(jLabel46)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 490, 70, 67));

        Panel73.setBackground(new java.awt.Color(255, 255, 255));

        jLabel52.setText("73");

        javax.swing.GroupLayout Panel73Layout = new javax.swing.GroupLayout(Panel73);
        Panel73.setLayout(Panel73Layout);
        Panel73Layout.setHorizontalGroup(
            Panel73Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel73Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel52)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel73Layout.setVerticalGroup(
            Panel73Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel73Layout.createSequentialGroup()
                .addComponent(jLabel52)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel73, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 570, 70, 67));

        Panel64.setBackground(new java.awt.Color(255, 255, 255));

        jLabel55.setText("64");

        javax.swing.GroupLayout Panel64Layout = new javax.swing.GroupLayout(Panel64);
        Panel64.setLayout(Panel64Layout);
        Panel64Layout.setHorizontalGroup(
            Panel64Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel64Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel55)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel64Layout.setVerticalGroup(
            Panel64Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel64Layout.createSequentialGroup()
                .addComponent(jLabel55)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 490, 70, 67));

        Panel52.setBackground(new java.awt.Color(255, 255, 255));

        jLabel44.setText("52");

        javax.swing.GroupLayout Panel52Layout = new javax.swing.GroupLayout(Panel52);
        Panel52.setLayout(Panel52Layout);
        Panel52Layout.setHorizontalGroup(
            Panel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel52Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel44)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel52Layout.setVerticalGroup(
            Panel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel52Layout.createSequentialGroup()
                .addComponent(jLabel44)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 410, 70, 67));

        Panel71.setBackground(new java.awt.Color(255, 255, 255));

        jLabel47.setText("71");

        javax.swing.GroupLayout Panel71Layout = new javax.swing.GroupLayout(Panel71);
        Panel71.setLayout(Panel71Layout);
        Panel71Layout.setHorizontalGroup(
            Panel71Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel71Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel47)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel71Layout.setVerticalGroup(
            Panel71Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel71Layout.createSequentialGroup()
                .addComponent(jLabel47)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel71, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 570, 70, 67));

        Panel65.setBackground(new java.awt.Color(255, 255, 255));

        jLabel59.setText("65");

        javax.swing.GroupLayout Panel65Layout = new javax.swing.GroupLayout(Panel65);
        Panel65.setLayout(Panel65Layout);
        Panel65Layout.setHorizontalGroup(
            Panel65Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel65Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel59)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel65Layout.setVerticalGroup(
            Panel65Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel65Layout.createSequentialGroup()
                .addComponent(jLabel59)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 490, 70, 67));

        Panel53.setBackground(new java.awt.Color(255, 255, 255));

        jLabel50.setText("53");

        javax.swing.GroupLayout Panel53Layout = new javax.swing.GroupLayout(Panel53);
        Panel53.setLayout(Panel53Layout);
        Panel53Layout.setHorizontalGroup(
            Panel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel53Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel50)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel53Layout.setVerticalGroup(
            Panel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel53Layout.createSequentialGroup()
                .addComponent(jLabel50)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 410, 70, 67));

        Panel51.setBackground(new java.awt.Color(255, 255, 255));
        Panel51.setForeground(new java.awt.Color(255, 255, 255));

        jLabel43.setText("51");

        javax.swing.GroupLayout Panel51Layout = new javax.swing.GroupLayout(Panel51);
        Panel51.setLayout(Panel51Layout);
        Panel51Layout.setHorizontalGroup(
            Panel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel51Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel43)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel51Layout.setVerticalGroup(
            Panel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel51Layout.createSequentialGroup()
                .addComponent(jLabel43)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 410, 70, 67));

        Panel74.setBackground(new java.awt.Color(255, 255, 255));

        jLabel56.setText("74");

        javax.swing.GroupLayout Panel74Layout = new javax.swing.GroupLayout(Panel74);
        Panel74.setLayout(Panel74Layout);
        Panel74Layout.setHorizontalGroup(
            Panel74Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel74Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel56)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel74Layout.setVerticalGroup(
            Panel74Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel74Layout.createSequentialGroup()
                .addComponent(jLabel56)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel74, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 570, 70, 67));

        Panel54.setBackground(new java.awt.Color(255, 255, 255));

        jLabel54.setText("54");

        javax.swing.GroupLayout Panel54Layout = new javax.swing.GroupLayout(Panel54);
        Panel54.setLayout(Panel54Layout);
        Panel54Layout.setHorizontalGroup(
            Panel54Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel54Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel54)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel54Layout.setVerticalGroup(
            Panel54Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel54Layout.createSequentialGroup()
                .addComponent(jLabel54)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 410, 70, 67));

        Panel75.setBackground(new java.awt.Color(255, 255, 255));

        jLabel60.setText("75");

        javax.swing.GroupLayout Panel75Layout = new javax.swing.GroupLayout(Panel75);
        Panel75.setLayout(Panel75Layout);
        Panel75Layout.setHorizontalGroup(
            Panel75Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel75Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel60)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel75Layout.setVerticalGroup(
            Panel75Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel75Layout.createSequentialGroup()
                .addComponent(jLabel60)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel75, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 570, 70, 67));

        Panel44.setBackground(new java.awt.Color(255, 255, 255));

        jLabel53.setText("44");

        javax.swing.GroupLayout Panel44Layout = new javax.swing.GroupLayout(Panel44);
        Panel44.setLayout(Panel44Layout);
        Panel44Layout.setHorizontalGroup(
            Panel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel44Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel53)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel44Layout.setVerticalGroup(
            Panel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel44Layout.createSequentialGroup()
                .addComponent(jLabel53)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 330, 70, 67));

        Panel63.setBackground(new java.awt.Color(255, 255, 255));

        jLabel51.setText("63");

        javax.swing.GroupLayout Panel63Layout = new javax.swing.GroupLayout(Panel63);
        Panel63.setLayout(Panel63Layout);
        Panel63Layout.setHorizontalGroup(
            Panel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel63Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel51)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel63Layout.setVerticalGroup(
            Panel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel63Layout.createSequentialGroup()
                .addComponent(jLabel51)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 490, 70, 67));

        Panel45.setBackground(new java.awt.Color(255, 255, 255));

        jLabel57.setText("45");

        javax.swing.GroupLayout Panel45Layout = new javax.swing.GroupLayout(Panel45);
        Panel45.setLayout(Panel45Layout);
        Panel45Layout.setHorizontalGroup(
            Panel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel45Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel57)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel45Layout.setVerticalGroup(
            Panel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel45Layout.createSequentialGroup()
                .addComponent(jLabel57)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 330, 70, 67));

        Panel27.setBackground(new java.awt.Color(255, 255, 255));

        jLabel27.setText("27");

        javax.swing.GroupLayout Panel27Layout = new javax.swing.GroupLayout(Panel27);
        Panel27.setLayout(Panel27Layout);
        Panel27Layout.setHorizontalGroup(
            Panel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel27)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel27Layout.setVerticalGroup(
            Panel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel27Layout.createSequentialGroup()
                .addComponent(jLabel27)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 170, 70, 67));

        Panel36.setBackground(new java.awt.Color(255, 255, 255));

        jLabel36.setText("36");

        javax.swing.GroupLayout Panel36Layout = new javax.swing.GroupLayout(Panel36);
        Panel36.setLayout(Panel36Layout);
        Panel36Layout.setHorizontalGroup(
            Panel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel36Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel36)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel36Layout.setVerticalGroup(
            Panel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel36Layout.createSequentialGroup()
                .addComponent(jLabel36)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 250, 70, 67));

        Panel48.setBackground(new java.awt.Color(255, 255, 255));

        jLabel66.setText("48");

        javax.swing.GroupLayout Panel48Layout = new javax.swing.GroupLayout(Panel48);
        Panel48.setLayout(Panel48Layout);
        Panel48Layout.setHorizontalGroup(
            Panel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel48Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel66)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel48Layout.setVerticalGroup(
            Panel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel48Layout.createSequentialGroup()
                .addComponent(jLabel66)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 330, 70, 67));

        Panel18.setBackground(new java.awt.Color(255, 255, 255));

        jLabel18.setText("18");

        javax.swing.GroupLayout Panel18Layout = new javax.swing.GroupLayout(Panel18);
        Panel18.setLayout(Panel18Layout);
        Panel18Layout.setHorizontalGroup(
            Panel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel18Layout.setVerticalGroup(
            Panel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel18Layout.createSequentialGroup()
                .addComponent(jLabel18)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 90, 70, 67));

        Panel76.setBackground(new java.awt.Color(255, 255, 255));

        jLabel64.setText("76");

        javax.swing.GroupLayout Panel76Layout = new javax.swing.GroupLayout(Panel76);
        Panel76.setLayout(Panel76Layout);
        Panel76Layout.setHorizontalGroup(
            Panel76Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel76Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel64)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel76Layout.setVerticalGroup(
            Panel76Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel76Layout.createSequentialGroup()
                .addComponent(jLabel64)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel76, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 570, 70, 67));

        Panel10.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setText("10");

        javax.swing.GroupLayout Panel10Layout = new javax.swing.GroupLayout(Panel10);
        Panel10.setLayout(Panel10Layout);
        Panel10Layout.setHorizontalGroup(
            Panel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel10Layout.setVerticalGroup(
            Panel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel10Layout.createSequentialGroup()
                .addComponent(jLabel11)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 10, 70, 67));

        Panel67.setBackground(new java.awt.Color(255, 255, 255));

        jLabel70.setText("67");

        javax.swing.GroupLayout Panel67Layout = new javax.swing.GroupLayout(Panel67);
        Panel67.setLayout(Panel67Layout);
        Panel67Layout.setHorizontalGroup(
            Panel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel67Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel70)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel67Layout.setVerticalGroup(
            Panel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel67Layout.createSequentialGroup()
                .addComponent(jLabel70)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 490, 70, 67));

        Panel39.setBackground(new java.awt.Color(255, 255, 255));

        jLabel39.setText("39");

        javax.swing.GroupLayout Panel39Layout = new javax.swing.GroupLayout(Panel39);
        Panel39.setLayout(Panel39Layout);
        Panel39Layout.setHorizontalGroup(
            Panel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel39Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel39)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel39Layout.setVerticalGroup(
            Panel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel39Layout.createSequentialGroup()
                .addComponent(jLabel39)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 250, 70, 67));

        Panel59.setBackground(new java.awt.Color(255, 255, 255));

        jLabel75.setText("59");

        javax.swing.GroupLayout Panel59Layout = new javax.swing.GroupLayout(Panel59);
        Panel59.setLayout(Panel59Layout);
        Panel59Layout.setHorizontalGroup(
            Panel59Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel59Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel75)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel59Layout.setVerticalGroup(
            Panel59Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel59Layout.createSequentialGroup()
                .addComponent(jLabel75)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 410, 70, 67));

        Panel56.setBackground(new java.awt.Color(255, 255, 255));
        Panel56.setForeground(new java.awt.Color(255, 255, 255));

        jLabel62.setText("56");

        javax.swing.GroupLayout Panel56Layout = new javax.swing.GroupLayout(Panel56);
        Panel56.setLayout(Panel56Layout);
        Panel56Layout.setHorizontalGroup(
            Panel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel56Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel62)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel56Layout.setVerticalGroup(
            Panel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel56Layout.createSequentialGroup()
                .addComponent(jLabel62)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 410, 70, 67));

        Panel9.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setText("9");

        javax.swing.GroupLayout Panel9Layout = new javax.swing.GroupLayout(Panel9);
        Panel9.setLayout(Panel9Layout);
        Panel9Layout.setHorizontalGroup(
            Panel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        Panel9Layout.setVerticalGroup(
            Panel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel9Layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 10, 70, 67));

        Panel16.setBackground(new java.awt.Color(255, 255, 255));
        Panel16.setForeground(new java.awt.Color(255, 255, 255));

        jLabel16.setText("16");

        javax.swing.GroupLayout Panel16Layout = new javax.swing.GroupLayout(Panel16);
        Panel16.setLayout(Panel16Layout);
        Panel16Layout.setHorizontalGroup(
            Panel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel16Layout.setVerticalGroup(
            Panel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel16Layout.createSequentialGroup()
                .addComponent(jLabel16)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 90, 70, 67));

        Panel78.setBackground(new java.awt.Color(255, 255, 255));

        jLabel74.setText("78");

        javax.swing.GroupLayout Panel78Layout = new javax.swing.GroupLayout(Panel78);
        Panel78.setLayout(Panel78Layout);
        Panel78Layout.setHorizontalGroup(
            Panel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel78Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel74)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel78Layout.setVerticalGroup(
            Panel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel78Layout.createSequentialGroup()
                .addComponent(jLabel74)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel78, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 570, 70, 67));

        Panel19.setBackground(new java.awt.Color(255, 255, 255));

        jLabel19.setText("19");

        javax.swing.GroupLayout Panel19Layout = new javax.swing.GroupLayout(Panel19);
        Panel19.setLayout(Panel19Layout);
        Panel19Layout.setHorizontalGroup(
            Panel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel19Layout.setVerticalGroup(
            Panel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel19Layout.createSequentialGroup()
                .addComponent(jLabel19)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 90, 70, 67));

        Panel46.setBackground(new java.awt.Color(255, 255, 255));
        Panel46.setForeground(new java.awt.Color(255, 255, 255));

        jLabel61.setText("46");

        javax.swing.GroupLayout Panel46Layout = new javax.swing.GroupLayout(Panel46);
        Panel46.setLayout(Panel46Layout);
        Panel46Layout.setHorizontalGroup(
            Panel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel46Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel61)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel46Layout.setVerticalGroup(
            Panel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel46Layout.createSequentialGroup()
                .addComponent(jLabel61)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 330, 70, 67));

        Panel6.setBackground(new java.awt.Color(255, 255, 255));
        Panel6.setForeground(new java.awt.Color(255, 255, 255));

        jLabel6.setText("6");

        javax.swing.GroupLayout Panel6Layout = new javax.swing.GroupLayout(Panel6);
        Panel6.setLayout(Panel6Layout);
        Panel6Layout.setHorizontalGroup(
            Panel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        Panel6Layout.setVerticalGroup(
            Panel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel6Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 10, 70, -1));

        Panel49.setBackground(new java.awt.Color(255, 255, 255));

        jLabel67.setText("49");

        javax.swing.GroupLayout Panel49Layout = new javax.swing.GroupLayout(Panel49);
        Panel49.setLayout(Panel49Layout);
        Panel49Layout.setHorizontalGroup(
            Panel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel49Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel67)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel49Layout.setVerticalGroup(
            Panel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel49Layout.createSequentialGroup()
                .addComponent(jLabel67)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 330, 70, -1));

        Panel50.setBackground(new java.awt.Color(255, 255, 255));

        jLabel68.setText("50");

        javax.swing.GroupLayout Panel50Layout = new javax.swing.GroupLayout(Panel50);
        Panel50.setLayout(Panel50Layout);
        Panel50Layout.setHorizontalGroup(
            Panel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel50Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel68)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel50Layout.setVerticalGroup(
            Panel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel50Layout.createSequentialGroup()
                .addComponent(jLabel68)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 330, 70, 67));

        Panel47.setBackground(new java.awt.Color(255, 255, 255));

        jLabel65.setText("47");

        javax.swing.GroupLayout Panel47Layout = new javax.swing.GroupLayout(Panel47);
        Panel47.setLayout(Panel47Layout);
        Panel47Layout.setHorizontalGroup(
            Panel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel47Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel65)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel47Layout.setVerticalGroup(
            Panel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel47Layout.createSequentialGroup()
                .addComponent(jLabel65)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 330, 70, 67));

        Panel30.setBackground(new java.awt.Color(255, 255, 255));

        jLabel30.setText("30");

        javax.swing.GroupLayout Panel30Layout = new javax.swing.GroupLayout(Panel30);
        Panel30.setLayout(Panel30Layout);
        Panel30Layout.setHorizontalGroup(
            Panel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel30Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel30)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel30Layout.setVerticalGroup(
            Panel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel30Layout.createSequentialGroup()
                .addComponent(jLabel30)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 170, 70, 67));

        Panel79.setBackground(new java.awt.Color(255, 255, 255));

        jLabel77.setText("79");

        javax.swing.GroupLayout Panel79Layout = new javax.swing.GroupLayout(Panel79);
        Panel79.setLayout(Panel79Layout);
        Panel79Layout.setHorizontalGroup(
            Panel79Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel79Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel77)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel79Layout.setVerticalGroup(
            Panel79Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel79Layout.createSequentialGroup()
                .addComponent(jLabel77)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel79, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 570, 70, 67));

        Panel60.setBackground(new java.awt.Color(255, 255, 255));

        jLabel78.setText("60");

        javax.swing.GroupLayout Panel60Layout = new javax.swing.GroupLayout(Panel60);
        Panel60.setLayout(Panel60Layout);
        Panel60Layout.setHorizontalGroup(
            Panel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel60Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel78)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel60Layout.setVerticalGroup(
            Panel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel60Layout.createSequentialGroup()
                .addComponent(jLabel78)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 410, 70, -1));

        Panel58.setBackground(new java.awt.Color(255, 255, 255));

        jLabel72.setText("58");

        javax.swing.GroupLayout Panel58Layout = new javax.swing.GroupLayout(Panel58);
        Panel58.setLayout(Panel58Layout);
        Panel58Layout.setHorizontalGroup(
            Panel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel58Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel72)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel58Layout.setVerticalGroup(
            Panel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel58Layout.createSequentialGroup()
                .addComponent(jLabel72)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 410, 70, 67));

        Panel66.setBackground(new java.awt.Color(255, 255, 255));

        jLabel63.setText("66");

        javax.swing.GroupLayout Panel66Layout = new javax.swing.GroupLayout(Panel66);
        Panel66.setLayout(Panel66Layout);
        Panel66Layout.setHorizontalGroup(
            Panel66Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel66Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel63)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel66Layout.setVerticalGroup(
            Panel66Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel66Layout.createSequentialGroup()
                .addComponent(jLabel63)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel66, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 490, 70, 67));

        Panel26.setBackground(new java.awt.Color(255, 255, 255));

        jLabel26.setText("26");

        javax.swing.GroupLayout Panel26Layout = new javax.swing.GroupLayout(Panel26);
        Panel26.setLayout(Panel26Layout);
        Panel26Layout.setHorizontalGroup(
            Panel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel26)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel26Layout.setVerticalGroup(
            Panel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel26Layout.createSequentialGroup()
                .addComponent(jLabel26)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 170, 70, 67));

        Panel29.setBackground(new java.awt.Color(255, 255, 255));

        jLabel29.setText("29");

        javax.swing.GroupLayout Panel29Layout = new javax.swing.GroupLayout(Panel29);
        Panel29.setLayout(Panel29Layout);
        Panel29Layout.setHorizontalGroup(
            Panel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel29)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel29Layout.setVerticalGroup(
            Panel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel29Layout.createSequentialGroup()
                .addComponent(jLabel29)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 170, 70, 67));

        Panel69.setBackground(new java.awt.Color(255, 255, 255));

        jLabel76.setText("69");

        javax.swing.GroupLayout Panel69Layout = new javax.swing.GroupLayout(Panel69);
        Panel69.setLayout(Panel69Layout);
        Panel69Layout.setHorizontalGroup(
            Panel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel69Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel76)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel69Layout.setVerticalGroup(
            Panel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel69Layout.createSequentialGroup()
                .addComponent(jLabel76)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 490, 70, 67));

        Panel77.setBackground(new java.awt.Color(255, 255, 255));

        jLabel71.setText("77");

        javax.swing.GroupLayout Panel77Layout = new javax.swing.GroupLayout(Panel77);
        Panel77.setLayout(Panel77Layout);
        Panel77Layout.setHorizontalGroup(
            Panel77Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel77Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel71)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel77Layout.setVerticalGroup(
            Panel77Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel77Layout.createSequentialGroup()
                .addComponent(jLabel71)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel77, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 570, 70, 67));

        Panel80.setBackground(new java.awt.Color(255, 255, 255));

        jLabel80.setText("80");

        javax.swing.GroupLayout Panel80Layout = new javax.swing.GroupLayout(Panel80);
        Panel80.setLayout(Panel80Layout);
        Panel80Layout.setHorizontalGroup(
            Panel80Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel80Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel80)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel80Layout.setVerticalGroup(
            Panel80Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel80Layout.createSequentialGroup()
                .addComponent(jLabel80)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel80, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 570, 70, 67));

        Panel17.setBackground(new java.awt.Color(255, 255, 255));

        jLabel17.setText("17");

        javax.swing.GroupLayout Panel17Layout = new javax.swing.GroupLayout(Panel17);
        Panel17.setLayout(Panel17Layout);
        Panel17Layout.setHorizontalGroup(
            Panel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel17Layout.setVerticalGroup(
            Panel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel17Layout.createSequentialGroup()
                .addComponent(jLabel17)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 90, 70, 67));

        Panel28.setBackground(new java.awt.Color(255, 255, 255));

        jLabel28.setText("28");

        javax.swing.GroupLayout Panel28Layout = new javax.swing.GroupLayout(Panel28);
        Panel28.setLayout(Panel28Layout);
        Panel28Layout.setHorizontalGroup(
            Panel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel28Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel28)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel28Layout.setVerticalGroup(
            Panel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel28Layout.createSequentialGroup()
                .addComponent(jLabel28)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 170, 70, 67));

        Panel68.setBackground(new java.awt.Color(255, 255, 255));

        jLabel73.setText("68");

        javax.swing.GroupLayout Panel68Layout = new javax.swing.GroupLayout(Panel68);
        Panel68.setLayout(Panel68Layout);
        Panel68Layout.setHorizontalGroup(
            Panel68Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel68Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel73)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel68Layout.setVerticalGroup(
            Panel68Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel68Layout.createSequentialGroup()
                .addComponent(jLabel73)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 490, 70, 67));

        Panel8.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setText("8");

        javax.swing.GroupLayout Panel8Layout = new javax.swing.GroupLayout(Panel8);
        Panel8.setLayout(Panel8Layout);
        Panel8Layout.setHorizontalGroup(
            Panel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        Panel8Layout.setVerticalGroup(
            Panel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel8Layout.createSequentialGroup()
                .addComponent(jLabel8)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 10, 70, 67));

        Panel38.setBackground(new java.awt.Color(255, 255, 255));

        jLabel38.setText("38");

        javax.swing.GroupLayout Panel38Layout = new javax.swing.GroupLayout(Panel38);
        Panel38.setLayout(Panel38Layout);
        Panel38Layout.setHorizontalGroup(
            Panel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel38Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel38)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel38Layout.setVerticalGroup(
            Panel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel38Layout.createSequentialGroup()
                .addComponent(jLabel38)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 250, 70, 67));

        Panel20.setBackground(new java.awt.Color(255, 255, 255));

        jLabel20.setText("20");

        javax.swing.GroupLayout Panel20Layout = new javax.swing.GroupLayout(Panel20);
        Panel20.setLayout(Panel20Layout);
        Panel20Layout.setHorizontalGroup(
            Panel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel20Layout.setVerticalGroup(
            Panel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel20Layout.createSequentialGroup()
                .addComponent(jLabel20)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 90, 70, 67));

        Panel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setText("7");

        javax.swing.GroupLayout Panel7Layout = new javax.swing.GroupLayout(Panel7);
        Panel7.setLayout(Panel7Layout);
        Panel7Layout.setHorizontalGroup(
            Panel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        Panel7Layout.setVerticalGroup(
            Panel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel7Layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 10, 70, -1));

        Panel37.setBackground(new java.awt.Color(255, 255, 255));

        jLabel37.setText("37");

        javax.swing.GroupLayout Panel37Layout = new javax.swing.GroupLayout(Panel37);
        Panel37.setLayout(Panel37Layout);
        Panel37Layout.setHorizontalGroup(
            Panel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel37Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel37)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel37Layout.setVerticalGroup(
            Panel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel37Layout.createSequentialGroup()
                .addComponent(jLabel37)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 250, 70, 67));

        Panel40.setBackground(new java.awt.Color(255, 255, 255));

        jLabel40.setText("40");

        javax.swing.GroupLayout Panel40Layout = new javax.swing.GroupLayout(Panel40);
        Panel40.setLayout(Panel40Layout);
        Panel40Layout.setHorizontalGroup(
            Panel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel40Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel40)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel40Layout.setVerticalGroup(
            Panel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel40Layout.createSequentialGroup()
                .addComponent(jLabel40)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 250, 70, 67));

        Panel57.setBackground(new java.awt.Color(255, 255, 255));

        jLabel69.setText("57");

        javax.swing.GroupLayout Panel57Layout = new javax.swing.GroupLayout(Panel57);
        Panel57.setLayout(Panel57Layout);
        Panel57Layout.setHorizontalGroup(
            Panel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel57Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel69)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel57Layout.setVerticalGroup(
            Panel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel57Layout.createSequentialGroup()
                .addComponent(jLabel69)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 410, 70, 67));

        Panel70.setBackground(new java.awt.Color(255, 255, 255));

        jLabel79.setText("70");

        javax.swing.GroupLayout Panel70Layout = new javax.swing.GroupLayout(Panel70);
        Panel70.setLayout(Panel70Layout);
        Panel70Layout.setHorizontalGroup(
            Panel70Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel70Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel79)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        Panel70Layout.setVerticalGroup(
            Panel70Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel70Layout.createSequentialGroup()
                .addComponent(jLabel79)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1123123.add(Panel70, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 490, 70, 67));

        createFile.setText("Crear Archivo");
        createFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createFileActionPerformed(evt);
            }
        });
        jPanel1123123.add(createFile, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 650, 130, -1));

        createDir.setText("Crear Directorio");
        createDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createDirActionPerformed(evt);
            }
        });
        jPanel1123123.add(createDir, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 690, 130, -1));

        change.setText("Cambiar de Modo");
        change.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeActionPerformed(evt);
            }
        });
        jPanel1123123.add(change, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 650, 140, -1));

        modo.setText("Actual: Administrador");
        jPanel1123123.add(modo, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 680, -1, -1));

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        Tree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane3.setViewportView(Tree);

        jPanel1123123.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 380, 330));

        deleteFile.setText("Eliminar Archivo");
        deleteFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteFileActionPerformed(evt);
            }
        });
        jPanel1123123.add(deleteFile, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 650, 160, -1));

        deleteDir.setText("Eliminar Directorio");
        deleteDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteDirActionPerformed(evt);
            }
        });
        jPanel1123123.add(deleteDir, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 690, 160, -1));

        editFile.setText("Editar Archivo");
        editFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editFileActionPerformed(evt);
            }
        });
        jPanel1123123.add(editFile, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 650, 130, -1));

        editDir.setText("Editar Directorio");
        editDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editDirActionPerformed(evt);
            }
        });
        jPanel1123123.add(editDir, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 690, 130, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1123123, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1123123, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void changeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeActionPerformed
        // TODO add your handling code here:
        if(this.mode=="Administrador"){
            this.setMode("Usuario");
            this.createDir.setVisible(false);
            this.createFile.setVisible(false);
            this.editDir.setVisible(false);
            this.editFile.setVisible(false);
            this.deleteDir.setVisible(false);
            this.deleteFile.setVisible(false);
            this.modo.setText("Actual: Usuario");
        }else{
            this.setMode("Administrador");
            this.createDir.setVisible(true);
            this.createFile.setVisible(true);
            this.editDir.setVisible(true);
this.editFile.setVisible(true);
this.deleteDir.setVisible(true);
this.deleteFile.setVisible(true);
            this.modo.setText("Actual: Administrador");
        }
    }//GEN-LAST:event_changeActionPerformed

    private void createFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createFileActionPerformed
        // TODO add your handling code here:
        CrearArchivo cr = new CrearArchivo(this);
    }//GEN-LAST:event_createFileActionPerformed

    private void createDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createDirActionPerformed
        // TODO add your handling code here:
        CrearDirectorio cr = new CrearDirectorio(this);
    }//GEN-LAST:event_createDirActionPerformed

    private void deleteFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteFileActionPerformed
        // TODO add your handling code here:
        EliminarArchivo ea = new EliminarArchivo(this);
    }//GEN-LAST:event_deleteFileActionPerformed

    private void deleteDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteDirActionPerformed
        // TODO add your handling code here:
        EliminarDirectorio ed = new EliminarDirectorio(this);
    }//GEN-LAST:event_deleteDirActionPerformed

    private void editFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editFileActionPerformed
        // TODO add your handling code here:
        EditarArchivo eaa = new EditarArchivo(this);
    }//GEN-LAST:event_editFileActionPerformed

    private void editDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editDirActionPerformed
        // TODO add your handling code here:
        EditarDirectorio edd = new EditarDirectorio(this);
    }//GEN-LAST:event_editDirActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FileSystemSimulator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FileSystemSimulator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FileSystemSimulator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FileSystemSimulator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new FileSystemSimulator().setVisible(true);
                } catch (NoSuchFieldException ex) {
                    Logger.getLogger(FileSystemSimulator.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(FileSystemSimulator.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(FileSystemSimulator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Panel1;
    private javax.swing.JPanel Panel10;
    private javax.swing.JPanel Panel11;
    private javax.swing.JPanel Panel12;
    private javax.swing.JPanel Panel13;
    private javax.swing.JPanel Panel14;
    private javax.swing.JPanel Panel15;
    private javax.swing.JPanel Panel16;
    private javax.swing.JPanel Panel17;
    private javax.swing.JPanel Panel18;
    private javax.swing.JPanel Panel19;
    private javax.swing.JPanel Panel2;
    private javax.swing.JPanel Panel20;
    private javax.swing.JPanel Panel21;
    private javax.swing.JPanel Panel22;
    private javax.swing.JPanel Panel23;
    private javax.swing.JPanel Panel24;
    private javax.swing.JPanel Panel25;
    private javax.swing.JPanel Panel26;
    private javax.swing.JPanel Panel27;
    private javax.swing.JPanel Panel28;
    private javax.swing.JPanel Panel29;
    private javax.swing.JPanel Panel3;
    private javax.swing.JPanel Panel30;
    private javax.swing.JPanel Panel31;
    private javax.swing.JPanel Panel32;
    private javax.swing.JPanel Panel33;
    private javax.swing.JPanel Panel34;
    private javax.swing.JPanel Panel35;
    private javax.swing.JPanel Panel36;
    private javax.swing.JPanel Panel37;
    private javax.swing.JPanel Panel38;
    private javax.swing.JPanel Panel39;
    private javax.swing.JPanel Panel4;
    private javax.swing.JPanel Panel40;
    private javax.swing.JPanel Panel41;
    private javax.swing.JPanel Panel42;
    private javax.swing.JPanel Panel43;
    private javax.swing.JPanel Panel44;
    private javax.swing.JPanel Panel45;
    private javax.swing.JPanel Panel46;
    private javax.swing.JPanel Panel47;
    private javax.swing.JPanel Panel48;
    private javax.swing.JPanel Panel49;
    private javax.swing.JPanel Panel5;
    private javax.swing.JPanel Panel50;
    private javax.swing.JPanel Panel51;
    private javax.swing.JPanel Panel52;
    private javax.swing.JPanel Panel53;
    private javax.swing.JPanel Panel54;
    private javax.swing.JPanel Panel55;
    private javax.swing.JPanel Panel56;
    private javax.swing.JPanel Panel57;
    private javax.swing.JPanel Panel58;
    private javax.swing.JPanel Panel59;
    private javax.swing.JPanel Panel6;
    private javax.swing.JPanel Panel60;
    private javax.swing.JPanel Panel61;
    private javax.swing.JPanel Panel62;
    private javax.swing.JPanel Panel63;
    private javax.swing.JPanel Panel64;
    private javax.swing.JPanel Panel65;
    private javax.swing.JPanel Panel66;
    private javax.swing.JPanel Panel67;
    private javax.swing.JPanel Panel68;
    private javax.swing.JPanel Panel69;
    private javax.swing.JPanel Panel7;
    private javax.swing.JPanel Panel70;
    private javax.swing.JPanel Panel71;
    private javax.swing.JPanel Panel72;
    private javax.swing.JPanel Panel73;
    private javax.swing.JPanel Panel74;
    private javax.swing.JPanel Panel75;
    private javax.swing.JPanel Panel76;
    private javax.swing.JPanel Panel77;
    private javax.swing.JPanel Panel78;
    private javax.swing.JPanel Panel79;
    private javax.swing.JPanel Panel8;
    private javax.swing.JPanel Panel80;
    private javax.swing.JPanel Panel9;
    private javax.swing.JTable Tabla;
    private javax.swing.JTree Tree;
    private javax.swing.JButton change;
    private javax.swing.JButton createDir;
    private javax.swing.JButton createFile;
    private javax.swing.JButton deleteDir;
    private javax.swing.JButton deleteFile;
    private javax.swing.JButton editDir;
    private javax.swing.JButton editFile;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1123123;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel modo;
    // End of variables declaration//GEN-END:variables
}
