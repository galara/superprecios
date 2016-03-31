/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import BD.BdConexion;
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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Glara
 */
public class pagoclientes extends javax.swing.JInternalFrame {

    private String archivoRecurso = "controlador-bd";
    DefaultTableModel model, model2;
    Connection conn;//getConnection intentara establecer una conexión.
    Statement sent;
    String[] titulos = {"Id", "Codigo", "Descripción", "Cantidad", "Precio", "Subtotal"};
    String[] titulos2 = {"idp", "idv", "Fecha", "No.Doc", "Total", "Saldo", "interes", "FechaPago"};
    //java.sql.Connection conn;//getConnection intentara establecer una conexión.
    String idcompras, idcliente;
    SimpleDateFormat formatof = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Creates new form pedido
     */
    public pagoclientes() {

        initComponents();
        //Llenar();
        formatotabla2();
        formatotabla();
        addEscapeKey();

        tablacomprasporpagar.getModel().addTableModelListener((TableModelEvent e) -> {
            sumartotal();
            formatotabla2();

            tablacomprasporpagar.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyPressed(java.awt.event.KeyEvent arg0) {
                    int key = arg0.getKeyCode();

                    if (key == java.awt.event.KeyEvent.VK_F2) {
                        liquidardeudas();
                    }
                    if (key == java.awt.event.KeyEvent.VK_F1) {
                        abonar();
                    }
                    if (key == java.awt.event.KeyEvent.VK_F3) {
                        detalleabonos();
                    }

                    if (key == java.awt.event.KeyEvent.VK_SPACE) {
                        int fila = tablacomprasporpagar.getSelectedRow();
                        String dato2 = tablacomprasporpagar.getValueAt(fila, 1).toString();
                        MostrarTodo(dato2);
                        idcompras = dato2;
                    }
                    if (key == java.awt.event.KeyEvent.VK_DOWN || key == java.awt.event.KeyEvent.VK_UP) {
                        removejtable();
                    }
                }
            });
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
        } else {
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
        idcliente = datos[5];
    }

