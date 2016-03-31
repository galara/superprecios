/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * frmventa.java
 *
 * Created on 22-oct-2012, 19:01:56
 */
package GUI;

import BD.BdConexion;
import BD.sqlprod;
import static GUI.MenuPrincipal.panel_center;
import Modelos.AccesoUsuario;
import Modelos.AddForms;
import Modelos.MiModelo;
import Modelos.Utilidades;
import Modelos.codigoproductocombo;
import Modelos.formadepago;
import com.mysql.jdbc.Statement;
import excepciones.FormatoDecimal;
import java.applet.AudioClip;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import reporte.GeneraReportes;
import reporte.imprimiendo;

/**
 *
 * @author Otto
 */
public class frmventas extends javax.swing.JInternalFrame {

    private AudioClip sonido;

    private String archivoRecurso = "controlador-bd";
    public static String idfactura = "", fechasalida = "";
    public static String fecha = "";
    java.sql.Connection conn;//getConnection intentara establecer una conexión.
    int idabono = 0;
    public static float montoabonado = 0, saldoventa = 0, saldototalc = 0;

    /**
     * Creates new form frmventa
     */
    public frmventas() {
        initComponents();
        llenarcombo();

        //llenarcombocodigo();
//        codigoproductos.addItemListener(
//                (ItemEvent e) -> {
//                    if (e.getStateChange() == ItemEvent.SELECTED) {
//                        //selecciongrupo();
//                        seleccionnombreproducto();
//                    }
//                });
        addEscapeKey();
        for (int i = 0; i <= 7; i++) {
            if ((i == 0) || (i == 3) || (i == 06) || (i == 7)) {
                TableColumn desaparece = tablaventas.getColumnModel().getColumn(i);
                desaparece.setMaxWidth(0);
                desaparece.setMinWidth(0);
                desaparece.setPreferredWidth(0);
                tablaventas.doLayout();
            }
            if ((i == 2) || (i == 4) || (i == 5)) {
                TableColumn desaparece = tablaventas.getColumnModel().getColumn(i);
                desaparece.setMaxWidth(90);
                desaparece.setMinWidth(90);
                desaparece.setPreferredWidth(90);
                tablaventas.doLayout();
            }
        }
        tablaventas.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent arg0) {
                int key = arg0.getKeyCode();
                if (key == java.awt.event.KeyEvent.VK_ENTER) {
                    eliminaarticulo();
                }
            }
        });
        busquedaproducto.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    DefaultTableModel temp = (DefaultTableModel) busquedaproducto.getModel();
                    descricionP.setText("" + temp.getValueAt(busquedaproducto.getSelectedRow(), 5));
                }
            }
        });

        busquedacliente.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent arg0) {
                int key = arg0.getKeyCode();
                if (key == java.awt.event.KeyEvent.VK_ENTER) {
                    seleccionarcliente();

//                    DefaultTableModel temp = (DefaultTableModel) busquedacliente.getModel();
//                    //temp.removeRow(temp.removeRow(tablita.getSelectedRow()););
//                    //decmodifica.setText(temp.getValueAt(tablamodifica.getSelectedRow(), 4).toString())
//                    idcliente.setText(temp.getValueAt(busquedacliente.getSelectedRow(), 0).toString());
//                    nombrecliente.setText(temp.getValueAt(busquedacliente.getSelectedRow(), 1).toString());
//                    //System.out.println(temp.getValueAt(busquedacliente.getSelectedRow(), 1));
//                    if (temp.getValueAt(busquedacliente.getSelectedRow(), 3) == null) {
//                        nittxt.setText("C/F");
//                    } else {
//                        nittxt.setText(temp.getValueAt(busquedacliente.getSelectedRow(), 3).toString());
//                    }
//                    direccion.setText(temp.getValueAt(busquedacliente.getSelectedRow(), 2).toString());
//                    //tele.setText(temp.getValueAt(busquedacliente.getSelectedRow(), 4).toString());
//                    cliente.setVisible(false);
                }
            }
        });

        busquedaproducto.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent arg0) {
                int key = arg0.getKeyCode();
                if (key == java.awt.event.KeyEvent.VK_ENTER) {
                    int p = busquedaproducto.getSelectedRow();
                    float C = 0;
                    DefaultTableModel temps = (DefaultTableModel) busquedaproducto.getModel();

                    //JOptionPane.showMessageDialog(null, "<HTML><font color= blue size=+2></font> \n" + "<HTML><font color=blue size=+2> Su cambio: " + resta + "</font> ");    
                    String Cant = JOptionPane.showInputDialog(null, "<HTML><font color= blue size=+2></font> \n" + "<HTML><font color=blue size=+2> Ingresa Cantidad Venta Producto </font> ");
                    if (Cant.isEmpty()) {

                    } else {
                        C = Float.parseFloat(Cant);
                        if (C <= Float.parseFloat(temps.getValueAt(p, 3).toString())) {
                            idproducto.setText("" + temps.getValueAt(p, 0));
                            cantidadP.setText(C + "");
                            existencia.setText("" + temps.getValueAt(p, 3));
                            guardarventa();
                            busqueda.requestFocus();
                            busqueda.selectAll();
                        }
                    }
                }
            }
        });

    }

    private void seleccionarcliente() {
        DefaultTableModel temp = (DefaultTableModel) busquedacliente.getModel();
        //temp.removeRow(temp.removeRow(tablita.getSelectedRow()););
        //decmodifica.setText(temp.getValueAt(tablamodifica.getSelectedRow(), 4).toString())
        idcliente.setText(temp.getValueAt(busquedacliente.getSelectedRow(), 0).toString());
        nombrecliente.setText(temp.getValueAt(busquedacliente.getSelectedRow(), 1).toString());
        //System.out.println(temp.getValueAt(busquedacliente.getSelectedRow(), 1));
        if (temp.getValueAt(busquedacliente.getSelectedRow(), 3) == null) {
            nittxt.setText("C/F");
        } else {
            nittxt.setText(temp.getValueAt(busquedacliente.getSelectedRow(), 3).toString());
        }
        direccion.setText(temp.getValueAt(busquedacliente.getSelectedRow(), 2).toString());
        //tele.setText(temp.getValueAt(busquedacliente.getSelectedRow(), 4).toString());

        cliente.setVisible(false);
        busquedacompra.setText("");
        busquedacompra.requestFocus();
    }

    private void seleccionarclienteautomatico() {
        DefaultTableModel temp = (DefaultTableModel) busquedacliente.getModel();
        //temp.removeRow(temp.removeRow(tablita.getSelectedRow()););
        //decmodifica.setText(temp.getValueAt(tablamodifica.getSelectedRow(), 4).toString())
        idcliente.setText(temp.getValueAt(0, 0).toString());
        nombrecliente.setText(temp.getValueAt(0, 1).toString());
        //System.out.println(temp.getValueAt(busquedacliente.getSelectedRow(), 1));
        if (temp.getValueAt(0, 3) == null) {
            nittxt.setText("C/F");
        } else {
            nittxt.setText(temp.getValueAt(0, 3).toString());
        }
        direccion.setText(temp.getValueAt(0, 2).toString());
        //tele.setText(temp.getValueAt(busquedacliente.getSelectedRow(), 4).toString());
        cliente.setVisible(false);
    }

    private void addEscapeKey() {
        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        Action escapeAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (tablaventas.getRowCount() <= 0) {
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "No Puedes cerrar Tienes Ventas Pendientes");
                }
            }
        };
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);
    }

    public void llenarcombo() {
        busquedacompra.setText("");
        busquedacompra.requestFocus();

        DefaultComboBoxModel value2;
        value2 = new DefaultComboBoxModel();
        formapago.setModel(value2);
        try {
            conn = BdConexion.getConexion();
            //conn = BdConexion.getConexion();
            Statement s = (Statement) conn.createStatement();
            ResultSet rs = s.executeQuery(sqlprod.COMBOTP);

            while (rs.next()) {
                value2.addElement(new formadepago(rs.getString("descripcion"), rs.getInt("dias"), "" + rs.getInt("idtipopago")));
            }
            ////conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurio un Error al cargar los datos\n" + ex.toString());
        }
    }

