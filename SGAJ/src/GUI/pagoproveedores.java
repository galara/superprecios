/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import BD.BdConexion;
import BD.sqlp;
import static GUI.MenuPrincipal.panel_center;
import Modelos.AccesoUsuario;
import excepciones.FormatoDecimal;
import excepciones.VerificadorEntrada;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author Glara
 */
public class pagoproveedores extends javax.swing.JInternalFrame {

    private String archivoRecurso = "controlador-bd";
    DefaultTableModel model, model2;
    Connection conn;//getConnection intentara establecer una conexión.
    Statement sent;
    String[] titulos = {"Id", "Codigo", "Descripción", "Cantidad", "Precio", "Subtotal"};
    String[] titulos2 = {"idp", "idc", "Fecha", "No.Doc", "Total", "saldo", "FechaPago"};
    String idproveedor, idcompras;
    SimpleDateFormat formatof = new SimpleDateFormat("dd/MM/yyyy");
    //java.sql.Connection conn;//getConnection intentara establecer una conexión.
    /**
     * Creates new form pedido
     */
    public pagoproveedores() {

        initComponents();
        //Llenar();
        formatotabla2();
        formatotabla();
        addEscapeKey();

        tablacomprasporpagar.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent arg0) {
                int key = arg0.getKeyCode();

                if (key == java.awt.event.KeyEvent.VK_SPACE) {
                    int fila = tablacomprasporpagar.getSelectedRow();
                    String dato = tablacomprasporpagar.getValueAt(fila, 1).toString();
                    MostrarTodo(dato);
                    idcompras = dato;
                }
                if (key == java.awt.event.KeyEvent.VK_DOWN || key == java.awt.event.KeyEvent.VK_UP) {
                    removejtable();
                }
//                if (key == java.awt.event.KeyEvent.VK_F2) {
//                    liquidardeudas();
//                }
//                if (key == java.awt.event.KeyEvent.VK_F1) {
//                    abonar();
//                }
//                if (key == java.awt.event.KeyEvent.VK_F3) {
//                    detalleabonos();
//                }
            }
        });

        tablacomprasporpagar.getModel().addTableModelListener((TableModelEvent e) -> {
            sumartotal();
            formatotabla2();
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

//        //EVENTO AL PRESIONAR LA TECLA F1 ABONAR **********************
//        KeyStroke F1 = KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0, false);
//        Action F1Action = new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
//                abonar();
//            }
//        };
//        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(F1, "F1");
//        getRootPane().getActionMap().put("F1", F1Action);
//
//        //EVENTO AL PRESIONAR LA TECLA F3 ABONAR **********************
//        KeyStroke F2 = KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0, false);
//        Action F2Action = new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
//                liquidardeudas();
//            }
//        };
//        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(F2, "F2");
//        getRootPane().getActionMap().put("F2", F2Action);
//
//        //EVENTO AL PRESIONAR LA TECLA F3 ABONAR **********************
//        KeyStroke F3 = KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0, false);
//        Action F3Action = new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
//                detalleabonos();
//            }
//        };
//        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(F3, "F3");
//        getRootPane().getActionMap().put("F3", F3Action);
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
        while (tabladetallecompra.getRowCount() != 0) {
            model.removeRow(0);
        }

    }

    public void removejtable2() {
        while (tablacomprasporpagar.getRowCount() != 0) {
            model2.removeRow(0);
        }

    }

    public void llenardatos(String[] datos) {
        nombre.setText(datos[0]);
        direccion.setText(datos[1]);
        correo.setText(datos[2]);
        nit.setText(datos[3]);
        telefono.setText(datos[4]);
        idproveedor = datos[5];

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

    public void formatotabla() {
        //TableCellRenderer clase que se encarga de dibujar los datos que hay en cada celda la cual podemos modificar
        //nos proporciona la posibilidad de cambiar su aspercto por uno personalizado y no el standar.
        // model = (MiModelo) new DefaultTableModel(null, titulos);
        DefaultTableCellRenderer modelocentrar = new DefaultTableCellRenderer();
        modelocentrar.setHorizontalAlignment(SwingConstants.CENTER);
        //TableColumn representa todos los atributos de una columna en un JTable , como el ancho, resizibility, mínimo y máximo ancho
        //en este caso defien el ancho de cada columna las cuales pueden ser de distinto ancho.
        TableColumn column;// = null;
        for (int i = 0; i < 6; i++) {
            column = tabladetallecompra.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setMaxWidth(0);
                column.setMinWidth(0);
            } else if (i == 1) {
                column.setPreferredWidth(20); //Difine el ancho de la columna
                tabladetallecompra.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);
            } else if (i == 2) {
                column.setPreferredWidth(230); //Difine el ancho de la columna
            } else if (i > 2 & i <= 5) {
                column.setPreferredWidth(40);//Difine el ancho de la columna
                tabladetallecompra.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);
            }
        }
    }

    public void formatotabla2() {
        //TableCellRenderer clase que se encarga de dibujar los datos que hay en cada celda la cual podemos modificar
        //nos proporciona la posibilidad de cambiar su aspercto por uno personalizado y no el standar.
        // model = (MiModelo) new DefaultTableModel(null, titulos);
        DefaultTableCellRenderer modelocentrar = new DefaultTableCellRenderer();
        modelocentrar.setHorizontalAlignment(SwingConstants.CENTER);
        //TableColumn representa todos los atributos de una columna en un JTable , como el ancho, resizibility, mínimo y máximo ancho
        //en este caso defien el ancho de cada columna las cuales pueden ser de distinto ancho.
        TableColumn column;// = null;
        for (int i = 0; i < 7; i++) {
            column = tablacomprasporpagar.getColumnModel().getColumn(i);
            if (i == 0 || i == 1) {
                column.setMaxWidth(0);
                column.setMinWidth(0);
            } else if (i > 2 & i <= 5) {
                column.setPreferredWidth(29);//Difine el ancho de la columna
                tablacomprasporpagar.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);
            } else if (i == 2 || i == 6) {
                column.setPreferredWidth(40);//Difine el ancho de la columna
                tablacomprasporpagar.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);
            }
        }
    }

    private boolean existenabonos() {
        try {
            int fila = tablacomprasporpagar.getSelectedRow();
            String Dato = (String) tablacomprasporpagar.getValueAt(fila, 1).toString();
            conn = BdConexion.getConexion();
            String sql = "select xpagardetalle.idxpagardetalle,xpagardetalle.fecha,xpagardetalle.monto,compra.idcompra,compra.numdoc,usuario.nombreusuario from xpagardetalle INNER JOIN compra on compra.idcompra=xpagardetalle.compra_idcompra INNER JOIN usuario on usuario.idusuario=xpagardetalle.usuario_idusuario where xpagardetalle.compra_idcompra=" + Dato + " group by xpagardetalle.idxpagardetalle order by xpagardetalle.idxpagardetalle asc";
            sent = conn.createStatement();// crea objeto Statement para consultar la base de datos
            ResultSet rs = sent.executeQuery(sql);// especifica la consulta y la ejecuta

            if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                rs.beforeFirst();//regresa el puntero al primer registro
                //conn.close();
                return true;
            } else {
                //conn.close();
                return false;
            }
        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(this, "Ocurrio un error al cargar los datos");
            System.out.print(e.getMessage());
        }
        return false;
    }

    public void Llenar(String Dato) {
        try {
            conn = BdConexion.getConexion();
            removejtable2();
            sent = conn.createStatement();// crea objeto Statement para consultar la base de datos
            String sql = "select * from compra where proveedor_idproveedor=" + Dato + " and status='T'";
            ResultSet rs = sent.executeQuery(sql);// especifica la consulta y la ejecuta

            String[] fila = new String[7];
            while (rs.next()) {

                fila[0] = rs.getString("proveedor_idproveedor");
                fila[1] = rs.getString("idcompra");
                fila[2] = formatof.format(rs.getDate("fecha"));
                fila[3] = rs.getString("numdoc");
                fila[4] = rs.getString("total");
                fila[5] = rs.getString("saldo");
                fila[6] = formatof.format(rs.getDate("fechapago"));
                model2.addRow(fila);
            }
            tablacomprasporpagar.setModel(model2);
            //conn.close();
            formatotabla2();

            sumartotal();
//            this.bntGuardar.setEnabled(false);
//            this.bntModificar.setEnabled(false);
//            this.bntEliminar.setEnabled(false);
//            this.bntNuevo.setEnabled(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en BD " + e.toString());
            //this.dispose();
        }
    }

    private void MostrarTodo(String Dato) {
        try {
            conn = BdConexion.getConexion();
            String sql = "select producto_idproducto,producto.codigo, producto.nombre,lote.cantidad,lote.precio,lote.cantidad*lote.precio  from lote INNER JOIN producto on producto.idproducto=lote.producto_idproducto where lote.compra_idcompra=" + Dato + " order by idlote asc";
            removejtable();
            sent = conn.createStatement();// crea objeto Statement para consultar la base de datos
            ResultSet rs = sent.executeQuery(sql);// especifica la consulta y la ejecuta

            if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                int count = 0;
                float subt, cant, prec;
                rs.beforeFirst();//regresa el puntero al primer registro
                String[] fila = new String[7];
                while (rs.next()) {//mientras tenga registros que haga lo siguiente

                    fila[0] = rs.getString("producto_idproducto");
                    fila[1] = rs.getString("producto.codigo");
                    fila[2] = rs.getString("producto.nombre");
                    fila[3] = rs.getString("lote.cantidad");

                    cant = rs.getFloat("lote.cantidad");
                    prec = rs.getFloat("lote.precio");
                    subt = (float) (Math.round((cant * prec) * 100.0) / 100.0);

                    fila[4] = rs.getString("lote.precio");
                    fila[5] = (String) ("" + subt);

                    model.addRow(fila);
                    count = count + 1;
                }
                tabladetallecompra.setModel(model);
                formatotabla();
//                this.bntGuardar.setEnabled(false);
//                this.bntModificar.setEnabled(false);
//                this.bntEliminar.setEnabled(false);
//                this.bntNuevo.setEnabled(true);
//                JOptionPane.showInternalMessageDialog(this, "Se encontraron " + count + " registros");
//                Limpiar();
//                Desabilitar();

            } else {
                JOptionPane.showInternalMessageDialog(this, "El dato no fue encontrado");
            }
        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(this, "El dato no fue encontrado");
            System.out.print(e.getMessage());
        }
    }

    private String Validar(String x) {
        String y;
        if (x.equals("")) {
            y = "0";
            return y;
        } else {
            y = x;
            return y;
        }
    }

    private void abonar() {
        if (tablacomprasporpagar.getRowCount() == 0 && tablacomprasporpagar.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "La tabla no contiene datos que modificar");
        } else {
            if (tablacomprasporpagar.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(null, "No se ha seleccionado un registro");
            } else {
                dcFecha.setDate(Calendar.getInstance().getTime());
                montoabono.setValue(null);
                observacionabono.setText("");
                abono.setVisible(true);
                montoabono.requestFocus();
                abono.setSize(504, 190);
                abono.setResizable(false);
                Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
                Dimension ventana = abono.getSize();
                abono.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 2);
                abono.toFront();//aparece al frente
            }
        }
    }

    private void liquidardeudas() {
        if (tablacomprasporpagar.getRowCount() == 0 && tablacomprasporpagar.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "La tabla no contiene datos que modificar");
        } else {
            if (tablacomprasporpagar.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(null, "No se ha seleccionado un registro");
            } else {
                dcFecha.setDate(Calendar.getInstance().getTime());
                montoabono.setValue(null);
                observacionabono.setText("");
                abono.setVisible(true);
                montoabono.requestFocus();
                abono.setSize(504, 190);
                abono.setResizable(false);
                Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
                Dimension ventana = abono.getSize();
                abono.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 2);
                abono.toFront();//aparece al frente
                float saldoc;
                int fila = tablacomprasporpagar.getSelectedRow();
                saldoc = Float.parseFloat(tablacomprasporpagar.getValueAt(fila, 5).toString());
                saldoc = (float) (Math.round((saldoc) * 100.0) / 100.0);
                montoabono.setValue(saldoc);
            }
        }
    }

    private void detalleabonos() {
        if (tablacomprasporpagar.getRowCount() == 0 && tablacomprasporpagar.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "La tabla no contiene datos que mostrar");
        } else {
            if (tablacomprasporpagar.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(null, "No se ha seleccionado un registro");
            } else {

                if (existenabonos() == true) {

                    detalleabonoproveedor nuevasol = new detalleabonoproveedor();
                    if (panel_center.getComponentCount() > 0 & panel_center.getComponentCount() < 3) //solo uno en t
                    {
                        int fila = tablacomprasporpagar.getSelectedRow();
                        String dao = (String) tablacomprasporpagar.getValueAt(fila, 1).toString();
                        panel_center.add(nuevasol);
                        nuevasol.show();// ver interno
                        nuevasol.setClosable(true);// icono de cerrar
                        nuevasol.toFront();//aparece al frente

                        nuevasol.MostrarTodo(idcompras);
                    }
                } else {
                    if (existenabonos() == false) {
                        JOptionPane.showInternalMessageDialog(this, " No existen abonos de la compra seleccionada ");
                    }
                }
            }
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
        java.awt.GridBagConstraints gridBagConstraints;

        abono = new javax.swing.JDialog();
        jcMousePanel5 = new jcMousePanel.jcMousePanel();
        jLabel2 = new javax.swing.JLabel();
        idcompra = new javax.swing.JTextField();
        montoabono = new javax.swing.JFormattedTextField();
        dcFecha = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        pnlActionButtons1 = new javax.swing.JPanel();
        butnguardarabono = new elaprendiz.gui.button.ButtonRect();
        btncancelarabono = new elaprendiz.gui.button.ButtonRect();
        btncancelarabono1 = new elaprendiz.gui.button.ButtonRect();
        jScrollPane2 = new javax.swing.JScrollPane();
        observacionabono = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jcMousePanelprincipal = new jcMousePanel.jcMousePanel();
        jPanel5 = new javax.swing.JPanel();
        jcMousePanel6 = new jcMousePanel.jcMousePanel();
        jLabel4 = new javax.swing.JLabel();
        nombre = new elaprendiz.gui.textField.TextField();
        nit = new elaprendiz.gui.textField.TextField();
        telefono = new elaprendiz.gui.textField.TextField();
        correo = new elaprendiz.gui.textField.TextField();
        direccion = new elaprendiz.gui.textField.TextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        lbltotalcompra = new javax.swing.JLabel();
        totalcompra = new javax.swing.JLabel();
        jcMousePanel7 = new jcMousePanel.jcMousePanel();
        jScrollPanedetallecompra = new javax.swing.JScrollPane();
        tabladetallecompra = new javax.swing.JTable();
        jScrollPanedetallecompra2 = new javax.swing.JScrollPane();
        tablacomprasporpagar = new javax.swing.JTable();
        pnlActionButtons = new javax.swing.JPanel();
        btnabonar = new elaprendiz.gui.button.ButtonRect();
        btnliquidaradeudo = new elaprendiz.gui.button.ButtonRect();
        btndetalleabonos = new elaprendiz.gui.button.ButtonRect();
        bntSalir2 = new elaprendiz.gui.button.ButtonRect();
        pnlPaginador = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();

        abono.setBounds(new java.awt.Rectangle(220, 200, 270, 200));
        abono.setFocusTraversalPolicyProvider(true);
        abono.setMinimumSize(null);
        abono.setUndecorated(true);

        jcMousePanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 3, true));
        jcMousePanel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fondo/índice.jpg"))); // NOI18N
        jcMousePanel5.setMaximumSize(null);
        jcMousePanel5.setLayout(null);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Fecha:");
        jcMousePanel5.add(jLabel2);
        jLabel2.setBounds(40, 20, 80, 20);

        idcompra.setVisible(false);
        idcompra.setOpaque(false);
        jcMousePanel5.add(idcompra);
        idcompra.setBounds(30, 40, 10, 20);

        montoabono.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
        montoabono.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        montoabono.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        montoabono.setName("precioalmayor"); // NOI18N
        montoabono.setPreferredSize(new java.awt.Dimension(80, 23));
        montoabono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butnguardarabonoActionPerformed(evt);
            }
        });
        montoabono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                montoabonoKeyReleased(evt);
            }
        });
        jcMousePanel5.add(montoabono);
        montoabono.setBounds(130, 50, 140, 23);

        dcFecha.setDate(Calendar.getInstance().getTime());
        dcFecha.setDateFormatString("dd/MM/yyyy");
        dcFecha.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        dcFecha.setMaxSelectableDate(new java.util.Date(3093496470100000L));
        dcFecha.setMinSelectableDate(new java.util.Date(-62135744300000L));
        dcFecha.setPreferredSize(new java.awt.Dimension(120, 22));
        jcMousePanel5.add(dcFecha);
        dcFecha.setBounds(130, 20, 140, 21);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Monto:");
        jcMousePanel5.add(jLabel3);
        jLabel3.setBounds(40, 50, 80, 20);

        pnlActionButtons1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 153, 255), 1, true));
        pnlActionButtons1.setOpaque(false);
        pnlActionButtons1.setPreferredSize(new java.awt.Dimension(786, 52));
        pnlActionButtons1.setLayout(null);

        butnguardarabono.setBackground(new java.awt.Color(102, 204, 0));
        butnguardarabono.setText("Guardar");
        butnguardarabono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butnguardarabonoActionPerformed(evt);
            }
        });
        pnlActionButtons1.add(butnguardarabono);
        butnguardarabono.setBounds(120, 7, 81, 25);

        btncancelarabono.setBackground(new java.awt.Color(102, 204, 0));
        btncancelarabono.setText("Salir");
        btncancelarabono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncancelarabonoActionPerformed(evt);
            }
        });
        pnlActionButtons1.add(btncancelarabono);
        btncancelarabono.setBounds(300, 7, 81, 25);

        btncancelarabono1.setBackground(new java.awt.Color(102, 204, 0));
        btncancelarabono1.setText("Cancelar");
        btncancelarabono1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncancelarabono1ActionPerformed(evt);
            }
        });
        pnlActionButtons1.add(btncancelarabono1);
        btncancelarabono1.setBounds(210, 7, 81, 25);

        jcMousePanel5.add(pnlActionButtons1);
        pnlActionButtons1.setBounds(0, 150, 500, 40);

        observacionabono.setColumns(20);
        observacionabono.setRows(5);
        observacionabono.setAutoscrolls(false);
        observacionabono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                observacionabonoKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(observacionabono);

        jcMousePanel5.add(jScrollPane2);
        jScrollPane2.setBounds(130, 90, 350, 40);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Observación:");
        jcMousePanel5.add(jLabel5);
        jLabel5.setBounds(10, 90, 110, 20);

        javax.swing.GroupLayout abonoLayout = new javax.swing.GroupLayout(abono.getContentPane());
        abono.getContentPane().setLayout(abonoLayout);
        abonoLayout.setHorizontalGroup(
            abonoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jcMousePanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
        );
        abonoLayout.setVerticalGroup(
            abonoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jcMousePanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setMaximizable(true);
        setTitle("Estado de cuenta Proveedor");
        setToolTipText("");
        setName("pagoproveedores"); // NOI18N
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

        jcMousePanelprincipal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fondo/índice.jpg"))); // NOI18N
        jcMousePanelprincipal.setModo(4);
        jcMousePanelprincipal.setName(""); // NOI18N
        jcMousePanelprincipal.setOpaque(false);
        jcMousePanelprincipal.setLayout(null);

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel5.setOpaque(false);

        jcMousePanel6.setModo(5);
        jcMousePanel6.setName(""); // NOI18N
        jcMousePanel6.setOpaque(false);
        jcMousePanel6.setLayout(null);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("*Nombre:");
        jcMousePanel6.add(jLabel4);
        jLabel4.setBounds(130, 20, 80, 20);

        nombre.setEditable(false);
        nombre.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        nombre.setPreferredSize(new java.awt.Dimension(120, 21));
        jcMousePanel6.add(nombre);
        nombre.setBounds(230, 20, 250, 21);

        nit.setEditable(false);
        nit.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        nit.setPreferredSize(new java.awt.Dimension(120, 21));
        jcMousePanel6.add(nit);
        nit.setBounds(580, 20, 150, 21);

        telefono.setEditable(false);
        telefono.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        telefono.setPreferredSize(new java.awt.Dimension(120, 21));
        jcMousePanel6.add(telefono);
        telefono.setBounds(580, 50, 150, 21);

        correo.setEditable(false);
        correo.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        correo.setInputVerifier(new VerificadorEntrada(125,VerificadorEntrada.EMAIL));
        correo.setPreferredSize(new java.awt.Dimension(120, 21));
        jcMousePanel6.add(correo);
        correo.setBounds(230, 50, 250, 21);

        direccion.setEditable(false);
        direccion.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        direccion.setPreferredSize(new java.awt.Dimension(120, 21));
        jcMousePanel6.add(direccion);
        direccion.setBounds(230, 80, 500, 21);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel6.setText("Correo:");
        jcMousePanel6.add(jLabel6);
        jLabel6.setBounds(130, 50, 80, 20);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel11.setText("*Dirección:");
        jcMousePanel6.add(jLabel11);
        jLabel11.setBounds(130, 80, 80, 20);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel12.setText("Telefono:");
        jcMousePanel6.add(jLabel12);
        jLabel12.setBounds(490, 50, 80, 20);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel13.setText("*Nit:");
        jcMousePanel6.add(jLabel13);
        jLabel13.setBounds(490, 20, 80, 20);

        lbltotalcompra.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lbltotalcompra.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbltotalcompra.setText("Saldo Actual:");
        jcMousePanel6.add(lbltotalcompra);
        lbltotalcompra.setBounds(1, 120, 120, 40);

        totalcompra.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        totalcompra.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalcompra.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        totalcompra.setOpaque(true);
        jcMousePanel6.add(totalcompra);
        totalcompra.setBounds(120, 120, 110, 40);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jcMousePanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 927, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jcMousePanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jcMousePanelprincipal.add(jPanel5);
        jPanel5.setBounds(0, 40, 930, 170);

        jcMousePanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jcMousePanel7.setModo(5);
        jcMousePanel7.setOpaque(false);

        jScrollPanedetallecompra.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPanedetallecompra.setViewportBorder(javax.swing.BorderFactory.createTitledBorder("Detalle de Compra"));

        tabladetallecompra.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tabladetallecompra.setModel(model = new DefaultTableModel(null, titulos)
            {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
            tabladetallecompra.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            tabladetallecompra.setEnabled(false);
            tabladetallecompra.setName(""); // NOI18N
            tabladetallecompra.setRowHeight(24);
            tabladetallecompra.setRowSelectionAllowed(false);
            formatotabla();
            jScrollPanedetallecompra.setViewportView(tabladetallecompra);

            jScrollPanedetallecompra2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            jScrollPanedetallecompra2.setViewportBorder(javax.swing.BorderFactory.createTitledBorder("Espacio=Seleccionar"));

            tablacomprasporpagar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
            tablacomprasporpagar.setModel(model2 = new DefaultTableModel(null, titulos2)
                {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                });
                tablacomprasporpagar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                tablacomprasporpagar.setName(""); // NOI18N
                tablacomprasporpagar.setRowHeight(24);
                formatotabla2();
                tablacomprasporpagar.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        tablacomprasporpagarMouseClicked(evt);
                    }
                    public void mousePressed(java.awt.event.MouseEvent evt) {
                        tablacomprasporpagarMouseClicked(evt);
                    }
                });
                jScrollPanedetallecompra2.setViewportView(tablacomprasporpagar);

                javax.swing.GroupLayout jcMousePanel7Layout = new javax.swing.GroupLayout(jcMousePanel7);
                jcMousePanel7.setLayout(jcMousePanel7Layout);
                jcMousePanel7Layout.setHorizontalGroup(
                    jcMousePanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jcMousePanel7Layout.createSequentialGroup()
                        .addComponent(jScrollPanedetallecompra2, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPanedetallecompra, javax.swing.GroupLayout.PREFERRED_SIZE, 579, javax.swing.GroupLayout.PREFERRED_SIZE))
                );
                jcMousePanel7Layout.setVerticalGroup(
                    jcMousePanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPanedetallecompra, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                    .addComponent(jScrollPanedetallecompra2, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                );

                jcMousePanelprincipal.add(jcMousePanel7);
                jcMousePanel7.setBounds(0, 210, 930, 230);

                pnlActionButtons.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 153, 255), 1, true));
                pnlActionButtons.setOpaque(false);
                pnlActionButtons.setPreferredSize(new java.awt.Dimension(786, 52));
                java.awt.GridBagLayout pnlActionButtonsLayout = new java.awt.GridBagLayout();
                pnlActionButtonsLayout.columnWidths = new int[] {70};
                pnlActionButtons.setLayout(pnlActionButtonsLayout);

                btnabonar.setBackground(new java.awt.Color(51, 153, 255));
                btnabonar.setMnemonic(KeyEvent.VK_A);
                btnabonar.setText("Abonar");
                btnabonar.setToolTipText("ALT+A");
                btnabonar.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        buttonRect6ActionPerformed(evt);
                    }
                });
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 1;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                gridBagConstraints.insets = new java.awt.Insets(13, 5, 12, 0);
                pnlActionButtons.add(btnabonar, gridBagConstraints);

                btnliquidaradeudo.setBackground(new java.awt.Color(51, 153, 255));
                btnliquidaradeudo.setMnemonic(KeyEvent.VK_L);
                btnliquidaradeudo.setText("Liquidar Adeudo");
                btnliquidaradeudo.setToolTipText("ALT+L");
                btnliquidaradeudo.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        btnliquidaradeudoActionPerformed(evt);
                    }
                });
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 2;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                gridBagConstraints.insets = new java.awt.Insets(13, 5, 12, 0);
                pnlActionButtons.add(btnliquidaradeudo, gridBagConstraints);

                btndetalleabonos.setBackground(new java.awt.Color(51, 153, 255));
                btndetalleabonos.setMnemonic(KeyEvent.VK_D);
                btndetalleabonos.setText("Detalle de Abonos");
                btndetalleabonos.setToolTipText("ALT+D");
                btndetalleabonos.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        buttonRect1ActionPerformed(evt);
                    }
                });
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 3;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                gridBagConstraints.insets = new java.awt.Insets(13, 5, 12, 0);
                pnlActionButtons.add(btndetalleabonos, gridBagConstraints);

                bntSalir2.setBackground(new java.awt.Color(51, 153, 255));
                bntSalir2.setText("Salir    ");
                bntSalir2.setToolTipText("ESC");
                bntSalir2.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        bntSalir2ActionPerformed(evt);
                    }
                });
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 5;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                gridBagConstraints.insets = new java.awt.Insets(13, 5, 12, 93);
                pnlActionButtons.add(bntSalir2, gridBagConstraints);

                jcMousePanelprincipal.add(pnlActionButtons);
                pnlActionButtons.setBounds(0, 440, 930, 50);

                pnlPaginador.setBackground(new java.awt.Color(0, 153, 204));
                pnlPaginador.setPreferredSize(new java.awt.Dimension(786, 40));
                pnlPaginador.setLayout(new java.awt.GridBagLayout());

                jLabel15.setFont(new java.awt.Font("Script MT Bold", 1, 32)); // NOI18N
                jLabel15.setForeground(new java.awt.Color(255, 255, 255));
                jLabel15.setText("<--Estado de Cuenta Proveedores-->");
                pnlPaginador.add(jLabel15, new java.awt.GridBagConstraints());

                jcMousePanelprincipal.add(pnlPaginador);
                pnlPaginador.setBounds(0, 0, 930, 40);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcMousePanelprincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 931, Short.MAX_VALUE)
                );
                layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcMousePanelprincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE)
                );

                pack();
            }// </editor-fold>//GEN-END:initComponents

    public void sumartotal() {
        //corregir cuando solo hay una linea da error
        if (tablacomprasporpagar.getRowCount() == 0 && tablacomprasporpagar.getSelectedRow() == -1) {
            //JOptionPane.showMessageDialog(null, "La tabla no contiene datos que modificar");
            totalcompra.setText("0.00");
        } else {
            float Actual, Resultado = 0;
            for (int i = 0; i < model2.getRowCount(); i++) {
                {
                    Actual = Float.parseFloat(tablacomprasporpagar.getValueAt(i, 5).toString());
                    Resultado = Resultado + Actual;
                }
                totalcompra.setText("" + Math.round(Resultado * 100.0) / 100.0);
            }
        }
    }
    private void tablacomprasporpagarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablacomprasporpagarMouseClicked
        // TODO add your handling code here:
        int fila = tablacomprasporpagar.getSelectedRow();
        String dato = tablacomprasporpagar.getValueAt(fila, 1).toString();
        MostrarTodo(dato);
        idcompras = dato;
    }//GEN-LAST:event_tablacomprasporpagarMouseClicked

    private void bntSalir2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntSalir2ActionPerformed
        // TODO add your handling code here:
        cerrarVentana();
    }//GEN-LAST:event_bntSalir2ActionPerformed

    private void buttonRect6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRect6ActionPerformed
        // TODO add your handling code here:
        abonar();

    }//GEN-LAST:event_buttonRect6ActionPerformed

    private void btnliquidaradeudoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnliquidaradeudoActionPerformed
        liquidardeudas();
    }//GEN-LAST:event_btnliquidaradeudoActionPerformed

    private void buttonRect1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRect1ActionPerformed
        // TODO add your handling code here:
        detalleabonos();
    }//GEN-LAST:event_buttonRect1ActionPerformed

    private void butnguardarabonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butnguardarabonoActionPerformed
        // TODO add your handling code here:
        int fil = tablacomprasporpagar.getSelectedRow();
        float dat = Float.parseFloat(tablacomprasporpagar.getValueAt(fil, 5).toString());
        float dato3 = Float.parseFloat(Validar(montoabono.getValue().toString()));

        dat = (float) (Math.round((dat) * 100.0) / 100.0);
        dato3 = (float) (Math.round((dato3) * 100.0) / 100.0);

        if (dato3 > dat) {
            JOptionPane.showInternalMessageDialog(this, "El abono es mayor al saldo ");
        } else {
            if (Validar(montoabono.getValue().toString()).equals(0) || getFecha() == null || Validar(montoabono.getValue().toString()).equals("0")) {
                JOptionPane.showInternalMessageDialog(this, "Complete los campos obligatorios");
            } else {
                int resp;
                resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Grabar el Registro?", "Pregunta", 0);
                if (resp == 0) {
                    try {
                        abono.setVisible(false);
                        conn = BdConexion.getConexion();
                        //PreparedStatement nos permite crear instrucciones SQL compiladas, que se ejecutan con más efi ciencia que los objetos Statement
                        //también pueden especifi car parámetros,lo cual las hace más fl exibles que las instrucciones Statement
                        int fila = tablacomprasporpagar.getSelectedRow();
                        String dato = tablacomprasporpagar.getValueAt(fila, 1).toString();

                        PreparedStatement ps = conn.prepareCall("insert into xpagardetalle (fecha,monto,compra_idcompra,usuario_idusuario,observacion) values (?,?,?,?,?)");
                        ps.setString(1, getFecha());
                        ps.setString(2, Validar(montoabono.getText()));
                        ps.setString(3, dato);
                        //Login entrar = new Login();
                        ps.setString(4, "" + AccesoUsuario.idusu());
                        ps.setString(5, observacionabono.getText());

                        int n = ps.executeUpdate();

                        float montoc, saldoc, nsaldo;
                        montoc = Float.parseFloat(Validar(montoabono.getText()));
                        saldoc = Float.parseFloat(tablacomprasporpagar.getValueAt(fila, 5).toString());
                        nsaldo = (float) (Math.round((saldoc - montoc) * 100.0) / 100.0);

                        //tablacomprasporpagar.getValueAt(i, 4).toString()
                        int n2 = 0;
                        char status;

                        if (montoc > 0 & montoc <= saldoc) {
                            status = 'T';
                            if (montoc == saldoc) {
                                status = 'F';
                            }
                            PreparedStatement ps2 = conn.prepareStatement(sqlp.UPDATECOMPRA);
                            ps2.setFloat(1, nsaldo);
                            ps2.setString(2, "" + status);
                            ps2.setString(3, dato);

                            n2 = ps2.executeUpdate();

                        }

//                    if (n2 > 0) {
//                            JOptionPane.showInternalMessageDialog(this, "Datos guardados correctamente");
//                            Llenar();
//                            Desabilitar();
//                            Limpiar();
//                        }
                        //conn.close();
                        if (n > 0 & n2 > 0) {
                            dcFecha.setDate(Calendar.getInstance().getTime());
                            montoabono.setValue(null);
                            observacionabono.setText("");

                            abono.setVisible(false);

                            int fila2 = tablacomprasporpagar.getSelectedRow();
                            String dato2 = tablacomprasporpagar.getValueAt(fila2, 0).toString();
                            Llenar(idproveedor);
                            //MostrarTodo(idcompras);
                            removejtable();
                            sumartotal();
                            JOptionPane.showInternalMessageDialog(this, "Datos guardados correctamente");
                        }
                    } catch (SQLException e) {
                        JOptionPane.showInternalMessageDialog(this, "Error  al guardar \n Verifique los datos e intente nuevamente ", "Error", JOptionPane.ERROR_MESSAGE);
                        System.out.print(e.getMessage());
                    }
//                Llenar();
//                Desabilitar();
//                Limpiar();

                }
            }
        }
    }//GEN-LAST:event_butnguardarabonoActionPerformed

    private void btncancelarabonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelarabonoActionPerformed
        // TODO add your handling code here:
        this.abono.dispose();
    }//GEN-LAST:event_btncancelarabonoActionPerformed

    private void btncancelarabono1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelarabono1ActionPerformed
        // TODO add your handling code here:
        dcFecha.setDate(Calendar.getInstance().getTime());
        montoabono.setValue(null);

    }//GEN-LAST:event_btncancelarabono1ActionPerformed

    private void observacionabonoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_observacionabonoKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == 27) {
            abono.setVisible(false);
        }
    }//GEN-LAST:event_observacionabonoKeyReleased

    private void montoabonoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_montoabonoKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == 27) {
            abono.setVisible(false);
        }
    }//GEN-LAST:event_montoabonoKeyReleased

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        cerrarVentana();
    }//GEN-LAST:event_formInternalFrameClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog abono;
    private elaprendiz.gui.button.ButtonRect bntSalir2;
    private elaprendiz.gui.button.ButtonRect btnabonar;
    private elaprendiz.gui.button.ButtonRect btncancelarabono;
    private elaprendiz.gui.button.ButtonRect btncancelarabono1;
    private elaprendiz.gui.button.ButtonRect btndetalleabonos;
    private elaprendiz.gui.button.ButtonRect btnliquidaradeudo;
    private elaprendiz.gui.button.ButtonRect butnguardarabono;
    private elaprendiz.gui.textField.TextField correo;
    private com.toedter.calendar.JDateChooser dcFecha;
    private elaprendiz.gui.textField.TextField direccion;
    private javax.swing.JTextField idcompra;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPanedetallecompra;
    private javax.swing.JScrollPane jScrollPanedetallecompra2;
    private jcMousePanel.jcMousePanel jcMousePanel5;
    private jcMousePanel.jcMousePanel jcMousePanel6;
    private jcMousePanel.jcMousePanel jcMousePanel7;
    private jcMousePanel.jcMousePanel jcMousePanelprincipal;
    private javax.swing.JLabel lbltotalcompra;
    private javax.swing.JFormattedTextField montoabono;
    private elaprendiz.gui.textField.TextField nit;
    private elaprendiz.gui.textField.TextField nombre;
    private javax.swing.JTextArea observacionabono;
    private javax.swing.JPanel pnlActionButtons;
    private javax.swing.JPanel pnlActionButtons1;
    private javax.swing.JPanel pnlPaginador;
    public static javax.swing.JTable tablacomprasporpagar;
    public static javax.swing.JTable tabladetallecompra;
    private elaprendiz.gui.textField.TextField telefono;
    private javax.swing.JLabel totalcompra;
    // End of variables declaration//GEN-END:variables
}