    private String getFecha() {

        try {
            String fecha;
            int años = dcFecha.getCalendar().get(Calendar.YEAR);
            int dias = dcFecha.getCalendar().get(Calendar.DAY_OF_MONTH);
            int mess = dcFecha.getCalendar().get(Calendar.MONTH) + 1;
//            int hours = dcFecha.getCalendar().get(Calendar.HOUR_OF_DAY);
//            int minutes = dcFecha.getCalendar().get(Calendar.MINUTE);
//            int seconds = dcFecha.getCalendar().get(Calendar.SECOND);

            fecha = "" + años + "-" + mess + "-" + dias;// + " " + hours + ":" + minutes + ":" + seconds;
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
                column.setPreferredWidth(30); //Difine el ancho de la columna
                tabladetallecompra.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);
            } else if (i == 2) {
                column.setPreferredWidth(210); //Difine el ancho de la columna
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
        for (int i = 0; i < 8; i++) {
            column = tablacomprasporpagar.getColumnModel().getColumn(i);
            if (i == 0 || i == 1) {
                column.setMaxWidth(0);
                column.setMinWidth(0);
            } else if (i > 2 & i <= 5) {
                column.setPreferredWidth(30);//Difine el ancho de la columna
                tablacomprasporpagar.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);
            } else if (i == 2 || i == 6 || i == 7) {
                column.setPreferredWidth(45);//Difine el ancho de la columna
                tablacomprasporpagar.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);
            }
        }
    }

    private boolean existenabonos() {
        try {
            int fila = tablacomprasporpagar.getSelectedRow();
            String Dato = (String) tablacomprasporpagar.getValueAt(fila, 1).toString();
            conn = BdConexion.getConexion();
            String sql = "select xcobrarclientes.idxcobrarclientes,xcobrarclientes.fecha,xcobrarclientes.monto,salida.idsalida,salida.salida,usuario.nombreusuario from xcobrarclientes INNER JOIN salida on salida.idsalida=xcobrarclientes.salida_idsalida INNER JOIN usuario on usuario.idusuario=xcobrarclientes.usuario_idusuario where xcobrarclientes.salida_idsalida=" + Dato + " group by xcobrarclientes.idxcobrarclientes order by xcobrarclientes.idxcobrarclientes asc";
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
            String sql = "select * from salida where clientes_idclientes=" + Dato + " and estado='T'";
            ResultSet rs = sent.executeQuery(sql);// especifica la consulta y la ejecuta

            String[] fila = new String[8];
            while (rs.next()) {

                fila[0] = rs.getString("clientes_idclientes");
                fila[1] = rs.getString("idsalida");
                fila[2] = formatof.format(rs.getDate("fecha"));
                fila[3] = rs.getString("salida");
                fila[4] = rs.getString("total");
                fila[5] = rs.getString("saldo");
                fila[7] = formatof.format(rs.getDate("fechapago"));
                if (rs.getString("interesactual") == null) {
                    fila[6] = "0";
                } else {
                    fila[6] = "" + rs.getFloat("interesactual");
                }

                model2.addRow(fila);
            }
            //conn.close();
            tablacomprasporpagar.setModel(model2);
            formatotabla2();
            sumartotal();

//            this.bntGuardar.setEnabled(false);
//            this.bntModificar.setEnabled(false);
//            this.bntEliminar.setEnabled(false);
//            this.bntNuevo.setEnabled(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en BD " + e.toString());
            this.dispose();
        }
    }

    private void MostrarTodo(String Dato) {
        try {
            conn = BdConexion.getConexion();
            String sql = "SELECT producto.idproducto,producto.codigo,producto.nombre,detallesalida.cantidad,detallesalida.precio,detallesalida.lote_idlote FROM  producto INNER JOIN lote ON producto.idproducto = lote.producto_idproducto INNER JOIN detallesalida ON lote.idlote = detallesalida.lote_idlote where detallesalida.salida_idsalida=" + Dato + " order by detallesalida.iddetallesalida asc";
            removejtable();
            sent = conn.createStatement();// crea objeto Statement para consultar la base de datos
            ResultSet rs = sent.executeQuery(sql);// especifica la consulta y la ejecuta

            if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                int count = 0;
                float subt, cant, prec;
                rs.beforeFirst();//regresa el puntero al primer registro
                String[] fila = new String[7];
                while (rs.next()) {//mientras tenga registros que haga lo siguiente

                    fila[0] = rs.getString("producto.idproducto");
                    fila[1] = rs.getString("producto.codigo");
                    fila[2] = rs.getString("producto.nombre");
                    fila[3] = rs.getString("detallesalida.cantidad");

                    cant = rs.getFloat("detallesalida.cantidad");
                    prec = rs.getFloat("detallesalida.precio");
                    subt = (float) (Math.round((cant * prec) * 100.0) / 100.0);

                    fila[4] = rs.getString("detallesalida.precio");
                    fila[5] = (String) ("" + subt);

                    model.addRow(fila);
                    count = count + 1;
                }
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
            sql = " select iddetint,montointeres,fechaal from detalleinteres where salida_idsalida=" + Dato + "";
            sent = conn.createStatement();// crea objeto Statement para consultar la base de datos
            ResultSet rss = sent.executeQuery(sql);// especifica la consulta y la ejecuta
            String[] fila = new String[7];
            float prec = 0, subt = 0;
            while (rss.next()) {//mientras tenga registros que haga lo siguiente

                fila[0] = "" + rss.getInt("iddetint");
                fila[1] = "S/N";
                fila[2] = "Interes " + formatof.format(rss.getDate("fechaal"));
                fila[3] = "1";

                prec = rss.getFloat("montointeres");
                subt = (float) (Math.round((1 * prec) * 100.0) / 100.0);

                fila[4] = "" + prec;

                fila[5] = (String) ("" + subt);

                model.addRow(fila);

            }

            tabladetallecompra.setModel(model);
            //formatotabla();
            //conn.close();
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

    private void detalleabonos() {
        if (tablacomprasporpagar.getRowCount() == 0 && tablacomprasporpagar.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "La tabla no contiene datos que mostrar");
        } else {
            if (tablacomprasporpagar.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(null, "No se ha seleccionado un registro");
            } else {

                if (existenabonos() == true) {

                    detalleabonoclientes nuevasol = new detalleabonoclientes();

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

    private void abonar() {
        if (tablacomprasporpagar.getRowCount() == 0 && tablacomprasporpagar.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "La tabla no contiene datos que modificar");
        } else {
            if (tablacomprasporpagar.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(null, "No se ha seleccionado un registro");
            } else {
                dcFecha.setDate(Calendar.getInstance().getTime());
                montoabono.setValue(null);
                efectivo.setValue(null);
                observacionabono.setText("");
                abono.setVisible(true);
                formapagoabono.setSelectedIndex(-1);
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
                float saldoc, interes;
                int fila = tablacomprasporpagar.getSelectedRow();
                saldoc = Float.parseFloat(tablacomprasporpagar.getValueAt(fila, 5).toString());
                interes = Float.parseFloat(tablacomprasporpagar.getValueAt(fila, 6).toString());
                saldoc = (float) (Math.round((saldoc + interes) * 100.0) / 100.0);
                montoabono.setValue(saldoc);

            }
        }
    }

    private void abrircomprobante(int idabono) {
        if (idabono > 0) {
            try {
                String ids = "";
                ids = ("" + idabono);
                int tipo = Integer.parseInt(ids);

                String archivo = "";
                archivo = "comprobanteabonocliente.jasper";

                if (archivo == null) {
                    System.out.println("No hay ARCHIVO " + archivo);
                    System.exit(2);
                }
                JasperReport masterReport = null;

                try {
                    masterReport = (JasperReport) JRLoader.loadObject(archivo);
                } catch (JRException e) {
                    System.out.println("error cargado el reporte maestro " + e.getMessage());
                    System.exit(3);
                }

                conn = BdConexion.getConexion();
                Map parametro = new HashMap();
                parametro.put("idsalida", idabono);
                JasperPrint impresor = JasperFillManager.fillReport(masterReport, parametro, conn);
                JasperViewer jviewer = new JasperViewer(impresor, false);
                jviewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
                jviewer.setTitle("Comprobante de Abono");
                jviewer.setVisible(true);
                //conn.close();
            } catch (JRException e) {
                JOptionPane.showMessageDialog(null, "Error " + e.toString());
            }
        } else {
            JOptionPane.showMessageDialog(null, "No hay parametro para abrir el reporte");
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
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        observacionabono = new javax.swing.JTextArea();
        formapagoabono = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        efectivo = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
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
        interesT = new javax.swing.JLabel();
        lbltotalcompra1 = new javax.swing.JLabel();
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

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Observación:");
        jcMousePanel5.add(jLabel5);
        jLabel5.setBounds(10, 90, 110, 20);

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

        formapagoabono.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        formapagoabono.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "Contado", "Cheque", "Deposito", "Otro" }));
        formapagoabono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                formapagoabonoActionPerformed(evt);
            }
        });
        jcMousePanel5.add(formapagoabono);
        formapagoabono.setBounds(390, 50, 90, 23);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Efectivo");
        jcMousePanel5.add(jLabel1);
        jLabel1.setBounds(290, 20, 100, 20);

        efectivo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
        efectivo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        efectivo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        efectivo.setName("precioalmayor"); // NOI18N
        efectivo.setPreferredSize(new java.awt.Dimension(80, 23));
        efectivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                efectivobutnguardarabonoActionPerformed(evt);
            }
        });
        efectivo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                efectivoKeyReleased(evt);
            }
        });
        jcMousePanel5.add(efectivo);
        efectivo.setBounds(390, 20, 90, 23);

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Tipo Pago");
        jcMousePanel5.add(jLabel7);
        jLabel7.setBounds(290, 50, 100, 20);

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
        setTitle("Estado de cuenta Cliente");
        setToolTipText("");
        setName("pagoclientes"); // NOI18N
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
        lbltotalcompra.setText("Intereses");
        jcMousePanel6.add(lbltotalcompra);
        lbltotalcompra.setBounds(230, 120, 120, 40);

        totalcompra.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        totalcompra.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalcompra.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        totalcompra.setOpaque(true);
        jcMousePanel6.add(totalcompra);
        totalcompra.setBounds(120, 120, 110, 40);

        interesT.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        interesT.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        interesT.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        interesT.setOpaque(true);
        jcMousePanel6.add(interesT);
        interesT.setBounds(350, 120, 110, 40);

        lbltotalcompra1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lbltotalcompra1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbltotalcompra1.setText("Saldo Actual:");
        jcMousePanel6.add(lbltotalcompra1);
        lbltotalcompra1.setBounds(1, 120, 120, 40);

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
        jScrollPanedetallecompra.setViewportBorder(javax.swing.BorderFactory.createTitledBorder("Detalle Venta Enter modificar interes"));

        tabladetallecompra.setModel(model = new DefaultTableModel(null, titulos)
            {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
            tabladetallecompra.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            tabladetallecompra.setName(""); // NOI18N
            tabladetallecompra.setRowHeight(24);
            formatotabla();
            tabladetallecompra.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    tabladetallecompraKeyPressed(evt);
                }
            });
            jScrollPanedetallecompra.setViewportView(tabladetallecompra);

            jScrollPanedetallecompra2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            jScrollPanedetallecompra2.setViewportBorder(javax.swing.BorderFactory.createTitledBorder("Espacio=Seleccionar"));

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
                        .addComponent(jScrollPanedetallecompra2, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPanedetallecompra, javax.swing.GroupLayout.PREFERRED_SIZE, 543, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                pnlActionButtons.setLayout(null);

                btnabonar.setBackground(new java.awt.Color(51, 153, 255));
                btnabonar.setMnemonic(KeyEvent.VK_A);
                btnabonar.setText("Abonar");
                btnabonar.setToolTipText("ALT+A");
                btnabonar.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        buttonRect6ActionPerformed(evt);
                    }
                });
                pnlActionButtons.add(btnabonar);
                btnabonar.setBounds(179, 13, 82, 25);

                btnliquidaradeudo.setBackground(new java.awt.Color(51, 153, 255));
                btnliquidaradeudo.setMnemonic(KeyEvent.VK_L);
                btnliquidaradeudo.setText("Liquidar Adeudo");
                btnliquidaradeudo.setToolTipText("ALT+L");
                btnliquidaradeudo.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        btnliquidaradeudoActionPerformed(evt);
                    }
                });
                pnlActionButtons.add(btnliquidaradeudo);
                btnliquidaradeudo.setBounds(266, 13, 147, 25);

                btndetalleabonos.setBackground(new java.awt.Color(51, 153, 255));
                btndetalleabonos.setMnemonic(KeyEvent.VK_D);
                btndetalleabonos.setText("Detalle de Abonos");
                btndetalleabonos.setToolTipText("ALT+D");
                btndetalleabonos.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        buttonRect1ActionPerformed(evt);
                    }
                });
                pnlActionButtons.add(btndetalleabonos);
                btndetalleabonos.setBounds(418, 13, 160, 25);

                bntSalir2.setBackground(new java.awt.Color(51, 153, 255));
                bntSalir2.setText("Salir    ");
                bntSalir2.setToolTipText("ESC");
                bntSalir2.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        bntSalir2ActionPerformed(evt);
                    }
                });
                pnlActionButtons.add(bntSalir2);
                bntSalir2.setBounds(583, 13, 79, 25);

                jcMousePanelprincipal.add(pnlActionButtons);
                pnlActionButtons.setBounds(0, 440, 930, 50);

                pnlPaginador.setBackground(new java.awt.Color(0, 153, 204));
                pnlPaginador.setPreferredSize(new java.awt.Dimension(786, 40));
                pnlPaginador.setLayout(new java.awt.GridBagLayout());

                jLabel15.setFont(new java.awt.Font("Script MT Bold", 1, 32)); // NOI18N
                jLabel15.setForeground(new java.awt.Color(255, 255, 255));
                jLabel15.setText("<--Estado de Cuenta Clientes-->");
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
            float Actual, Resultado = 0, Actual1, Resultado1 = 0;
            for (int i = 0; i < model2.getRowCount(); i++) {
                {
                    Actual = Float.parseFloat(tablacomprasporpagar.getValueAt(i, 5).toString());
                    Resultado = Resultado + Actual;
                    Actual1 = Float.parseFloat(tablacomprasporpagar.getValueAt(i, 6).toString());
                    Resultado1 = Resultado1 + Actual1;
                }
                interesT.setText("" + Math.round(Resultado1 * 100.0) / 100.0);
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
        float dat = 0, dato3 = 0, vuelto = 0, interes = 0;
        int indicador = 0;
        interes = Float.parseFloat(tablacomprasporpagar.getValueAt(fil, 6).toString());
        dat = Float.parseFloat(tablacomprasporpagar.getValueAt(fil, 5).toString());
        dat = (float) (Math.round((dat) * 100.0) / 100.0) + (float) (Math.round((interes) * 100.0) / 100.0);

//        if (montoabono.getValue() == null) {
//            dato3 = Float.parseFloat(Validar(montoabono.getValue().toString()));
//            dato3 = (float) (Math.round((dato3) * 100.0) / 100.0);
//        }
//        
//        else if (efectivo.getValue() == null) {
//            vuelto = Float.parseFloat(Validar(efectivo.getValue().toString()));
//            vuelto = (float) (Math.round((vuelto) * 100.0) / 100.0);
//        }
//        if (dato3 > dat) {
//            JOptionPane.showInternalMessageDialog(this, "El abono es mayor al saldo ");
//        }
        //else {
        if (getFecha() == null || formapagoabono.getSelectedIndex() <= 0 || montoabono.getValue() == null || montoabono.getValue().toString().equals("0") || efectivo.getValue() == null || efectivo.getValue().toString().equals("0")) {
            JOptionPane.showInternalMessageDialog(this, "Complete los campos obligatorios");
        } else {

            //if (!montoabono.getValue().equals(0)) {
            dato3 = Float.parseFloat(Validar(montoabono.getValue().toString()));
            dato3 = (float) (Math.round((dato3) * 100.0) / 100.0);
            //}
            //if (!efectivo.getValue().equals(0)) {
            vuelto = Float.parseFloat(Validar(efectivo.getValue().toString()));
            vuelto = (float) (Math.round((vuelto) * 100.0) / 100.0);
            //}

            if (dato3 > dat) {
                JOptionPane.showInternalMessageDialog(this, "El abono debe ser menor o igual al saldo ");
            } else if (vuelto < dato3) {
                JOptionPane.showInternalMessageDialog(this, "El efectivo debe ser mayor o igual al abono ");
            } else {
                int resp;
                resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Grabar el Registro?", "Pregunta", 0);
                if (resp == 0) {
                    try {
                        abono.setVisible(false);
                        conn = BdConexion.getConexion();
                        //PreparedStatement nos permite crear instrucciones SQL compiladas, que se ejecutan con más efi ciencia que los objetos Statement
                        //también pueden especifi car parámetros,lo cual las hace más fl exibles que las instrucciones Statement
                        int idabono = 0;
                        int fila = tablacomprasporpagar.getSelectedRow();

                        float montoc, saldoc, nsaldo, nsaldototal, nsaldototal2, Tinteres = 0;
                        montoc = Float.parseFloat(Validar(montoabono.getText()));
                        saldoc = Float.parseFloat(tablacomprasporpagar.getValueAt(fila, 5).toString());
                        nsaldo = (float) (Math.round((saldoc - montoc) * 100.0) / 100.0);
                        Tinteres = Float.parseFloat(Validar(interesT.getText()));

                        if (nsaldo < 0) {
                            nsaldo = (float) (Math.round((montoc - saldoc) * 100.0) / 100.0);
                            Tinteres = (Float.parseFloat(Validar(interesT.getText()))) - nsaldo;

                            nsaldo = 0;
                        }

                        String dato = tablacomprasporpagar.getValueAt(fila, 1).toString();
                        String sql = "insert into xcobrarclientes (fecha,monto,salida_idsalida,usuario_idusuario,observacion,tipopago,nsaldoventa,nsaldototal,saldointeres) values (?,?,?,?,?,?,?,?,?)";
                        PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                        ps.setString(1, getFecha());
                        ps.setString(2, Validar(montoabono.getText()));
                        ps.setString(3, dato);
                        //Login entrar = new Login();
                        ps.setString(4, "" + AccesoUsuario.idusu());
                        ps.setString(5, observacionabono.getText());
                        ps.setString(6, (formapagoabono.getSelectedItem().toString()));
                        ps.setFloat(7, nsaldo);
                        nsaldototal = Float.parseFloat(Validar(totalcompra.getText()));
                        if (nsaldototal >= montoc) {
                            if (montoc > saldoc) {
                                float sobrante2 = montoc - saldoc;
                                nsaldototal2 = (float) (Math.round(((nsaldototal + sobrante2) - montoc) * 100.0) / 100.0);
                                ps.setFloat(8, nsaldototal2);
                            } else if (montoc < saldoc) {
                                nsaldototal2 = (float) (Math.round((nsaldototal - montoc) * 100.0) / 100.0);
                                ps.setFloat(8, nsaldototal2);
                            }
                            else if (montoc == saldoc) {
                                nsaldototal2 = (float) (Math.round((nsaldototal - montoc) * 100.0) / 100.0);
                                ps.setFloat(8, nsaldototal2);
                            }
                        } else {
                            ps.setFloat(8, 0);
                        }

                        ps.setFloat(9, Tinteres);

                        int n = ps.executeUpdate();
                        if (n > 0) {
                            ResultSet rs = ps.getGeneratedKeys();
                            while (rs.next()) {
                                idabono = rs.getInt(1);
                            }

                        }

                        //tablacomprasporpagar.getValueAt(i, 4).toString()
                        int n2 = 0;
                        char status;

                        if (montoc > 0 & montoc <= (saldoc + interes)) {
                            status = 'T';

                            if (montoc > saldoc) {
                                float sobrante = montoc - saldoc;
                                interes = (float) (Math.round((interes - sobrante) * 100.0) / 100.0);
                            }
                            //if (montoc == (saldoc + interes)) {
                            if (interes == 0 & nsaldo == 0) {
                                status = 'F';
                            }
                            String abono = "update salida set  saldo=?, estado=?, interesactual=? where idsalida=?";
                            PreparedStatement ps2 = conn.prepareStatement(abono);
                            ps2.setFloat(1, nsaldo);
                            ps2.setString(2, "" + status);
                            ps2.setString(3, "" + interes);
                            ps2.setString(4, dato);

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
                            efectivo.setValue(null);
                            formapagoabono.setSelectedIndex(-1);
                            observacionabono.setText("");
                            abono.setVisible(false);
                            int fila2 = tablacomprasporpagar.getSelectedRow();
                            String dato2 = tablacomprasporpagar.getValueAt(fila2, 0).toString();
                            Llenar(idcliente);

                            //MostrarTodo(idcompras);
                            removejtable();
                            sumartotal();
                            JOptionPane.showInternalMessageDialog(this, "Datos guardados correctamente\n\n Vuelto = Q. " + ((float) (Math.round((vuelto - dato3) * 100.0) / 100.0)));
                            abrircomprobante(idabono);
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
            //}//****
        }
    }//GEN-LAST:event_butnguardarabonoActionPerformed

    private void btncancelarabonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelarabonoActionPerformed
        // TODO add your handling code here:
        dcFecha.setDate(Calendar.getInstance().getTime());
        montoabono.setValue(null);
        efectivo.setValue(null);
        observacionabono.setText("");
        formapagoabono.setSelectedIndex(-1);
        this.abono.dispose();
    }//GEN-LAST:event_btncancelarabonoActionPerformed

    private void btncancelarabono1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelarabono1ActionPerformed
        // TODO add your handling code here:
        dcFecha.setDate(Calendar.getInstance().getTime());
        montoabono.setValue(null);
        efectivo.setValue(null);
        observacionabono.setText("");
        formapagoabono.setSelectedIndex(-1);

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

    private void formapagoabonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_formapagoabonoActionPerformed
        // TODO add your handling code here:
        //System.out.print(formapagoabono.getSelectedItem());
    }//GEN-LAST:event_formapagoabonoActionPerformed

    private void efectivobutnguardarabonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_efectivobutnguardarabonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_efectivobutnguardarabonoActionPerformed

    private void efectivoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_efectivoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_efectivoKeyReleased

    private void tabladetallecompraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabladetallecompraKeyPressed
        // TODO add your handling code here:
        int key = evt.getKeyCode();

        if (key == java.awt.event.KeyEvent.VK_ENTER) {
            //Datos de interes detalle de venta (interes de un mes de mora)
            int fila = tabladetallecompra.getSelectedRow();
            int id = Integer.parseInt(tabladetallecompra.getValueAt(fila, 0).toString());
            float interes = Float.parseFloat(tabladetallecompra.getValueAt(fila, 4).toString());
            interes = (float) (Math.round(interes * 100.0) / 100.0);
            //Datos de interes de la venta (General suma de todos los meses con mora)
            int filas = tablacomprasporpagar.getSelectedRow();
            String ida = tablacomprasporpagar.getValueAt(filas, 1).toString();
            float interesc = Float.parseFloat(tablacomprasporpagar.getValueAt(filas, 6).toString());
            interesc = (float) (Math.round(interesc * 100.0) / 100.0);

            float saldoc = Float.parseFloat(tablacomprasporpagar.getValueAt(filas, 5).toString());
            saldoc = (float) (Math.round(saldoc * 100.0) / 100.0);

            String comp = tabladetallecompra.getValueAt(fila, 1).toString();
            String abono = "update detalleinteres set  montointeres=? where iddetint=?";

            try {
                if (comp == "S/N") {
                    if (interes > 0) {
                        String Cant = JOptionPane.showInputDialog(null, "Ingrese Monto para restar al interes");
                        float C = Float.parseFloat(Cant);
                        C = (float) (Math.round((C) * 100.0) / 100.0);

                        if (C <= interesc) {

                            if (C <= interes) {

                                interes = (float) (Math.round((interes - C) * 100.0) / 100.0);
                                conn = BdConexion.getConexion();
                                PreparedStatement ps2 = conn.prepareStatement(abono);
                                ps2.setFloat(1, interes);
                                ps2.setString(2, "" + id);
                                int n2 = ps2.executeUpdate();

                                interesc = (float) (Math.round((interesc - C) * 100.0) / 100.0);
                                char status = 'T';
                                if (saldoc == 0 & interesc == 0) {
                                    status = 'F';
                                }

                                if (status == 'T') {
                                    abono = "update salida set  interesactual=? where idsalida=?";
                                    PreparedStatement ps1 = conn.prepareStatement(abono);
                                    ps1.setFloat(1, interesc);
                                    ps1.setString(2, "" + ida);
                                    n2 = ps1.executeUpdate();
                                } else if (status == 'F') {
                                    abono = "update salida set  interesactual=?, estado=? where idsalida=?";
                                    PreparedStatement ps1 = conn.prepareStatement(abono);
                                    ps1.setFloat(1, interesc);
                                    ps1.setString(2, "" + status);
                                    ps1.setString(3, "" + ida);
                                    n2 = ps1.executeUpdate();
                                }

                                //conn.close();
                                Llenar(idcliente);
                                MostrarTodo(ida);
                                tablacomprasporpagar.setRowSelectionInterval(filas, filas);
                            } else {
                                JOptionPane.showMessageDialog(null, "ocurrio un error Interes esta en cero");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Ocurrio un error solo puedes restar al Interes = " + interesc
                                    + "\n El resto de interes ya fue abonado por el Cliente");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Cantidad Incorrecta\n debe ser mayor que 0 y menor o igual que interes");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No es un interes");
                }
            } catch (SQLException ex) {
                Logger.getLogger(pagoclientes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }//GEN-LAST:event_tabladetallecompraKeyPressed

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
    private javax.swing.JFormattedTextField efectivo;
    private javax.swing.JComboBox formapagoabono;
    private javax.swing.JTextField idcompra;
    private javax.swing.JLabel interesT;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPanedetallecompra;
    private javax.swing.JScrollPane jScrollPanedetallecompra2;
    private jcMousePanel.jcMousePanel jcMousePanel5;
    private jcMousePanel.jcMousePanel jcMousePanel6;
    private jcMousePanel.jcMousePanel jcMousePanel7;
    private jcMousePanel.jcMousePanel jcMousePanelprincipal;
    private javax.swing.JLabel lbltotalcompra;
    private javax.swing.JLabel lbltotalcompra1;
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
