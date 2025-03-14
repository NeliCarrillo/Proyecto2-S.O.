/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUIs;

import EDD.Lista;
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


    public String getMode() {
        return mode;
    }
    
    public Lista getDirectorios() {
        return directorios;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * Creates new form Dos
     */
    public FileSystemSimulator() {
        initComponents();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        LoadRoot("FileSystem");
    }
    
    public void LoadRoot(String n){
        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode (n);
        DefaultMutableTreeNode aux = new DefaultMutableTreeNode ("");
        raiz.add(aux);
        model = (DefaultTreeModel)Tree.getModel();
        model.setRoot(raiz);
        Tree.setModel(model);
        directorios.agregar(n);
    }
    
    /**
    * Método para añadir un archivo (hoja) a un nodo padre específico.
    * Los hijos de un mismo padre no pueden tener el mismo nombre.
    *
    * @param nombrePadre El nombre del nodo padre al que se añadirá el archivo.
    * @param nombreArchivo El nombre del archivo que se añadirá como hoja.
    * @return true si el archivo se añadió correctamente, false si ya existe un hijo con el mismo nombre.
    */
   public boolean anadirArchivoJTree(String nombrePadre, String nombreArchivo) {
      DefaultTableModel modeloTabla = (DefaultTableModel) Tabla.getModel();

      // Crear un arreglo con los datos de la nueva fila
      Object[] nuevaFila = {nombreArchivo, 1, 1, "lol"};

        // Agregar la fila al modelo de la tabla
        modeloTabla.addRow(nuevaFila);
       this.eliminarArchivoJTree(nombrePadre,"");
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

           // Si no existe un hijo con el mismo nombre, añadir el nuevo archivo
           DefaultMutableTreeNode newFileNode = new DefaultMutableTreeNode(nombreArchivo);
           parentNode.add(newFileNode);
           model.reload(parentNode); // Actualiza el modelo para reflejar los cambios
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
   public void anadirDirectorio(String nombrePadre, String nombreDirectorio) {
       DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
       DefaultMutableTreeNode parentNode = findNodeJTree(root, nombrePadre);

       if (parentNode != null) {
           // Crear el nuevo directorio (nodo) con un hijo predeterminado vacío
           DefaultMutableTreeNode nuevoDirectorio = new DefaultMutableTreeNode(nombreDirectorio);
           DefaultMutableTreeNode hijoPredeterminado = new DefaultMutableTreeNode("");
           nuevoDirectorio.add(hijoPredeterminado);

           // Añadir el nuevo directorio como hijo del nodo padre
           parentNode.add(nuevoDirectorio);
           model.reload(parentNode); // Actualizar el modelo para reflejar los cambios
           directorios.agregar(nombreDirectorio); // Asume que Lista tiene un método agregar
           System.out.println("Directorio '" + nombreDirectorio + "' añadido correctamente al nodo padre '" + nombrePadre + "'.");
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
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jPanel32 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        jLabel58 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jPanel40 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jPanel39 = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        jPanel35 = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        jPanel41 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jPanel36 = new javax.swing.JPanel();
        jLabel59 = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        jPanel31 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jPanel38 = new javax.swing.JPanel();
        jLabel56 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        jPanel37 = new javax.swing.JPanel();
        jLabel60 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jLabel53 = new javax.swing.JLabel();
        jPanel34 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        jPanel53 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jPanel61 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jPanel64 = new javax.swing.JPanel();
        jLabel66 = new javax.swing.JLabel();
        jPanel49 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jPanel81 = new javax.swing.JPanel();
        jLabel64 = new javax.swing.JLabel();
        jPanel46 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel73 = new javax.swing.JPanel();
        jLabel70 = new javax.swing.JLabel();
        jPanel58 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jPanel68 = new javax.swing.JPanel();
        jLabel75 = new javax.swing.JLabel();
        jPanel71 = new javax.swing.JPanel();
        jLabel62 = new javax.swing.JLabel();
        jPanel45 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel51 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jPanel79 = new javax.swing.JPanel();
        jLabel74 = new javax.swing.JLabel();
        jPanel48 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel62 = new javax.swing.JPanel();
        jLabel61 = new javax.swing.JLabel();
        jPanel42 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel65 = new javax.swing.JPanel();
        jLabel67 = new javax.swing.JLabel();
        jPanel66 = new javax.swing.JPanel();
        jLabel68 = new javax.swing.JLabel();
        jPanel63 = new javax.swing.JPanel();
        jLabel65 = new javax.swing.JLabel();
        jPanel56 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jPanel78 = new javax.swing.JPanel();
        jLabel77 = new javax.swing.JLabel();
        jPanel67 = new javax.swing.JPanel();
        jLabel78 = new javax.swing.JLabel();
        jPanel69 = new javax.swing.JPanel();
        jLabel72 = new javax.swing.JLabel();
        jPanel72 = new javax.swing.JPanel();
        jLabel63 = new javax.swing.JLabel();
        jPanel52 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jPanel55 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jPanel75 = new javax.swing.JPanel();
        jLabel76 = new javax.swing.JLabel();
        jPanel80 = new javax.swing.JPanel();
        jLabel71 = new javax.swing.JLabel();
        jPanel77 = new javax.swing.JPanel();
        jLabel80 = new javax.swing.JLabel();
        jPanel50 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jPanel54 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jPanel74 = new javax.swing.JPanel();
        jLabel73 = new javax.swing.JLabel();
        jPanel44 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel59 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        jPanel47 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jPanel43 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel60 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jPanel57 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jPanel70 = new javax.swing.JPanel();
        jLabel69 = new javax.swing.JLabel();
        jPanel76 = new javax.swing.JPanel();
        jLabel79 = new javax.swing.JLabel();
        createFile = new javax.swing.JButton();
        createDir = new javax.swing.JButton();
        change = new javax.swing.JButton();
        modo = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        Tree = new javax.swing.JTree();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Tabla.setBackground(new java.awt.Color(204, 255, 255));
        Tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Bloque Inicial", "Longitud", "Color"
            }
        ));
        jScrollPane2.setViewportView(Tabla);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 328, 380, 310));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("1");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 10, 70, 67));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setText("2");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 10, 70, 67));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setText("3");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 10, 70, 67));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setText("4");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 10, 70, 67));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setText("5");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 10, 70, 67));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setForeground(new java.awt.Color(255, 255, 255));

        jLabel10.setText("11");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel10)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(401, 91, 70, 67));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setText("12");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel12)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 90, 70, 67));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setText("13");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel13)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 90, 70, 67));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        jLabel14.setText("14");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jLabel14)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 90, 70, 67));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        jLabel15.setText("15");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jLabel15)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 90, 70, 67));

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        jLabel21.setText("21");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jLabel21)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 170, 70, 67));

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));

        jLabel22.setText("22");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jLabel22)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 170, 70, 67));

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));

        jLabel23.setText("23");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jLabel23)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 170, 70, 67));

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));

        jLabel24.setText("24");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jLabel24)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 170, 70, 67));

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));

        jLabel25.setText("25");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jLabel25)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 170, 70, 67));

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));

        jLabel31.setText("31");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel31)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(jLabel31)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 250, 70, 67));

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));

        jLabel32.setText("32");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addComponent(jLabel32)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 250, 70, 67));

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));

        jLabel33.setText("33");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addComponent(jLabel33)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 250, 70, 67));

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));

        jLabel34.setText("34");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel34)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addComponent(jLabel34)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 250, 70, 67));

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));

        jLabel35.setText("35");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel35)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addComponent(jLabel35)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 250, 70, 67));

        jPanel32.setBackground(new java.awt.Color(255, 255, 255));

        jLabel45.setText("61");

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel45)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addComponent(jLabel45)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 490, 70, 67));

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));

        jLabel42.setText("42");

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel42)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addComponent(jLabel42)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 330, 70, -1));

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));

        jLabel58.setText("55");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel58)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addComponent(jLabel58)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 410, 70, 67));

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));
        jPanel22.setForeground(new java.awt.Color(255, 255, 255));

        jLabel41.setText("41");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel41)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addComponent(jLabel41)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 330, 70, -1));

        jPanel24.setBackground(new java.awt.Color(255, 255, 255));

        jLabel49.setText("43");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel49)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addComponent(jLabel49)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 330, 70, 67));

        jPanel40.setBackground(new java.awt.Color(255, 255, 255));

        jLabel48.setText("72");

        javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
        jPanel40.setLayout(jPanel40Layout);
        jPanel40Layout.setHorizontalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel48)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel40Layout.setVerticalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addComponent(jLabel48)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 570, 70, 67));

        jPanel33.setBackground(new java.awt.Color(255, 255, 255));

        jLabel46.setText("62");

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel46)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addComponent(jLabel46)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 490, 70, 67));

        jPanel39.setBackground(new java.awt.Color(255, 255, 255));

        jLabel52.setText("73");

        javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
        jPanel39.setLayout(jPanel39Layout);
        jPanel39Layout.setHorizontalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel52)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel39Layout.setVerticalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addComponent(jLabel52)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 570, 70, 67));

        jPanel35.setBackground(new java.awt.Color(255, 255, 255));

        jLabel55.setText("64");

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel55)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addComponent(jLabel55)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 490, 70, 67));

        jPanel30.setBackground(new java.awt.Color(255, 255, 255));

        jLabel44.setText("52");

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel44)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addComponent(jLabel44)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 410, 70, 67));

        jPanel41.setBackground(new java.awt.Color(255, 255, 255));

        jLabel47.setText("71");

        javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
        jPanel41.setLayout(jPanel41Layout);
        jPanel41Layout.setHorizontalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel47)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel41Layout.setVerticalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addComponent(jLabel47)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 570, 70, 67));

        jPanel36.setBackground(new java.awt.Color(255, 255, 255));

        jLabel59.setText("65");

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel59)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addComponent(jLabel59)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 490, 70, 67));

        jPanel29.setBackground(new java.awt.Color(255, 255, 255));

        jLabel50.setText("53");

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel50)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addComponent(jLabel50)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 410, 70, 67));

        jPanel31.setBackground(new java.awt.Color(255, 255, 255));
        jPanel31.setForeground(new java.awt.Color(255, 255, 255));

        jLabel43.setText("51");

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel43)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addComponent(jLabel43)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 410, 70, 67));

        jPanel38.setBackground(new java.awt.Color(255, 255, 255));

        jLabel56.setText("74");

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel56)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addComponent(jLabel56)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 570, 70, 67));

        jPanel28.setBackground(new java.awt.Color(255, 255, 255));

        jLabel54.setText("54");

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel54)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addComponent(jLabel54)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 410, 70, 67));

        jPanel37.setBackground(new java.awt.Color(255, 255, 255));

        jLabel60.setText("75");

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel60)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addComponent(jLabel60)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 570, 70, 67));

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));

        jLabel53.setText("44");

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel53)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addComponent(jLabel53)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 330, 70, 67));

        jPanel34.setBackground(new java.awt.Color(255, 255, 255));

        jLabel51.setText("63");

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel51)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addComponent(jLabel51)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 490, 70, 67));

        jPanel26.setBackground(new java.awt.Color(255, 255, 255));

        jLabel57.setText("45");

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel57)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addComponent(jLabel57)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 330, 70, 67));

        jPanel53.setBackground(new java.awt.Color(255, 255, 255));

        jLabel27.setText("27");

        javax.swing.GroupLayout jPanel53Layout = new javax.swing.GroupLayout(jPanel53);
        jPanel53.setLayout(jPanel53Layout);
        jPanel53Layout.setHorizontalGroup(
            jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel53Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel27)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel53Layout.setVerticalGroup(
            jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel53Layout.createSequentialGroup()
                .addComponent(jLabel27)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 170, 70, 67));

        jPanel61.setBackground(new java.awt.Color(255, 255, 255));

        jLabel36.setText("36");

        javax.swing.GroupLayout jPanel61Layout = new javax.swing.GroupLayout(jPanel61);
        jPanel61.setLayout(jPanel61Layout);
        jPanel61Layout.setHorizontalGroup(
            jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel61Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel36)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel61Layout.setVerticalGroup(
            jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel61Layout.createSequentialGroup()
                .addComponent(jLabel36)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel61, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 250, 70, 67));

        jPanel64.setBackground(new java.awt.Color(255, 255, 255));

        jLabel66.setText("48");

        javax.swing.GroupLayout jPanel64Layout = new javax.swing.GroupLayout(jPanel64);
        jPanel64.setLayout(jPanel64Layout);
        jPanel64Layout.setHorizontalGroup(
            jPanel64Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel64Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel66)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel64Layout.setVerticalGroup(
            jPanel64Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel64Layout.createSequentialGroup()
                .addComponent(jLabel66)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 330, 70, 67));

        jPanel49.setBackground(new java.awt.Color(255, 255, 255));

        jLabel18.setText("18");

        javax.swing.GroupLayout jPanel49Layout = new javax.swing.GroupLayout(jPanel49);
        jPanel49.setLayout(jPanel49Layout);
        jPanel49Layout.setHorizontalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel49Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel49Layout.setVerticalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel49Layout.createSequentialGroup()
                .addComponent(jLabel18)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 90, 70, 67));

        jPanel81.setBackground(new java.awt.Color(255, 255, 255));

        jLabel64.setText("76");

        javax.swing.GroupLayout jPanel81Layout = new javax.swing.GroupLayout(jPanel81);
        jPanel81.setLayout(jPanel81Layout);
        jPanel81Layout.setHorizontalGroup(
            jPanel81Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel81Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel64)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel81Layout.setVerticalGroup(
            jPanel81Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel81Layout.createSequentialGroup()
                .addComponent(jLabel64)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel81, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 570, 70, 67));

        jPanel46.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setText("10");

        javax.swing.GroupLayout jPanel46Layout = new javax.swing.GroupLayout(jPanel46);
        jPanel46.setLayout(jPanel46Layout);
        jPanel46Layout.setHorizontalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel46Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel46Layout.setVerticalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel46Layout.createSequentialGroup()
                .addComponent(jLabel11)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 10, 70, 67));

        jPanel73.setBackground(new java.awt.Color(255, 255, 255));

        jLabel70.setText("67");

        javax.swing.GroupLayout jPanel73Layout = new javax.swing.GroupLayout(jPanel73);
        jPanel73.setLayout(jPanel73Layout);
        jPanel73Layout.setHorizontalGroup(
            jPanel73Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel73Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel70)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel73Layout.setVerticalGroup(
            jPanel73Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel73Layout.createSequentialGroup()
                .addComponent(jLabel70)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel73, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 490, 70, 67));

        jPanel58.setBackground(new java.awt.Color(255, 255, 255));

        jLabel39.setText("39");

        javax.swing.GroupLayout jPanel58Layout = new javax.swing.GroupLayout(jPanel58);
        jPanel58.setLayout(jPanel58Layout);
        jPanel58Layout.setHorizontalGroup(
            jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel58Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel39)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel58Layout.setVerticalGroup(
            jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel58Layout.createSequentialGroup()
                .addComponent(jLabel39)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 250, 70, 67));

        jPanel68.setBackground(new java.awt.Color(255, 255, 255));

        jLabel75.setText("59");

        javax.swing.GroupLayout jPanel68Layout = new javax.swing.GroupLayout(jPanel68);
        jPanel68.setLayout(jPanel68Layout);
        jPanel68Layout.setHorizontalGroup(
            jPanel68Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel68Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel75)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel68Layout.setVerticalGroup(
            jPanel68Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel68Layout.createSequentialGroup()
                .addComponent(jLabel75)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 410, 70, 67));

        jPanel71.setBackground(new java.awt.Color(255, 255, 255));
        jPanel71.setForeground(new java.awt.Color(255, 255, 255));

        jLabel62.setText("56");

        javax.swing.GroupLayout jPanel71Layout = new javax.swing.GroupLayout(jPanel71);
        jPanel71.setLayout(jPanel71Layout);
        jPanel71Layout.setHorizontalGroup(
            jPanel71Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel71Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel62)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel71Layout.setVerticalGroup(
            jPanel71Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel71Layout.createSequentialGroup()
                .addComponent(jLabel62)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel71, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 410, 70, 67));

        jPanel45.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setText("9");

        javax.swing.GroupLayout jPanel45Layout = new javax.swing.GroupLayout(jPanel45);
        jPanel45.setLayout(jPanel45Layout);
        jPanel45Layout.setHorizontalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel45Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        jPanel45Layout.setVerticalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel45Layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 10, 70, 67));

        jPanel51.setBackground(new java.awt.Color(255, 255, 255));
        jPanel51.setForeground(new java.awt.Color(255, 255, 255));

        jLabel16.setText("16");

        javax.swing.GroupLayout jPanel51Layout = new javax.swing.GroupLayout(jPanel51);
        jPanel51.setLayout(jPanel51Layout);
        jPanel51Layout.setHorizontalGroup(
            jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel51Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel51Layout.setVerticalGroup(
            jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel51Layout.createSequentialGroup()
                .addComponent(jLabel16)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 90, 70, 67));

        jPanel79.setBackground(new java.awt.Color(255, 255, 255));

        jLabel74.setText("78");

        javax.swing.GroupLayout jPanel79Layout = new javax.swing.GroupLayout(jPanel79);
        jPanel79.setLayout(jPanel79Layout);
        jPanel79Layout.setHorizontalGroup(
            jPanel79Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel79Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel74)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel79Layout.setVerticalGroup(
            jPanel79Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel79Layout.createSequentialGroup()
                .addComponent(jLabel74)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel79, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 570, 70, 67));

        jPanel48.setBackground(new java.awt.Color(255, 255, 255));

        jLabel19.setText("19");

        javax.swing.GroupLayout jPanel48Layout = new javax.swing.GroupLayout(jPanel48);
        jPanel48.setLayout(jPanel48Layout);
        jPanel48Layout.setHorizontalGroup(
            jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel48Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel48Layout.setVerticalGroup(
            jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel48Layout.createSequentialGroup()
                .addComponent(jLabel19)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 90, 70, 67));

        jPanel62.setBackground(new java.awt.Color(255, 255, 255));
        jPanel62.setForeground(new java.awt.Color(255, 255, 255));

        jLabel61.setText("46");

        javax.swing.GroupLayout jPanel62Layout = new javax.swing.GroupLayout(jPanel62);
        jPanel62.setLayout(jPanel62Layout);
        jPanel62Layout.setHorizontalGroup(
            jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel62Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel61)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel62Layout.setVerticalGroup(
            jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel62Layout.createSequentialGroup()
                .addComponent(jLabel61)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 330, 70, 67));

        jPanel42.setBackground(new java.awt.Color(255, 255, 255));
        jPanel42.setForeground(new java.awt.Color(255, 255, 255));

        jLabel6.setText("6");

        javax.swing.GroupLayout jPanel42Layout = new javax.swing.GroupLayout(jPanel42);
        jPanel42.setLayout(jPanel42Layout);
        jPanel42Layout.setHorizontalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        jPanel42Layout.setVerticalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 10, 70, -1));

        jPanel65.setBackground(new java.awt.Color(255, 255, 255));

        jLabel67.setText("49");

        javax.swing.GroupLayout jPanel65Layout = new javax.swing.GroupLayout(jPanel65);
        jPanel65.setLayout(jPanel65Layout);
        jPanel65Layout.setHorizontalGroup(
            jPanel65Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel65Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel67)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel65Layout.setVerticalGroup(
            jPanel65Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel65Layout.createSequentialGroup()
                .addComponent(jLabel67)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 330, 70, -1));

        jPanel66.setBackground(new java.awt.Color(255, 255, 255));

        jLabel68.setText("50");

        javax.swing.GroupLayout jPanel66Layout = new javax.swing.GroupLayout(jPanel66);
        jPanel66.setLayout(jPanel66Layout);
        jPanel66Layout.setHorizontalGroup(
            jPanel66Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel66Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel68)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel66Layout.setVerticalGroup(
            jPanel66Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel66Layout.createSequentialGroup()
                .addComponent(jLabel68)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel66, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 330, 70, 67));

        jPanel63.setBackground(new java.awt.Color(255, 255, 255));

        jLabel65.setText("47");

        javax.swing.GroupLayout jPanel63Layout = new javax.swing.GroupLayout(jPanel63);
        jPanel63.setLayout(jPanel63Layout);
        jPanel63Layout.setHorizontalGroup(
            jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel63Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel65)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel63Layout.setVerticalGroup(
            jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel63Layout.createSequentialGroup()
                .addComponent(jLabel65)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 330, 70, 67));

        jPanel56.setBackground(new java.awt.Color(255, 255, 255));

        jLabel30.setText("30");

        javax.swing.GroupLayout jPanel56Layout = new javax.swing.GroupLayout(jPanel56);
        jPanel56.setLayout(jPanel56Layout);
        jPanel56Layout.setHorizontalGroup(
            jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel56Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel30)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel56Layout.setVerticalGroup(
            jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel56Layout.createSequentialGroup()
                .addComponent(jLabel30)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 170, 70, 67));

        jPanel78.setBackground(new java.awt.Color(255, 255, 255));

        jLabel77.setText("79");

        javax.swing.GroupLayout jPanel78Layout = new javax.swing.GroupLayout(jPanel78);
        jPanel78.setLayout(jPanel78Layout);
        jPanel78Layout.setHorizontalGroup(
            jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel78Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel77)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel78Layout.setVerticalGroup(
            jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel78Layout.createSequentialGroup()
                .addComponent(jLabel77)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel78, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 570, 70, 67));

        jPanel67.setBackground(new java.awt.Color(255, 255, 255));

        jLabel78.setText("60");

        javax.swing.GroupLayout jPanel67Layout = new javax.swing.GroupLayout(jPanel67);
        jPanel67.setLayout(jPanel67Layout);
        jPanel67Layout.setHorizontalGroup(
            jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel67Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel78)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel67Layout.setVerticalGroup(
            jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel67Layout.createSequentialGroup()
                .addComponent(jLabel78)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 410, 70, -1));

        jPanel69.setBackground(new java.awt.Color(255, 255, 255));

        jLabel72.setText("58");

        javax.swing.GroupLayout jPanel69Layout = new javax.swing.GroupLayout(jPanel69);
        jPanel69.setLayout(jPanel69Layout);
        jPanel69Layout.setHorizontalGroup(
            jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel69Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel72)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel69Layout.setVerticalGroup(
            jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel69Layout.createSequentialGroup()
                .addComponent(jLabel72)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 410, 70, 67));

        jPanel72.setBackground(new java.awt.Color(255, 255, 255));

        jLabel63.setText("66");

        javax.swing.GroupLayout jPanel72Layout = new javax.swing.GroupLayout(jPanel72);
        jPanel72.setLayout(jPanel72Layout);
        jPanel72Layout.setHorizontalGroup(
            jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel72Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel63)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel72Layout.setVerticalGroup(
            jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel72Layout.createSequentialGroup()
                .addComponent(jLabel63)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel72, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 490, 70, 67));

        jPanel52.setBackground(new java.awt.Color(255, 255, 255));

        jLabel26.setText("26");

        javax.swing.GroupLayout jPanel52Layout = new javax.swing.GroupLayout(jPanel52);
        jPanel52.setLayout(jPanel52Layout);
        jPanel52Layout.setHorizontalGroup(
            jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel52Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel26)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel52Layout.setVerticalGroup(
            jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel52Layout.createSequentialGroup()
                .addComponent(jLabel26)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 170, 70, 67));

        jPanel55.setBackground(new java.awt.Color(255, 255, 255));

        jLabel29.setText("29");

        javax.swing.GroupLayout jPanel55Layout = new javax.swing.GroupLayout(jPanel55);
        jPanel55.setLayout(jPanel55Layout);
        jPanel55Layout.setHorizontalGroup(
            jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel55Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel29)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel55Layout.setVerticalGroup(
            jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel55Layout.createSequentialGroup()
                .addComponent(jLabel29)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 170, 70, 67));

        jPanel75.setBackground(new java.awt.Color(255, 255, 255));

        jLabel76.setText("69");

        javax.swing.GroupLayout jPanel75Layout = new javax.swing.GroupLayout(jPanel75);
        jPanel75.setLayout(jPanel75Layout);
        jPanel75Layout.setHorizontalGroup(
            jPanel75Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel75Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel76)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel75Layout.setVerticalGroup(
            jPanel75Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel75Layout.createSequentialGroup()
                .addComponent(jLabel76)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel75, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 490, 70, 67));

        jPanel80.setBackground(new java.awt.Color(255, 255, 255));

        jLabel71.setText("77");

        javax.swing.GroupLayout jPanel80Layout = new javax.swing.GroupLayout(jPanel80);
        jPanel80.setLayout(jPanel80Layout);
        jPanel80Layout.setHorizontalGroup(
            jPanel80Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel80Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel71)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel80Layout.setVerticalGroup(
            jPanel80Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel80Layout.createSequentialGroup()
                .addComponent(jLabel71)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel80, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 570, 70, 67));

        jPanel77.setBackground(new java.awt.Color(255, 255, 255));

        jLabel80.setText("80");

        javax.swing.GroupLayout jPanel77Layout = new javax.swing.GroupLayout(jPanel77);
        jPanel77.setLayout(jPanel77Layout);
        jPanel77Layout.setHorizontalGroup(
            jPanel77Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel77Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel80)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel77Layout.setVerticalGroup(
            jPanel77Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel77Layout.createSequentialGroup()
                .addComponent(jLabel80)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel77, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 570, 70, 67));

        jPanel50.setBackground(new java.awt.Color(255, 255, 255));

        jLabel17.setText("17");

        javax.swing.GroupLayout jPanel50Layout = new javax.swing.GroupLayout(jPanel50);
        jPanel50.setLayout(jPanel50Layout);
        jPanel50Layout.setHorizontalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel50Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel50Layout.setVerticalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel50Layout.createSequentialGroup()
                .addComponent(jLabel17)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 90, 70, 67));

        jPanel54.setBackground(new java.awt.Color(255, 255, 255));

        jLabel28.setText("28");

        javax.swing.GroupLayout jPanel54Layout = new javax.swing.GroupLayout(jPanel54);
        jPanel54.setLayout(jPanel54Layout);
        jPanel54Layout.setHorizontalGroup(
            jPanel54Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel54Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel28)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel54Layout.setVerticalGroup(
            jPanel54Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel54Layout.createSequentialGroup()
                .addComponent(jLabel28)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 170, 70, 67));

        jPanel74.setBackground(new java.awt.Color(255, 255, 255));

        jLabel73.setText("68");

        javax.swing.GroupLayout jPanel74Layout = new javax.swing.GroupLayout(jPanel74);
        jPanel74.setLayout(jPanel74Layout);
        jPanel74Layout.setHorizontalGroup(
            jPanel74Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel74Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel73)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel74Layout.setVerticalGroup(
            jPanel74Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel74Layout.createSequentialGroup()
                .addComponent(jLabel73)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel74, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 490, 70, 67));

        jPanel44.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setText("8");

        javax.swing.GroupLayout jPanel44Layout = new javax.swing.GroupLayout(jPanel44);
        jPanel44.setLayout(jPanel44Layout);
        jPanel44Layout.setHorizontalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel44Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        jPanel44Layout.setVerticalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel44Layout.createSequentialGroup()
                .addComponent(jLabel8)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 10, 70, 67));

        jPanel59.setBackground(new java.awt.Color(255, 255, 255));

        jLabel38.setText("38");

        javax.swing.GroupLayout jPanel59Layout = new javax.swing.GroupLayout(jPanel59);
        jPanel59.setLayout(jPanel59Layout);
        jPanel59Layout.setHorizontalGroup(
            jPanel59Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel59Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel38)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel59Layout.setVerticalGroup(
            jPanel59Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel59Layout.createSequentialGroup()
                .addComponent(jLabel38)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 250, 70, 67));

        jPanel47.setBackground(new java.awt.Color(255, 255, 255));

        jLabel20.setText("20");

        javax.swing.GroupLayout jPanel47Layout = new javax.swing.GroupLayout(jPanel47);
        jPanel47.setLayout(jPanel47Layout);
        jPanel47Layout.setHorizontalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel47Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel47Layout.setVerticalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel47Layout.createSequentialGroup()
                .addComponent(jLabel20)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 90, 70, 67));

        jPanel43.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setText("7");

        javax.swing.GroupLayout jPanel43Layout = new javax.swing.GroupLayout(jPanel43);
        jPanel43.setLayout(jPanel43Layout);
        jPanel43Layout.setHorizontalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel43Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        jPanel43Layout.setVerticalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel43Layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 10, 70, -1));

        jPanel60.setBackground(new java.awt.Color(255, 255, 255));

        jLabel37.setText("37");

        javax.swing.GroupLayout jPanel60Layout = new javax.swing.GroupLayout(jPanel60);
        jPanel60.setLayout(jPanel60Layout);
        jPanel60Layout.setHorizontalGroup(
            jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel60Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel37)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel60Layout.setVerticalGroup(
            jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel60Layout.createSequentialGroup()
                .addComponent(jLabel37)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 250, 70, 67));

        jPanel57.setBackground(new java.awt.Color(255, 255, 255));

        jLabel40.setText("40");

        javax.swing.GroupLayout jPanel57Layout = new javax.swing.GroupLayout(jPanel57);
        jPanel57.setLayout(jPanel57Layout);
        jPanel57Layout.setHorizontalGroup(
            jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel57Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel40)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel57Layout.setVerticalGroup(
            jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel57Layout.createSequentialGroup()
                .addComponent(jLabel40)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 250, 70, 67));

        jPanel70.setBackground(new java.awt.Color(255, 255, 255));

        jLabel69.setText("57");

        javax.swing.GroupLayout jPanel70Layout = new javax.swing.GroupLayout(jPanel70);
        jPanel70.setLayout(jPanel70Layout);
        jPanel70Layout.setHorizontalGroup(
            jPanel70Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel70Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel69)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel70Layout.setVerticalGroup(
            jPanel70Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel70Layout.createSequentialGroup()
                .addComponent(jLabel69)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel70, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 410, 70, 67));

        jPanel76.setBackground(new java.awt.Color(255, 255, 255));

        jLabel79.setText("70");

        javax.swing.GroupLayout jPanel76Layout = new javax.swing.GroupLayout(jPanel76);
        jPanel76.setLayout(jPanel76Layout);
        jPanel76Layout.setHorizontalGroup(
            jPanel76Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel76Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel79)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel76Layout.setVerticalGroup(
            jPanel76Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel76Layout.createSequentialGroup()
                .addComponent(jLabel79)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel76, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 490, 70, 67));

        createFile.setText("Crear Archivo");
        createFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createFileActionPerformed(evt);
            }
        });
        jPanel1.add(createFile, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 650, 120, -1));

        createDir.setText("Crear Directorio");
        createDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createDirActionPerformed(evt);
            }
        });
        jPanel1.add(createDir, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 650, -1, -1));

        change.setText("Cambiar de Modo");
        change.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeActionPerformed(evt);
            }
        });
        jPanel1.add(change, new org.netbeans.lib.awtextra.AbsoluteConstraints(395, 650, 140, -1));

        modo.setText("Actual: Administrador");
        jPanel1.add(modo, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 680, -1, -1));

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        Tree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane3.setViewportView(Tree);

        jPanel1.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 380, 330));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1316, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void changeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeActionPerformed
        // TODO add your handling code here:
        if(this.mode=="Administrador"){
            this.setMode("Usuario");
            this.createDir.setVisible(false);
            this.createFile.setVisible(false);
            this.modo.setText("Actual: Usuario");
        }else{
            this.setMode("Administrador");
            this.createDir.setVisible(true);
            this.createFile.setVisible(true);
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
                new FileSystemSimulator().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabla;
    private javax.swing.JTree Tree;
    private javax.swing.JButton change;
    private javax.swing.JButton createDir;
    private javax.swing.JButton createFile;
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel54;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel57;
    private javax.swing.JPanel jPanel58;
    private javax.swing.JPanel jPanel59;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel60;
    private javax.swing.JPanel jPanel61;
    private javax.swing.JPanel jPanel62;
    private javax.swing.JPanel jPanel63;
    private javax.swing.JPanel jPanel64;
    private javax.swing.JPanel jPanel65;
    private javax.swing.JPanel jPanel66;
    private javax.swing.JPanel jPanel67;
    private javax.swing.JPanel jPanel68;
    private javax.swing.JPanel jPanel69;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel70;
    private javax.swing.JPanel jPanel71;
    private javax.swing.JPanel jPanel72;
    private javax.swing.JPanel jPanel73;
    private javax.swing.JPanel jPanel74;
    private javax.swing.JPanel jPanel75;
    private javax.swing.JPanel jPanel76;
    private javax.swing.JPanel jPanel77;
    private javax.swing.JPanel jPanel78;
    private javax.swing.JPanel jPanel79;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel80;
    private javax.swing.JPanel jPanel81;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel modo;
    // End of variables declaration//GEN-END:variables
}
