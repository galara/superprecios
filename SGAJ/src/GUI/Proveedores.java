/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.https://www.youtube.com/watch?v=ICF-RldvSIo
 */
package GUI;

import BD.BdConexion;
import BD.sqlp;
import static GUI.MenuPrincipal.panel_center;
import excepciones.FiltraEntrada;
import excepciones.Helper;
import excepciones.VerificadorEntrada;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.Calendar;
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
 * @author GLARA
 */
public class Proveedores extends javax.swing.JInternalFrame {

    DefaultTableModel model;
    Connection conn;//getConnection intentara establecer una conexión.
    Statement sent;
    String[] titulos = {"Codigo", "Nombre Proveedor", "Dirección", "Nit", "Estado"};

    /**
     * Creates new form Proveedores
     */
    public Proveedores() {
        initComponents();
        Desabilitar();
        //Llenar();
        setFiltroTexto();
        addEscapeKey();

        proveedores.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent arg0) {
                int key = arg0.getKeyCode();
                if (key == java.awt.event.KeyEvent.VK_SPACE) {
                    filaseleccionada();
                }
                if (key == java.awt.event.KeyEvent.VK_DOWN || key == java.awt.event.KeyEvent.VK_UP) {
                    //removejtable();
                    Limpiar();
                }
            }
        });
    }

    private void addEscapeKey() {
        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        Action escapeAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                cerrarVentana();
            }
        };
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);

    }
    
    private void cerrarVentana() {
            int nu = JOptionPane.showConfirmDialog(this, "¿Desea Cerrar esta ventana?", "Cerrar ventana", JOptionPane.YES_NO_OPTION);

            if (nu == JOptionPane.YES_OPTION || nu == 0) {
                this.dispose();
        }
        else {
             }
    }
    
    public void removejtable() {
        while (proveedores.getRowCount() != 0) {
            model.removeRow(0);
        }
    }

    private void Desabilitar() {
        nombre.setEditable(false);
        direccion.setEditable(false);
        correo.setEditable(false);
        nit.setEditable(false);
        telefono.setEditable(false);
        rbEstado.setSelected(false);
        rbEstado.setEnabled(false);
        dcFecha.setEnabled(false);
        //buttonMostrar1.setEnabled(false);
        //txtDato.setEditable(true);
        txtDato.requestFocus();

    }

    private void Limpiar() {
        nombre.setText("");
        direccion.setText("");
        correo.setText("");
        nit.setText("");
        codigo.setText("");
        telefono.setText("");
        //txtDato.setText("");
        rbEstado.setSelected(false);
        rbEstado.setEnabled(false);
        rbEstado.setBackground(new java.awt.Color(51, 153, 255));
        dcFecha.setDate(Calendar.getInstance().getTime());
    }

    private void Habilitar() {
        nombre.setEditable(true);
        direccion.setEditable(true);
        correo.setEditable(true);
        nit.setEditable(true);
        telefono.setEditable(true);
        //nombre.requestFocus();
        rbEstado.setSelected(false);
        rbEstado.setEnabled(true);
        dcFecha.setEnabled(true);
        //txtDato.requestFocus();
    }

    public void setFiltroTexto() {
        Helper.setFiltraEntrada(nombre.getDocument(), FiltraEntrada.NUM_LETRAS, 150, true);
        Helper.setFiltraEntrada(direccion.getDocument(), FiltraEntrada.NUM_LETRAS, 200, true);
        Helper.setFiltraEntrada(nit.getDocument(), FiltraEntrada.NUM_LETRAS, 16, true);
        Helper.setFiltraEntrada(telefono.getDocument(), FiltraEntrada.SOLO_NUMEROS, 14, false);
        Helper.setFiltraEntrada(txtDato.getDocument(), FiltraEntrada.NUM_LETRAS, 150, true);
    }

    private String getFecha() {

        try {
            String fecha;
            int años = dcFecha.getCalendar().get(Calendar.YEAR);
            int dias = dcFecha.getCalendar().get(Calendar.DAY_OF_MONTH);
            int mess = dcFecha.getCalendar().get(Calendar.MONTH) + 1;
            int hours = dcFecha.getCalendar().get(Calendar.HOUR_OF_DAY);
            int minutes = dcFecha.getCalendar().get(Calendar.MINUTE);
            int seconds = dcFecha.getCalendar().get(Calendar.SECOND);

            fecha = "" + años + "-" + mess + "-" + dias + " " + hours + ":" + minutes + ":" + seconds;
            return fecha;
        } catch (Exception e) {
            JOptionPane.showInternalMessageDialog(this, "Verifique la fecha");
            System.out.print(e.getMessage());
        }
        return null;
    }

    private boolean existeacreedor() {
        try {
            conn = BdConexion.getConexion();
            int fila = proveedores.getSelectedRow();
            String idp = (String) proveedores.getValueAt(fila, 0);
            String sql;
            sql = "select  proveedor.idproveedor, proveedor.nombre,compra.status from proveedor INNER JOIN compra on compra.proveedor_idproveedor=proveedor.idproveedor where compra.status='T' and proveedor.idproveedor=" + idp + " group by proveedor.idproveedor order by proveedor.idproveedor asc";
            sent = conn.createStatement();// crea objeto Statement para consultar la base de datos
            ResultSet rs = sent.executeQuery(sql);// especifica la consulta y la ejecuta

            if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                rs.beforeFirst();//regresa el puntero al primer registro
                //conn.close();
                return true;
            } else {
                //JOptionPane.showInternalMessageDialog(this, " No existen deudas con el proveedor seleccionado ");
                //conn.close();
                return false;
            }

        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(this, "El dato no fue encontrado");
            System.out.print(e.getMessage());
        }
        return false;
    }

    public String[] datosproveedor() {
        String[] fila = new String[6];
        fila[0] = nombre.getText();
        fila[1] = direccion.getText();
        fila[2] = correo.getText();
        fila[3] = nit.getText();
        fila[4] = telefono.getText();
        int filas = proveedores.getSelectedRow();
        String dao = (String) proveedores.getValueAt(filas, 0);
        fila[5] = dao;

        return fila;
    }

    private void formatotabla() {
        //TableCellRenderer clase que se encarga de dibujar los datos que hay en cada celda la cual podemos modificar
        //nos proporciona la posibilidad de cambiar su aspercto por uno personalizado y no el standar.
        DefaultTableCellRenderer modelocentrar = new DefaultTableCellRenderer();
        modelocentrar.setHorizontalAlignment(SwingConstants.CENTER);

        //TableColumn representa todos los atributos de una columna en un JTable , como el ancho, resizibility, mínimo y máximo ancho
        //en este caso defien el ancho de cada columna las cuales pueden ser de distinto ancho.
        TableColumn column;// = null;
        for (int i = 0; i < 5; i++) {
            column = proveedores.getColumnModel().getColumn(i);
            if (i == 1) {
                column.setPreferredWidth(200); //Difine el ancho de la columna
            } else if (i == 0) {
                column.setPreferredWidth(5); //Difine el ancho de la columna
                proveedores.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);//Para centrar los datos en las columnas
            } else if (i == 2) {
                column.setPreferredWidth(250);//Difine el ancho de la columna
            } else if (i == 3) {
                column.setPreferredWidth(100); //Difine el ancho de la columna
                proveedores.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);//Para centrar los datos en las columnas
            } else if (i == 4) {
                column.setPreferredWidth(50);//Difine el ancho de la columna
                proveedores.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);//Para centrar los datos en las columnas
            }
        }
    }

    private void Llenar() {
        try {
            conn = BdConexion.getConexion();
            removejtable();
            //model = new DefaultTableModel(null, titulos);//objeto TableModel para proporcionar los datos del objeto ResultSet al objeto JTable
            sent = conn.createStatement();// crea objeto Statement para consultar la base de datos
            String sql;
            sql = "select  proveedor.idproveedor, proveedor.nombre,proveedor.direccion,proveedor.nitp,proveedor.estado,compra.status from proveedor INNER JOIN compra on compra.proveedor_idproveedor=proveedor.idproveedor where compra.status='T' group by proveedor.idproveedor order by proveedor.idproveedor asc";
            ResultSet rs = sent.executeQuery(sql);// especifica la consulta y la ejecuta

            String[] fila = new String[5];
            while (rs.next()) {

                fila[0] = rs.getString("idproveedor");
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getString("direccion");
                fila[3] = rs.getString("nitp");
                if (rs.getString("Estado").equals("T")) {
                    fila[4] = "Activo";
                } else {
                    fila[4] = "Inactivo";
                }
                model.addRow(fila);
            }
            proveedores.setModel(model);
            formatotabla();
            this.bntGuardar.setEnabled(false);
            this.bntModificar.setEnabled(false);
            this.bntEliminar.setEnabled(false);
            this.bntNuevo.setEnabled(true);
            //conn.close();
        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(this, "Error al cargar los datos", "Error ", JOptionPane.ERROR_MESSAGE);
            System.out.printf(e.toString());
            //this.dispose();
        }
    }

    private void MostrarTodo(String Dato) {
        try {
            conn = BdConexion.getConexion();
            String sql = "";
            if (this.rbNit.isSelected()) {
                sql = sqlp.BUSCANIT + "'" + Dato + "'";
            }
            if (this.rbNombre.isSelected()) {
                sql = sqlp.BUSCANOMBRE + Dato + sqlp.CUALQUIERA;
            }
            removejtable();
            //model = new DefaultTableModel(null, titulos);//objeto TableModel para proporcionar los datos del objeto ResultSet al objeto JTable
            sent = conn.createStatement();// crea objeto Statement para consultar la base de datos
            ResultSet rs = sent.executeQuery(sql);// especifica la consulta y la ejecuta

            if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                int count = 0;
                rs.beforeFirst();//regresa el puntero al primer registro
                String[] fila = new String[5];
                while (rs.next()) {//mientras tenga registros que haga lo siguiente
                    fila[0] = rs.getString("idproveedor");
                    fila[1] = rs.getString("nombre");
                    fila[2] = rs.getString("direccion");
                    fila[3] = rs.getString("nitp");
                    if (rs.getString("Estado").equals("T")) {
                        fila[4] = "Activo";
                    } else {
                        fila[4] = "Inactivo";
                    }
                    model.addRow(fila);
                    count = count + 1;
                }
                proveedores.setModel(model);
                formatotabla();
                this.bntGuardar.setEnabled(false);
                this.bntModificar.setEnabled(false);
                this.bntEliminar.setEnabled(false);
                this.bntNuevo.setEnabled(true);
                Limpiar();
                Desabilitar();
                //JOptionPane.showInternalMessageDialog(this, "Se encontraron " + count + " registros");

            } else {
                JOptionPane.showInternalMessageDialog(this, "El dato no fue encontrado");
            }
            //conn.close();
        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(this, "Error al cargar los datos", "Error ", JOptionPane.ERROR_MESSAGE);
            System.out.print(e.getMessage());
        }
    }

    private void filaseleccionada() {
        // if (evt.getButton() == 1) {
        int fila = proveedores.getSelectedRow();
        if (proveedores.getValueAt(fila, 0) != null) {
            try {
                conn = BdConexion.getConexion();
                Habilitar();
                String sql = sqlp.LLENAR + sqlp.WHERE + sqlp.ID + sqlp.IGUAL + proveedores.getValueAt(fila, 0);
                sent = conn.createStatement();
                ResultSet rs = sent.executeQuery(sql);
                rs.next();
                codigo.setText(rs.getString("idproveedor"));
                nombre.setText(rs.getString("nombre"));
                direccion.setText(rs.getString("direccion"));
                correo.setText(rs.getString("correo"));
                nit.setText(rs.getString("nitp"));
                telefono.setText(rs.getString("telefono"));
                if (rs.getString("Estado").equals("T")) {
                    rbEstado.setSelected(true);
                    rbEstado.setBackground(new java.awt.Color(102, 204, 0));
                } else {
                    rbEstado.setSelected(false);
                    rbEstado.setBackground(Color.red);
                }
                dcFecha.setDate(rs.getDate("fec_reg"));
                this.bntGuardar.setEnabled(false);
                this.bntModificar.setEnabled(true);
                this.bntEliminar.setEnabled(true);
                this.bntNuevo.setEnabled(false);
                //conn.close();
            } catch (SQLException e) {
                JOptionPane.showInternalMessageDialog(this, "Error al cargar los datos", "Error ", JOptionPane.ERROR_MESSAGE);
                System.out.print(e.getMessage());
            }
        }
        // }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rangodevolucion = new javax.swing.JDialog();
        jcMousePanel8 = new jcMousePanel.jcMousePanel();
        pnlActionButtons2 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jRadioButton2 = new javax.swing.JRadioButton();
        numerofactura = new javax.swing.JTextField();
        pnlPaginador1 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        dcFechaini = new com.toedter.calendar.JDateChooser();
        jLabel17 = new javax.swing.JLabel();
        dcFechafin = new com.toedter.calendar.JDateChooser();
        jRadioButton1 = new javax.swing.JRadioButton();
        panelImage1 = new elaprendiz.gui.panel.PanelImage();
        pnlActionButtons = new javax.swing.JPanel();
        bntNuevo = new elaprendiz.gui.button.ButtonRect();
        bntModificar = new elaprendiz.gui.button.ButtonRect();
        bntGuardar = new elaprendiz.gui.button.ButtonRect();
        bntEliminar = new elaprendiz.gui.button.ButtonRect();
        bntCancelar = new elaprendiz.gui.button.ButtonRect();
        bntSalir = new elaprendiz.gui.button.ButtonRect();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        nombre = new elaprendiz.gui.textField.TextField();
        nit = new elaprendiz.gui.textField.TextField();
        correo = new elaprendiz.gui.textField.TextField();
        telefono = new elaprendiz.gui.textField.TextField();
        direccion = new elaprendiz.gui.textField.TextField();
        dcFecha = new com.toedter.calendar.JDateChooser();
        rbEstado = new javax.swing.JRadioButton();
        jLabel10 = new javax.swing.JLabel();
        codigo = new elaprendiz.gui.textField.TextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        proveedores = new javax.swing.JTable();
        pnlPaginador = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtDato = new elaprendiz.gui.textField.TextField();
        rbNit = new javax.swing.JRadioButton();
        rbNombre = new javax.swing.JRadioButton();
        buttonMostrar = new elaprendiz.gui.button.ButtonRect();
        buttonMostrar1 = new elaprendiz.gui.button.ButtonRect();
        buttonMostrar2 = new elaprendiz.gui.button.ButtonRect();

        rangodevolucion.setBounds(new java.awt.Rectangle(220, 200, 270, 200));
        rangodevolucion.setFocusTraversalPolicyProvider(true);
        rangodevolucion.setMinimumSize(null);
        rangodevolucion.setUndecorated(true);

        jcMousePanel8.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 3, true));
        jcMousePanel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fondo/índice.jpg"))); // NOI18N
        jcMousePanel8.setMaximumSize(null);
        jcMousePanel8.setLayout(null);

        pnlActionButtons2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 153, 255), 1, true));
        pnlActionButtons2.setOpaque(false);
        pnlActionButtons2.setPreferredSize(new java.awt.Dimension(786, 52));
        pnlActionButtons2.setLayout(null);
        jcMousePanel8.add(pnlActionButtons2);
        pnlActionButtons2.setBounds(0, 178, 500, 52);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setOpaque(false);
        jPanel2.setLayout(null);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Factura:");
        jPanel2.add(jLabel9);
        jLabel9.setBounds(160, 20, 90, 30);

        jRadioButton2.setText("Filtrar por Factura");
        jPanel2.add(jRadioButton2);
        jRadioButton2.setBounds(33, 20, 120, 23);

        numerofactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numerofacturaActionPerformed(evt);
            }
        });
        jPanel2.add(numerofactura);
        numerofactura.setBounds(260, 20, 140, 30);

        jcMousePanel8.add(jPanel2);
        jPanel2.setBounds(0, 109, 430, 70);

        pnlPaginador1.setBackground(new java.awt.Color(0, 153, 204));
        pnlPaginador1.setPreferredSize(new java.awt.Dimension(786, 40));
        pnlPaginador1.setLayout(new java.awt.GridBagLayout());

        jLabel14.setFont(new java.awt.Font("Script MT Bold", 1, 32)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("<--Devoluciones-->");
        pnlPaginador1.add(jLabel14, new java.awt.GridBagConstraints());

        jcMousePanel8.add(pnlPaginador1);
        pnlPaginador1.setBounds(0, 0, 430, 40);

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel5.setOpaque(false);
        jPanel5.setLayout(null);

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Del:");
        jPanel5.add(jLabel16);
        jLabel16.setBounds(140, 10, 110, 20);

        dcFechaini.setDate(Calendar.getInstance().getTime());
        dcFechaini.setDateFormatString("dd/MM/yyyy");
        dcFechaini.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        dcFechaini.setMaxSelectableDate(new java.util.Date(3093496470100000L));
        dcFechaini.setMinSelectableDate(new java.util.Date(-62135744300000L));
        dcFechaini.setPreferredSize(new java.awt.Dimension(120, 22));
        jPanel5.add(dcFechaini);
        dcFechaini.setBounds(260, 10, 140, 21);

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Al:");
        jPanel5.add(jLabel17);
        jLabel17.setBounds(140, 40, 110, 20);

        dcFechafin.setDate(Calendar.getInstance().getTime());
        dcFechafin.setDateFormatString("dd/MM/yyyy");
        dcFechafin.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        dcFechafin.setMaxSelectableDate(new java.util.Date(3093496470100000L));
        dcFechafin.setMinSelectableDate(new java.util.Date(-62135744300000L));
        dcFechafin.setPreferredSize(new java.awt.Dimension(120, 22));
        dcFechafin.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                dcFechafinAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        dcFechafin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dcFechafinKeyPressed(evt);
            }
        });
        jPanel5.add(dcFechafin);
        dcFechafin.setBounds(260, 40, 140, 21);

        jRadioButton1.setText("Filtrar por Fecha");
        jPanel5.add(jRadioButton1);
        jRadioButton1.setBounds(33, 20, 120, 23);

        jcMousePanel8.add(jPanel5);
        jPanel5.setBounds(0, 40, 430, 70);

        javax.swing.GroupLayout rangodevolucionLayout = new javax.swing.GroupLayout(rangodevolucion.getContentPane());
        rangodevolucion.getContentPane().setLayout(rangodevolucionLayout);
        rangodevolucionLayout.setHorizontalGroup(
            rangodevolucionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jcMousePanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE)
        );
        rangodevolucionLayout.setVerticalGroup(
            rangodevolucionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jcMousePanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setBackground(new java.awt.Color(0, 0, 0));
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setForeground(new java.awt.Color(0, 0, 0));
        setTitle("Proveedores");
        setToolTipText("");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFocusTraversalPolicyProvider(true);
        setName("Proveedores"); // NOI18N
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        panelImage1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fondo/índice.jpg"))); // NOI18N
        panelImage1.setLayout(null);

        pnlActionButtons.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 153, 255), 1, true));
        pnlActionButtons.setOpaque(false);
        pnlActionButtons.setPreferredSize(new java.awt.Dimension(786, 52));
        pnlActionButtons.setLayout(new java.awt.GridBagLayout());

        bntNuevo.setBackground(new java.awt.Color(51, 153, 255));
        bntNuevo.setMnemonic(KeyEvent.VK_N);
        bntNuevo.setText("Nuevo");
        bntNuevo.setToolTipText("ALT+N");
        bntNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntNuevoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 84, 12, 0);
        pnlActionButtons.add(bntNuevo, gridBagConstraints);

        bntModificar.setBackground(new java.awt.Color(51, 153, 255));
        bntModificar.setMnemonic(KeyEvent.VK_M);
        bntModificar.setText("Modificar");
        bntModificar.setToolTipText("ALT+M");
        bntModificar.setEnabled(false);
        bntModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntModificarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 5, 12, 0);
        pnlActionButtons.add(bntModificar, gridBagConstraints);

        bntGuardar.setBackground(new java.awt.Color(51, 153, 255));
        bntGuardar.setMnemonic(KeyEvent.VK_G);
        bntGuardar.setText("Guardar");
        bntGuardar.setToolTipText("ALT+G");
        bntGuardar.setEnabled(false);
        bntGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntGuardarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 5, 12, 0);
        pnlActionButtons.add(bntGuardar, gridBagConstraints);

        bntEliminar.setBackground(new java.awt.Color(51, 153, 255));
        bntEliminar.setMnemonic(KeyEvent.VK_E);
        bntEliminar.setText("Eliminar");
        bntEliminar.setToolTipText("ALT+E");
        bntEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntEliminarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 5, 12, 0);
        pnlActionButtons.add(bntEliminar, gridBagConstraints);

        bntCancelar.setBackground(new java.awt.Color(51, 153, 255));
        bntCancelar.setMnemonic(KeyEvent.VK_X);
        bntCancelar.setText("Cancelar");
        bntCancelar.setToolTipText("ALT+X");
        bntCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntCancelarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 5, 12, 0);
        pnlActionButtons.add(bntCancelar, gridBagConstraints);

        bntSalir.setBackground(new java.awt.Color(51, 153, 255));
        bntSalir.setText("Salir");
        bntSalir.setToolTipText("ESC");
        bntSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntSalirActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 5, 12, 93);
        pnlActionButtons.add(bntSalir, gridBagConstraints);

        panelImage1.add(pnlActionButtons);
        pnlActionButtons.setBounds(0, 430, 880, 50);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setOpaque(false);
        jPanel1.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("*Nombre:");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(45, 40, 80, 20);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Correo:");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(45, 65, 80, 20);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("*Dirección:");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(45, 90, 80, 20);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("Telefono:");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(400, 65, 80, 20);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("Nit:");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(400, 40, 80, 20);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("*Fecha de registro:");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(680, 40, 140, 21);

        nombre.setEditable(false);
        nombre.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        nombre.setPreferredSize(new java.awt.Dimension(120, 21));
        jPanel1.add(nombre);
        nombre.setBounds(140, 40, 250, 21);

        nit.setEditable(false);
        nit.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        nit.setPreferredSize(new java.awt.Dimension(120, 21));
        jPanel1.add(nit);
        nit.setBounds(490, 40, 150, 21);

        correo.setEditable(false);
        correo.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        correo.setInputVerifier(new VerificadorEntrada(125,VerificadorEntrada.EMAIL));
        correo.setPreferredSize(new java.awt.Dimension(120, 21));
        jPanel1.add(correo);
        correo.setBounds(140, 65, 250, 21);

        telefono.setEditable(false);
        telefono.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        telefono.setPreferredSize(new java.awt.Dimension(120, 21));
        jPanel1.add(telefono);
        telefono.setBounds(490, 65, 150, 21);

        direccion.setEditable(false);
        direccion.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        direccion.setPreferredSize(new java.awt.Dimension(120, 21));
        jPanel1.add(direccion);
        direccion.setBounds(140, 90, 500, 21);

        dcFecha.setDate(Calendar.getInstance().getTime());
        dcFecha.setDateFormatString("dd/MM/yyyy");
        dcFecha.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        dcFecha.setMaxSelectableDate(new java.util.Date(3093496470100000L));
        dcFecha.setMinSelectableDate(new java.util.Date(-62135744300000L));
        dcFecha.setPreferredSize(new java.awt.Dimension(120, 22));
        jPanel1.add(dcFecha);
        dcFecha.setBounds(680, 65, 140, 21);

        rbEstado.setBackground(new java.awt.Color(51, 153, 255));
        rbEstado.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        rbEstado.setForeground(new java.awt.Color(255, 255, 255));
        rbEstado.setSelected(true);
        rbEstado.setText("Activo");
        rbEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbEstadoActionPerformed(evt);
            }
        });
        jPanel1.add(rbEstado);
        rbEstado.setBounds(680, 90, 140, 21);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel10.setText("Codigo:");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(45, 15, 80, 17);

        codigo.setEditable(false);
        codigo.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        codigo.setPreferredSize(new java.awt.Dimension(120, 21));
        jPanel1.add(codigo);
        codigo.setBounds(140, 15, 120, 21);

        panelImage1.add(jPanel1);
        jPanel1.setBounds(0, 40, 880, 140);

        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(786, 402));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        proveedores.setModel(model = new DefaultTableModel(null, titulos)
            {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
            proveedores.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            proveedores.setFocusCycleRoot(true);
            proveedores.setRowHeight(24);
            proveedores.setSurrendersFocusOnKeystroke(true);
            proveedores.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    proveedoresMouseClicked(evt);
                }
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    proveedoresMouseClicked(evt);
                }
            });
            jScrollPane1.setViewportView(proveedores);
            proveedores.getAccessibleContext().setAccessibleName("");

            jPanel3.add(jScrollPane1, java.awt.BorderLayout.CENTER);

            panelImage1.add(jPanel3);
            jPanel3.setBounds(0, 250, 880, 180);

            pnlPaginador.setBackground(new java.awt.Color(0, 153, 204));
            pnlPaginador.setPreferredSize(new java.awt.Dimension(786, 40));
            pnlPaginador.setLayout(new java.awt.GridBagLayout());

            jLabel8.setFont(new java.awt.Font("Script MT Bold", 1, 32)); // NOI18N
            jLabel8.setForeground(new java.awt.Color(255, 255, 255));
            jLabel8.setText("<--Proveedores-->");
            jLabel8.setToolTipText("");
            pnlPaginador.add(jLabel8, new java.awt.GridBagConstraints());

            panelImage1.add(pnlPaginador);
            pnlPaginador.setBounds(0, 0, 880, 40);

            jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            jPanel4.setOpaque(false);
            jPanel4.setLayout(null);

            jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            jLabel7.setText("Buscar Por:");
            jPanel4.add(jLabel7);
            jLabel7.setBounds(25, 10, 80, 17);

            txtDato.setPreferredSize(new java.awt.Dimension(250, 27));
            txtDato.requestFocus();
            txtDato.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    txtDatoActionPerformed(evt);
                }
            });
            jPanel4.add(txtDato);
            txtDato.setBounds(110, 10, 250, 27);

            rbNit.setBackground(new java.awt.Color(51, 153, 255));
            rbNit.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            rbNit.setForeground(new java.awt.Color(255, 255, 255));
            rbNit.setText("Codigo");
            rbNit.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    rbNitActionPerformed(evt);
                }
            });
            jPanel4.add(rbNit);
            rbNit.setBounds(140, 40, 80, 25);

            rbNombre.setBackground(new java.awt.Color(51, 153, 255));
            rbNombre.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            rbNombre.setForeground(new java.awt.Color(255, 255, 255));
            rbNombre.setSelected(true);
            rbNombre.setText("Nombre");
            rbNombre.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    rbNombreActionPerformed(evt);
                }
            });
            jPanel4.add(rbNombre);
            rbNombre.setBounds(260, 40, 81, 25);

            buttonMostrar.setBackground(new java.awt.Color(102, 204, 0));
            buttonMostrar.setText("Acreedores");
            buttonMostrar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    buttonMostrarActionPerformed(evt);
                }
            });
            jPanel4.add(buttonMostrar);
            buttonMostrar.setBounds(480, 10, 110, 25);

            buttonMostrar1.setBackground(new java.awt.Color(102, 204, 0));
            buttonMostrar1.setText("Estado de cuenta");
            buttonMostrar1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    buttonMostrar1ActionPerformed(evt);
                }
            });
            jPanel4.add(buttonMostrar1);
            buttonMostrar1.setBounds(595, 10, 140, 25);

            buttonMostrar2.setBackground(new java.awt.Color(102, 204, 0));
            buttonMostrar2.setText("Devoluciones");
            buttonMostrar2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    buttonMostrar2ActionPerformed(evt);
                }
            });
            jPanel4.add(buttonMostrar2);
            buttonMostrar2.setBounds(740, 10, 129, 25);

            panelImage1.add(jPanel4);
            jPanel4.setBounds(0, 179, 880, 70);

            getContentPane().add(panelImage1, java.awt.BorderLayout.CENTER);

            setBounds(0, 0, 894, 512);
        }// </editor-fold>//GEN-END:initComponents

    private void bntNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntNuevoActionPerformed
        // TODO add your handling code here:
        Limpiar();
        Habilitar();
        this.bntGuardar.setEnabled(true);
        this.bntModificar.setEnabled(false);
        this.bntEliminar.setEnabled(false);
        this.bntNuevo.setEnabled(false);
        nombre.requestFocus();
    }//GEN-LAST:event_bntNuevoActionPerformed

    private void bntGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntGuardarActionPerformed
        // TODO add your handling code here:
        if (nombre.getText().equals("") || direccion.getText().equals("") || getFecha() == null) {
            JOptionPane.showInternalMessageDialog(this, "Complete los campos obligatorios");
        } else {
            int resp;
            resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Grabar el Registro?", "Pregunta", 0);
            if (resp == 0) {
                try {
                    conn = BdConexion.getConexion();
                    //PreparedStatement nos permite crear instrucciones SQL compiladas, que se ejecutan con más efi ciencia que los objetos Statement
                    //también pueden especifi car parámetros,lo cual las hace más fl exibles que las instrucciones Statement
                    PreparedStatement ps = conn.prepareCall(sqlp.NUEVOC);
                    ps.setString(1, nombre.getText());
                    ps.setString(2, direccion.getText());
                    ps.setString(3, correo.getText());
                    ps.setString(4, nit.getText());
                    ps.setString(5, telefono.getText());
                    ps.setString(6, getFecha());
                    int n = ps.executeUpdate();
                    if (n > 0) {
                        MostrarTodo(txtDato.getText());
                        Desabilitar();
                        Limpiar();
                        txtDato.requestFocus();
                        JOptionPane.showInternalMessageDialog(this, "Datos guardados correctamente");
                    }
                    //conn.close();
                } catch (SQLException e) {
                    if (e.getErrorCode() == 1062) {
                        JOptionPane.showInternalMessageDialog(this,
                                "Error al guardar \n El nit ya existe ingrese uno diferente", "Error ", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showInternalMessageDialog(this,
                                "Error al modificar \n Verifique los datos e intente nuevamente", "Error ", JOptionPane.ERROR_MESSAGE);
                    }
                    System.out.print(e.getMessage() + "\n");
                }
                //Llenar();

            }
        }
    }//GEN-LAST:event_bntGuardarActionPerformed

    private void bntSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntSalirActionPerformed
        cerrarVentana();
    }//GEN-LAST:event_bntSalirActionPerformed

    private void proveedoresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_proveedoresMouseClicked
        // TODO add your handling code here:
        if (evt.getButton() == 1) {
            int fila = proveedores.getSelectedRow();
            if (proveedores.getValueAt(fila, 0) != null) {
                try {
                    conn = BdConexion.getConexion();
                    Habilitar();
                    String sql = sqlp.LLENAR + sqlp.WHERE + sqlp.ID + sqlp.IGUAL + proveedores.getValueAt(fila, 0);
                    sent = conn.createStatement();
                    ResultSet rs = sent.executeQuery(sql);
                    rs.next();
                    codigo.setText(rs.getString("idproveedor"));
                    nombre.setText(rs.getString("nombre"));
                    direccion.setText(rs.getString("direccion"));
                    correo.setText(rs.getString("correo"));
                    nit.setText(rs.getString("nitp"));
                    telefono.setText(rs.getString("telefono"));
                    if (rs.getString("Estado").equals("T")) {
                        rbEstado.setSelected(true);
                        rbEstado.setBackground(new java.awt.Color(102, 204, 0));
                    } else {
                        rbEstado.setSelected(false);
                        rbEstado.setBackground(Color.red);
                    }
                    dcFecha.setDate(rs.getDate("fec_reg"));
                    this.bntGuardar.setEnabled(false);
                    this.bntModificar.setEnabled(true);
                    this.bntEliminar.setEnabled(true);
                    this.bntNuevo.setEnabled(false);
                    //conn.close();
                } catch (SQLException e) {
                    JOptionPane.showInternalMessageDialog(this, "Error al cargar los datos", "Error ", JOptionPane.ERROR_MESSAGE);
                    System.out.print(e.getMessage());
                }
            }
        }
    }//GEN-LAST:event_proveedoresMouseClicked

    private void bntEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntEliminarActionPerformed
        // TODO add your handling code here:
        int resp;
        resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Eliminar el Registro?", "Pregunta", 0);
        if (resp == 0) {
            try {
                conn = BdConexion.getConexion();
                int fila = proveedores.getSelectedRow();
                String sql = sqlp.DELETEC + sqlp.WHERE + sqlp.ID + sqlp.IGUAL + proveedores.getValueAt(fila, 0);
                sent = conn.createStatement();//El programa utiliza al objeto Statement para enviar instrucciones de SQL a la base de datos.
                int n = sent.executeUpdate(sql);
                if (n > 0) {
                    //Llenar();
                    MostrarTodo(txtDato.getText());
                    Limpiar();
                    Desabilitar();
                    txtDato.requestFocus();
                    JOptionPane.showInternalMessageDialog(this, "Datos eliminados correctamente");

                }
                //conn.close();
            } catch (SQLException e) {
                if (e.getErrorCode() == 1451) {
                    JOptionPane.showInternalMessageDialog(this,
                            "Error al eliminar \n El proveedor esta siendo utilizada por compras y no puede ser eliminado ", "Error ", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showInternalMessageDialog(this,
                            "Error al eliminar \n Verifique los datos e intente nuevamente", "Error ", JOptionPane.ERROR_MESSAGE);
                }
                System.out.print(e.getMessage() + "\n");
            }
        }
    }//GEN-LAST:event_bntEliminarActionPerformed

    private void bntModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntModificarActionPerformed
        // TODO add your handling code here:
        if (nombre.getText().equals("") || nit.getText().equals("") || direccion.getText().equals("") || getFecha() == null) {
            JOptionPane.showInternalMessageDialog(this, "Complete los campos obligatorios");
        } else {
            int resp;
            resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Modidicar el Registro?", "Pregunta", 0);
            if (resp == 0) {
                try {
                    conn = BdConexion.getConexion();
                    int fila = proveedores.getSelectedRow();
                    String dao = (String) proveedores.getValueAt(fila, 0);

                    //PreparedStatement nos permite crear instrucciones SQL compiladas, que se ejecutan con más efi ciencia que los objetos Statement
                    //también pueden especifi car parámetros,lo cual las hace más fl exibles que las instrucciones Statement
                    PreparedStatement ps = conn.prepareCall(sqlp.UPDATEC);
                    ps.setString(1, nombre.getText());
                    ps.setString(2, direccion.getText());
                    ps.setString(3, correo.getText());
                    ps.setString(4, nit.getText());
                    ps.setString(5, telefono.getText());
                    if (this.rbEstado.isSelected()) {
                        ps.setString(6, "T");
                    } else {
                        ps.setString(6, "F");
                    }
                    ps.setString(7, getFecha());
                    ps.setString(8, dao);

                    int n = ps.executeUpdate();
                    if (n > 0) {
                        Limpiar();
                        Desabilitar();
                        //Llenar();
                        MostrarTodo(txtDato.getText());
                        bntModificar.setEnabled(false);
                        this.bntEliminar.setEnabled(false);
                        bntNuevo.setEnabled(false);
                        bntGuardar.setEnabled(true);
                        txtDato.requestFocus();
                        JOptionPane.showInternalMessageDialog(this, "Datos modificados correctamente");

                    }
                    //conn.close();
                } catch (SQLException e) {
                    if (e.getErrorCode() == 1062) {
                        JOptionPane.showInternalMessageDialog(this,
                                "Error al modificar \n El nit ya existe ingrese uno diferente", "Error ", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showInternalMessageDialog(this,
                                "Error al modificar \n Verifique los datos e intente nuevamente", "Error ", JOptionPane.ERROR_MESSAGE);
                    }
                    System.out.print(e.getMessage() + "\n");
                }
            }
        }
    }//GEN-LAST:event_bntModificarActionPerformed

    private void bntCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntCancelarActionPerformed
        // TODO add your handling code here:
        Limpiar();
        Desabilitar();
        //Llenar();
        MostrarTodo(txtDato.getText());
        txtDato.requestFocus();
    }//GEN-LAST:event_bntCancelarActionPerformed

    private void buttonMostrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonMostrarActionPerformed
        // TODO add your handling code here:
        Llenar();
        Limpiar();
        Desabilitar();
        //buttonMostrar1.setEnabled(true);

    }//GEN-LAST:event_buttonMostrarActionPerformed

    private void rbNitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbNitActionPerformed
        // TODO add your handling code here:
        rbNombre.setSelected(false);
        txtDato.requestFocus();
    }//GEN-LAST:event_rbNitActionPerformed

    private void rbNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbNombreActionPerformed
        // TODO add your handling code here:
        rbNit.setSelected(false);
        txtDato.requestFocus();
    }//GEN-LAST:event_rbNombreActionPerformed

    private void rbEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbEstadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbEstadoActionPerformed

    private void buttonMostrar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonMostrar1ActionPerformed
        // TODO add your handling code here:
        if (proveedores.getRowCount() == 0 && proveedores.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "La tabla no contiene datos que mostrar");
        } else {
            if (proveedores.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(null, "No se ha seleccionado un registro");
            } else {

                if (existeacreedor() == true) {

                    pagoproveedores nuevasol = new pagoproveedores();
                    if (panel_center.getComponentCount() > 0 & panel_center.getComponentCount() < 3) //solo uno en t
                    {
                        int fila = proveedores.getSelectedRow();
                        String dao = (String) proveedores.getValueAt(fila, 0);
                        panel_center.add(nuevasol);
                        nuevasol.show();// ver interno
                        nuevasol.setClosable(true);// icono de cerrar
                        nuevasol.toFront();//aparece al frente

                        String[] datosprov = datosproveedor();
                        nuevasol.llenardatos(datosprov);
                        nuevasol.Llenar(dao);
                    }
                } else {
                    if (existeacreedor() == false) {
                        JOptionPane.showInternalMessageDialog(this, " No existen deudas con el proveedor seleccionado ");
                    }
                }
            }
        }


    }//GEN-LAST:event_buttonMostrar1ActionPerformed

    private void txtDatoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDatoActionPerformed
        // TODO add your handling code here:
        MostrarTodo(txtDato.getText());
    }//GEN-LAST:event_txtDatoActionPerformed

    private void buttonMostrar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonMostrar2ActionPerformed
        // TODO add your handling code here:
        if (proveedores.getRowCount() == 0 && proveedores.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "La tabla no contiene datos que mostrar");
        } else {
            if (proveedores.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(null, "No se ha seleccionado un registro");
            } else {

                //if (existeacreedor() == true) {
                Devolucionproveedores nuevasol = new Devolucionproveedores();
                if (panel_center.getComponentCount() > 0 & panel_center.getComponentCount() < 3) //solo uno en t
                {
//                        int fila = proveedores.getSelectedRow();
//                        String dao = (String) proveedores.getValueAt(fila, 0);
                    panel_center.add(nuevasol);
                    nuevasol.show();// ver interno
                    nuevasol.setClosable(true);// icono de cerrar
                    nuevasol.toFront();//aparece al frente

                    String[] datosprov = datosproveedor();
                    nuevasol.llenardatos(datosprov);
                    //nuevasol.Llenar(dao);
                }
            }
//                else{
//                    if (existeacreedor() == false) {
//                       JOptionPane.showInternalMessageDialog(this, " No existen deudas con el proveedor seleccionado ");
//                    }
//                }
        }

    }//GEN-LAST:event_buttonMostrar2ActionPerformed

    private void dcFechafinAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_dcFechafinAncestorAdded
        // TODO add your handling code here:

    }//GEN-LAST:event_dcFechafinAncestorAdded

    private void numerofacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numerofacturaActionPerformed
        // TODO add your handling code here:
        Devolucionproveedores nuevasol = new Devolucionproveedores();
        //if (panel_center.getComponentCount() > 0 & panel_center.getComponentCount() < 3) //solo uno en t
        {
            int fila = proveedores.getSelectedRow();
            String dao = (String) proveedores.getValueAt(fila, 0);
            panel_center.add(nuevasol);
            nuevasol.show();// ver interno
            nuevasol.setClosable(true);// icono de cerrar
            nuevasol.toFront();//aparece al frente

            String[] datosprov = datosproveedor();
            nuevasol.llenardatos(datosprov);
            nuevasol.Llenar(dao);
        }
    }//GEN-LAST:event_numerofacturaActionPerformed

    private void dcFechafinKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dcFechafinKeyPressed
        // TODO add your handling code here:
        Devolucionproveedores nuevasol = new Devolucionproveedores();
        //if (panel_center.getComponentCount() > 0 & panel_center.getComponentCount() < 3) //solo uno en t
        {
            int fila = proveedores.getSelectedRow();
            String dao = (String) proveedores.getValueAt(fila, 0);
            panel_center.add(nuevasol);
            nuevasol.show();// ver interno
            nuevasol.setClosable(true);// icono de cerrar
            nuevasol.toFront();//aparece al frente

            String[] datosprov = datosproveedor();
            nuevasol.llenardatos(datosprov);
            nuevasol.Llenar(dao);
        }
    }//GEN-LAST:event_dcFechafinKeyPressed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        cerrarVentana();
    }//GEN-LAST:event_formInternalFrameClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private elaprendiz.gui.button.ButtonRect bntCancelar;
    private elaprendiz.gui.button.ButtonRect bntEliminar;
    private elaprendiz.gui.button.ButtonRect bntGuardar;
    private elaprendiz.gui.button.ButtonRect bntModificar;
    private elaprendiz.gui.button.ButtonRect bntNuevo;
    private elaprendiz.gui.button.ButtonRect bntSalir;
    private elaprendiz.gui.button.ButtonRect buttonMostrar;
    private elaprendiz.gui.button.ButtonRect buttonMostrar1;
    private elaprendiz.gui.button.ButtonRect buttonMostrar2;
    private elaprendiz.gui.textField.TextField codigo;
    private elaprendiz.gui.textField.TextField correo;
    private com.toedter.calendar.JDateChooser dcFecha;
    private com.toedter.calendar.JDateChooser dcFechafin;
    private com.toedter.calendar.JDateChooser dcFechaini;
    private elaprendiz.gui.textField.TextField direccion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private jcMousePanel.jcMousePanel jcMousePanel8;
    private elaprendiz.gui.textField.TextField nit;
    private elaprendiz.gui.textField.TextField nombre;
    private javax.swing.JTextField numerofactura;
    private elaprendiz.gui.panel.PanelImage panelImage1;
    private javax.swing.JPanel pnlActionButtons;
    private javax.swing.JPanel pnlActionButtons2;
    private javax.swing.JPanel pnlPaginador;
    private javax.swing.JPanel pnlPaginador1;
    private javax.swing.JTable proveedores;
    private javax.swing.JDialog rangodevolucion;
    private javax.swing.JRadioButton rbEstado;
    private javax.swing.JRadioButton rbNit;
    private javax.swing.JRadioButton rbNombre;
    private elaprendiz.gui.textField.TextField telefono;
    private elaprendiz.gui.textField.TextField txtDato;
    // End of variables declaration//GEN-END:variables
}
