package reporte;

import BD.BdConexion;
import BD.Conectiondb;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.*;

public class Rmovimientos2 extends javax.swing.JInternalFrame {

    static Connection conn = null;
    static Statement st = null;
    static ResultSet rs = null;
    static ResultSet rs2 = null;

    DefaultTableModel dtm = new DefaultTableModel();
    DefaultTableModel salida = new DefaultTableModel();
    SimpleDateFormat formatof = new SimpleDateFormat("dd/MM/yyyy");
    /*creamos el codigo constructor para el formulario de clientes*/

    public Rmovimientos2() {
        initComponents();
        addEscapeKey();
        //cargandor.setVisible(false);
        //activaBotones(true,false,false,false);
        String titulos[] = {"Id", "Factura", "Fecha", "Total", "Usuario"};
        dtm.setColumnIdentifiers(titulos);
        TablaCliente.setModel(dtm);

        String titulos2[] = {"Id", "Codigo", "Producto", "Cantidad", "Precio", "Factura"};
        salida.setColumnIdentifiers(titulos2);
        TablaDetalle.setModel(salida);

        TableColumn column = null;
        for (int i = 0; i < 5; i++) {
            column = TablaDetalle.getColumnModel().getColumn(i);
            if (i == 2) {
                column.setPreferredWidth(200); // la tercera columna sera la mas grande
            } else {
                column.setPreferredWidth(30);
            }
        }

        setSize(875, 617);
        setLocation(180, 100);
        this.setResizable(false);
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

    private String getFecha() {

        try {
            String fecha;
            int años = fechaini.getCalendar().get(Calendar.YEAR);
            int dias = fechaini.getCalendar().get(Calendar.DAY_OF_MONTH);
            int mess = fechaini.getCalendar().get(Calendar.MONTH) + 1;
            fecha = "" + años + "-" + mess + "-" + dias;
            return fecha;
        } catch (Exception e) {
            JOptionPane.showInternalMessageDialog(this, "Verifique la fecha");
            //System.out.print(e.getMessage());
        }
        return null;

    }

    private String getFecha2() {

        try {
            String fecha;
            int años = fechafin.getCalendar().get(Calendar.YEAR);
            int dias = fechafin.getCalendar().get(Calendar.DAY_OF_MONTH);
            int mess = fechafin.getCalendar().get(Calendar.MONTH) + 1;
            fecha = "" + años + "-" + mess + "-" + dias;
            return fecha;
        } catch (Exception e) {
            JOptionPane.showInternalMessageDialog(this, "Verifique la fecha");
            //System.out.print(e.getMessage());
        }
        return null;

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jcMousePanel2 = new jcMousePanel.jcMousePanel();
        jPanel3 = new javax.swing.JPanel();
        btnCerrar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TablaDetalle = new javax.swing.JTable();
        btnGenerar = new javax.swing.JButton();
        btnVer = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        fechaini = new org.freixas.jcalendar.JCalendarCombo();
        jLabel19 = new javax.swing.JLabel();
        fechafin = new org.freixas.jcalendar.JCalendarCombo();
        jScrollPane2 = new javax.swing.JScrollPane();
        TablaCliente = new javax.swing.JTable();

        setBackground(new java.awt.Color(192, 219, 213));
        setTitle("REPORTE DE INGRESOS");
        setName("Rmovimientos2"); // NOI18N
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jcMousePanel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fondo/índice.jpg"))); // NOI18N
        jcMousePanel2.setModo(4);
        jcMousePanel2.setName(""); // NOI18N
        jcMousePanel2.setOpaque(false);

        jPanel3.setBackground(new java.awt.Color(192, 219, 213));
        jPanel3.setOpaque(false);
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/exit.png"))); // NOI18N
        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });
        jPanel3.add(btnCerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 10, 120, 40));

        jPanel1.setBackground(new java.awt.Color(192, 219, 213));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setOpaque(false);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Detalle de Ingreso seleccionado:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, -1, 20));

        TablaDetalle.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        TablaDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Producto", "Cantidad", "Devolución", "Factura"
            }
        ));
        TablaDetalle.setGridColor(new java.awt.Color(204, 204, 204));
        TablaDetalle.setRowHeight(20);
        jScrollPane3.setViewportView(TablaDetalle);

        jPanel1.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 810, 190));

        btnGenerar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/print_64.png"))); // NOI18N
        btnGenerar.setText("Generar Comprobante de Ingreso");
        btnGenerar.setPreferredSize(new java.awt.Dimension(99, 41));
        btnGenerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarActionPerformed(evt);
            }
        });
        jPanel1.add(btnGenerar, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 460, 260, 50));

        btnVer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/order2.png"))); // NOI18N
        btnVer.setText("Ver Lista Ingresos");
        btnVer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerActionPerformed(evt);
            }
        });
        jPanel1.add(btnVer, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 180, -1));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Del:");
        jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 20, -1, 28));

        fechaini.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        fechaini.setName(""); // NOI18N
        fechaini.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerActionPerformed(evt);
            }
        });
        jPanel1.add(fechaini, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 20, 200, 28));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Al:");
        jPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 20, -1, 28));

        fechafin.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        fechafin.setName(""); // NOI18N
        fechafin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerActionPerformed(evt);
            }
        });
        jPanel1.add(fechafin, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 20, 200, 28));

        TablaCliente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        TablaCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Salida", "Fecha", "Total", "Cliente", "Usuario"
            }
        ));
        TablaCliente.setGridColor(new java.awt.Color(204, 204, 204));
        TablaCliente.setRowHeight(20);
        TablaCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaClienteMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                TablaClienteMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(TablaCliente);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 810, 160));

        jPanel3.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 840, 520));

        javax.swing.GroupLayout jcMousePanel2Layout = new javax.swing.GroupLayout(jcMousePanel2);
        jcMousePanel2.setLayout(jcMousePanel2Layout);
        jcMousePanel2Layout.setHorizontalGroup(
            jcMousePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 880, Short.MAX_VALUE)
            .addGroup(jcMousePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jcMousePanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 870, Short.MAX_VALUE)))
        );
        jcMousePanel2Layout.setVerticalGroup(
            jcMousePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
            .addGroup(jcMousePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jcMousePanel2Layout.createSequentialGroup()
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 20, Short.MAX_VALUE)))
        );

        getContentPane().add(jcMousePanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 880, 600));

        getAccessibleContext().setAccessibleName("Mantenimiento de cliente ===== Christian");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnVerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerActionPerformed
        // TODO add your handling code here:
        try {
            //setSize(700,600);
            int f, i;
            String fecha1, fecha2;
            fecha1 = getFecha();
            fecha2 = getFecha2();
            conn = BdConexion.getConexion();
            rs = Conectiondb.EnlIngresos(rs, fecha1, fecha2);
            String datos[] = new String[5];
            f = dtm.getRowCount();
            if (f > 0) {
                for (i = 0; i < f; i++) {
                    dtm.removeRow(0);
                }
            }
            while (rs.next()) {
                datos[0] = (String) rs.getString(1);
                datos[1] = (String) rs.getString(2);
                datos[2] = formatof.format(rs.getDate(3));
                datos[3] = (String) rs.getString(4);
                datos[4] = (String) rs.getString(5);
                dtm.addRow(datos);
            }
            TableColumn desaparece = TablaCliente.getColumnModel().getColumn(0);
            desaparece.setMaxWidth(0);
            desaparece.setMinWidth(0);
            desaparece.setPreferredWidth(0);
            
            TablaCliente.doLayout();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en BD" + e.toString());
        }
    }//GEN-LAST:event_btnVerActionPerformed

    private void btnGenerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarActionPerformed

        if (TablaCliente.getRowCount() == 0 && TablaCliente.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "La tabla no contiene datos que mostrar");
        } else {
            if (TablaCliente.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(null, "No se ha seleccionado un registro");
            } else {
                String nombrereporte = "compras_1.jasper";
                Map parametro = new HashMap();
                String id;
                id = (dtm.getValueAt(TablaCliente.getSelectedRow(), 0).toString());
                int y = Integer.parseInt(id);
                parametro.put("idsalida", y);
                GeneraReportes.AbrirReporte(nombrereporte, parametro);
            }
        }

    }//GEN-LAST:event_btnGenerarActionPerformed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void TablaClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaClienteMouseClicked
        // TODO add your handling code here:
        //limpiarObjetos();
        //txtCodigo.setText(dtm.getValueAt(TablaCliente.getSelectedRow(), 0).toString());

        String id;
        id = (dtm.getValueAt(TablaCliente.getSelectedRow(), 0).toString());

        try {
            //setSize(700,600);
            int f, i;
            conn = BdConexion.getConexion();
            rs2 = Conectiondb.EnlDIngresos(rs2, id);

            String datos[] = new String[6];
            f = salida.getRowCount();
            //System.out.println(f);
            if (f > 0) {
                for (i = 0; i < f; i++) {
                    salida.removeRow(0);
                }
            }
            while (rs2.next()) {

                datos[0] = (String) rs2.getString(1);
                datos[1] = (String) rs2.getString(2);
                datos[2] = (String) rs2.getString(3);
                datos[3] = (String) rs2.getString(4);
                datos[4] = (String) rs2.getString(5);
                datos[5] = (String) rs2.getString(6);
                //datos[6]=(String)rs.getString(7);
                salida.addRow(datos);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en BD" + e.toString());
        }

    }//GEN-LAST:event_TablaClienteMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TablaCliente;
    private javax.swing.JTable TablaDetalle;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnGenerar;
    private javax.swing.JButton btnVer;
    private org.freixas.jcalendar.JCalendarCombo fechafin;
    private org.freixas.jcalendar.JCalendarCombo fechaini;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private jcMousePanel.jcMousePanel jcMousePanel2;
    // End of variables declaration//GEN-END:variables

}
