/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import BD.BdConexion;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author 30234
 */
public class detalledevolucionesclientes extends javax.swing.JInternalFrame {

    /**
     * Creates new form busquedaproducto
     */
    DefaultTableModel model;
    String[] titulos = {"Id","Codigo","Nombre","Cantidad","Precio","Subtotal","Lote","No.Venta","Factura","Fecha","Usuario"};
    int estado = 0;
    java.sql.Connection conn;//getConnection intentara establecer una conexión.
    Statement sent;
    SimpleDateFormat formatof = new SimpleDateFormat("dd/MM/yyyy");
    

    public detalledevolucionesclientes() {
        initComponents();
        formatotabla();
        addEscapeKey();
    }
    
    private void addEscapeKey() {
        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        Action escapeAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        };
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);
    }
    
    public void removejtable() {
        while (detalleabonos.getRowCount() != 0) {
            model.removeRow(0);
        }
    }

    private void formatotabla() {
        //TableCellRenderer clase que se encarga de dibujar los datos que hay en cada celda la cual podemos modificar
        //nos proporciona la posibilidad de cambiar su aspercto por uno personalizado y no el standar.
        DefaultTableCellRenderer modelocentrar = new DefaultTableCellRenderer();
        modelocentrar.setHorizontalAlignment(SwingConstants.CENTER);

        //TableColumn representa todos los atributos de una columna en un JTable , como el ancho, resizibility, mínimo y máximo ancho
        //en este caso defien el ancho de cada columna las cuales pueden ser de distinto ancho.
        TableColumn column;// = null;
        for (int i = 0; i < 11; i++) {
            column = detalleabonos.getColumnModel().getColumn(i);
            //"Id", "Fecha", "Monto", "No.Compra", "No.Doc", "Usuario
            if (i == 0) {
                //column.setPreferredWidth(5); //Difine el ancho de la columna
                //tablaproductos.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);
                column.setMaxWidth(0);
                column.setMinWidth(0);
            } 
                else if ( i==1 || i > 2 & i <= 8 ) {
                column.setPreferredWidth(27);//Difine el ancho de la columna
                detalleabonos.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);
            }
            else if ( i== 2 ) {
                column.setPreferredWidth(200);//Difine el ancho de la columna
                detalleabonos.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);
            }
            else if ( i==9 || i== 10) {
                column.setPreferredWidth(35);//Difine el ancho de la columna
                detalleabonos.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);
            }
        }
    }

    public void MostrarTodo(String Dato) {
        try {

            conn = BdConexion.getConexion();
            String sql = "";
            sql = "select producto.idproducto,producto.codigo,producto.nombre,devoluciones.cantidad,devoluciones.precio,devoluciones.subtotal,devoluciones.idlote,devoluciones.idcompra,salida.salida,devoluciones.fecha,usuario.nombreusuario,devoluciones.entradasalida from devoluciones INNER JOIN producto on producto.idproducto=devoluciones.idproducto INNER JOIN salida on salida.idsalida=devoluciones.idcompra INNER JOIN usuario on usuario.idusuario=devoluciones.idusuario where devoluciones.idcompra=" + Dato + " and devoluciones.entradasalida='CLIENTE' order by devoluciones.fecha asc";
            removejtable();
            sent = conn.createStatement();// crea objeto Statement para consultar la base de datos
            ResultSet rs = sent.executeQuery(sql);// especifica la consulta y la ejecuta

            if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                //int count = 0;
                rs.beforeFirst();//regresa el puntero al primer registro
                String[] fila = new String[11];
                while (rs.next()) {
                   // "Id","Codigo","Nombre","Cantidad","Precio","Subtotal","Lote","No.Venta","Factura","Fecha","Usuario"
                    fila[0] = rs.getString("producto.idproducto");
                    fila[1] = rs.getString("producto.codigo");
                    fila[2] = rs.getString("producto.nombre");
                    fila[3] = rs.getString("devoluciones.cantidad");
                    fila[4] = rs.getString("devoluciones.precio");
                    fila[5] = rs.getString("devoluciones.subtotal");
                    fila[6] = rs.getString("devoluciones.idlote");
                    fila[7] = rs.getString("devoluciones.idcompra");
                    fila[8] = rs.getString("salida.salida");
                    fila[9] = formatof.format(rs.getDate("devoluciones.fecha"));
                    fila[10] = rs.getString("usuario.nombreusuario");
                    
                    model.addRow(fila);
                    //count = count + 1;
                }
                detalleabonos.setModel(model);
                formatotabla();
                //JOptionPane.showInternalMessageDialog(this, "Se encontraron " + count + " registros");

            } else {
                JOptionPane.showInternalMessageDialog(this, " No se encontraron abonos ");
            }
            //conn.close();
        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(this, "Ocurrio un error al cargar los datos ");
            System.out.print(e.getMessage());
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

        jcMousePanel4 = new jcMousePanel.jcMousePanel();
        jPaneldetalleabonos = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        detalleabonos = new javax.swing.JTable();
        pnlPaginador1 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();

        setTitle("Detalle de Devoluciones de Cliente");
        setName("detalledevolucionesclientes"); // NOI18N

        jcMousePanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jcMousePanel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fondo/índice.jpg"))); // NOI18N
        jcMousePanel4.setModo(4);
        jcMousePanel4.setName(""); // NOI18N
        jcMousePanel4.setOpaque(false);

        jPaneldetalleabonos.setBackground(new java.awt.Color(255, 255, 255));
        jPaneldetalleabonos.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPaneldetalleabonos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        detalleabonos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        detalleabonos.setModel(model = new DefaultTableModel(null, titulos)
            {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
            detalleabonos.setRowHeight(20);
            jScrollPane6.setViewportView(detalleabonos);

            jPaneldetalleabonos.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 16, 780, 230));

            pnlPaginador1.setBackground(new java.awt.Color(0, 153, 204));
            pnlPaginador1.setPreferredSize(new java.awt.Dimension(786, 40));
            pnlPaginador1.setLayout(new java.awt.GridBagLayout());

            jLabel21.setFont(new java.awt.Font("Script MT Bold", 1, 32)); // NOI18N
            jLabel21.setForeground(new java.awt.Color(255, 255, 255));
            jLabel21.setText("<--Detalle de Devoluciones Cliente-->");
            pnlPaginador1.add(jLabel21, new java.awt.GridBagConstraints());

            jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            jPanel7.setOpaque(false);
            jPanel7.setLayout(null);

            javax.swing.GroupLayout jcMousePanel4Layout = new javax.swing.GroupLayout(jcMousePanel4);
            jcMousePanel4.setLayout(jcMousePanel4Layout);
            jcMousePanel4Layout.setHorizontalGroup(
                jcMousePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jcMousePanel4Layout.createSequentialGroup()
                    .addGroup(jcMousePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPaneldetalleabonos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pnlPaginador1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGap(0, 0, Short.MAX_VALUE))
            );
            jcMousePanel4Layout.setVerticalGroup(
                jcMousePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jcMousePanel4Layout.createSequentialGroup()
                    .addComponent(pnlPaginador1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPaneldetalleabonos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            );

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jcMousePanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jcMousePanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable detalleabonos;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPaneldetalleabonos;
    private javax.swing.JScrollPane jScrollPane6;
    private jcMousePanel.jcMousePanel jcMousePanel4;
    private javax.swing.JPanel pnlPaginador1;
    // End of variables declaration//GEN-END:variables
}
