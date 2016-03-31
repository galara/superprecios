/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import BD.BdConexion;
import BD.sqlprod;
import static GUI.MenuPrincipal.panel_center;
import Modelos.AccesoUsuario;
import Modelos.AddForms;
import Modelos.formadepago;
import Modelos.proveedor;
import com.mysql.jdbc.Statement;
import excepciones.FiltraEntrada;
import excepciones.FormatoDecimal;
import excepciones.Helper;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author Glara
 */
public class compras extends javax.swing.JInternalFrame {

    private String archivoRecurso = "controlador-bd";
    DefaultTableModel model;
    DefaultTableModel modelprecios;
    String[] titulos = {"Id", "Codigo", "Descripción", "Cantidad", "Precio", "Subtotal", "Selección"};
    String[] titulos2 = {"Codigo", "Descripción", "Precio Venta Actual", "Precio Compra Nuevo", "Precio Venta Sugerido"};
    java.sql.Connection conn;//getConnection intentara establecer una conexión.
    String statu;

    /**
     * Creates new form pedido
     */
    public compras() {

        initComponents();
        llenarcombo();
        limpiar();
        desactivar();
        formatotabla();
        setFiltroTexto();
        addEscapeKey();

        tabladetallecompra.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent arg0) {
                int key = arg0.getKeyCode();
                if (key == java.awt.event.KeyEvent.VK_SPACE) {

//                    int fila = tabladetallecompra.getSelectedRow();
//                    if (tabladetallecompra.getValueAt(fila, 0) != null) {
//                        modificar();
//                    } else {
//                        JOptionPane.showMessageDialog(null, "No ha seleccionado ningun dato");
//                    }
                    if (tabladetallecompra.getRowCount() == 0 && tabladetallecompra.getSelectedRow() == -1) {
                        JOptionPane.showInternalMessageDialog(null, "La tabla no contiene datos que modificar");
                    } else {
                        if (tabladetallecompra.getSelectedRow() == -1) {
                            JOptionPane.showInternalMessageDialog(null, "No se ha seleccionado un registro");
                        } else {
                            modificar();
                        }
                    }
                }
                if (key == java.awt.event.KeyEvent.VK_ENTER) {
//                    int fila = tabladetallecompra.getSelectedRow();
//                    if (tabladetallecompra.getValueAt(fila, 0) != null) {
//                        eliminar();
//                    } else {
//                        JOptionPane.showMessageDialog(null, "No ha seleccionado ningun dato");
//                    }
                    if (tabladetallecompra.getRowCount() == 0 && tabladetallecompra.getSelectedRow() == -1) {
                        JOptionPane.showInternalMessageDialog(null, "La tabla no contiene datos que eliminar");
                    } else {
                        if (tabladetallecompra.getSelectedRow() == -1) {
                            JOptionPane.showInternalMessageDialog(null, "No se ha seleccionado un registro");
                        } else {
                            eliminar();
                        }
                    }
                }

            }
        });

        tabladetallecompra.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                sumartotal();
                formatotabla();
            }
        });
    }

    private void cerrarVentana() {
        int nu = JOptionPane.showConfirmDialog(this, "¿Se perderan los datos que no haya guardado,Desea Cerrar esta ventana?", "Cerrar ventana", JOptionPane.YES_NO_OPTION);

        if (nu == JOptionPane.YES_OPTION || nu == 0) {
            this.dispose();
        } else {
        }
    }

    private void addEscapeKey() {
        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        Action escapeAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                //dispose();
                if (tabladetallecompra.getRowCount() <= 0) {
                    cerrarVentana();
                } else {
                    JOptionPane.showInternalMessageDialog(null, "No Puedes cerrar Tienes Compras Pendientes");
                }
            }
        };
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);
    }

    public void removejtable() {
        while (tabladetallecompra.getRowCount() != 0) {
            model.removeRow(0);
        }
    }

    public void removejtable2() {
        while (tablapreciosnuevos.getRowCount() != 0) {
            modelprecios.removeRow(0);
        }
    }

    public void limpiar() {
        totalcompra.setText("");
        factura.setText("");
        seriefactura.setText("");
        fechaactual.setDate(new Date());
        nombreproducto.setText("");
        cantidadP.setText("");
        preciocompra.setText("");
        codigo.setText("");
        llenarcombo();
        formatotabla();
    }

    public void activar() {
        totalcompra.setEnabled(true);
        factura.setEnabled(true);
        seriefactura.setEnabled(true);
        fechaactual.setEnabled(true);
        nombreproducto.setEnabled(true);
        cantidadP.setEnabled(true);
        preciocompra.setEnabled(true);
        codigo.setEnabled(true);
        ProveedorN.setEnabled(true);
    }

    public void setFiltroTexto() {
        Helper.setFiltraEntrada(nombreproducto.getDocument(), FiltraEntrada.NUM_LETRAS, 100, true);
        Helper.setFiltraEntrada(codigo.getDocument(), FiltraEntrada.NUM_LETRAS, 50, true);
        Helper.setFiltraEntrada(factura.getDocument(), FiltraEntrada.SOLO_NUMEROS, 10, true);
        //Helper.setFiltraEntrada(cantidadP.getDocument(), FiltraEntrada.SOLO_NUMEROS, 10, true);
        Helper.setFiltraEntrada(seriefactura.getDocument(), FiltraEntrada.NUM_LETRAS, 12, true);
    }

    public void desactivar() {
        totalcompra.setEnabled(false);
        factura.setEnabled(false);
        seriefactura.setEnabled(false);
        fechaactual.setEnabled(false);
        nombreproducto.setEnabled(false);
        cantidadP.setEnabled(false);
        preciocompra.setEnabled(false);
        codigo.setEnabled(false);
        ProveedorN.setEnabled(false);
    }

    private String getFecha() {

        try {
            String fecha;
            int años = fechaactual.getCalendar().get(Calendar.YEAR);
            int dias = fechaactual.getCalendar().get(Calendar.DAY_OF_MONTH);
            int mess = fechaactual.getCalendar().get(Calendar.MONTH) + 1;
//            int hours = fechaactual.getCalendar().get(Calendar.HOUR_OF_DAY);
//            int minutes = fechaactual.getCalendar().get(Calendar.MINUTE);
//            int seconds = fechaactual.getCalendar().get(Calendar.SECOND);

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
        for (int i = 0; i < 7; i++) {
            column = tabladetallecompra.getColumnModel().getColumn(i);
            if (i == 0 || i == 6) {
                column.setMaxWidth(0);
                column.setMinWidth(0);
                //column.setPreferredWidth(20); //Difine el ancho de la columna
                //tabladetallecompra.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);
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
        for (int i = 0; i < 5; i++) {
            column = tablapreciosnuevos.getColumnModel().getColumn(i);
//            if (i == 0 || i == 6) {
//                column.setMaxWidth(0);
//                column.setMinWidth(0);
//                //column.setPreferredWidth(20); //Difine el ancho de la columna
//                //tabladetallecompra.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);
//            } else
            if (i == 0) {
                column.setPreferredWidth(25); //Difine el ancho de la columna
                tablapreciosnuevos.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);
            } else if (i == 1) {
                column.setPreferredWidth(150); //Difine el ancho de la columna
            } else if (i > 1 & i <= 4) {
                column.setPreferredWidth(45);//Difine el ancho de la columna
                tablapreciosnuevos.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);
            }
        }
    }

    public void llenarcombo() {
        DefaultComboBoxModel value1, value2;
        value1 = new DefaultComboBoxModel();
        ProveedorN.setModel(value1);
        value2 = new DefaultComboBoxModel();
        formapago.setModel(value2);

        try {
            conn = BdConexion.getConexion();
            // Se crea un Statement, para realizar la consulta
            Statement s = (Statement) conn.createStatement();
            String sql = "select idProveedor,nombre from proveedor where idproveedor!='1' and estado='T'";

            // Se realiza la consulta. Los resultados se guardan en el 
            // ResultSet rs
            ResultSet rs = s.executeQuery(sql);

            // Se recorre el ResultSet, mostrando por pantalla los resultados.
            value1.addElement(new proveedor(" ", "0"));
            while (rs.next()) {
                value1.addElement(new proveedor(rs.getString("nombre"), "" + rs.getInt("idProveedor")));
            }

            // Se cierra la conexión con la base de datos.
            //conn.close();
        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(this,
                    "Cantidad no valida", "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.out.print("Error " + e);
        }

        try {
            conn = BdConexion.getConexion();
            Statement s = (Statement) conn.createStatement();
            ResultSet rs = s.executeQuery(sqlprod.COMBOTP);

            value2.addElement(new formadepago(" ", "0"));
            while (rs.next()) {
                //value2.addElement(new formadepago(rs.getString("descripcion"), "" + rs.getInt("idtipopago")));
                value2.addElement(new formadepago(rs.getString("descripcion"), rs.getInt("dias"), "" + rs.getInt("idtipopago")));
            }
            //conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurio un Error al cargar los datos\n" + ex.toString());
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

        cambio = new javax.swing.JDialog();
        jcMousePanel5 = new jcMousePanel.jcMousePanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        ps = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        Cantidad = new javax.swing.JFormattedTextField();
        precio = new javax.swing.JFormattedTextField();
        ingreso = new javax.swing.JDialog();
        jcMousePanel2 = new jcMousePanel.jcMousePanel();
        jLabel7 = new javax.swing.JLabel();
        Productos = new javax.swing.JTextField();
        cantidad = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        precios = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        PopupMenudetallecompra = new javax.swing.JPopupMenu();
        Modificarlinea = new javax.swing.JMenuItem();
        Eliminarlinea = new javax.swing.JMenuItem();
        preciosn = new javax.swing.JDialog();
        jcMousePanel3 = new jcMousePanel.jcMousePanel();
        combioprecios = new javax.swing.JScrollPane();
        tablapreciosnuevos = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        bntsalir1 = new elaprendiz.gui.button.ButtonRect();
        jLabel12 = new javax.swing.JLabel();
        jcMousePanelprincipal = new jcMousePanel.jcMousePanel();
        jPanel5 = new javax.swing.JPanel();
        jcMousePanel6 = new jcMousePanel.jcMousePanel();
        lblcodigo = new javax.swing.JLabel();
        codigo = new javax.swing.JTextField();
        lbldireccion = new javax.swing.JLabel();
        nombreproducto = new javax.swing.JTextField();
        lblproveedor = new javax.swing.JLabel();
        ProveedorN = new javax.swing.JComboBox();
        lblcantidadp = new javax.swing.JLabel();
        lblfecha = new javax.swing.JLabel();
        factura = new javax.swing.JTextField();
        lblfactura = new javax.swing.JLabel();
        lblprecio = new javax.swing.JLabel();
        preciocompra = new javax.swing.JTextField();
        bntSalir = new elaprendiz.gui.button.ButtonRect();
        fechaactual = new org.freixas.jcalendar.JCalendarCombo();
        btnbuscarproducto = new elaprendiz.gui.button.ButtonRect();
        seriefactura = new javax.swing.JTextField();
        lblfactura1 = new javax.swing.JLabel();
        cantidadP = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        formapago = new javax.swing.JComboBox();
        lbltotalcompra = new javax.swing.JLabel();
        totalcompra = new javax.swing.JLabel();
        idP = new javax.swing.JTextField();
        txtcod = new javax.swing.JTextField();
        btncrearproducto = new elaprendiz.gui.button.ButtonRect();
        jcMousePanel7 = new jcMousePanel.jcMousePanel();
        jScrollPanedetallecompra = new javax.swing.JScrollPane();
        tabladetallecompra = new javax.swing.JTable();
        pnlActionButtons = new javax.swing.JPanel();
        btnnuevo = new elaprendiz.gui.button.ButtonRect();
        btnmodificar = new elaprendiz.gui.button.ButtonRect();
        btngrabar = new elaprendiz.gui.button.ButtonRect();
        bnteliminarfila = new elaprendiz.gui.button.ButtonRect();
        btncancelarcompra = new elaprendiz.gui.button.ButtonRect();
        bntsalir = new elaprendiz.gui.button.ButtonRect();
        bntSalir2 = new elaprendiz.gui.button.ButtonRect();
        pnlPaginador = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        lblopciones = new javax.swing.JLabel();

        cambio.setBounds(new java.awt.Rectangle(220, 200, 270, 200));
        cambio.setFocusTraversalPolicyProvider(true);
        cambio.setMinimumSize(null);
        cambio.setUndecorated(true);

        jcMousePanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 3, true));
        jcMousePanel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fondo/índice.jpg"))); // NOI18N
        jcMousePanel5.setMaximumSize(null);
        jcMousePanel5.setLayout(null);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Cantidad");
        jcMousePanel5.add(jLabel2);
        jLabel2.setBounds(70, 50, 69, 30);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Precio Costo");
        jcMousePanel5.add(jLabel3);
        jLabel3.setBounds(40, 80, 98, 30);

        ps.setVisible(false);
        ps.setOpaque(false);
        jcMousePanel5.add(ps);
        ps.setBounds(20, 90, 10, 20);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("ESC para cancelar los cambios.");
        jcMousePanel5.add(jLabel1);
        jLabel1.setBounds(10, 20, 300, 20);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Intro para aplicar los cambios.");
        jcMousePanel5.add(jLabel5);
        jLabel5.setBounds(10, 5, 300, 20);

        Cantidad.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
        Cantidad.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        Cantidad.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Cantidad.setName("precioalmayor"); // NOI18N
        Cantidad.setPreferredSize(new java.awt.Dimension(80, 23));
        Cantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CantidadActionPerformed(evt);
            }
        });
        Cantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                CantidadKeyReleased(evt);
            }
        });
        jcMousePanel5.add(Cantidad);
        Cantidad.setBounds(160, 50, 142, 25);

        precio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
        precio.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        precio.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        precio.setName("precioalmayor"); // NOI18N
        precio.setPreferredSize(new java.awt.Dimension(80, 23));
        precio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                precioActionPerformed(evt);
            }
        });
        precio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                precioKeyReleased(evt);
            }
        });
        jcMousePanel5.add(precio);
        precio.setBounds(160, 80, 142, 25);

        javax.swing.GroupLayout cambioLayout = new javax.swing.GroupLayout(cambio.getContentPane());
        cambio.getContentPane().setLayout(cambioLayout);
        cambioLayout.setHorizontalGroup(
            cambioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cambioLayout.createSequentialGroup()
                .addComponent(jcMousePanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        cambioLayout.setVerticalGroup(
            cambioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cambioLayout.createSequentialGroup()
                .addComponent(jcMousePanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        ingreso.setBounds(new java.awt.Rectangle(200, 200, 200, 270));
        ingreso.setUndecorated(true);

        jcMousePanel2.setColor1(new java.awt.Color(51, 51, 51));
        jcMousePanel2.setColor2(new java.awt.Color(0, 51, 51));
        jcMousePanel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fondo/índice.jpg"))); // NOI18N
        jcMousePanel2.setModo(4);
        jcMousePanel2.setOpaque(false);

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Producto");

        Productos.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Productos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ProductosKeyReleased(evt);
            }
        });

        cantidad.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cantidadKeyReleased(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Cantidad");

        precios.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        precios.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                preciosKeyReleased(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Precio   Q");

        jButton3.setBackground(new java.awt.Color(255, 204, 0));
        jButton3.setText("Agregar");
        jButton3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jButton3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jButton3KeyReleased(evt);
            }
        });

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Esc Para Cancelar");

        jButton10.setBackground(new java.awt.Color(255, 204, 0));
        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/productos.png"))); // NOI18N
        jButton10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jcMousePanel2Layout = new javax.swing.GroupLayout(jcMousePanel2);
        jcMousePanel2.setLayout(jcMousePanel2Layout);
        jcMousePanel2Layout.setHorizontalGroup(
            jcMousePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jcMousePanel2Layout.createSequentialGroup()
                .addGroup(jcMousePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jcMousePanel2Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jcMousePanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jcMousePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(Productos, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(precios, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jcMousePanel2Layout.setVerticalGroup(
            jcMousePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jcMousePanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Productos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(precios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jcMousePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout ingresoLayout = new javax.swing.GroupLayout(ingreso.getContentPane());
        ingreso.getContentPane().setLayout(ingresoLayout);
        ingresoLayout.setHorizontalGroup(
            ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jcMousePanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ingresoLayout.setVerticalGroup(
            ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jcMousePanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        Modificarlinea.setText("Modificar Articulo");
        Modificarlinea.setToolTipText("");
        Modificarlinea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModificarlineaActionPerformed(evt);
            }
        });
        PopupMenudetallecompra.add(Modificarlinea);

        Eliminarlinea.setText("Eliminar Articulo");
        Eliminarlinea.setToolTipText("");
        Eliminarlinea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarlineaActionPerformed(evt);
            }
        });
        PopupMenudetallecompra.add(Eliminarlinea);

        preciosn.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        preciosn.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                preciosnWindowClosing(evt);
            }
        });

        jcMousePanel3.setColor1(new java.awt.Color(51, 51, 51));
        jcMousePanel3.setColor2(new java.awt.Color(0, 51, 51));
        jcMousePanel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fondo/índice.jpg"))); // NOI18N
        jcMousePanel3.setModo(4);
        jcMousePanel3.setOpaque(false);

        tablapreciosnuevos.setModel(modelprecios = new DefaultTableModel(null, titulos2)
            {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
            combioprecios.setViewportView(tablapreciosnuevos);

            jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            jLabel6.setForeground(new java.awt.Color(255, 255, 255));
            jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel6.setText("Nota: Considere cambiar los precios de Venta Para los Materiales de la Tabla.");

            jLabel11.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
            jLabel11.setForeground(new java.awt.Color(255, 255, 255));
            jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel11.setText("Productos con Margen de Ganancia menor al 10%");

            bntsalir1.setBackground(new java.awt.Color(51, 153, 255));
            bntsalir1.setText("Salir");
            bntsalir1.setToolTipText("ESC");
            bntsalir1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    bntsalir1ActionPerformed(evt);
                }
            });

            jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            jLabel12.setForeground(new java.awt.Color(255, 255, 255));
            jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel12.setText("Los productos de la tabla corresponden unicamente a los ingresados por la compra.");

            javax.swing.GroupLayout jcMousePanel3Layout = new javax.swing.GroupLayout(jcMousePanel3);
            jcMousePanel3.setLayout(jcMousePanel3Layout);
            jcMousePanel3Layout.setHorizontalGroup(
                jcMousePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jcMousePanel3Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bntsalir1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(302, 302, 302))
                .addGroup(jcMousePanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jcMousePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(combioprecios)
                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
            );
            jcMousePanel3Layout.setVerticalGroup(
                jcMousePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jcMousePanel3Layout.createSequentialGroup()
                    .addGap(8, 8, 8)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(combioprecios, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel6)
                    .addGap(2, 2, 2)
                    .addComponent(jLabel12)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(bntsalir1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(6, 6, 6))
            );

            javax.swing.GroupLayout preciosnLayout = new javax.swing.GroupLayout(preciosn.getContentPane());
            preciosn.getContentPane().setLayout(preciosnLayout);
            preciosnLayout.setHorizontalGroup(
                preciosnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 772, Short.MAX_VALUE)
                .addGroup(preciosnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcMousePanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            preciosnLayout.setVerticalGroup(
                preciosnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(preciosnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcMousePanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
            setMaximizable(true);
            setTitle("Ingresos");
            setToolTipText("");
            setName("compras"); // NOI18N
            addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
                public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
                }
                public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
                }
                public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                }
                public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
                }
                public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
                }
                public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                    formInternalFrameClosing(evt);
                }
                public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
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

            lblcodigo.setBackground(new java.awt.Color(0, 0, 0));
            lblcodigo.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
            lblcodigo.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
            lblcodigo.setText("Código:");
            lblcodigo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            lblcodigo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
            jcMousePanel6.add(lblcodigo);
            lblcodigo.setBounds(10, 45, 100, 30);

            codigo.setBackground(new java.awt.Color(204, 204, 204));
            codigo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            codigo.setEnabled(false);
            codigo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
            codigo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            codigo.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    codigoActionPerformed(evt);
                }
            });
            jcMousePanel6.add(codigo);
            codigo.setBounds(115, 45, 200, 25);

            lbldireccion.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
            lbldireccion.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
            lbldireccion.setText("Descripcion:");
            lbldireccion.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            jcMousePanel6.add(lbldireccion);
            lbldireccion.setBounds(10, 80, 100, 30);

            nombreproducto.setEditable(false);
            nombreproducto.setEditable(false);
            nombreproducto.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
            nombreproducto.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            jcMousePanel6.add(nombreproducto);
            nombreproducto.setBounds(115, 80, 420, 25);

            lblproveedor.setBackground(new java.awt.Color(0, 0, 0));
            lblproveedor.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
            lblproveedor.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
            lblproveedor.setText("Proveedor:");
            lblproveedor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            jcMousePanel6.add(lblproveedor);
            lblproveedor.setBounds(10, 10, 100, 30);

            ProveedorN.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
            ProveedorN.setEnabled(false);
            ProveedorN.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    ProveedorNActionPerformed(evt);
                }
            });
            jcMousePanel6.add(ProveedorN);
            ProveedorN.setBounds(115, 10, 330, 25);

            lblcantidadp.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
            lblcantidadp.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
            lblcantidadp.setText("Cantidad:");
            lblcantidadp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            jcMousePanel6.add(lblcantidadp);
            lblcantidadp.setBounds(10, 115, 100, 30);

            lblfecha.setBackground(new java.awt.Color(0, 0, 0));
            lblfecha.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
            lblfecha.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
            lblfecha.setText("Fecha: ");
            jcMousePanel6.add(lblfecha);
            lblfecha.setBounds(460, 10, 60, 25);

            factura.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
            jcMousePanel6.add(factura);
            factura.setBounds(410, 150, 125, 25);

            lblfactura.setBackground(new java.awt.Color(0, 0, 0));
            lblfactura.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
            lblfactura.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
            lblfactura.setText("No. Documento:");
            lblfactura.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            jcMousePanel6.add(lblfactura);
            lblfactura.setBounds(270, 150, 130, 30);

            lblprecio.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
            lblprecio.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
            lblprecio.setText("Precio Unitario:");
            jcMousePanel6.add(lblprecio);
            lblprecio.setBounds(272, 115, 128, 30);

            precios.setEditable(false);
            preciocompra.setEditable(false);
            preciocompra.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            preciocompra.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            preciocompra.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            jcMousePanel6.add(preciocompra);
            preciocompra.setBounds(410, 115, 125, 25);

            bntSalir.setBackground(new java.awt.Color(0, 153, 51));
            bntSalir.setText("...");
            bntSalir.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    bntSalirActionPerformed(evt);
                }
            });
            jcMousePanel6.add(bntSalir);
            bntSalir.setBounds(320, 45, 15, 25);

            fechaactual.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
            fechaactual.setName(""); // NOI18N
            jcMousePanel6.add(fechaactual);
            fechaactual.setBounds(520, 10, 200, 25);

            btnbuscarproducto.setBackground(new java.awt.Color(102, 204, 0));
            btnbuscarproducto.setText("Buscar Producto");
            btnbuscarproducto.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnbuscarproductoActionPerformed(evt);
                }
            });
            jcMousePanel6.add(btnbuscarproducto);
            btnbuscarproducto.setBounds(560, 150, 160, 25);

            seriefactura.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
            seriefactura.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    seriefacturaActionPerformed(evt);
                }
            });
            jcMousePanel6.add(seriefactura);
            seriefactura.setBounds(115, 150, 140, 25);

            lblfactura1.setBackground(new java.awt.Color(0, 0, 0));
            lblfactura1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
            lblfactura1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
            lblfactura1.setText("No. Serie:");
            lblfactura1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            jcMousePanel6.add(lblfactura1);
            lblfactura1.setBounds(10, 150, 100, 30);

            cantidadP.setEditable(false);
            cantidadP.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
            cantidadP.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
            cantidadP.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
            cantidadP.setName("precioalmayor"); // NOI18N
            cantidadP.setPreferredSize(new java.awt.Dimension(80, 23));
            cantidadP.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    cantidadPActionPerformed(evt);
                }
            });
            jcMousePanel6.add(cantidadP);
            cantidadP.setBounds(115, 115, 140, 23);

            jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
            jLabel4.setText("Forma Pago: ");
            jcMousePanel6.add(jLabel4);
            jLabel4.setBounds(420, 40, 100, 20);

            formapago.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
            formapago.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    formapagoActionPerformed(evt);
                }
            });
            jcMousePanel6.add(formapago);
            formapago.setBounds(520, 40, 200, 25);

            lbltotalcompra.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
            lbltotalcompra.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lbltotalcompra.setText("Total Compra:");

            totalcompra.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
            totalcompra.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            totalcompra.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
            totalcompra.setOpaque(true);

            idP.setVisible(false);

            txtcod.setVisible(false);

            btncrearproducto.setBackground(new java.awt.Color(102, 204, 0));
            btncrearproducto.setText("Crear Producto");
            btncancelarcompra.setEnabled(false);
            btncrearproducto.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btncrearproductoActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
            jPanel5.setLayout(jPanel5Layout);
            jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addComponent(jcMousePanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 724, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(totalcompra, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbltotalcompra, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btncrearproducto, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addGap(35, 35, 35)
                            .addComponent(txtcod, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(idP, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap())
            );
            jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtcod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(idP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbltotalcompra, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(totalcompra, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(37, 37, 37)
                    .addComponent(btncrearproducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
                .addComponent(jcMousePanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );

            jcMousePanelprincipal.add(jPanel5);
            jPanel5.setBounds(0, 40, 880, 190);

            jcMousePanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            jcMousePanel7.setModo(5);
            jcMousePanel7.setOpaque(false);

            jScrollPanedetallecompra.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

            tabladetallecompra.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
            tabladetallecompra.setModel(model = new DefaultTableModel(null, titulos)
                {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                });
                tabladetallecompra.setComponentPopupMenu(PopupMenudetallecompra);
                tabladetallecompra.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                tabladetallecompra.setGridColor(new java.awt.Color(51, 51, 255));
                tabladetallecompra.setName(""); // NOI18N
                tabladetallecompra.setRowHeight(24);
                jScrollPanedetallecompra.setViewportView(tabladetallecompra);

                javax.swing.GroupLayout jcMousePanel7Layout = new javax.swing.GroupLayout(jcMousePanel7);
                jcMousePanel7.setLayout(jcMousePanel7Layout);
                jcMousePanel7Layout.setHorizontalGroup(
                    jcMousePanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPanedetallecompra, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 876, Short.MAX_VALUE)
                );
                jcMousePanel7Layout.setVerticalGroup(
                    jcMousePanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPanedetallecompra, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                );

                jcMousePanelprincipal.add(jcMousePanel7);
                jcMousePanel7.setBounds(0, 250, 880, 190);

                pnlActionButtons.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 153, 255), 1, true));
                pnlActionButtons.setOpaque(false);
                pnlActionButtons.setPreferredSize(new java.awt.Dimension(786, 52));
                pnlActionButtons.setLayout(new java.awt.GridBagLayout());

                btnnuevo.setBackground(new java.awt.Color(51, 153, 255));
                btnnuevo.setMnemonic(KeyEvent.VK_N);
                btnnuevo.setText("Nuevo");
                btnnuevo.setToolTipText("ALT+N");
                btnnuevo.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        btnnuevoActionPerformed(evt);
                    }
                });
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                gridBagConstraints.insets = new java.awt.Insets(13, 84, 12, 0);
                pnlActionButtons.add(btnnuevo, gridBagConstraints);

                btnmodificar.setBackground(new java.awt.Color(51, 153, 255));
                btnmodificar.setMnemonic(KeyEvent.VK_M);
                btnmodificar.setText("Modificar");
                btnmodificar.setToolTipText("ALT+M");
                btnmodificar.setEnabled(false);
                btnmodificar.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        buttonRect6ActionPerformed(evt);
                    }
                });
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 1;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                gridBagConstraints.insets = new java.awt.Insets(13, 5, 12, 0);
                pnlActionButtons.add(btnmodificar, gridBagConstraints);

                btngrabar.setBackground(new java.awt.Color(51, 153, 255));
                btngrabar.setMnemonic(KeyEvent.VK_G);
                btngrabar.setText("Guardar");
                btngrabar.setToolTipText("ALT+G");
                btngrabar.setEnabled(false);
                btngrabar.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        btngrabarActionPerformed(evt);
                    }
                });
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 2;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                gridBagConstraints.insets = new java.awt.Insets(13, 5, 12, 0);
                pnlActionButtons.add(btngrabar, gridBagConstraints);

                bnteliminarfila.setBackground(new java.awt.Color(51, 153, 255));
                bnteliminarfila.setMnemonic(KeyEvent.VK_E);
                bnteliminarfila.setText("Eliminar");
                bnteliminarfila.setToolTipText("ALT+E");
                bnteliminarfila.setEnabled(false);
                bnteliminarfila.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        buttonRect1ActionPerformed(evt);
                    }
                });
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 3;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                gridBagConstraints.insets = new java.awt.Insets(13, 5, 12, 0);
                pnlActionButtons.add(bnteliminarfila, gridBagConstraints);

                btncancelarcompra.setBackground(new java.awt.Color(51, 153, 255));
                btncancelarcompra.setMnemonic(KeyEvent.VK_X);
                btncancelarcompra.setText("Cancelar");
                btncancelarcompra.setToolTipText("ALT+X");
                btncancelarcompra.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        btncancelarcompraActionPerformed(evt);
                    }
                });
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 4;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                gridBagConstraints.insets = new java.awt.Insets(13, 5, 12, 0);
                pnlActionButtons.add(btncancelarcompra, gridBagConstraints);

                bntsalir.setBackground(new java.awt.Color(51, 153, 255));
                bntsalir.setText("Salir");
                bntsalir.setToolTipText("ESC");
                bntsalir.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        bntsalirActionPerformed(evt);
                    }
                });
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 5;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                gridBagConstraints.insets = new java.awt.Insets(13, 5, 12, 93);
                pnlActionButtons.add(bntsalir, gridBagConstraints);

                bntSalir2.setBackground(new java.awt.Color(51, 153, 255));
                bntSalir2.setText("Salir");
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
                pnlActionButtons.setBounds(0, 440, 880, 50);

                pnlPaginador.setBackground(new java.awt.Color(0, 153, 204));
                pnlPaginador.setPreferredSize(new java.awt.Dimension(786, 40));
                pnlPaginador.setLayout(new java.awt.GridBagLayout());

                jLabel15.setFont(new java.awt.Font("Script MT Bold", 1, 32)); // NOI18N
                jLabel15.setForeground(new java.awt.Color(255, 255, 255));
                jLabel15.setText("<--Compras-->");
                pnlPaginador.add(jLabel15, new java.awt.GridBagConstraints());

                jcMousePanelprincipal.add(pnlPaginador);
                pnlPaginador.setBounds(0, 0, 880, 40);

                lblopciones.setForeground(new java.awt.Color(255, 255, 255));
                lblopciones.setText("Espacio=Modificar      Enter=Eliminar Articulos");
                jcMousePanelprincipal.add(lblopciones);
                lblopciones.setBounds(10, 234, 270, 20);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcMousePanelprincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 878, Short.MAX_VALUE)
                );
                layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcMousePanelprincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE)
                );

                pack();
            }// </editor-fold>//GEN-END:initComponents

    public void modificar() {

        Float ca, pr;
        int fila = tabladetallecompra.getSelectedRow();
        ca = Float.parseFloat(Validar(tabladetallecompra.getValueAt(fila, 3).toString()));
        pr = Float.parseFloat(Validar(tabladetallecompra.getValueAt(fila, 4).toString()));
        ps.setText("" + tabladetallecompra.getSelectedRow());
        if (tabladetallecompra.getSelectedColumn() == 6) {
        } else {

            cambio.setVisible(true);
            Cantidad.setValue(ca);
            precio.setValue(pr);
            cambio.setSize(319, 115);
            cambio.setResizable(false);
            Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension ventana = cambio.getSize();
            cambio.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 2);
            cambio.toFront();//aparece al frente
            sumartotal();
        }
    }
    private void ProveedorNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProveedorNActionPerformed
        // TODO add your handling code here:
