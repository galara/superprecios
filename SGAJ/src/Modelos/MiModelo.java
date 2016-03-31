/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

/**
 *
 * @author Otto
 */
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Guille
 */
public class MiModelo extends DefaultTableModel
{
   public boolean isCellEditable (int row, int column)
   {
       // Aquí devolvemos true o false según queramos que una celda
       // identificada por fila,columna (row,column), sea o no editable
       //JOptionPane.showMessageDialog(null, row+" "+column);
 return false;

   }
}