//    public void llenarcombocodigo() {
//        DefaultComboBoxModel value;
//        value = new DefaultComboBoxModel();
//        codigoproductos.setModel(value);
//        try {
//            conn = BdConexion.getConexion();
//            Statement s = (Statement) conn.createStatement();
//            ResultSet rs = s.executeQuery("select idproducto,codigo from producto");
//            value.addElement(new codigoproductocombo(" ", "0"));
//
//            while (rs.next()) {
//                value.addElement(new codigoproductocombo(rs.getString("codigo"), "" + rs.getInt("idproducto")));
//            }
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Ocurio un Error al cargar los datos\n" + ex.toString());
//        }
//    }
    public void seleccionnombreproducto() {
        if (/*codigoproductos.getSelectedIndex() > 0*/!busquedacompra.getText().isEmpty()) {
            try {
                conn = BdConexion.getConexion();
                Statement s = (Statement) conn.createStatement();
                ResultSet rs = s.executeQuery("SELECT producto.nombre,unidad.Nombre FROM unidad INNER JOIN producto ON unidad.idunidad = producto.idunidad where codigo='" + /*codigoproductos.getSelectedItem()*/ busquedacompra.getText() + "'");

                while (rs.next()) {
                    nombreproducto.setText(rs.getString("producto.nombre"));
                    unidadproducto.setText(rs.getString("unidad.nombre"));
                }
                rs.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocurio un Error al cargar los datos\n" + ex.toString());
            }

        } else if (/*codigoproductos.getSelectedIndex() == 0*/busquedacompra.getText().isEmpty()) {
            nombreproducto.setText("");
            unidadproducto.setText("");
            //nombreproducto.setText("");
            existencia.setText("");
            precios.setText("");
            //unidadproducto.setText("");
        }
    }

    public static float montoabonado() {

        return montoabonado;
    }

    public static float saldoventa() {

        return saldoventa;
    }

    public static float saldototalc() {

        return saldototalc;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regeneratd by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cliente = new javax.swing.JDialog();
        jcMousePanel1 = new jcMousePanel.jcMousePanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        busquedacliente = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jcMousePanel14 = new jcMousePanel.jcMousePanel();
        jLabel3 = new javax.swing.JLabel();
        buscacliente = new elaprendiz.gui.textField.TextFieldRectIcon();
        jcMousePanel17 = new jcMousePanel.jcMousePanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        nitn = new javax.swing.JTextField();
        nombren = new javax.swing.JTextField();
        telefonon = new javax.swing.JTextField();
        direccionn = new javax.swing.JTextField();
        Mensaje = new elaprendiz.gui.label.LabelRect();
        cancelar = new elaprendiz.gui.button.ButtonRect();
        cancelar1 = new elaprendiz.gui.button.ButtonRect();
        diascredito = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        cantdias = new javax.swing.JTextField();
        buscar = new javax.swing.JDialog();
        jcMousePanel12 = new jcMousePanel.jcMousePanel();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        busquedaproducto = new javax.swing.JTable();
        jLabel19 = new javax.swing.JLabel();
        busqueda = new elaprendiz.gui.textField.TextFieldRectIcon();
        bntSalir1 = new elaprendiz.gui.button.ButtonRect();
        descricionP = new javax.swing.JLabel();
        precio = new javax.swing.JDialog();
        jPanel3 = new javax.swing.JPanel();
        btnbusscar2 = new elaprendiz.gui.button.ButtonRect();
        btnbusscar3 = new elaprendiz.gui.button.ButtonRect();
        Actual = new elaprendiz.gui.label.LabelRect();
        Cprecio = new javax.swing.JTextField();
        ps = new elaprendiz.gui.label.LabelRect();
        Ccantidad = new elaprendiz.gui.label.LabelRect();
        costo = new elaprendiz.gui.label.LabelRect();
        iddcambio = new elaprendiz.gui.label.LabelRect();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        abono = new javax.swing.JDialog();
        jcMousePanel6 = new jcMousePanel.jcMousePanel();
        jLabel4 = new javax.swing.JLabel();
        idcompra = new javax.swing.JTextField();
        montoabono = new javax.swing.JFormattedTextField();
        dcFecha = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        pnlActionButtons1 = new javax.swing.JPanel();
        butnguardarabono = new elaprendiz.gui.button.ButtonRect();
        btncancelarabono = new elaprendiz.gui.button.ButtonRect();
        btncancelarabono1 = new elaprendiz.gui.button.ButtonRect();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        observacionabono = new javax.swing.JTextArea();
        formapagoabono = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        efectivo = new javax.swing.JFormattedTextField();
        jLabel29 = new javax.swing.JLabel();
        pago2 = new javax.swing.JDialog();
        jPanel2 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        cantpago = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jcMousePanel4 = new jcMousePanel.jcMousePanel();
        jcMousePanel3 = new jcMousePanel.jcMousePanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        fechainicial = new org.freixas.jcalendar.JCalendarCombo();
        nombrecliente = new javax.swing.JLabel();
        nittxt = new javax.swing.JLabel();
        idproducto = new javax.swing.JTextField();
        idfac = new javax.swing.JTextField();
        idcliente = new javax.swing.JTextField();
        tele = new javax.swing.JTextField();
        direccion = new javax.swing.JTextField();
        precioscostos = new javax.swing.JTextField();
        jcMousePanel2 = new jcMousePanel.jcMousePanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        nombreproducto = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        existencia = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        bntSalir = new elaprendiz.gui.button.ButtonRect();
        jLabel21 = new javax.swing.JLabel();
        comboprecio = new javax.swing.JComboBox();
        unidadproducto = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        btncrearproducto1 = new elaprendiz.gui.button.ButtonRect();
        jLabel11 = new javax.swing.JLabel();
        formapago = new javax.swing.JComboBox();
        busquedacompra = new javax.swing.JTextField();
        precios = new javax.swing.JTextField();
        cantidadP = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaventas = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        TotalPagar = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtarticulos = new javax.swing.JLabel();
        iniciar = new elaprendiz.gui.button.ButtonAction();
        jcMousePanel9 = new jcMousePanel.jcMousePanel();
        jLabel22 = new javax.swing.JLabel();
        Nodefac = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        clockDigital1 = new elaprendiz.gui.varios.ClockDigital();
        jButton4 = new javax.swing.JButton();
        jcMousePanel11 = new jcMousePanel.jcMousePanel();
        elart = new elaprendiz.gui.button.ButtonRect();
        btnmodificar = new elaprendiz.gui.button.ButtonRect();
        btnbusscar = new elaprendiz.gui.button.ButtonRect();
        btnbusscar1 = new elaprendiz.gui.button.ButtonRect();

        cliente.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        cliente.setTitle("Cliente");
        cliente.setAlwaysOnTop(true);
        cliente.setBounds(new java.awt.Rectangle(0, 0, 724, 532));
        cliente.setMinimumSize(null);
        cliente.setModal(true);
        cliente.setResizable(false);
        buscacliente.requestFocus();
        cliente.getContentPane().setLayout(new javax.swing.BoxLayout(cliente.getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jcMousePanel1.setColor1(new java.awt.Color(204, 204, 204));
        jcMousePanel1.setColor2(new java.awt.Color(153, 153, 153));
        jcMousePanel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fondo/índice.jpg"))); // NOI18N
        jcMousePanel1.setName(""); // NOI18N
        jcMousePanel1.setOpaque(false);
        jcMousePanel1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jcMousePanel1KeyPressed(evt);
            }
        });

        busquedacliente.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        busquedacliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Nombre", "Dirección", "Nit"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        busquedacliente.setRowHeight(19);
        jScrollPane2.setViewportView(busquedacliente);

        jPanel9.setBackground(new java.awt.Color(61, 139, 189));

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Presiona ENTER para seleccionar cliente");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(117, 117, 117)
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(38, 38, 38))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel10))
        );

        jcMousePanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Busqueda de Cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12), java.awt.Color.white)); // NOI18N
        jcMousePanel14.setModo(2);
        jcMousePanel14.setOpaque(false);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Busqueda:");

        buscacliente.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        buscacliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/1390607350_Search.png"))); // NOI18N
        buscacliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscaclienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jcMousePanel14Layout = new javax.swing.GroupLayout(jcMousePanel14);
        jcMousePanel14.setLayout(jcMousePanel14Layout);
        jcMousePanel14Layout.setHorizontalGroup(
            jcMousePanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jcMousePanel14Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buscacliente, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(152, Short.MAX_VALUE))
        );
        jcMousePanel14Layout.setVerticalGroup(
            jcMousePanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jcMousePanel14Layout.createSequentialGroup()
                .addGroup(jcMousePanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buscacliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jcMousePanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cliente Nuevo", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jcMousePanel17.setForeground(new java.awt.Color(102, 102, 255));
        jcMousePanel17.setColor1(new java.awt.Color(192, 219, 213));
        jcMousePanel17.setColor2(new java.awt.Color(192, 219, 213));
        jcMousePanel17.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jcMousePanel17.setModo(2);
        jcMousePanel17.setName(""); // NOI18N
        jcMousePanel17.setOpaque(false);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setText("Nit");

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(51, 51, 51));
        jLabel26.setText("Nombres*");

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(51, 51, 51));
        jLabel27.setText("Dirección*");

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(51, 51, 51));
        jLabel28.setText("Teléfono");

        nitn.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        nombren.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        telefonon.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        direccionn.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        Mensaje.setBackground(new java.awt.Color(102, 102, 102));
        Mensaje.setText("Ingresa Cliente");

        cancelar.setBackground(new java.awt.Color(51, 153, 255));
        cancelar.setText("Cancelar");
        cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarActionPerformed(evt);
            }
        });

        cancelar1.setBackground(new java.awt.Color(51, 153, 255));
        cancelar1.setText("Guardar");
        cancelar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jcMousePanel17Layout = new javax.swing.GroupLayout(jcMousePanel17);
        jcMousePanel17.setLayout(jcMousePanel17Layout);
        jcMousePanel17Layout.setHorizontalGroup(
            jcMousePanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jcMousePanel17Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jcMousePanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel26)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jcMousePanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(nombren)
                    .addGroup(jcMousePanel17Layout.createSequentialGroup()
                        .addComponent(nitn, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(telefonon, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jcMousePanel17Layout.createSequentialGroup()
                        .addComponent(cancelar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Mensaje, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(direccionn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jcMousePanel17Layout.setVerticalGroup(
            jcMousePanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jcMousePanel17Layout.createSequentialGroup()
                .addGroup(jcMousePanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jcMousePanel17Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(jcMousePanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(nombren, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jcMousePanel17Layout.createSequentialGroup()
                        .addGroup(jcMousePanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addComponent(nitn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jcMousePanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jcMousePanel17Layout.createSequentialGroup()
                                    .addGap(3, 3, 3)
                                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(telefonon, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(34, 34, 34)))
                .addGap(9, 9, 9)
                .addGroup(jcMousePanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(direccionn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Mensaje, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jcMousePanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jcMousePanel1Layout = new javax.swing.GroupLayout(jcMousePanel1);
        jcMousePanel1.setLayout(jcMousePanel1Layout);
        jcMousePanel1Layout.setHorizontalGroup(
            jcMousePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jcMousePanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jcMousePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcMousePanel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jcMousePanel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jcMousePanel1Layout.setVerticalGroup(
            jcMousePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jcMousePanel1Layout.createSequentialGroup()
                .addComponent(jcMousePanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcMousePanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        cliente.getContentPane().add(jcMousePanel1);

        diascredito.setTitle("pago");
        diascredito.setBounds(new java.awt.Rectangle(200, 200, 260, 107));
        diascredito.setResizable(false);

        jLabel14.setText("Ingrese Días de crédito y presione ENTER");

        cantdias.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        cantdias.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cantdias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cantdiasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cantdias)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(0, 33, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cantdias, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout diascreditoLayout = new javax.swing.GroupLayout(diascredito.getContentPane());
        diascredito.getContentPane().setLayout(diascreditoLayout);
        diascreditoLayout.setHorizontalGroup(
            diascreditoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        diascreditoLayout.setVerticalGroup(
            diascreditoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        buscar.setBounds(new java.awt.Rectangle(100, 100, 806, 413));
        buscar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        buscar.setMinimumSize(null);
        buscar.getContentPane().setLayout(new javax.swing.BoxLayout(buscar.getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jcMousePanel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fondo/índice.jpg"))); // NOI18N
        jcMousePanel12.setMaximumSize(null);
        jcMousePanel12.setPreferredSize(new java.awt.Dimension(724, 410));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Enter Para Agregar Producto"));

        jScrollPane6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        busquedaproducto.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        busquedaproducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "codigo", "producto", "cantidad", "Precio V", "Descripcion", "Unidad"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        busquedaproducto.setRowHeight(20);
        jScrollPane6.setViewportView(busquedaproducto);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Busqueda");

        busqueda.setToolTipText("");
        busqueda.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        busqueda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/1390607350_Search.png"))); // NOI18N
        busqueda.setPreferredSize(new java.awt.Dimension(150, 24));
        busqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                busquedaActionPerformed(evt);
            }
        });

        bntSalir1.setBackground(new java.awt.Color(102, 204, 0));
        bntSalir1.setText("Salir");
        bntSalir1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntSalir1ActionPerformed(evt);
            }
        });

        descricionP.setForeground(new java.awt.Color(255, 255, 255));
        descricionP.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        descricionP.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        javax.swing.GroupLayout jcMousePanel12Layout = new javax.swing.GroupLayout(jcMousePanel12);
        jcMousePanel12.setLayout(jcMousePanel12Layout);
        jcMousePanel12Layout.setHorizontalGroup(
            jcMousePanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jcMousePanel12Layout.createSequentialGroup()
                .addGroup(jcMousePanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jcMousePanel12Layout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(busqueda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jcMousePanel12Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(descricionP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jcMousePanel12Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bntSalir1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jcMousePanel12Layout.setVerticalGroup(
            jcMousePanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jcMousePanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jcMousePanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(busqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(descricionP, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bntSalir1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53))
        );

        buscar.getContentPane().add(jcMousePanel12);

        precio.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        precio.setBounds(new java.awt.Rectangle(0, 0, 310, 149));

        btnbusscar2.setBackground(new java.awt.Color(51, 153, 255));
        btnbusscar2.setText("Cambiar");
        btnbusscar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbusscar2ActionPerformed(evt);
            }
        });

        btnbusscar3.setBackground(new java.awt.Color(51, 153, 255));
        btnbusscar3.setText("Cancelar");
        btnbusscar3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbusscar3ActionPerformed(evt);
            }
        });

        Actual.setForeground(new java.awt.Color(0, 0, 0));
        Actual.setText(".");
        Actual.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N

        Cprecio.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        Cprecio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Cprecio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CprecioActionPerformed(evt);
            }
        });

        ps.setText(".");
        ps.setVisible(false);

        Ccantidad.setText(".");
        Ccantidad.setVisible(false);

        costo.setText(".");
        costo.setVisible(false);

        iddcambio.setText(".");
        iddcambio.setVisible(false);

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel34.setText("Precio Actual       Q");

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel35.setText("Nuevo Precio       Q");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(iddcambio, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(costo, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(Ccantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(ps, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(btnbusscar2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnbusscar3, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Cprecio, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                            .addComponent(Actual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Actual, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Cprecio, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ps, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Ccantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(costo, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(iddcambio, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnbusscar2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnbusscar3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout precioLayout = new javax.swing.GroupLayout(precio.getContentPane());
        precio.getContentPane().setLayout(precioLayout);
        precioLayout.setHorizontalGroup(
            precioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        precioLayout.setVerticalGroup(
            precioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        abono.setBounds(new java.awt.Rectangle(220, 200, 270, 200));
        abono.setFocusTraversalPolicyProvider(true);
        abono.setMinimumSize(null);
        abono.setUndecorated(true);

        jcMousePanel6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 3, true));
        jcMousePanel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fondo/índice.jpg"))); // NOI18N
        jcMousePanel6.setMaximumSize(null);
        jcMousePanel6.setLayout(null);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Fecha:");
        jcMousePanel6.add(jLabel4);
        jLabel4.setBounds(40, 20, 80, 20);

        idcompra.setVisible(false);
        idcompra.setOpaque(false);
        jcMousePanel6.add(idcompra);
        idcompra.setBounds(30, 40, 10, 20);

        montoabono.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
        montoabono.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        montoabono.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        montoabono.setName("precioalmayor"); // NOI18N
        montoabono.setPreferredSize(new java.awt.Dimension(80, 23));
        montoabono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                montoabonoKeyReleased(evt);
            }
        });
        jcMousePanel6.add(montoabono);
        montoabono.setBounds(130, 50, 140, 23);

        dcFecha.setDate(Calendar.getInstance().getTime());
        dcFecha.setDateFormatString("dd/MM/yyyy");
        dcFecha.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        dcFecha.setMaxSelectableDate(new java.util.Date(3093496470100000L));
        dcFecha.setMinSelectableDate(new java.util.Date(-62135744300000L));
        dcFecha.setPreferredSize(new java.awt.Dimension(120, 22));
        jcMousePanel6.add(dcFecha);
        dcFecha.setBounds(130, 20, 140, 21);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Monto:");
        jcMousePanel6.add(jLabel5);
        jLabel5.setBounds(40, 50, 80, 20);

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

        jcMousePanel6.add(pnlActionButtons1);
        pnlActionButtons1.setBounds(0, 150, 500, 40);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Observación:");
        jcMousePanel6.add(jLabel6);
        jLabel6.setBounds(10, 90, 110, 20);

        observacionabono.setColumns(20);
        observacionabono.setRows(5);
        observacionabono.setAutoscrolls(false);
        observacionabono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                observacionabonoKeyReleased(evt);
            }
        });
        jScrollPane4.setViewportView(observacionabono);

        jcMousePanel6.add(jScrollPane4);
        jScrollPane4.setBounds(130, 90, 350, 40);

        formapagoabono.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        formapagoabono.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "Contado", "Cheque", "Deposito", "Otro" }));
        formapagoabono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                formapagoabonoActionPerformed(evt);
            }
        });
        jcMousePanel6.add(formapagoabono);
        formapagoabono.setBounds(390, 50, 90, 23);

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Efectivo");
        jcMousePanel6.add(jLabel8);
        jLabel8.setBounds(290, 20, 100, 20);

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
        jcMousePanel6.add(efectivo);
        efectivo.setBounds(390, 20, 90, 23);

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("Tipo Pago");
        jcMousePanel6.add(jLabel29);
        jLabel29.setBounds(290, 50, 100, 20);

        javax.swing.GroupLayout abonoLayout = new javax.swing.GroupLayout(abono.getContentPane());
        abono.getContentPane().setLayout(abonoLayout);
        abonoLayout.setHorizontalGroup(
            abonoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jcMousePanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
        );
        abonoLayout.setVerticalGroup(
            abonoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jcMousePanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
        );

        pago2.setTitle("PAGO");
        pago2.setBounds(new java.awt.Rectangle(100, 100, 265, 115));
        pago2.setResizable(false);

        jPanel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel32.setText("Q");

        cantpago.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        cantpago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cantpagoActionPerformed(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel33.setText("Ingrese el valor de pago y presione ENTER");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cantpago, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cantpago, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel33)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pago2Layout = new javax.swing.GroupLayout(pago2.getContentPane());
        pago2.getContentPane().setLayout(pago2Layout);
        pago2Layout.setHorizontalGroup(
            pago2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        pago2Layout.setVerticalGroup(
            pago2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("\"SÚPER PRECIOS\"");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMaximumSize(null);
        setName("frmventas"); // NOI18N
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jcMousePanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jcMousePanel4.setColor1(new java.awt.Color(153, 153, 153));
        jcMousePanel4.setColor2(new java.awt.Color(204, 204, 204));
        jcMousePanel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fondo/índice.jpg"))); // NOI18N
        jcMousePanel4.setOpaque(false);

        jcMousePanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true), "Datos de Cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jcMousePanel3.setColor1(new java.awt.Color(153, 153, 153));
        jcMousePanel3.setModo(5);
        jcMousePanel3.setName(""); // NOI18N
        jcMousePanel3.setOpaque(false);

        jLabel23.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel23.setText("Fecha:");

        jLabel24.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel24.setText("Cliente:");

        jLabel25.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel25.setText("Nit:");

        fechainicial.setFocusable(false);
        fechainicial.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        fechainicial.setRequestFocusEnabled(false);

        nombrecliente.setBackground(new java.awt.Color(255, 255, 255));
        nombrecliente.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        nombrecliente.setForeground(new java.awt.Color(0, 51, 153));
        nombrecliente.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 153)));
        nombrecliente.setOpaque(true);

        nittxt.setBackground(new java.awt.Color(255, 255, 255));
        nittxt.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        nittxt.setForeground(new java.awt.Color(0, 51, 153));
        nittxt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 153)));
        nittxt.setOpaque(true);

        idproducto.setVisible(false);
        idproducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idproductoActionPerformed(evt);
            }
        });

        idfac.setVisible(false);
        idfac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idfacActionPerformed(evt);
            }
        });

        idcliente.setVisible(false);

        tele.setVisible(false);

        direccion.setVisible(false);

        precioscostos.setText("costo");
        precioscostos.setVisible(false);

        javax.swing.GroupLayout jcMousePanel3Layout = new javax.swing.GroupLayout(jcMousePanel3);
        jcMousePanel3.setLayout(jcMousePanel3Layout);
        jcMousePanel3Layout.setHorizontalGroup(
            jcMousePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jcMousePanel3Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jcMousePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jcMousePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nombrecliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jcMousePanel3Layout.createSequentialGroup()
                        .addGroup(jcMousePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fechainicial, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jcMousePanel3Layout.createSequentialGroup()
                                .addComponent(nittxt, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(42, 42, 42)
                                .addGroup(jcMousePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jcMousePanel3Layout.createSequentialGroup()
                                        .addComponent(direccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tele, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(idfac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jcMousePanel3Layout.createSequentialGroup()
                                        .addComponent(precioscostos, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(idproducto, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(idcliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 18, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jcMousePanel3Layout.setVerticalGroup(
            jcMousePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jcMousePanel3Layout.createSequentialGroup()
                .addGroup(jcMousePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jcMousePanel3Layout.createSequentialGroup()
                        .addGroup(jcMousePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(precioscostos, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(idproducto, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(idcliente, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jcMousePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(direccion, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tele, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(idfac, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jcMousePanel3Layout.createSequentialGroup()
                        .addGroup(jcMousePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fechainicial, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jcMousePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nombrecliente, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jcMousePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nittxt, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE))))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jcMousePanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true), "Datos de Venta", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14))); // NOI18N
        jcMousePanel2.setColor1(new java.awt.Color(102, 102, 102));
        jcMousePanel2.setColor2(new java.awt.Color(204, 204, 204));
        jcMousePanel2.setModo(5);
        jcMousePanel2.setOpaque(false);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Cantidad");
        jLabel15.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Descripcion");
        jLabel16.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        nombreproducto.setEditable(false);
        nombreproducto.setEditable(false);
        nombreproducto.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        nombreproducto.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        nombreproducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nombreproductoActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Existencia");
        jLabel17.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        existencia.setEditable(false);
        existencia.setEditable(false);
        existencia.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        existencia.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        existencia.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        existencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                existenciaActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Precio");
        jLabel18.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        bntSalir.setBackground(new java.awt.Color(51, 153, 255));
        bntSalir.setText("...");
        bntSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntSalirActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel21.setText("Tipo de Precio");

        comboprecio.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        comboprecio.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Contado", "Credito", "Distribuidor", "Especial" }));
        comboprecio.setToolTipText("");

        nombreproducto.setEditable(false);
        unidadproducto.setEditable(false);
        unidadproducto.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        unidadproducto.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        unidadproducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unidadproductoActionPerformed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("Unidad de Medida");
        jLabel30.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("Código");
        jLabel31.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        btncrearproducto1.setBackground(new java.awt.Color(102, 204, 0));
        btncrearproducto1.setText("Crear Producto");
        btncrearproducto1.setFocusable(false);
        btncrearproducto1.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        btncrearproducto1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncrearproductoActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel11.setText("Forma Pago:");

        formapago.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        busquedacompra.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        busquedacompra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                codigoproductosKeyPressed(evt);
            }
        });

        existencia.setEditable(false);
        precios.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        precios.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        precios.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        precios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                preciosActionPerformed(evt);
            }
        });

        existencia.setEditable(false);
        cantidadP.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        cantidadP.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cantidadP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cantidadP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cantidadPActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jcMousePanel2Layout = new javax.swing.GroupLayout(jcMousePanel2);
        jcMousePanel2.setLayout(jcMousePanel2Layout);
        jcMousePanel2Layout.setHorizontalGroup(
            jcMousePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jcMousePanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jcMousePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jcMousePanel2Layout.createSequentialGroup()
                        .addComponent(busquedacompra, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bntSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jcMousePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jcMousePanel2Layout.createSequentialGroup()
                        .addComponent(comboprecio, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(formapago, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59)
                        .addComponent(btncrearproducto1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jcMousePanel2Layout.createSequentialGroup()
                        .addGroup(jcMousePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nombreproducto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jcMousePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addComponent(unidadproducto, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jcMousePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cantidadP, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jcMousePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(existencia, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jcMousePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(precios, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jcMousePanel2Layout.setVerticalGroup(
            jcMousePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jcMousePanel2Layout.createSequentialGroup()
                .addGroup(jcMousePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jcMousePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(formapago, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboprecio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btncrearproducto1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jcMousePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jcMousePanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jcMousePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jcMousePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jcMousePanel2Layout.createSequentialGroup()
                                    .addGap(41, 41, 41)
                                    .addComponent(nombreproducto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jcMousePanel2Layout.createSequentialGroup()
                                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jcMousePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(busquedacompra)
                                    .addComponent(bntSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jcMousePanel2Layout.createSequentialGroup()
                                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39))
                            .addGroup(jcMousePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jcMousePanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jcMousePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(existencia, javax.swing.GroupLayout.DEFAULT_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(precios, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cantidadP, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jcMousePanel2Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(unidadproducto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tablaventas.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        tablaventas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "idproducto", "PRODUCTO", "CANTIDAD", "precio C", "PRECIO", "SUB-TOTAL", "idlote", "idd"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaventas.setGridColor(new java.awt.Color(51, 51, 255));
        tablaventas.setRowHeight(20);
        jScrollPane3.setViewportView(tablaventas);
        if (tablaventas.getColumnModel().getColumnCount() > 0) {
            tablaventas.getColumnModel().getColumn(0).setResizable(false);
            tablaventas.getColumnModel().getColumn(1).setResizable(false);
            tablaventas.getColumnModel().getColumn(2).setResizable(false);
            tablaventas.getColumnModel().getColumn(3).setResizable(false);
            tablaventas.getColumnModel().getColumn(4).setResizable(false);
            tablaventas.getColumnModel().getColumn(5).setResizable(false);
            tablaventas.getColumnModel().getColumn(6).setResizable(false);
            tablaventas.getColumnModel().getColumn(7).setResizable(false);
        }

        jPanel8.setBackground(new java.awt.Color(61, 139, 189));
        jPanel8.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        jPanel8.setForeground(new java.awt.Color(204, 204, 204));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Presiona ENTER encima del producto para elminar de ventas");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 2, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Total");

        TotalPagar.setBackground(new java.awt.Color(204, 204, 204));
        TotalPagar.setFont(TotalPagar.getFont().deriveFont((TotalPagar.getFont().getStyle() | java.awt.Font.ITALIC) | java.awt.Font.BOLD, 28));
        TotalPagar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TotalPagar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 204)));

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Articulos:");

        txtarticulos.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtarticulos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtarticulos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 255)));

        iniciar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/accept1.png"))); // NOI18N
        iniciar.setText("Cobrar");
        iniciar.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        iniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtarticulos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(TotalPagar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(iniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtarticulos, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TotalPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addComponent(iniciar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jcMousePanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true), "-", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jcMousePanel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fondo/índice.jpg"))); // NOI18N
        jcMousePanel9.setModo(5);
        jcMousePanel9.setOpaque(false);

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 26)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Registro de Ventas");

        Nodefac.setFont(Nodefac.getFont().deriveFont((Nodefac.getFont().getStyle() | java.awt.Font.ITALIC) | java.awt.Font.BOLD, 32));
        Nodefac.setForeground(new java.awt.Color(255, 255, 255));
        Nodefac.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Nodefac.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("No. Venta:");

        clockDigital1.setForeground(new java.awt.Color(0, 0, 255));
        clockDigital1.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 18)); // NOI18N

        jButton4.setBackground(new java.awt.Color(51, 153, 255));
        jButton4.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/cancel.png"))); // NOI18N
        jButton4.setText("Salir");
        jButton4.setToolTipText("ESC");
        jButton4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jcMousePanel9Layout = new javax.swing.GroupLayout(jcMousePanel9);
        jcMousePanel9.setLayout(jcMousePanel9Layout);
        jcMousePanel9Layout.setHorizontalGroup(
            jcMousePanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jcMousePanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jcMousePanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jcMousePanel9Layout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jcMousePanel9Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(clockDigital1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(Nodefac, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );
        jcMousePanel9Layout.setVerticalGroup(
            jcMousePanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jcMousePanel9Layout.createSequentialGroup()
                .addGroup(jcMousePanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jcMousePanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Nodefac, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clockDigital1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jcMousePanel11.setBackground(new java.awt.Color(255, 255, 255));
        jcMousePanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jcMousePanel11.setModo(5);

        elart.setBackground(new java.awt.Color(51, 153, 255));
        elart.setMnemonic(KeyEvent.VK_E);
        elart.setText("Eliminar Articulo");
        elart.setToolTipText("ALT+E");
        elart.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        elart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elartActionPerformed(evt);
            }
        });
        jcMousePanel11.add(elart);

        btnmodificar.setBackground(new java.awt.Color(51, 153, 255));
        btnmodificar.setMnemonic(KeyEvent.VK_M);
        btnmodificar.setText("Modificar Precio");
        btnmodificar.setToolTipText("ALT+M");
        btnmodificar.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        btnmodificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmodificarActionPerformed(evt);
            }
        });
        jcMousePanel11.add(btnmodificar);

        btnbusscar.setBackground(new java.awt.Color(51, 153, 255));
        btnbusscar.setMnemonic(KeyEvent.VK_B);
        btnbusscar.setText("Buscar");
        btnbusscar.setToolTipText("ALT+B");
        btnbusscar.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        btnbusscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbusscarActionPerformed(evt);
            }
        });
        jcMousePanel11.add(btnbusscar);

        btnbusscar1.setBackground(new java.awt.Color(51, 153, 255));
        btnbusscar1.setMnemonic(KeyEvent.VK_X);
        btnbusscar1.setText("Cancelar");
        btnbusscar1.setToolTipText("ALT+X");
        btnbusscar1.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        btnbusscar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbusscar1ActionPerformed(evt);
            }
        });
        jcMousePanel11.add(btnbusscar1);

        javax.swing.GroupLayout jcMousePanel4Layout = new javax.swing.GroupLayout(jcMousePanel4);
        jcMousePanel4.setLayout(jcMousePanel4Layout);
        jcMousePanel4Layout.setHorizontalGroup(
            jcMousePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jcMousePanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jcMousePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jcMousePanel4Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jcMousePanel4Layout.createSequentialGroup()
                        .addComponent(jcMousePanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jcMousePanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11))
                    .addGroup(jcMousePanel4Layout.createSequentialGroup()
                        .addGroup(jcMousePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jcMousePanel4Layout.createSequentialGroup()
                                .addComponent(jcMousePanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jcMousePanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jcMousePanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jcMousePanel4Layout.setVerticalGroup(
            jcMousePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jcMousePanel4Layout.createSequentialGroup()
                .addGroup(jcMousePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jcMousePanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcMousePanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcMousePanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jcMousePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcMousePanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(jcMousePanel4);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void cuentaprecio() {
        DefaultTableModel tempe = (DefaultTableModel) tablaventas.getModel();
        float Actual = 0, Resultado = 0;
        float res = 0, act = 0;
        for (int i = 0; i < tempe.getRowCount(); i++) {
            Actual = Float.parseFloat(tempe.getValueAt(i, 5).toString());
            act = Float.parseFloat(tempe.getValueAt(i, 2).toString());
            Resultado = Resultado + Actual;
            res = res + act;
            //temp.getValueAt(i, 6);
        }
        Resultado = (float) (Math.round(Resultado * 100.0) / 100.0);
        TotalPagar.setText("" + Resultado);
        txtarticulos.setText("" + res);
        busquedacompra.setText("");
        //codigoproductos.setSelectedIndex(0);

    }

    public void buscarproductocodigo() {

        if (busquedacompra.getText().equals("")/*codigoproductos.getSelectedIndex() == 0*/) {

            JOptionPane.showMessageDialog(this,
                    "Sin datos a buscar en ventas", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {

            try {
                String idP = null, prod = null;
                int idPrv = 0, idfact = 0, Nofac = 0;
                float cantP = 0, loteC = 0, C = 0, restalote = 0, restapro = 0, preV = 0, preC = 0;

                float suma = 0, est = 0;
                String precioventa = "";
                if (comboprecio.getSelectedIndex() == 0) {
                    precioventa = "producto.precioventa";
                }
                if (comboprecio.getSelectedIndex() == 1) {
                    precioventa = "producto.preciocredito";
                }
                if (comboprecio.getSelectedIndex() == 2) {
                    precioventa = "producto.preciodistribuidor";
                }
                if (comboprecio.getSelectedIndex() == 3) {
                    precioventa = "producto.precioespecial";
                }

                String sqlfac = "", cancel = "C", sql3 = "", estado = "T", sql = "select producto.preciocompra,producto.idProducto,producto.Cantidad,producto.nombre," + precioventa + " from lote INNER JOIN producto ON  lote.producto_idProducto=producto.idproducto where Codigo='" + /*codigoproductos.getSelectedItem()*/ busquedacompra.getText() + "' and lote.estado='" + estado + "' and producto.estado='T' group by idproducto";

                //System.out.println(sql);
                String fecha = "";
                java.util.Date fechas = new Date();
                int mes = fechas.getMonth() + 1;
                GregorianCalendar calendarios
                        = new GregorianCalendar();
                Calendar c1 = Calendar.getInstance();

                fecha = "" + c1.get(calendarios.YEAR) + "-" + mes + "-" + fechas.getDate();// + " " + fechas.getHours() + ":" + fechas.getMinutes() + ":" + fechas.getSeconds();
                conn = BdConexion.getConexion();
                Statement s = (Statement) conn.createStatement();
                //s.executeUpdate("LOCK TABLES lote WRITE;");
                s.executeUpdate("LOCK TABLES producto WRITE, lote WRITE, unidad WRITE;");
                ResultSet rs = s.executeQuery(sql);

                //s.executeUpdate("LOCK TABLES lote WRITE;");
                if (rs.next() == true) {

                    prod = rs.getString("producto.nombre");
                    nombreproducto.setText(prod);
                    precioscostos.setText(rs.getString("producto.preciocompra"));
                    cantP = rs.getFloat("producto.cantidad");
                    existencia.setText(cantP + "");
                    idP = rs.getString("producto.idProducto");
                    idproducto.setText(idP);

                    preV = rs.getFloat(precioventa);
                    //unidadproducto.setText(rs.getString("unidad.nombre"));
                    seleccionnombreproducto();
                    precios.setText("" + preV);

                    if (cantP <= 0) {
                        idproducto.setText("");
                        nombreproducto.setText("");
                        existencia.setText("");
                        precios.setText("");
                        unidadproducto.setText("");

                        JOptionPane.showMessageDialog(null, "No Hay Productos");
                    }

                    cantidadP.requestFocus();
                    cantidadP.selectAll();
                }//fin del doooo
                else {
                    idproducto.setText("");
                    nombreproducto.setText("");
                    unidadproducto.setText("");
                    existencia.setText("");
                    precios.setText("");

                    JOptionPane.showMessageDialog(null, "No Hay Productos");

                }
                s.executeUpdate("UNLOCK TABLES;");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }

        }
    }

    public static String idfac() {
        return idfactura;
    }

    public static String fechafact() {
        return fechasalida;
    }
    private void cantpagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cantpagoActionPerformed
        // TODO add your handling code here:
        String fecha = "";
        montoabonado = 0;//mod
        java.util.Date fechas = new Date();
        int mes = fechas.getMonth() + 1;
        GregorianCalendar calendarios
                = new GregorianCalendar();
        Calendar c1 = Calendar.getInstance();

        fecha = "" + c1.get(calendarios.YEAR) + "-" + mes + "-" + fechas.getDate();// + " " + fechas.getHours() + ":" + fechas.getMinutes() + ":" + fechas.getSeconds();

        float resta = 0;
        String sqlfac = "";

        try {
            if (Float.parseFloat(cantpago.getText().toString()) >= Float.parseFloat(TotalPagar.getText().toString())) {
                conn = BdConexion.getConexion();
                // Se crea un Statement, para realizar la consulta
                Statement s = (Statement) conn.createStatement();

                formadepago formadepago = (formadepago) formapago.getSelectedItem();
                String idTP = formadepago.getID();
                char sta = 'T';
                String saldov = "", total = "";

                sta = 'F';
                saldov = "0";
                total = (TotalPagar.getText());
                //System.out.print(total + "  total a pagar \n");
                sqlfac = "UPDATE salida SET estado='" + sta + "',total='" + total + "',saldo='" + saldov + "' WHERE idsalida =" + idfac.getText();
                s.executeUpdate(sqlfac);

                resta = Float.parseFloat(cantpago.getText()) - Float.parseFloat(TotalPagar.getText());
                resta = (float) (Math.round(resta * 100.0) / 100.0);

                Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
                Dimension ventana = pago2.getSize();
                pago2.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 2);
                pago2.setVisible(false);

                JOptionPane.showMessageDialog(null, "<HTML><font color= blue size=+2></font> \n" + "<HTML><font color=blue size=+2> Su cambio: " + resta + "</font> ");

                int resp;
                resp = JOptionPane.showOptionDialog(this, "¿Desea Imprimir el comprobante?", "Pregunta", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{"No", "Si"}, "No");
                if (resp == 1) {
                    String nombrereporte = "reimpresionContado.jasper";
                    Map parametro = new HashMap();
                    parametro.put("idsalida", Integer.parseInt(idfac()));
                    GeneraReportes.AbrirReporte(nombrereporte, parametro);
                    limipiarventas();
                }
                if (resp == 0) {
                    limipiarventas();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Cantidad Menor a consumido");
            }

        } catch (NumberFormatException | NullPointerException | SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            //Logger.getLogger(frmventas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cantpagoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String sqlfac = "";
        if (formapago.getSelectedIndex() >= 0) {
            if (formapago.getSelectedItem().toString().equals("CONTADO")) {
                if (idfac.getText().equals("")) {
                    JOptionPane.showMessageDialog(this,
                            "No hay productos", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
                    Dimension ventana = pago2.getSize();
                    pago2.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 2);
                    pago2.setVisible(true);

                }

            }
            if (!formapago.getSelectedItem().toString().equals("CONTADO")) {
                if (idfac.getText().equals("")) {
                    JOptionPane.showMessageDialog(this,
                            "No hay productos", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
                    Dimension ventana = diascredito.getSize();
                    diascredito.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 2);
                    formadepago formadepago = (formadepago) formapago.getSelectedItem();
                    int dias = formadepago.todia();
                    cantdias.setText("" + dias);
                    diascredito.setVisible(true);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Selecciona tipo de pago", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void cantdiasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cantdiasActionPerformed
        // TODO add your handling code here:
        float resta = 0;
        montoabonado = 0;//mod
        String sqlfac = "";
        try {
            if (Float.parseFloat(cantdias.getText().toString()) >= 1) {
                int fes1 = 0;
                Calendar cal = new GregorianCalendar(año(), mes(), dia());
                fes1 = Integer.parseInt(cantdias.getText());
                cal.add(Calendar.DATE, fes1);
                int mes2 = cal.get(Calendar.MONTH) + 1;
                String fechacred = cal.get(Calendar.YEAR) + "-" + mes2 + "-" + cal.get(Calendar.DAY_OF_MONTH);// + " " + cal.getTime().getHours() + ":" + cal.getTime().getMinutes() + ":" + cal.getTime().getSeconds();
                fechasalida = fechacred;
                conn = BdConexion.getConexion();
                // Se crea un Statement, para realizar la consulta
                Statement s = (Statement) conn.createStatement();

                formadepago formadepago = (formadepago) formapago.getSelectedItem();
                String idTP = formadepago.getID();
                char sta = 'T';
                String saldov = "", total = "";

                sta = 'T';
                saldov = (TotalPagar.getText());
                total = (TotalPagar.getText());
                sqlfac = "UPDATE salida SET fechapago='" + fechacred + "',estado='" + sta + "',total='" + total + "',saldo='" + saldov + "' WHERE idsalida =" + idfac.getText();
                s.executeUpdate(sqlfac);

                diascredito.setVisible(false);

                int resp;
                resp = JOptionPane.showOptionDialog(this, "¿Desea realizar un Abono?", "Pregunta", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{"No", "Si"}, "No");
                if (resp == 1) {
                    dcFecha.setDate(Calendar.getInstance().getTime());
                    montoabono.setValue(null);
                    efectivo.setValue(null); //mod
                    observacionabono.setText("");
                    formapagoabono.setSelectedIndex(0);//mod
                    abono.setVisible(true);
                    montoabono.requestFocus();
                    abono.setSize(504, 190);
                    abono.setResizable(false);
                    Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
                    Dimension ventana = abono.getSize();
                    abono.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 2);
                    abono.toFront();//aparece al frente
                    //new imprimiendo().setVisible(true);
                }
                if (resp == 0) {
                    saldototal();
                    new imprimiendo().setVisible(true);
                    limipiarventas();
                }

            } else {
                JOptionPane.showMessageDialog(null, "Error dias no puede ser menor a 1");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }//GEN-LAST:event_cantdiasActionPerformed

    public boolean esentero(double x) {
        if (x % 1 == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void guardarventa() {

        if ((formapago.getSelectedIndex() < 0)) {
            JOptionPane.showInternalMessageDialog(this, "Seleccione el tipo de pago ");
        } else {
            String fecha = "";
            fecha = getFecha();
            //char sta='T';

            if ((idproducto.getText() != "") && (cantidadP.getText() != "")) {
                try {

                    String idP = null, prod = null/*preV = null, preC = null*/;
                    int idPrv = 0, /*cantP = 0, loteC = 0, C = 0, restalote = 0, restapro = 0,*/ idfact = 0, Nofac = 0/*, C2 = 0*/;
                    float suma = 0, est = 0, C = 0, cantP = 0, loteC = 0, preV = 0, C2 = 0, restalote = 0, restapro = 0, preC = 0;
                    String precioventa = "";
                    if (comboprecio.getSelectedIndex() == 0) {
                        precioventa = "producto.precioventa";
                    }
                    if (comboprecio.getSelectedIndex() == 1) {
                        precioventa = "producto.preciocredito";
                    }
                    if (comboprecio.getSelectedIndex() == 2) {
                        precioventa = "producto.preciodistribuidor";
                    }
                    if (comboprecio.getSelectedIndex() == 3) {
                        precioventa = "producto.precioespecial";
                    }
                    conn = BdConexion.getConexion();
                    // Se crea un Statement, para realizar la consulta
                    Statement s = (Statement) conn.createStatement();
                    C2 = Float.parseFloat(cantidadP.getText().toString());

                    String sqlfac = "", sqlupdate = "", cancel = "C", sql3 = "", estado = "T", sql = null;
                    if (esentero(C2) == true) {
                        sql = "select lote.idlote,lote.precio,lote.stock,producto.idProducto,producto.Cantidad,producto.nombre," + precioventa + " from lote INNER JOIN producto ON  lote.producto_idProducto=producto.idproducto where producto.idproducto='" + idproducto.getText() + "'and lote.estado='" + estado + "' and lote.stock > 0.99 order by lote.idlote asc ";
                    }
                    if (esentero(C2) == false) {
                        sql = "select lote.idlote,lote.precio,lote.stock,producto.idProducto,producto.Cantidad,producto.nombre," + precioventa + " from lote INNER JOIN producto ON  lote.producto_idProducto=producto.idproducto where producto.idproducto='" + idproducto.getText() + "'and lote.estado='" + estado + "' order by lote.idlote asc";
                    }
                    if (C2 <= Float.parseFloat(existencia.getText())) {
                        if (tablaventas.getRowCount() == 0) {

                            Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
                            Dimension ventana = cliente.getSize();
                            cliente.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 2);

                            //cliente.setSize(651, 568);
                            cliente.setVisible(true);

                        }
                    }
                    try {
                        conn.setAutoCommit(false);
                        //s.executeUpdate("LOCK TABLES productos WRITE;");
                        s.executeUpdate("LOCK TABLES producto WRITE,detallesalida WRITE, lote WRITE, salida WRITE;");
                        while (est == 0) {
                            ResultSet rs = s.executeQuery(sql);

                            if (rs.next() == true) {
                                if (C == 0) {
                                    //           String Cant = JOptionPane.showInputDialog(null, "ingresa Cantidad Venta Producto");
                                    C = Float.parseFloat(cantidadP.getText().toString());
                                    C = (float) (Math.round(C * 100.0) / 100.0);
                                }
                                if (C > 0) {

                                    prod = rs.getString("producto.nombre");
                                    //nombreproducto.setText(prod);
                                    cantP = rs.getFloat("producto.cantidad");

                                    idP = rs.getString("producto.idProducto");
                                    idPrv = rs.getInt("lote.idlote");

                                    preC = rs.getFloat("lote.precio");
                                    loteC = rs.getFloat("lote.stock");

                                    if (!precios.getText().isEmpty()) {
                                        preV = Float.parseFloat(precios.getText());//rs.getFloat("" + precioventa);
                                        //preV = rs.getFloat("" + precioventa);
                                        preV = (float) (Math.round(preV * 100.0) / 100.0);
                                    } else {
                                        preV = rs.getFloat("" + precioventa);
                                        preV = (float) (Math.round(preV * 100.0) / 100.0);
                                    }

                                    if (cantP >= C) {
                                        if (tablaventas.getRowCount() == 0) {

                                            if (idfac.getText().equals("")) {

                                                formadepago formadepago = (formadepago) formapago.getSelectedItem();
                                                String idTP = formadepago.getID();
                                                int dias = formadepago.todia();
                                                char sta = 'T';
//                                            String saldov = "";
                                                String fechapago = "";
                                                if (formapago.getSelectedItem().toString().equals("CONTADO")) {
                                                    fechapago = fecha;
                                                    sta = 'F';
                                                }
                                                if (!formapago.getSelectedItem().toString().equals("CONTADO")) {
                                                    sta = 'T';
                                                    Calendar cal = new GregorianCalendar(año(), mes(), dia());

                                                    cal.add(Calendar.DATE, dias);
                                                    int mes2 = cal.get(Calendar.MONTH) + 1;
                                                    String fechacred = cal.get(Calendar.YEAR) + "-" + mes2 + "-" + cal.get(Calendar.DAY_OF_MONTH);// + " " + cal.getTime().getHours() + ":" + cal.getTime().getMinutes() + ":" + cal.getTime().getSeconds();
                                                    fechapago = fechacred;

                                                }
                                                fechainicial.setEnabled(false);

                                                sqlfac = "insert into salida" + "(fecha,fechapago,usuario_idusuario,tipopago_idtipopago,estado,clientes_idClientes) values" + "('" + fecha + "','" + fechapago + "','" + AccesoUsuario.idusu() + "','" + idTP + "','" + sta + "','" + idcliente.getText() + "')";
                                                s.executeUpdate(sqlfac, Statement.RETURN_GENERATED_KEYS);
                                                ResultSet rs4 = s.getGeneratedKeys();
                                                if (rs4 != null && rs4.next()) {
                                                    idfact = rs4.getInt(1);
                                                }
                                                idfactura = idfact + "";
                                                Nofac = idfact + 500;
                                                sqlfac = "UPDATE salida SET salida='" + Nofac + "' WHERE idsalida =" + idfact;
                                                s.executeUpdate(sqlfac);
                                                idfac.setText("" + idfact);
                                                Nodefac.setText("" + Nofac);
                                            }
                                        }

                                        if (loteC >= C) {
                                            int iddetalle = 0;
                                            suma = C * preV;
                                            suma = (float) (Math.round(suma * 100.0) / 100.0);
                                            DefaultTableModel tempe = (DefaultTableModel) tablaventas.getModel();

                                            restalote = loteC - C;
                                            if (restalote == 0) {
                                                sql3 = "UPDATE lote SET stock='" + restalote + "',estado='" + cancel + "' WHERE idlote =" + idPrv;
                                                s.executeUpdate(sql3);
                                                restapro = cantP - C;
                                                sql3 = "UPDATE producto SET cantidad='" + restapro + "' WHERE idproducto =" + idP;
                                                s.executeUpdate(sql3);
                                                sql3 = "insert into detallesalida" + "(Cantidad,Precio,lote_idlote,salida_idsalida)values" + "('" + C + "','" + preV + "','" + idPrv + "','" + idfac.getText() + "')";
                                                s.executeUpdate(sql3, Statement.RETURN_GENERATED_KEYS);
                                                ResultSet rs4 = s.getGeneratedKeys();
                                                if (rs4 != null && rs4.next()) {
                                                    iddetalle = rs4.getInt(1);
                                                }
                                                Object nuevo[] = {idP, prod, C, preC, preV, suma, idPrv, iddetalle};
                                                tempe.addRow(nuevo);
                                                cuentaprecio();
                                            } else {
                                                sql3 = "UPDATE lote SET stock='" + restalote + "' WHERE idlote =" + idPrv;
                                                s.executeUpdate(sql3);
                                                restapro = cantP - C;
                                                sql3 = "UPDATE producto SET cantidad='" + restapro + "' WHERE idproducto =" + idP;
                                                s.executeUpdate(sql3);
                                                sql3 = "insert into detallesalida" + "(cantidad,Precio,lote_idlote,salida_idsalida)values" + "('" + C + "','" + preV + "','" + idPrv + "','" + idfac.getText() + "')";
                                                s.executeUpdate(sql3, Statement.RETURN_GENERATED_KEYS);
                                                ResultSet rs4 = s.getGeneratedKeys();
                                                if (rs4 != null && rs4.next()) {
                                                    iddetalle = rs4.getInt(1);
                                                }
                                                Object nuevo[] = {idP, prod, C, preC, preV, suma, idPrv, iddetalle};
                                                tempe.addRow(nuevo);
                                                cuentaprecio();
                                            }
                                            est = 1;
                                            //Prueba para que actualice el total cada vez que se agrega un producto
                                            String saldoventa = "", totalventa = "";
                                            if (formapago.getSelectedItem().toString().equals("CONTADO")) {
                                                saldoventa = "0";
                                            } else {
                                                saldoventa = (TotalPagar.getText());
                                            }
                                            totalventa = (TotalPagar.getText());
                                            sqlupdate = "UPDATE salida SET total='" + totalventa + "',saldo='" + saldoventa + "' WHERE idsalida =" + idfac.getText();
                                            s.executeUpdate(sqlupdate);
                                            //***************************************************

                                        }//fin del if lote
                                        else {
                                            float numetero = (C);
                                            int cantidadd = (int) loteC;
                                            float diff = (float) (Math.round((loteC - cantidadd) * 100.0) / 100.0);
                                            if (esentero(numetero) == true) {
                                                loteC = cantidadd;

                                            } else {
                                                loteC = (float) (Math.round(loteC * 100.0) / 100.0);
                                                diff = 0;
                                            }
                                            int iddetalle = 0;
                                            suma = (loteC * preV);
                                            suma = (float) (Math.round(suma * 100.0) / 100.0);
                                            DefaultTableModel tempe = (DefaultTableModel) tablaventas.getModel();
                                            //idproveedorfac.setText(""+temps.getValueAt(p, 8));
                                            String cancellote = "T";
                                            if (diff > 0) {
                                                cancellote = "T";
                                            } else {
                                                cancellote = "C";
                                            }
                                            //System.out.print("diff " + diff + " - cancelalote " + cancellote);
                                            est = 0;
                                            sql3 = "UPDATE lote SET stock='" + diff + "', estado='" + cancellote + "' WHERE idlote =" + idPrv;
                                            s.executeUpdate(sql3);
                                            restapro = (float) (Math.round((cantP - loteC) * 100.0) / 100.0);
                                            sql3 = "UPDATE producto SET cantidad='" + restapro + "' WHERE idproducto =" + idP;
                                            s.executeUpdate(sql3);
                                            sql3 = "insert into detallesalida" + "(cantidad,Precio,lote_idlote,salida_idsalida)values" + "('" + loteC + "','" + preV + "','" + idPrv + "','" + idfac.getText() + "')";
                                            s.executeUpdate(sql3, Statement.RETURN_GENERATED_KEYS);
                                            ResultSet rs4 = s.getGeneratedKeys();
                                            if (rs4 != null && rs4.next()) {
                                                iddetalle = rs4.getInt(1);
                                            }
                                            Object nuevo[] = {idP, prod, loteC, preC, preV, suma, idPrv, iddetalle};
                                            tempe.addRow(nuevo);
                                            cuentaprecio();
                                            C = C - loteC;

                                            //Prueba para que actualice el total cada vez que se agrega un producto
                                            String saldoventa = "", totalventa = "";
                                            if (formapago.getSelectedItem().toString().equals("CONTADO")) {
                                                saldoventa = "0";
                                            } else {
                                                saldoventa = (TotalPagar.getText());
                                            }
                                            totalventa = (TotalPagar.getText());
                                            sqlupdate = "UPDATE salida SET total='" + totalventa + "',saldo='" + saldoventa + "' WHERE idsalida =" + idfac.getText();
                                            s.executeUpdate(sqlupdate);
                                            //***************************************************
                                        }
                                    }//fin el if contador 
                                    else {
                                        est = 1;
                                        JOptionPane.showMessageDialog(this,
                                                "No tienes esa cantidad de productos", "Error",
                                                JOptionPane.ERROR_MESSAGE);
                                        est = 1;

                                    }
                                } else {
                                    est = 1;
                                    JOptionPane.showMessageDialog(this,
                                            "Cantidad tiene que ser mayor a cero", "Error",
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                est = 1;
                                JOptionPane.showMessageDialog(this,
                                        "Cantidad mayor a productos existentes", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }//fin del doooo
                        System.out.print("cierre de unlock");
                        s.executeUpdate("UNLOCK TABLES;");
                        conn.commit();
                        s.close();
                        if (!conn.getAutoCommit()) {
                            conn.setAutoCommit(true);
                        }
                        ////conn.close();
                        if (est == 1) {
                            idproducto.setText("");
                            nombreproducto.setText("");
                            existencia.setText("");
                            precios.setText("");
                            cantidadP.setText("");
                            busquedacompra.setText("");
                            busquedacompra.requestFocus();
                            unidadproducto.setText("");
                            //codigoproductos.setSelectedIndex(0);
                            //codigoproductos.requestFocus();

                            comboprecio.setEnabled(false);
                            formapago.setEnabled(false);
                            // llenarcombo();
                        }
//busquedacompra.selectAll();
                    } catch (Exception ex) {
                        System.out.print("cierre de unlock");
                        s.executeUpdate("UNLOCK TABLES;");
                        conn.rollback();
                        s.close();
                        if (!conn.getAutoCommit()) {
                            conn.setAutoCommit(true);
                        }
                        fechainicial.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "Ocurrio un error en el Proceso " + ex);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Ocurrio un error en el Proceso " + ex);
                    //Logger.getLogger(frmventas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }


    private void nombreproductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nombreproductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nombreproductoActionPerformed

    private void existenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_existenciaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_existenciaActionPerformed

    private void preciosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_preciosActionPerformed
        // TODO add your handling code here:
        //modificado el 12-03-2015 GLARA..
        if (!cantidadP.getText().isEmpty()) {
            guardarventa();
        } else {
            JOptionPane.showMessageDialog(null, "Ingrese cantidad ");
        }
    }//GEN-LAST:event_preciosActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (tablaventas.getRowCount() <= 0) {
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, "No Puedes cerrar Tienes Ventas Pendientes");
        }
    }//GEN-LAST:event_jButton4ActionPerformed


    private void idfacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idfacActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idfacActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        String fecha = "", sql = "";
        java.util.Date fechas = new Date();
        int mes = fechas.getMonth() + 1;
        GregorianCalendar calendarios
                = new GregorianCalendar();
        Calendar c1 = Calendar.getInstance();

        fecha = "" + c1.get(calendarios.YEAR) + "-" + mes + "-" + fechas.getDate();// + " " + fechas.getHours() + ":" + fechas.getMinutes() + ":" + fechas.getSeconds();

        if (nombren.getText().equals("") || direccionn.getText().equals("")) {

            Mensaje.setText("*Faltan datos por ingresar ");

        } else {

//            if ((telefonon.getText().equals("") || telefonon.getText().equals(" ") || telefonon.getText().equals("  ")) || (nitn.getText().equals("") || nitn.getText().equals(" ") || nitn.getText().equals("  "))) {
//                String tel = telefonon.getText(), nits = nitn.getText();
//                sql = "insert into clientes" + "(Nombre,Direccion,Fec_reg)values" + "('" + nombren.getText() + "','" + direccionn.getText() + "','" + fecha + "')";
//
//                if (/*(telefonon.getText().equals("") || telefonon.getText().equals(" ")) || */telefonon.getText().isEmpty() && !nitn.getText().isEmpty()/*(!nitn.equals(""))*/ ) {
//                    sql = "insert into clientes" + "(Nombre,nit,Direccion,Fec_reg)values" + "('" + nombren.getText() + "','" + nits + "','" + direccionn.getText() + "','" + fecha + "')";
//
//                }
//                if ((nitn.getText().equals("") || nitn.getText().equals(" ") || nitn.getText().equals("  ")) && (!telefonon.equals(""))) {
//
//                    sql = "insert into clientes" + "(Nombre,telefono,Direccion,Fec_reg)values" + "('" + nombren.getText() + "','" + telefonon.getText() + "','" + direccionn.getText() + "','" + fecha + "')";
//                }
//
//            } else {
//                sql = "insert into clientes" + "(Nombre,Nit,telefono,Direccion,Fec_reg)values" + "('" + nombren.getText() + "','" + nitn.getText() + "','" + telefonon.getText() + "','" + direccionn.getText() + "','" + fecha + "')";
//
//            }
            if (nitn.getText().isEmpty()) {
                sql = "insert into clientes" + "(Nombre,telefono,Direccion,Fec_reg)values" + "('" + nombren.getText() + "','" + telefonon.getText() + "','" + direccionn.getText() + "','" + fecha + "')";
                buscacliente.setText(nombren.getText());
            } else if (!nitn.getText().isEmpty()) {
                sql = "insert into clientes" + "(Nombre,Nit,telefono,Direccion,Fec_reg)values" + "('" + nombren.getText() + "','" + nitn.getText() + "','" + telefonon.getText() + "','" + direccionn.getText() + "','" + fecha + "')";
                buscacliente.setText(nitn.getText());
            }

            //sql = "insert into clientes" + "(Nombre,Nit,telefono,Direccion,Fec_reg) values" + "('" + nombren.getText() + "','" + nitn.getText() + "','" + telefonon.getText() + "','" + direccionn.getText() + "','" + fecha + "')";
            try {
                conn = BdConexion.getConexion();
                // Se crea un Statement, para realizar la consulta
                Statement s = (Statement) conn.createStatement();
                s.executeUpdate(sql);
                //conexion.close();
                //Mensaje.setText("Ingresa Cliente");

                telefonon.setText("");
                nombren.setText("");
                direccionn.setText("");
                nitn.setText("");
                buscarcliente();
                seleccionarclienteautomatico();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this, "Error : " + ex, "Error", JOptionPane.ERROR_MESSAGE);
                //Logger.getLogger(frmventas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void bntSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntSalirActionPerformed
        // TODO add your handling code here:
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = buscar.getSize();
        buscar.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 2);
        busqueda.setText("");
        buscar.setVisible(true);

    }//GEN-LAST:event_bntSalirActionPerformed

    private void busquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_busquedaActionPerformed
        // TODO add your handling code here:
        try {
            conn = BdConexion.getConexion();
            // Se crea un Statement, para realizar la consulta
            Statement s = (Statement) conn.createStatement();
            float suma = 0, cantPrestamo = 0, cantidad = 0;
            MiModelo modelo = new MiModelo();
            busquedaproducto.setModel(modelo);
            modelo.addColumn("Id");
            modelo.addColumn("Código");
            modelo.addColumn("Producto");
            modelo.addColumn("Cantidad");
            modelo.addColumn("Precio V");
            modelo.addColumn("Descripción");
            modelo.addColumn("Unidad");
            String precioventa = "";
            if (comboprecio.getSelectedIndex() == 0) {
                precioventa = "producto.precioventa";
            }
            if (comboprecio.getSelectedIndex() == 1) {
                precioventa = "producto.preciocredito";
            }
            if (comboprecio.getSelectedIndex() == 2) {
                precioventa = "producto.preciodistribuidor";
            }
            if (comboprecio.getSelectedIndex() == 3) {
                precioventa = "producto.precioespecial";
            }

            String sql = "select " + precioventa + ",producto.cantidad,producto.idProducto,producto.Codigo,producto.nombre,producto.descripcion,unidad.nombre from producto INNER JOIN unidad on producto.idunidad=unidad.idunidad where producto.nombre like '%" + busqueda.getText() + "%' or producto.Codigo='" + busqueda.getText() + "' and producto.estado='T'";
            ResultSet rs = s.executeQuery(sql);

            while (rs.next()) {
                Object[] fila = new Object[7]; // Hay tres columnas en la tabla
                // Se rellena cada posición del array con una de las columnas de la tabla en base de datos.
                fila[0] = rs.getInt("producto.idproducto");
                fila[1] = rs.getString("producto.Codigo");
                fila[2] = rs.getString("producto.nombre");
                cantidad = rs.getFloat("producto.cantidad");
                fila[3] = cantidad;
                fila[4] = rs.getString(precioventa);
                fila[5] = rs.getString("producto.descripcion");
                fila[6] = rs.getString("unidad.nombre");

                modelo.addRow(fila);
            }
            Utilidades.ajustarAnchoColumnas(busquedaproducto);
            TableColumn desaparece4 = busquedaproducto.getColumnModel().getColumn(5);
            desaparece4.setMaxWidth(0);
            desaparece4.setMinWidth(0);
            desaparece4.setPreferredWidth(0);
            TableColumn desaparece5 = busquedaproducto.getColumnModel().getColumn(0);
            desaparece5.setMaxWidth(0);
            desaparece5.setMinWidth(0);
            desaparece5.setPreferredWidth(0);
            busquedaproducto.doLayout();
            //Utilidades.ajustarAnchoColumnas(busquedaproducto);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }//GEN-LAST:event_busquedaActionPerformed

    private void bntSalir1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntSalir1ActionPerformed
        // TODO add your handling code here:
        busqueda.setText("");
        buscar.setVisible(false);
    }//GEN-LAST:event_bntSalir1ActionPerformed
    public void cancelararticulo() {
        try {

            conn = BdConexion.getConexion();
            // Se crea un Statement, para realizar la consulta
            Statement s = (Statement) conn.createStatement();
            int p = 0;
            int resp;

            resp = JOptionPane.showConfirmDialog(null, "¿Desea Cancelar la venta= " + fecha + "?", "Pregunta", 0);

            if (resp == 0) {
                if (tablaventas.getRowCount() > 0) {
                    for (int i = tablaventas.getRowCount(); i > 0; i--) {
                        DefaultTableModel teme = (DefaultTableModel) tablaventas.getModel();
                        p = i - 1;
                        if (p >= 0) {
                            ResultSet rs = s.executeQuery("SELECT idProducto, cantidad FROM Producto WHERE idproducto='" + teme.getValueAt(p, 0) + "'");
                            float cant = 0;
                            while (rs.next()) {
                                cant = rs.getFloat("cantidad");
                            }
                            rs = s.executeQuery("SELECT  stock FROM lote WHERE idlote='" + teme.getValueAt(p, 6) + "'");
                            float cant2 = 0;
                            while (rs.next()) {
                                cant2 = rs.getFloat("stock");
                            }

                            float cant1 = Float.parseFloat(teme.getValueAt(p, 2).toString());
                            float Suma = cant + cant1;
                            float suma2 = cant2 + cant1;
                            String estado1 = "T";
                            s.executeUpdate("UPDATE producto SET cantidad='" + Suma + "' WHERE idProducto =" + teme.getValueAt(p, 0));
                            s.executeUpdate("UPDATE lote SET stock='" + suma2 + "', estado='" + estado1 + "' WHERE idlote =" + teme.getValueAt(p, 6));
                            s.executeUpdate("delete from detallesalida WHERE iddetallesalida =" + teme.getValueAt(p, 7));
                            if ((p == 0) && (tablaventas.getRowCount() == 1)) {
                                // JOptionPane.showMessageDialog(null, idfac.getText());
                                s.executeUpdate("delete from salida WHERE idsalida =" + idfac.getText());
                                limpieza();
                            }
                            teme.removeRow(p);
                            cuentaprecio();

                        } else {
                            JOptionPane.showMessageDialog(this,
                                    "Selecciona Articulo a eliminar", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No hay articulos para eliminar ", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    public void eliminaarticulo() {
        try {

            conn = BdConexion.getConexion();
            // Se crea un Statement, para realizar la consulta
            Statement s = (Statement) conn.createStatement();
            int p = -1;
            int resp;

            resp = JOptionPane.showConfirmDialog(null, "¿Desea Eliminar el Articulo= " + fecha + "?", "Pregunta", 0);

            if (resp == 0) {
                if (tablaventas.getRowCount() > 0) {

                    DefaultTableModel teme = (DefaultTableModel) tablaventas.getModel();
                    p = tablaventas.getSelectedRow();
                    if (p >= 0) {
                        ResultSet rs = s.executeQuery("SELECT idProducto, cantidad FROM Producto WHERE idproducto='" + teme.getValueAt(p, 0) + "'");
                        float cant = 0;
                        while (rs.next()) {
                            cant = rs.getFloat("cantidad");
                        }
                        rs = s.executeQuery("SELECT  stock FROM lote WHERE idlote='" + teme.getValueAt(p, 6) + "'");
                        float cant2 = 0;
                        while (rs.next()) {
                            cant2 = rs.getFloat("stock");
                        }

                        float cant1 = Float.parseFloat(teme.getValueAt(p, 2).toString());
                        float Suma = cant + cant1;
                        float suma2 = cant2 + cant1;
                        String estado1 = "T";
                        s.executeUpdate("UPDATE producto SET cantidad='" + Suma + "' WHERE idProducto =" + teme.getValueAt(p, 0));
                        s.executeUpdate("UPDATE lote SET stock='" + suma2 + "', estado='" + estado1 + "' WHERE idlote =" + teme.getValueAt(p, 6));
                        s.executeUpdate("delete from detallesalida WHERE iddetallesalida =" + teme.getValueAt(p, 7));
                        if ((p == 0) && (tablaventas.getRowCount() == 1)) {
                            // JOptionPane.showMessageDialog(null, idfac.getText());
                            s.executeUpdate("delete from salida WHERE idsalida =" + idfac.getText());
                            limpieza();
                        }
                        teme.removeRow(p);
                        cuentaprecio();

                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Selecciona Articulo a eliminar", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No hay articulos para eliminar ", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
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

    public void saldototal() {
        try {
            conn = BdConexion.getConexion();
            //conn = BdConexion.getConexion();
            String sqls = "select sum(saldo) from salida where clientes_idclientes='" + idcliente.getText() + "' and salida.estado='T'";
            Statement ss = (Statement) conn.createStatement();
            float nsaldototall = 0;

            ResultSet rss = ss.executeQuery(sqls);
            if (rss.next() == true) {
                rss.beforeFirst();
                while (rss.next()) {
                    nsaldototall = rss.getFloat("sum(saldo)");

                }
                saldototalc = Float.parseFloat("" + nsaldototall);
            }
            ////conn.close();
        } catch (Exception e) {

        }

    }

    private void elartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elartActionPerformed
        // TODO add your handling code here:
        eliminaarticulo();
    }//GEN-LAST:event_elartActionPerformed

    private void btnmodificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmodificarActionPerformed
        // TODO add your handling code here:
        DefaultTableModel temp = (DefaultTableModel) tablaventas.getModel();
        if (tablaventas.getRowCount() > 0) {

            DefaultTableModel teme = (DefaultTableModel) tablaventas.getModel();
            int p = tablaventas.getSelectedRow();
            if (p >= 0) {
                Actual.setText(temp.getValueAt(tablaventas.getSelectedRow(), 4).toString());
                Ccantidad.setText(temp.getValueAt(tablaventas.getSelectedRow(), 2).toString());
                Cprecio.setText(temp.getValueAt(tablaventas.getSelectedRow(), 4).toString());
                costo.setText(temp.getValueAt(tablaventas.getSelectedRow(), 3).toString());
                iddcambio.setText(temp.getValueAt(tablaventas.getSelectedRow(), 7).toString());
                ps.setText("" + tablaventas.getSelectedRow());
                Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
                Dimension ventana = precio.getSize();
                precio.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 2);
                precio.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Selecciona Articulo a Modificar", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "No hay articulos para Modificar ", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        //modificaprecio();
    }//GEN-LAST:event_btnmodificarActionPerformed

    private void btnbusscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbusscarActionPerformed
        // TODO add your handling code here:
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = buscar.getSize();
        buscar.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 2);
        busqueda.setText("");
        buscar.setVisible(true);
    }//GEN-LAST:event_btnbusscarActionPerformed
    public void modificaprecio() {
        try {

            //DriverManager.registerDriver(new org.gjt.mm.mysql.Driver());
            //LeePropiedades.archivoRecurso = archivoRecurso;
            //Connection conexion = (Connection) DriverManager.getConnection(LeePropiedades.leeID("url"), LeePropiedades.leeID("usuario"), LeePropiedades.leeID("password"));
            conn = BdConexion.getConexion();
// Se crea un Statement, para realizar la consulta
            Statement s = (Statement) conn.createStatement();
            int p = -1;
            int resp;

            resp = JOptionPane.showConfirmDialog(null, "¿Desea Modificar precio del Articulo= " + fecha + "?", "Pregunta", 0);

            if (resp == 0) {
                if (tablaventas.getRowCount() > 0) {

                    DefaultTableModel teme = (DefaultTableModel) tablaventas.getModel();
                    p = tablaventas.getSelectedRow();
                    if (p >= 0) {
                        ResultSet rs = s.executeQuery("SELECT idProducto, cantidad FROM Producto WHERE idproducto='" + teme.getValueAt(p, 0) + "'");
                        float cant = 0;
                        while (rs.next()) {
                            cant = rs.getFloat("cantidad");
                        }
                        rs = s.executeQuery("SELECT  stock FROM lote WHERE idlote='" + teme.getValueAt(p, 6) + "'");
                        float cant2 = 0;
                        while (rs.next()) {
                            cant2 = rs.getFloat("stock");
                        }

                        float cant1 = Float.parseFloat(teme.getValueAt(p, 2).toString());
                        float Suma = cant + cant1;
                        float suma2 = cant2 + cant1;
                        String estado1 = "T";
                        s.executeUpdate("UPDATE producto SET cantidad='" + Suma + "' WHERE idProducto =" + teme.getValueAt(p, 0));
                        s.executeUpdate("UPDATE lote SET stock='" + suma2 + "', estado='" + estado1 + "' WHERE idlote =" + teme.getValueAt(p, 6));
                        s.executeUpdate("delete from detallesalida WHERE iddetallesalida =" + teme.getValueAt(p, 7));
                        if ((p == 0) && (tablaventas.getRowCount() == 1)) {
                            // JOptionPane.showMessageDialog(null, idfac.getText());
                            s.executeUpdate("delete from salida WHERE idsalida =" + idfac.getText());
                            limpieza();
                        }
                        teme.removeRow(p);
                        cuentaprecio();

                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Selecciona Articulo a eliminar", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No hay articulos para eliminar ", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    private void buscarcliente() {
        DefaultTableModel modelo = new DefaultTableModel();
        busquedacliente.setModel(modelo);
        modelo.addColumn("Código");
        modelo.addColumn("Nombre");
        modelo.addColumn("Dirección");
        modelo.addColumn("Nit");
        //modelo.addColumn("Télefono");

        String sql = "select idclientes,nombre,nit,direccion,telefono from clientes where nombre like '%" + buscacliente.getText() + "%' or nit='" + buscacliente.getText() + "' and estado='T'";
        try {
            conn = BdConexion.getConexion();
            // Se crea un Statement, para realizar la consulta
            Statement s = (Statement) conn.createStatement();

            ResultSet rs = s.executeQuery(sql);
            if (rs.next() == true) {
                rs.beforeFirst();
                while (rs.next()) {
                    //JOptionPane.showMessageDialog(null, ""+rs.getString("nombreusuario"));
                    Object[] fila = new Object[4]; // Hay tres columnas en la tabla
                    // Se rellena cada posición del array con una de las columnas de la tabla en base de datos.
                    fila[0] = rs.getInt("idclientes");
                    fila[1] = rs.getString("nombre");
                    fila[2] = rs.getString("direccion");
                    fila[3] = rs.getString("nit");
                    //fila[4] = rs.getString("telefono");
                    modelo.addRow(fila);
                }
                Utilidades.ajustarAnchoColumnas(busquedacliente);
                TableColumn desaparece = busquedacliente.getColumnModel().getColumn(0);
                desaparece.setMaxWidth(0);
                desaparece.setMinWidth(0);
                desaparece.setPreferredWidth(0);
                busquedacliente.doLayout();

            } else {

            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }

    }
    private void buscaclienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscaclienteActionPerformed
        // TODO add your handling code here:
        buscarcliente();

//        DefaultTableModel modelo = new DefaultTableModel();
//        busquedacliente.setModel(modelo);
//        modelo.addColumn("Código");
//        modelo.addColumn("Nombre");
//        modelo.addColumn("Dirección");
//        modelo.addColumn("Nit");
//        //modelo.addColumn("Télefono");
//
//        String sql = "select idclientes,nombre,nit,direccion,telefono from clientes where nombre like '%" + buscacliente.getText() + "%' or nit='"+ buscacliente.getText() + "' and estado='T'";
//        try {
//            conn = BdConexion.getConexion();
//            // Se crea un Statement, para realizar la consulta
//            Statement s = (Statement) conn.createStatement();
//
//            ResultSet rs = s.executeQuery(sql);
//            if (rs.next() == true) {
//                rs.beforeFirst();
//                while (rs.next()) {
//                    //JOptionPane.showMessageDialog(null, ""+rs.getString("nombreusuario"));
//                    Object[] fila = new Object[4]; // Hay tres columnas en la tabla
//                    // Se rellena cada posición del array con una de las columnas de la tabla en base de datos.
//                    fila[0] = rs.getInt("idclientes");
//                    fila[1] = rs.getString("nombre");
//                    fila[2] = rs.getString("direccion");
//                    fila[3] = rs.getString("nit");
//                    //fila[4] = rs.getString("telefono");
//                    modelo.addRow(fila);
//                }
//                Utilidades.ajustarAnchoColumnas(busquedacliente);
//                TableColumn desaparece = busquedacliente.getColumnModel().getColumn(0);
//                desaparece.setMaxWidth(0);
//                desaparece.setMinWidth(0);
//                desaparece.setPreferredWidth(0);
//                busquedacliente.doLayout();
//
//            } else {
//
//            }
//
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
//        }

    }//GEN-LAST:event_buscaclienteActionPerformed

    private void btnbusscar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbusscar1ActionPerformed
        // TODO add your handling code here:
        cancelararticulo();
    }//GEN-LAST:event_btnbusscar1ActionPerformed
    public void cambiar() {
        DefaultTableModel temps = (DefaultTableModel) tablaventas.getModel();
        int p = Integer.parseInt(ps.getText());

        if (Float.parseFloat(Cprecio.getText()) > Float.parseFloat(costo.getText())) {
            try {
                conn = BdConexion.getConexion();
                // Se crea un Statement, para realizar la consulta
                Statement s = (Statement) conn.createStatement();

                String sqlfac = "UPDATE detallesalida SET precio='" + Cprecio.getText() + "' WHERE iddetallesalida =" + iddcambio.getText();
                s.executeUpdate(sqlfac);

                //conexion.close();
                float suma = Float.parseFloat(Ccantidad.getText()) * Float.parseFloat(Cprecio.getText());
                suma = (float) (Math.round(suma * 100.0) / 100.0);
                temps.setValueAt(Cprecio.getText(), p, 4);
                temps.setValueAt(suma, p, 5);
                cuentaprecio();
                precio.setVisible(false);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }

        } else {
            JOptionPane.showMessageDialog(null, "Precio No puede ser Menor a Costo");
        }
    }
    private void btnbusscar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbusscar2ActionPerformed
        // TODO add your handling code here:
        cambiar();

    }//GEN-LAST:event_btnbusscar2ActionPerformed

    private void btnbusscar3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbusscar3ActionPerformed
        // TODO add your handling code here:
        precio.setVisible(false);
    }//GEN-LAST:event_btnbusscar3ActionPerformed

    private void idproductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idproductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idproductoActionPerformed
    private int año() {
        int años = fechainicial.getCalendar().get(Calendar.YEAR);
        return años;
    }

    private int mes() {
        int mess = fechainicial.getCalendar().get(Calendar.MONTH);
        return mess;
    }

    private int dia() {
        int dias = fechainicial.getCalendar().get(Calendar.DAY_OF_MONTH);
        return dias;
    }

    private String getFecha() {

        try {
            String fecha;
            int años = fechainicial.getCalendar().get(Calendar.YEAR);
            int dias = fechainicial.getCalendar().get(Calendar.DAY_OF_MONTH);
            int mess = fechainicial.getCalendar().get(Calendar.MONTH) + 1;

            fecha = "" + años + "-" + mess + "-" + dias;// + " " + hours + ":" + minutes + ":" + seconds;
            return fecha;
        } catch (Exception e) {
            JOptionPane.showInternalMessageDialog(this, "Verifique la fecha");
            //System.out.print(e.getMessage());
        }
        return null;
    }
    private void cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarActionPerformed
        // TODO add your handling code here:
        cliente.dispose();

    }//GEN-LAST:event_cancelarActionPerformed

    private void CprecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CprecioActionPerformed
        // TODO add your handling code here:
        cambiar();
    }//GEN-LAST:event_CprecioActionPerformed

    private void jcMousePanel1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jcMousePanel1KeyPressed
        // TODO add your handling code here:
//        JOptionPane.showMessageDialog(null, "rrrr");
    }//GEN-LAST:event_jcMousePanel1KeyPressed

    private void montoabonoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_montoabonoKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == 27) {
            abono.setVisible(false);
        }
    }//GEN-LAST:event_montoabonoKeyReleased

    private void butnguardarabonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butnguardarabonoActionPerformed
        // TODO add your handling code here:
        //int fil = tablacomprasporpagar.getSelectedRow();
        float dat = 0, dato3 = 0, vuelto = 0;
        int indicador = 0;
        montoabonado = 0;//mod
        dat = Float.parseFloat(Validar(TotalPagar.getText()));
        dat = (float) (Math.round((dat) * 100.0) / 100.0);

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
                        //conn = BdConexion.getConexion();
                        //PreparedStatement nos permite crear instrucciones SQL compiladas, que se ejecutan con más efi ciencia que los objetos Statement
                        //también pueden especifi car parámetros,lo cual las hace más fl exibles que las instrucciones Statement
                        int idabono = 0;
                        //int fila = tablacomprasporpagar.getSelectedRow();

                        float montoc, saldoc, nsaldo, nsaldototal, nsaldototal2;
                        montoc = Float.parseFloat(Validar(montoabono.getText()));
                        saldoc = Float.parseFloat(Validar(TotalPagar.getText()));
                        nsaldo = (float) (Math.round((saldoc - montoc) * 100.0) / 100.0);

                        String dato = idfactura;
                        float nsaldototall = 0;
                        String sqls = "select sum(saldo) from salida where clientes_idclientes='" + idcliente.getText() + "' and salida.estado='T'";
                        Statement ss = (Statement) conn.createStatement();

                        ResultSet rss = ss.executeQuery(sqls);
                        if (rss.next() == true) {
                            rss.beforeFirst();
                            while (rss.next()) {
                                nsaldototall = rss.getFloat("sum(saldo)");

                            }
                        }
                        montoabonado = Float.parseFloat(Validar(montoabono.getText()));
                        saldoventa = nsaldo;

                        String sql = "insert into xcobrarclientes (fecha,monto,salida_idsalida,usuario_idusuario,observacion,tipopago,nsaldoventa,nsaldototal) values (?,?,?,?,?,?,?,?)";
                        PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                        ps.setString(1, getFecha());
                        ps.setString(2, Validar(montoabono.getText()));
                        ps.setString(3, dato);
                        //Login entrar = new Login();
                        ps.setString(4, "" + AccesoUsuario.idusu());
                        ps.setString(5, observacionabono.getText());
                        ps.setString(6, (formapagoabono.getSelectedItem().toString()));
                        ps.setFloat(7, nsaldo);

                        //idcliente.getText();
                        nsaldototal = (float) (Math.round((nsaldototall) * 100.0) / 100.0);
                        /**
                         * *****
                         */
                        nsaldototal2 = (float) (Math.round((nsaldototal - montoc) * 100.0) / 100.0);
                        saldototalc = nsaldototal2;
                        ps.setFloat(8, nsaldototal2);

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

                        if (montoc > 0 & montoc <= saldoc) {
                            status = 'T';
                            if (montoc == saldoc) {
                                status = 'F';
                            }
                            String abono = "update salida set  saldo=?, estado=? where idsalida=?";
                            PreparedStatement ps2 = conn.prepareStatement(abono);
                            ps2.setFloat(1, nsaldo);
                            ps2.setString(2, "" + status);
                            ps2.setString(3, dato);

                            n2 = ps2.executeUpdate();

                        }

                        ////conn.close();
                        if (n > 0 & n2 > 0) {
                            dcFecha.setDate(Calendar.getInstance().getTime());
                            montoabono.setValue(null);
                            efectivo.setValue(null);//mod
                            formapagoabono.setSelectedIndex(0);//mod
                            observacionabono.setText("");
                            abono.setVisible(false);

                            JOptionPane.showInternalMessageDialog(this, "Datos guardados correctamente\n\n Vuelto = Q. " + ((float) (Math.round((vuelto - dato3) * 100.0) / 100.0)));
                            new imprimiendo().setVisible(true);
                            limipiarventas();
                            //abrircomprobante(idabono);
                        }

                    } catch (SQLException e) {
                        JOptionPane.showInternalMessageDialog(this, "Error  al guardar \n Verifique los datos e intente nuevamente ", "Error", JOptionPane.ERROR_MESSAGE);
                        //System.out.print(e.getMessage());
                    }

                }
            }
        }
    }//GEN-LAST:event_butnguardarabonoActionPerformed

    private void btncancelarabonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelarabonoActionPerformed
        // TODO add your handling code here:
        dcFecha.setDate(Calendar.getInstance().getTime());//mod
        montoabono.setValue(null);//mod
        efectivo.setValue(null);//mod
        formapagoabono.setSelectedIndex(0);//mod
        observacionabono.setText("");//mod
        this.abono.dispose();
    }//GEN-LAST:event_btncancelarabonoActionPerformed

    private void btncancelarabono1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelarabono1ActionPerformed
        // TODO add your handling code here:
        dcFecha.setDate(Calendar.getInstance().getTime());
        montoabono.setValue(null);
        efectivo.setValue(null);//mod
        formapagoabono.setSelectedIndex(0);//mod
        observacionabono.setText("");

    }//GEN-LAST:event_btncancelarabono1ActionPerformed

    private void observacionabonoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_observacionabonoKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == 27) {
            abono.setVisible(false);
        }
    }//GEN-LAST:event_observacionabonoKeyReleased

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

    private void btncrearproductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncrearproductoActionPerformed
        // TODO add your handling code here:
        Producto newfrm = new Producto();
        if (newfrm == null) {
            newfrm = new Producto();
        }
        AddForms.adminInternalFrame(panel_center, newfrm);
//        Producto nuevasol = new Producto();
//        if (panel_center.getComponentCount() > 0 & panel_center.getComponentCount() < 2) //solo uno en t
//        {
//            panel_center.add(nuevasol);
//            nuevasol.show();// ver interno
//            nuevasol.setClosable(true);// icono de cerrar
//            nuevasol.toFront();//aparece al frente
//        }
    }//GEN-LAST:event_btncrearproductoActionPerformed

    private void unidadproductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unidadproductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_unidadproductoActionPerformed

    private void codigoproductosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigoproductosKeyPressed
        // TODO add your handling code here:

        int key = evt.getKeyCode();
        if (key == java.awt.event.KeyEvent.VK_ENTER) {
            //eliminaarticulo();
            //cantidadP.requestFocus();
            buscarproductocodigo();
        }
    }//GEN-LAST:event_codigoproductosKeyPressed

    private void cantidadPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cantidadPActionPerformed
        // TODO add your handling code here:
        //modificado el 12-03-2015 GLARA..
        //guardarventa();
        precios.requestFocus();
    }//GEN-LAST:event_cantidadPActionPerformed

    public void limpieza() {
        nombrecliente.setText("");
        nittxt.setText("");
        cantpago.setText("");
        idfac.setText("");
        idcliente.setText("");
        TotalPagar.setText("0.0");
        Nodefac.setText("");
        formapago.setEnabled(true);
        comboprecio.setEnabled(true);
        fechainicial.setEnabled(true);

        idproducto.setText("");
        nombreproducto.setText("");
        existencia.setText("");
        precios.setText("");
        unidadproducto.setText("");
        cantidadP.setText("");
        
        llenarcombo();
    }

    public void limipiarventas() {
        nombrecliente.setText("");
        nittxt.setText("");
        cantpago.setText("");
        txtarticulos.setText("");
        idfac.setText("");
        idcliente.setText("");
        TotalPagar.setText("0.0");
        Nodefac.setText("");
        comboprecio.setEnabled(true);
        formapago.setEnabled(true);
        fechainicial.setEnabled(true);
        llenarcombo();
        DefaultTableModel teme = (DefaultTableModel) tablaventas.getModel();
        for (int h = teme.getRowCount(); h > 0; h--) {
            teme.removeRow(h - 1);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private elaprendiz.gui.label.LabelRect Actual;
    private elaprendiz.gui.label.LabelRect Ccantidad;
    private javax.swing.JTextField Cprecio;
    private elaprendiz.gui.label.LabelRect Mensaje;
    private javax.swing.JLabel Nodefac;
    private javax.swing.JLabel TotalPagar;
    private javax.swing.JDialog abono;
    private elaprendiz.gui.button.ButtonRect bntSalir;
    private elaprendiz.gui.button.ButtonRect bntSalir1;
    private elaprendiz.gui.button.ButtonRect btnbusscar;
    private elaprendiz.gui.button.ButtonRect btnbusscar1;
    private elaprendiz.gui.button.ButtonRect btnbusscar2;
    private elaprendiz.gui.button.ButtonRect btnbusscar3;
    private elaprendiz.gui.button.ButtonRect btncancelarabono;
    private elaprendiz.gui.button.ButtonRect btncancelarabono1;
    private elaprendiz.gui.button.ButtonRect btncrearproducto1;
    private elaprendiz.gui.button.ButtonRect btnmodificar;
    private elaprendiz.gui.textField.TextFieldRectIcon buscacliente;
    private javax.swing.JDialog buscar;
    private elaprendiz.gui.textField.TextFieldRectIcon busqueda;
    private javax.swing.JTable busquedacliente;
    private javax.swing.JTextField busquedacompra;
    private javax.swing.JTable busquedaproducto;
    private elaprendiz.gui.button.ButtonRect butnguardarabono;
    private elaprendiz.gui.button.ButtonRect cancelar;
    private elaprendiz.gui.button.ButtonRect cancelar1;
    private javax.swing.JTextField cantdias;
    private javax.swing.JTextField cantidadP;
    private javax.swing.JTextField cantpago;
    private javax.swing.JDialog cliente;
    private elaprendiz.gui.varios.ClockDigital clockDigital1;
    private javax.swing.JComboBox comboprecio;
    private elaprendiz.gui.label.LabelRect costo;
    private com.toedter.calendar.JDateChooser dcFecha;
    private javax.swing.JLabel descricionP;
    private javax.swing.JDialog diascredito;
    private javax.swing.JTextField direccion;
    private javax.swing.JTextField direccionn;
    private javax.swing.JFormattedTextField efectivo;
    private elaprendiz.gui.button.ButtonRect elart;
    private javax.swing.JTextField existencia;
    private org.freixas.jcalendar.JCalendarCombo fechainicial;
    private javax.swing.JComboBox formapago;
    private javax.swing.JComboBox formapagoabono;
    private javax.swing.JTextField idcliente;
    private javax.swing.JTextField idcompra;
    private elaprendiz.gui.label.LabelRect iddcambio;
    private javax.swing.JTextField idfac;
    private javax.swing.JTextField idproducto;
    private elaprendiz.gui.button.ButtonAction iniciar;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private jcMousePanel.jcMousePanel jcMousePanel1;
    private jcMousePanel.jcMousePanel jcMousePanel11;
    private jcMousePanel.jcMousePanel jcMousePanel12;
    private jcMousePanel.jcMousePanel jcMousePanel14;
    private jcMousePanel.jcMousePanel jcMousePanel17;
    private jcMousePanel.jcMousePanel jcMousePanel2;
    private jcMousePanel.jcMousePanel jcMousePanel3;
    private jcMousePanel.jcMousePanel jcMousePanel4;
    private jcMousePanel.jcMousePanel jcMousePanel6;
    private jcMousePanel.jcMousePanel jcMousePanel9;
    private javax.swing.JFormattedTextField montoabono;
    private javax.swing.JTextField nitn;
    private javax.swing.JLabel nittxt;
    private javax.swing.JLabel nombrecliente;
    private javax.swing.JTextField nombren;
    private javax.swing.JTextField nombreproducto;
    private javax.swing.JTextArea observacionabono;
    private javax.swing.JDialog pago2;
    private javax.swing.JPanel pnlActionButtons1;
    private javax.swing.JDialog precio;
    private javax.swing.JTextField precios;
    private javax.swing.JTextField precioscostos;
    private elaprendiz.gui.label.LabelRect ps;
    private javax.swing.JTable tablaventas;
    private javax.swing.JTextField tele;
    private javax.swing.JTextField telefonon;
    private javax.swing.JLabel txtarticulos;
    private javax.swing.JTextField unidadproducto;
    // End of variables declaration//GEN-END:variables

    private AudioClip getAudioClip(URL urL) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