//       busqueda();

    }//GEN-LAST:event_ProveedorNActionPerformed
    public void cambiar() {

        int p = Integer.parseInt(ps.getText());

        float Cant = Float.parseFloat(Validar(Cantidad.getText()));
        Cant = (float) (Math.round((Cant) * 100.0) / 100.0);
        float nprecios = Float.parseFloat(Validar(precio.getText()));
        nprecios = (float) (Math.round((nprecios) * 100.0) / 100.0);
        float suma, Actual, Resultado = 0, cant2;
        suma = (float) (Math.round((Cant * nprecios) * 100.0) / 100.0);

        if ((Cant > 0) && (nprecios > 0)) {
            model.setValueAt(Cant, p, 3);
            model.setValueAt(nprecios, p, 4);
            model.setValueAt(suma, p, 5);

            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 6).toString().equals("true")) {
                    Actual = Float.parseFloat(model.getValueAt(i, 5).toString());
                    Resultado = Resultado + Actual;
                }
            }
            totalcompra.setText("" + Math.round(Resultado * 100.0) / 100.0);
        }
    }
    private void ProductosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProductosKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == 27) {
            ingreso.setVisible(false);
            ingreso.toFront();//aparece al frente
        }
    }//GEN-LAST:event_ProductosKeyReleased

    private void cantidadKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cantidadKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == 27) {
            ingreso.setVisible(false);
            ingreso.toFront();//aparece al frente
        }

    }//GEN-LAST:event_cantidadKeyReleased

    private void preciosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_preciosKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == 27) {
            ingreso.setVisible(false);
            ingreso.toFront();//aparece al frente
        }
    }//GEN-LAST:event_preciosKeyReleased

    private void jButton3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton3KeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == 27) {
            ingreso.setVisible(false);
            ingreso.toFront();//aparece al frente
        }
    }//GEN-LAST:event_jButton3KeyReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        float multi;
        //DefaultTableModel tempe = (DefaultTableModel) tablapedido.getModel();
        multi = Float.parseFloat(cantidad.getText()) * Float.parseFloat(precios.getText());

        Object nuevo[] = {"1", "S/N", Productos.getText(), Float.parseFloat(cantidad.getText()), Float.parseFloat(precios.getText()), multi, false};
        model.addRow(nuevo);
        Productos.setText("");
        cantidad.setText("");
        precios.setText("");

        ingreso.setVisible(false);
        ingreso.toFront();//aparece al frente
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // ALS add your handling code here:
        if (ProveedorN.getSelectedIndex() <= 0 || formapago.getSelectedIndex() <= 0) {
            JOptionPane.showInternalMessageDialog(this, "Selecciona Proveedor y Foprama de Pago", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            Busquedadeproducto bprod = new Busquedadeproducto();
            ingreso.setVisible(false);
            ingreso.toFront();//aparece al frente
            bprod.setVisible(true);
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void codigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codigoActionPerformed
        // TODO add your handling code here:
        if (codigo.getText().equals("")) {
            JOptionPane.showInternalMessageDialog(this, "No hay datos Para la Busqueda", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
//                if (ProveedorN.getSelectedIndex() <= 0 || formapago.getSelectedIndex() <= 0 || factura.getText().equals("")) {
//                    JOptionPane.showInternalMessageDialog(this, "Selecciona Proveedor, Foprama de Pago y No.Documento", "Error", JOptionPane.ERROR_MESSAGE);
//                } else {
                    conn = BdConexion.getConexion();
                    Statement s = (Statement) conn.createStatement();
                    String sql = "select producto.preciocompra,producto.codigo,producto.idProducto,producto.nombre from producto where codigo ='" + codigo.getText() + "'";
                    ResultSet rs = s.executeQuery(sql);
                    if (rs.next()) {
                        idP.setText("" + rs.getInt("producto.idproducto"));
                        nombreproducto.setText("" + rs.getString("producto.nombre"));
                        preciocompra.setText("" + rs.getString("producto.preciocompra"));
                        txtcod.setText("" + rs.getString("producto.codigo"));
                        cantidadP.setEditable(true);
                        cantidadP.requestFocus();
                        cantidadP.selectAll();
                    } else {
                        JOptionPane.showInternalMessageDialog(this, "No hay Articulo con ese Código");
                    }

                //}
                //conn.close();
            } catch (HeadlessException | SQLException ex) {

                Logger.getLogger(compras.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }//GEN-LAST:event_codigoActionPerformed
    public void borrar() {
        codigo.setText("");
        //cantidadP.setText("");
        cantidadP.setValue(null);
        nombreproducto.setText("");
        txtcod.setText("");
        idP.setText("");
        preciocompra.setText("");
        cantidadP.setEnabled(true);

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

    private void bntSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntSalirActionPerformed
        buscar();
    }//GEN-LAST:event_bntSalirActionPerformed

    public void buscar() {

//        if ((ProveedorN.getSelectedIndex() <= 0) || factura.getText().equals("") || formapago.getSelectedIndex() <= 0) {
//            JOptionPane.showInternalMessageDialog(this, "Seleccione Proveedor, Forma de pago y No.Documento", "Error", JOptionPane.ERROR_MESSAGE);
//        } else {

            //Cliente newfrm = new Cliente();
            Busquedadeproducto newfrm = new Busquedadeproducto();
            if (newfrm == null) {
                newfrm = new Busquedadeproducto();
            }
            AddForms.adminInternalFrame(panel_center, newfrm);

//            Busquedadeproducto nuevasol = new Busquedadeproducto();
//            if (panel_center.getComponentCount() > 0 & panel_center.getComponentCount() < 2) //solo uno en t
//            {
//                panel_center.add(nuevasol);
//                nuevasol.show();// ver interno
//                nuevasol.setClosable(true);// icono de cerrar
//                nuevasol.toFront();//aparece al frente
//            }
       // }
    }
    private void btngrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btngrabarActionPerformed
        // TODO add your handling code here:
        //GregorianCalendar calendario = new GregorianCalendar();
        if (tabladetallecompra.getRowCount() == 0 && tabladetallecompra.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "La tabla no contiene productos que guardar");
        } else {

            String fecha;
            fecha = getFecha();
            //String status;
            int resp;
            resp = JOptionPane.showConfirmDialog(null, "¿Desea Guardar el Ingreso con fecha = " + fecha + "?", "Pregunta", 0);

            if (resp == 0) {
                try {
                    fechaactual.setEditable(false);

                    Statement s = (Statement) conn.createStatement();

                    try {

                        proveedor proveedor = (proveedor) ProveedorN.getSelectedItem();
                        String idPr = proveedor.getID(), proveedores = proveedor.toString();

                        formadepago formadepago = (formadepago) formapago.getSelectedItem();
                        String idTP = formadepago.getID();
                        int diaspago = formadepago.todia();
                        conn = BdConexion.getConexion();
                        //Statement s = (Statement) conn.createStatement();

                        String sql;
                        int ultimoid = 0;

                        if ((ProveedorN.getSelectedIndex() <= 0) || formapago.getSelectedIndex() <= 0) {
                            JOptionPane.showInternalMessageDialog(this, "Seleccione Proveedor, Forma de pago", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        } else {
                            Calendar c1 = fechaactual.getCalendar();
                            SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");

                            c1.add(Calendar.DATE, diaspago);
                            String fechapago = (sdf.format(c1.getTime()));
                            String fechapago2 = null;

                            char sta = 'T';
                            String saldo = "";
                            if (formapago.getSelectedItem().toString().equals("CONTADO")) {
                                sta = 'F';
                                saldo = "0";
                                fechapago2 = getFecha();
                            }
                            if (!formapago.getSelectedItem().toString().equals("CONTADO")) {
                                sta = 'T';
                                saldo = (totalcompra.getText());
                                fechapago2 = fechapago;
                            }

                            //AccesoUsuario entrar = new AccesoUsuario();
                            sql = "insert into compra" + "(numdoc,proveedor_idproveedor,idtipopago,status,fecha,fechapago,total,saldo,usuario_idusuario,nserie)values" + "('" + factura.getText() + "','" + idPr + "','" + idTP + "','" + sta + "','" + fecha + "','" + fechapago2 + "','" + totalcompra.getText() + "','" + saldo + "','" + AccesoUsuario.idusu() + "','" + seriefactura.getText() + "')";

                            // Se crea un Statement, para realizar la consulta
                            //Statement s = (Statement) conn.createStatement();
                            //              s.executeUpdate(sql);
                            conn.setAutoCommit(false);
                            //s.executeUpdate("LOCK TABLES producto WRITE, lote WRITE, compra, WRITE;");

                            s.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                            ResultSet rs = s.getGeneratedKeys();
                            if (rs != null && rs.next()) {
                                ultimoid = rs.getInt(1);
                            }
                            String sql2;
                            float cant = 0, suma, actual, preciovent = 0;
                            float ca = 0, nc, prom;
                            //double prom2;
                            String mensage = "";
                            String[] reporteprod;//=new String[100];
                            Object[] fila = new Object[5];
                            boolean camprec = false;
                            for (int i = 0; i < model.getRowCount(); i++) {
                                if (model.getValueAt(i, 6).toString().equals("true")) {

                                    sql2 = "select cantidad,PrecioCompra,precioventa from  producto where idProducto='" + model.getValueAt(i, 0) + "'";
                                    ResultSet rss = s.executeQuery(sql2);
                                    while (rss.next()) {
                                        // cantidad y precios actuales
                                        cant = rss.getFloat("cantidad");
                                        preciovent = rss.getFloat("Precioventa");
                                    }
                                    //cantidad y precios nuevos
                                    actual = Float.parseFloat(model.getValueAt(i, 3).toString());
                                    //nc = Float.parseFloat(model.getValueAt(i, 4).toString());
                                    suma = cant + actual;
                                    suma = (float) (Math.round((suma) * 100.0) / 100.0);
                                    //prom = ((cant * ca) + (actual * nc)) / suma;
                                    //prom2 = (Math.round(prom * 100.0) / 100.0);
                                    float precc;
                                    precc = Float.parseFloat(model.getValueAt(i, 4).toString());
                                    precc = (float) (Math.round(precc * 100.0) / 100.0);

                                    if (precc > preciovent) {
                                        mensage = mensage + "El precio de compra del material codigo: " + model.getValueAt(i, 1).toString() + "  es mayor al precio de Venta\n";
                                    }
                                    if (precc >= (preciovent - (preciovent * 0.07)) & precc <= preciovent) {
                                        mensage = mensage + "El precio de compra del material codigo: " + model.getValueAt(i, 1).toString() + "  es muy cercano al precio de Venta\n";
                                    }
                                    if ((precc + (precc * 0.10)) >= preciovent) {
                                        camprec = true;
                                        fila[0] = model.getValueAt(i, 1).toString();
                                        fila[1] = model.getValueAt(i, 2).toString();
                                        fila[2] = preciovent;
                                        fila[3] = precc;
                                        fila[4] = (float) (Math.round((precc + (precc * 0.10)) * 100.0) / 100.0); 
                                    }

                                    sql = "insert into lote" + "(cantidad,precio,fecha,producto_idProducto,compra_idcompra,stock,tipoentrada)values" + "('" + model.getValueAt(i, 3).toString() + "','" + model.getValueAt(i, 4).toString() + "','" + fecha + "','" + model.getValueAt(i, 0).toString() + "','" + ultimoid + "','" + model.getValueAt(i, 3).toString() + "','" + "COMPRA" + "')";
                                    s.executeUpdate(sql);

//                                sql = "insert into detallecompra" + "(cantidad,precio,compra_idcompra,producto_idProducto)values" + "('" + model.getValueAt(i, 3) + "','" + model.getValueAt(i, 4) + "','" + ultimoid + "','" + model.getValueAt(i, 0) + "')";
//                                s.executeUpdate(sql);
                                    //sql2 = "UPDATE producto SET cantidad='" + suma + "',preciocompra='" + prom2 + "',precioventa='" + prom2 + "' WHERE idProducto =" + model.getValueAt(i, 0).toString();
                                    sql2 = "UPDATE producto SET cantidad='" + suma + "' WHERE idProducto =" + model.getValueAt(i, 0).toString();
                                    s.executeUpdate(sql2);
                                }
                                modelprecios.addRow(fila);
                            }

                            for (int h = model.getRowCount(); h > 0; h--) {
                                model.removeRow(h - 1);
                            }
                            //conn.close();
                            limpiar();
                            desactivar();
                            btnnuevo.setEnabled(true);
                            btngrabar.setEnabled(false);
                            btncancelarcompra.setEnabled(false);
                            btnmodificar.setEnabled(false);
                            bnteliminarfila.setEnabled(false);
                            JOptionPane.showMessageDialog(null, "Compra Guardada correctamente");
                            if (!mensage.isEmpty()) {
                                JOptionPane.showMessageDialog(null, (mensage + " Considere cambiar los precios de Venta\n"));
                            }
                            if (camprec == true) {
                                formatotabla2();
                                preciosn.setVisible(true);
                                preciosn.setSize(772, 346);
                                preciosn.toFront();
                            }
                        }
                        //s.executeUpdate("UNLOCK TABLES;");
                        conn.commit();
                        s.close();
                        if (!conn.getAutoCommit()) {
                            conn.setAutoCommit(true);
                        }
                        //conn.close();
                    } catch (HeadlessException | NumberFormatException | SQLException ex) {
                        Logger.getLogger(compras.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null, "Error " + ex);
                        //s.executeUpdate("UNLOCK TABLES;");
                        conn.rollback();
                        s.close();
                        if (!conn.getAutoCommit()) {
                            conn.setAutoCommit(true);
                        }
                    }
                }//fin de if fecha
                catch (SQLException ex) {
                    Logger.getLogger(compras.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, "Error " + ex);
                }
            }
        }
    }//GEN-LAST:event_btngrabarActionPerformed

    private void btncancelarcompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelarcompraActionPerformed
        // TODO add your handling code here:
        desactivar();
        btnnuevo.setEnabled(true);
        btngrabar.setEnabled(false);
        btncancelarcompra.setEnabled(false);
        btnmodificar.setEnabled(false);
        bnteliminarfila.setEnabled(false);
        removejtable();
        limpiar();
    }//GEN-LAST:event_btncancelarcompraActionPerformed

    private void btnbuscarproductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarproductoActionPerformed
        // TODO add your handling code here:
//        if ((ProveedorN.getSelectedIndex() <= 0) || factura.getText().equals("") || formapago.getSelectedIndex() <= 0) {
//            JOptionPane.showInternalMessageDialog(this, "Seleccione Proveedor, Forma de pago y No.Documento", "Error", JOptionPane.ERROR_MESSAGE);
//        } else {
            
            Busquedadeproducto newfrm = new Busquedadeproducto();
            if (newfrm == null) {
                newfrm = new Busquedadeproducto();
            }
            AddForms.adminInternalFrame(panel_center, newfrm);
            
//            Busquedadeproducto nuevasol = new Busquedadeproducto();
//            if (panel_center.getComponentCount() > 0 & panel_center.getComponentCount() < 2) //solo uno en t
//            {
//
//                Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
//                Dimension ventana = nuevasol.getSize();
//                nuevasol.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 10);
//                panel_center.add(nuevasol);
//                nuevasol.show();// ver interno
//                nuevasol.setClosable(true);// icono de cerrar
//                nuevasol.toFront();//aparece al frente
//            }
      //  }
    }//GEN-LAST:event_btnbuscarproductoActionPerformed

    private void buttonRect6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRect6ActionPerformed
        // TODO add your handling code here:
        if (tabladetallecompra.getRowCount() == 0 && tabladetallecompra.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "La tabla no contiene datos que modificar");
        } else {
            if (tabladetallecompra.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(null, "No se ha seleccionado un registro");
            } else {
                modificar();
            }
        }

    }//GEN-LAST:event_buttonRect6ActionPerformed
    public void sumartotal() {
        //corregir cuando hay solo unalinea da error
        if (tabladetallecompra.getRowCount() == 0 && tabladetallecompra.getSelectedRow() == -1) {
            //JOptionPane.showMessageDialog(null, "La tabla no contiene datos que modificar");
            totalcompra.setText("0.00");
        } else {
            float Actual, Resultado = 0;

            for (int i = 0; i < model.getRowCount(); i++) {

                if (tabladetallecompra.getValueAt(i, 6).toString().equals("true")) {
                    Actual = Float.parseFloat(tabladetallecompra.getValueAt(i, 5).toString());
                    Resultado = Resultado + Actual;
                }
                totalcompra.setText("" + Math.round(Resultado * 100.0) / 100.0);
            }
        }
    }
    private void buttonRect1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRect1ActionPerformed
        // TODO add your handling code here:
        if (tabladetallecompra.getRowCount() == 0 && tabladetallecompra.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "La tabla no contiene datos que eliminar");
        } else {
            if (tabladetallecompra.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(null, "No se ha seleccionado un registro");
            } else {
                eliminar();
            }
        }
    }//GEN-LAST:event_buttonRect1ActionPerformed

    private void btncrearproductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncrearproductoActionPerformed
        // TODO add your handling code here:
        
        Producto newfrm = new Producto();
        if (newfrm == null) {
            newfrm = new Producto();
        }
        AddForms.adminInternalFrame(panel_center, newfrm);
        
