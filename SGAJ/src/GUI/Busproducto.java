/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import BD.BdConexion;
import BD.sqlprod;
import Modelos.FormatoFecha;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
//import eliminar.ESproducto;
import reporte.GeneraReportes;

/**
 *
 * @author 30234
 */
public class Busproducto extends javax.swing.JInternalFrame {

    /**
     * Creates new form busquedaproducto
     */
    DefaultTableModel model;
    String[] titulos = {"Id", "Codigo", "Descripción", "Precio", "Stock", "Marca"};
    int estado = 0;
    java.sql.Connection conn;//getConnection intentara establecer una conexión.
    Statement sent;
    public static String id;
    public static java.util.Date fecha, fechafin;

    public Busproducto() {
        initComponents();
        formatotabla();
        addEscapeKey();

        tablaproductos.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent arg0) {
                int key = arg0.getKeyCode();
                if (key == java.awt.event.KeyEvent.VK_ENTER) {
                    int p = tablaproductos.getSelectedRow();

                    reportefecha(Fincial.getDate(), Ffinal.getDate(), tablaproductos.getValueAt(p, 0).toString());
                    String nombrereporte = "salidaproducto.jasper";
                    Map parametros = new HashMap();
                    parametros.put("fecha1", fechaR());
                    parametros.put("fechaf", fechaRFin());
                    parametros.put("id", id());
                    //System.out.print(fechaR()+"\n"+fechaRFin()+"\n"+id()+"\n");
                    GeneraReportes.AbrirReporte(nombrereporte, parametros);

                }
            }
        });
        formatotabla();
    }

    public void reportefecha(java.util.Date fechas, java.util.Date fechaf, String ids) {
        fecha = fechas;
        id = ids;
        fechafin = fechaf;
    }

    public static String id() {
        return id;
    }

    public static java.util.Date fechaR() {
        return fecha;
    }

    public static java.util.Date fechaRFin() {
        return fechafin;
    }

    public void fecha(java.util.Date g) {
        fecha = g;
    }

    private void addEscapeKey() {
        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        Action escapeAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        };
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);
    }

    public void removejtable() {
        while (tablaproductos.getRowCount() != 0) {
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
        for (int i = 0; i < 6; i++) {
            column = tablaproductos.getColumnModel().getColumn(i);

            if (i == 0) {
                //column.setPreferredWidth(5); //Difine el ancho de la columna
                //tablaproductos.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);
                column.setMaxWidth(0);
                column.setMinWidth(0);
            } else if (i == 2) {
                column.setPreferredWidth(185); //Difine el ancho de la columna
            } else if (i == 1 || i == 3 || i == 4) {
                column.setPreferredWidth(45);//Difine el ancho de la columna
                tablaproductos.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);
            } else if (i == 5) {
                column.setPreferredWidth(85);//Difine el ancho de la columna
                tablaproductos.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);
            }
        }
    }

    private void Llenar() {
        try {
            removejtable();
            //conn = BdConexion.getConexion();
            conn = BdConexion.getConexion();
            sent = conn.createStatement();// crea objeto Statement para consultar la base de datos
            ResultSet rs = sent.executeQuery(/*sql*/sqlprod.LLENAR + sqlprod.ORDER_BY);// especifica la consulta y la ejecuta
            String[] fila = new String[6];
            while (rs.next()) {
                fila[0] = rs.getString("producto.idproducto");
                fila[1] = rs.getString("producto.Codigo");
                fila[2] = rs.getString("producto.nombre");
                fila[3] = rs.getString("producto.preciocompra");
                fila[4] = rs.getString("producto.cantidad");
                fila[5] = rs.getString("marca.nombre");
                model.addRow(fila);
            }
            tablaproductos.setModel(model);
            formatotabla();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en BD " + e.toString());
            this.dispose();
        }
    }

    private void MostrarTodo(String Dato) {
        try {

            //conn = BdConexion.getConexion();
            conn = BdConexion.getConexion();
            String sql = "";

            if (rbcodigo.isSelected()) {
                sql = sqlprod.BUSCANIT + "'" + Dato + "'";
            }
            if (rbnombre.isSelected()) {
                sql = sqlprod.BUSCANOMBRE + Dato + sqlprod.CUALQUIERA + " order by producto.idproducto asc";
            }
            if (rbmarca.isSelected()) {
                sql = sqlprod.BMARCA + Dato + sqlprod.CUALQUIERA + " order by producto.idproducto asc";
            }
            removejtable();
            sent = conn.createStatement();// crea objeto Statement para consultar la base de datos
            ResultSet rs = sent.executeQuery(sql);// especifica la consulta y la ejecuta

            if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                int count = 0;
                rs.beforeFirst();//regresa el puntero al primer registro
                String[] fila = new String[6];
                while (rs.next()) {
                    fila[0] = rs.getString("producto.idproducto");
                    fila[1] = rs.getString("producto.Codigo");
                    fila[2] = rs.getString("producto.nombre");
                    fila[3] = rs.getString("producto.preciocompra");
                    fila[4] = rs.getString("producto.cantidad");
                    fila[5] = rs.getString("marca.nombre");
                    model.addRow(fila);
                    count = count + 1;
                }
                tablaproductos.setModel(model);
                formatotabla();
                ////conn.close();
                //JOptionPane.showInternalMessageDialog(this, "Se encontraron " + count + " registros");

            } else {
                JOptionPane.showInternalMessageDialog(this, "El dato no fue encontrado");
            }
        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(this, "Error al cargar los datos");
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
        jLabel2 = new javax.swing.JLabel();
        Fincial = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        Ffinal = new com.toedter.calendar.JDateChooser();
        jPanelproductos = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tablaproductos = new javax.swing.JTable();
        pnlPaginador1 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        lblbuscar = new javax.swing.JLabel();
        txtDato = new elaprendiz.gui.textField.TextField();
        rbcodigo = new javax.swing.JRadioButton();
        rbnombre = new javax.swing.JRadioButton();
        rbmarca = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();

        setClosable(true);
        setTitle("Busqueda de productos");
        setName("Busproducto"); // NOI18N

        jcMousePanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jcMousePanel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fondo/índice.jpg"))); // NOI18N
        jcMousePanel4.setModo(4);
        jcMousePanel4.setName(""); // NOI18N
        jcMousePanel4.setOpaque(false);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Fecha Inicial: ");

        Fincial.setDate(Calendar.getInstance().getTime());

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Fecha Final: ");

        Ffinal.setDate(Calendar.getInstance().getTime());

        jPanelproductos.setBackground(new java.awt.Color(255, 255, 255));
        jPanelproductos.setBorder(javax.swing.BorderFactory.createTitledBorder("Enter reporte de producto"));
        jPanelproductos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaproductos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tablaproductos.setModel(model = new DefaultTableModel(null, titulos)
            {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
            tablaproductos.setRowHeight(20);
            jScrollPane6.setViewportView(tablaproductos);

            jPanelproductos.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 16, 610, 210));

            pnlPaginador1.setBackground(new java.awt.Color(0, 153, 204));
            pnlPaginador1.setPreferredSize(new java.awt.Dimension(786, 40));
            pnlPaginador1.setLayout(new java.awt.GridBagLayout());

            jLabel21.setFont(new java.awt.Font("Script MT Bold", 1, 32)); // NOI18N
            jLabel21.setForeground(new java.awt.Color(255, 255, 255));
            jLabel21.setText("<--Buscar Productos-->");
            pnlPaginador1.add(jLabel21, new java.awt.GridBagConstraints());

            jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            jPanel7.setOpaque(false);
            jPanel7.setLayout(null);

            lblbuscar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            lblbuscar.setText("Buscar Producto:");
            jPanel7.add(lblbuscar);
            lblbuscar.setBounds(90, 20, 120, 30);

            txtDato.setPreferredSize(new java.awt.Dimension(250, 27));
            txtDato.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    txtDatoActionPerformed(evt);
                }
            });
            jPanel7.add(txtDato);
            txtDato.setBounds(220, 20, 290, 27);

            rbcodigo.setBackground(new java.awt.Color(51, 153, 255));
            rbcodigo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            rbcodigo.setForeground(new java.awt.Color(255, 255, 255));
            rbcodigo.setText("Código");
            rbcodigo.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    rbcodigoActionPerformed(evt);
                }
            });
            jPanel7.add(rbcodigo);
            rbcodigo.setBounds(160, 60, 75, 25);

            rbnombre.setBackground(new java.awt.Color(51, 153, 255));
            rbnombre.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            rbnombre.setForeground(new java.awt.Color(255, 255, 255));
            rbnombre.setSelected(true);
            rbnombre.setText("Nombre");
            rbnombre.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    rbnombreActionPerformed(evt);
                }
            });
            jPanel7.add(rbnombre);
            rbnombre.setBounds(260, 60, 81, 25);

            rbmarca.setBackground(new java.awt.Color(51, 153, 255));
            rbmarca.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            rbmarca.setForeground(new java.awt.Color(255, 255, 255));
            rbmarca.setText("Marca");
            rbmarca.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    rbmarcaActionPerformed(evt);
                }
            });
            jPanel7.add(rbmarca);
            rbmarca.setBounds(370, 60, 67, 25);

            jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabel1.setForeground(new java.awt.Color(255, 255, 255));
            jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel1.setText("Enter para buscar producto");
            jPanel7.add(jLabel1);
            jLabel1.setBounds(220, 3, 290, 20);

            javax.swing.GroupLayout jcMousePanel4Layout = new javax.swing.GroupLayout(jcMousePanel4);
            jcMousePanel4.setLayout(jcMousePanel4Layout);
            jcMousePanel4Layout.setHorizontalGroup(
                jcMousePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlPaginador1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jcMousePanel4Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelproductos, javax.swing.GroupLayout.PREFERRED_SIZE, 622, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
                .addGroup(jcMousePanel4Layout.createSequentialGroup()
                    .addGap(129, 129, 129)
                    .addComponent(jLabel2)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(Fincial, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabel3)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(Ffinal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jcMousePanel4Layout.setVerticalGroup(
                jcMousePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jcMousePanel4Layout.createSequentialGroup()
                    .addComponent(pnlPaginador1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jcMousePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(Fincial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Ffinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                    .addComponent(jPanelproductos, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jcMousePanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jcMousePanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void rbcodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbcodigoActionPerformed
        // TODO add your handling code here:
        rbnombre.setSelected(false);
        rbmarca.setSelected(false);
        //rbCategoria.setSelected(false);
    }//GEN-LAST:event_rbcodigoActionPerformed

    private void rbnombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbnombreActionPerformed
        // TODO add your handling code here:
        rbcodigo.setSelected(false);
        rbmarca.setSelected(false);
        //rbCategoria.setSelected(false);
    }//GEN-LAST:event_rbnombreActionPerformed

    private void rbmarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbmarcaActionPerformed
        // TODO add your handling code here:
        rbnombre.setSelected(false);
        rbcodigo.setSelected(false);
        //rbCategoria.setSelected(false);
    }//GEN-LAST:event_rbmarcaActionPerformed

    private void txtDatoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDatoActionPerformed
        // TODO add your handling code here:
//        if (!txtDato.getText().isEmpty()) {
//            {
        MostrarTodo(txtDato.getText());
//            }
//        } else {
//            JOptionPane.showInternalMessageDialog(this, "No hay dato que buscar  ", "Error", JOptionPane.ERROR_MESSAGE);
//        }
    }//GEN-LAST:event_txtDatoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser Ffinal;
    private com.toedter.calendar.JDateChooser Fincial;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanelproductos;
    private javax.swing.JScrollPane jScrollPane6;
    private jcMousePanel.jcMousePanel jcMousePanel4;
    private javax.swing.JLabel lblbuscar;
    private javax.swing.JPanel pnlPaginador1;
    private javax.swing.JRadioButton rbcodigo;
    private javax.swing.JRadioButton rbmarca;
    private javax.swing.JRadioButton rbnombre;
    private javax.swing.JTable tablaproductos;
    private elaprendiz.gui.textField.TextField txtDato;
    // End of variables declaration//GEN-END:variables

}
