/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Objetos;

/**
 *
 * @author nelsoncarrillo
 */
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ColorCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // Llama al método de la superclase para mantener el comportamiento predeterminado
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Verifica si el valor es un color
        if (value instanceof Color) {
            setText(""); // Elimina cualquier texto
            setBackground((Color) value); // Establece el color de fondo
        } else {
            setText(value != null ? value.toString() : ""); // Muestra el texto si no es un color
            setBackground(Color.WHITE); // Fondo blanco por defecto
        }

        return this;
    }
}