//        Producto nuevasol = new Producto();
//        
//        if (panel_center.getComponentCount() > 0 & panel_center.getComponentCount() < 3) //solo uno en t
//        {
//            panel_center.add(nuevasol);
//            nuevasol.show();// ver interno
//            nuevasol.setClosable(true);// icono de cerrar
//            nuevasol.toFront();//aparece al frente
//        }
    }//GEN-LAST:event_btncrearproductoActionPerformed

    private void bntsalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntsalirActionPerformed
        cerrarVentana();
    }//GEN-LAST:event_bntsalirActionPerformed

    private void btnnuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnuevoActionPerformed
        // TODO add your handling code here:
        limpiar();
        activar();
        btnmodificar.setEnabled(true);
        btnnuevo.setEnabled(false);
        btncancelarcompra.setEnabled(true);
        btngrabar.setEnabled(true);
        bnteliminarfila.setEnabled(true);
        codigo.setBackground(new java.awt.Color(255, 255, 255));
        ProveedorN.setBackground(new java.awt.Color(255, 255, 255));
        llenarcombo();
    }//GEN-LAST:event_btnnuevoActionPerformed

    private void bntSalir2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntSalir2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bntSalir2ActionPerformed

    private void ModificarlineaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModificarlineaActionPerformed
        // TODO add your handling code here:
        if (tabladetallecompra.getRowCount() == 0 && tabladetallecompra.getSelectedRow() == -1) {
            JOptionPane.showInternalMessageDialog(null, "La tabla no contiene datos que modificar");
        } else {
            if (tabladetallecompra.getSelectedRow() == -1) {
                JOptionPane.showInternalMessageDialog(null, "No se ha seleccionado un registro");
            } else {
                modificar();
            }
        }
    }//GEN-LAST:event_ModificarlineaActionPerformed

    private void EliminarlineaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarlineaActionPerformed
        // TODO add your handling code here:
        if (tabladetallecompra.getRowCount() == 0 && tabladetallecompra.getSelectedRow() == -1) {
            JOptionPane.showInternalMessageDialog(null, "La tabla no contiene datos que eliminar");
        } else {
            if (tabladetallecompra.getSelectedRow() == -1) {
                JOptionPane.showInternalMessageDialog(null, "No se ha seleccionado un registro");
            } else {
                eliminar();
            }
        }
    }//GEN-LAST:event_EliminarlineaActionPerformed

    private void cantidadPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cantidadPActionPerformed
        // TODO add your handling code here:

        if ((cantidadP.getText().equals("")) || (cantidadP.getValue().equals(null)) || cantidadP.getValue().equals(0) || (cantidadP.getText().equals(""))) {
            JOptionPane.showInternalMessageDialog(this, "Cantidad no valida", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            float ncantidad;
            try {
                ncantidad = Float.parseFloat(cantidadP.getText());
                ncantidad = (float) (Math.round(ncantidad * 100.0) / 100.0);
                if (ncantidad > 0) {
                    float subtotal, multi, Actual, Resultado = 0;
                    subtotal = Float.parseFloat(preciocompra.getText());
                    subtotal = (float) (Math.round(subtotal * 100.0) / 100.0);
                    multi = (float) (Math.round((ncantidad * subtotal) * 100.0) / 100.0);

                    Object nuevo[] = {idP.getText(), txtcod.getText(), nombreproducto.getText(), ncantidad, subtotal, multi, new Boolean(true).toString()};
                    model.addRow(nuevo);
                    for (int i = 0; i < model.getRowCount(); i++) {
                        if (model.getValueAt(i, 6).toString().equals("true")) {
                            Actual = Float.parseFloat(model.getValueAt(i, 5).toString());
                            Resultado = Resultado + Actual;
                        }
                        //cantidadP.setText("0");
                        totalcompra.setText("" + Math.round(Resultado * 100.0) / 100.0);
                        cantidadP.setEditable(false);
                        //cantidadP.setText("");
                        borrar();
                        //ProveedorN.setEnabled(false);
                        codigo.requestFocus();
                    }
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showInternalMessageDialog(this,
                        "Cantidad no valida", "Error",
                        JOptionPane.ERROR_MESSAGE);
                System.out.print("Error " + ex);
            }
        }
    }//GEN-LAST:event_cantidadPActionPerformed

    private void formapagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_formapagoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_formapagoActionPerformed

    private void CantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CantidadActionPerformed
        // TODO add your handling code here:
        cambiar();
        sumartotal();
        cambio.setVisible(false);
    }//GEN-LAST:event_CantidadActionPerformed

    private void CantidadKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CantidadKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == 27) {
            cambio.setVisible(false);
        }
    }//GEN-LAST:event_CantidadKeyReleased

    private void precioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_precioActionPerformed
        // TODO add your handling code here:
        cambiar();
        sumartotal();
        cambio.setVisible(false);
    }//GEN-LAST:event_precioActionPerformed

    private void precioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_precioKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == 27) {
            cambio.setVisible(false);
        }
    }//GEN-LAST:event_precioKeyReleased

    private void seriefacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seriefacturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_seriefacturaActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        cerrarVentana();
    }//GEN-LAST:event_formInternalFrameClosing

    private void bntsalir1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntsalir1ActionPerformed
        // TODO add your handling code here:
        removejtable2();
        this.preciosn.dispose();
    }//GEN-LAST:event_bntsalir1ActionPerformed

    private void preciosnWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_preciosnWindowClosing
        // TODO add your handling code here:
        removejtable2();
        this.preciosn.dispose();
    }//GEN-LAST:event_preciosnWindowClosing

    public void eliminar() {
        int resp;
        resp = JOptionPane.showConfirmDialog(null, "¿Deseas eliminar este artículo?", "Pregunta", 0);

        if (resp == 0) {
            int p;
            p = tabladetallecompra.getSelectedRow();
            model.removeRow(p);
            sumartotal();
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField Cantidad;
    private javax.swing.JMenuItem Eliminarlinea;
    private javax.swing.JMenuItem Modificarlinea;
    private javax.swing.JPopupMenu PopupMenudetallecompra;
    private javax.swing.JTextField Productos;
    private javax.swing.JComboBox ProveedorN;
    private elaprendiz.gui.button.ButtonRect bntSalir;
    private elaprendiz.gui.button.ButtonRect bntSalir2;
    private elaprendiz.gui.button.ButtonRect bnteliminarfila;
    private elaprendiz.gui.button.ButtonRect bntsalir;
    private elaprendiz.gui.button.ButtonRect bntsalir1;
    private elaprendiz.gui.button.ButtonRect btnbuscarproducto;
    private elaprendiz.gui.button.ButtonRect btncancelarcompra;
    private elaprendiz.gui.button.ButtonRect btncrearproducto;
    private elaprendiz.gui.button.ButtonRect btngrabar;
    private elaprendiz.gui.button.ButtonRect btnmodificar;
    private elaprendiz.gui.button.ButtonRect btnnuevo;
    private javax.swing.JDialog cambio;
    private javax.swing.JTextField cantidad;
    private javax.swing.JFormattedTextField cantidadP;
    private javax.swing.JTextField codigo;
    private javax.swing.JScrollPane combioprecios;
    private javax.swing.JTextField factura;
    private org.freixas.jcalendar.JCalendarCombo fechaactual;
    private javax.swing.JComboBox formapago;
    private javax.swing.JTextField idP;
    private javax.swing.JDialog ingreso;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPanedetallecompra;
    private jcMousePanel.jcMousePanel jcMousePanel2;
    private jcMousePanel.jcMousePanel jcMousePanel3;
    private jcMousePanel.jcMousePanel jcMousePanel5;
    private jcMousePanel.jcMousePanel jcMousePanel6;
    private jcMousePanel.jcMousePanel jcMousePanel7;
    private jcMousePanel.jcMousePanel jcMousePanelprincipal;
    private javax.swing.JLabel lblcantidadp;
    private javax.swing.JLabel lblcodigo;
    private javax.swing.JLabel lbldireccion;
    private javax.swing.JLabel lblfactura;
    private javax.swing.JLabel lblfactura1;
    private javax.swing.JLabel lblfecha;
    private javax.swing.JLabel lblopciones;
    private javax.swing.JLabel lblprecio;
    private javax.swing.JLabel lblproveedor;
    private javax.swing.JLabel lbltotalcompra;
    private javax.swing.JTextField nombreproducto;
    private javax.swing.JPanel pnlActionButtons;
    private javax.swing.JPanel pnlPaginador;
    private javax.swing.JFormattedTextField precio;
    private javax.swing.JTextField preciocompra;
    private javax.swing.JTextField precios;
    private javax.swing.JDialog preciosn;
    private javax.swing.JTextField ps;
    private javax.swing.JTextField seriefactura;
    public static javax.swing.JTable tabladetallecompra;
    private javax.swing.JTable tablapreciosnuevos;
    private javax.swing.JLabel totalcompra;
    private javax.swing.JTextField txtcod;
    // End of variables declaration//GEN-END:variables
}
