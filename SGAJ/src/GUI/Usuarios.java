/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.https://www.youtube.com/watch?v=ICF-RldvSIo
 */
package GUI;

import BD.BdConexion;
//import BD.LeePropiedades;
import BD.sqlus;
import excepciones.FiltraEntrada;
import excepciones.Helper;
import excepciones.VerificadorEntrada;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
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
public class Usuarios extends javax.swing.JInternalFrame {

    private String archivoRecurso = "controlador-bd";
    DefaultTableModel model;
    //Connection conn;//getConnection intentara establecer una conexión.
    java.sql.Connection conn;//getConnection intentara establecer una conexión.
    Statement sent;
    String[] titulos = {"Id", "Nombre del Usuario", "Correo", "Usuario", "Password", "Puesto", "Fecha", "Estado"};
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Creates new form Usuario
     */
    public Usuarios() {
        initComponents();
        Desabilitar();
        Llenar();
        setFiltroTexto();
        addEscapeKey();

        usuarios.addKeyListener(new java.awt.event.KeyAdapter() {
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
        } else {
        }
    }

    public void removejtable() {
        while (usuarios.getRowCount() != 0) {
            model.removeRow(0);
        }
    }

    private void Desabilitar() {
        nombre.setEditable(false);
        puesto.setEditable(false);
        correo.setEditable(false);
        usuario.setEditable(false);
        password.setEditable(false);
        rbEstado.setSelected(false);
        rbEstado.setEnabled(false);
        dcFecha.setEnabled(false);
    }

    private void Limpiar() {
        nombre.setText("");
        puesto.setText("");
        correo.setText("");
        usuario.setText("");
        password.setText("");
        //txtDato.setText("");
        rbEstado.setSelected(false);
        rbEstado.setEnabled(false);
        rbEstado.setBackground(new java.awt.Color(51, 153, 255));
        dcFecha.setDate(Calendar.getInstance().getTime());
    }

    private void Habilitar() {
        nombre.setEditable(true);
        puesto.setEditable(true);
        correo.setEditable(true);
        usuario.setEditable(true);
        password.setEditable(true);
        //nombre.requestFocus();
        rbEstado.setSelected(false);
        rbEstado.setEnabled(true);
        dcFecha.setEnabled(true);
    }

    public void setFiltroTexto() {
        Helper.setFiltraEntrada(nombre.getDocument(), FiltraEntrada.NUM_LETRAS, 150, true);
        Helper.setFiltraEntrada(puesto.getDocument(), FiltraEntrada.NUM_LETRAS, 50, true);
        Helper.setFiltraEntrada(usuario.getDocument(), FiltraEntrada.DEFAULT, 45, true);
        Helper.setFiltraEntrada(password.getDocument(), FiltraEntrada.NUM_LETRAS, 45, true);
        Helper.setFiltraEntrada(txtDato.getDocument(), FiltraEntrada.DEFAULT, 150, true);
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

    private void formatotabla() {
        //TableCellRenderer clase que se encarga de dibujar los datos que hay en cada celda la cual podemos modificar
        //nos proporciona la posibilidad de cambiar su aspercto por uno personalizado y no el standar.
        DefaultTableCellRenderer modelocentrar = new DefaultTableCellRenderer();
        modelocentrar.setHorizontalAlignment(SwingConstants.CENTER);

        //TableColumn representa todos los atributos de una columna en un JTable , como el ancho, resizibility, mínimo y máximo ancho
        //en este caso defien el ancho de cada columna las cuales pueden ser de distinto ancho.
        TableColumn column;// = null;
        for (int i = 0; i < 7; i++) {
            column = usuarios.getColumnModel().getColumn(i);
            if (i == 1) {
                column.setPreferredWidth(200); //Difine el ancho de la columna
            } else if (i == 0) {
                column.setPreferredWidth(5); //Difine el ancho de la columna
                usuarios.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);//Para centrar los datos en las columnas
            } else if (i == 2) {
                column.setPreferredWidth(200);//Difine el ancho de la columna
                usuarios.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);//Para centrar los datos en las columnas
            } else if (i == 3 || i == 4) {
                column.setPreferredWidth(100); //Difine el ancho de la columna
                usuarios.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);//Para centrar los datos en las columnas
            } else if (i == 5 || i == 6) {
                column.setPreferredWidth(50);//Difine el ancho de la columna
                usuarios.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);//Para centrar los datos en las columnas
            }

        }

    }

    private void Llenar() {
        try {
            conn = BdConexion.getConexion();
            removejtable();
            //model = new DefaultTableModel(null, titulos);//objeto TableModel para proporcionar los datos del objeto ResultSet al objeto JTable
            sent = conn.createStatement();// crea objeto Statement para consultar la base de datos
            ResultSet rs = sent.executeQuery(/*sql*/sqlus.LLENAR + sqlus.ORDER_BY);// especifica la consulta y la ejecuta
            String[] fila = new String[8];
            while (rs.next()) {

                fila[0] = rs.getString("idusuario");
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getString("apellido");
                fila[3] = rs.getString("nombreusuario");
                fila[4] = rs.getString("contrasenia");
                fila[5] = rs.getString("puesto");
                fila[6] = sdf.format(rs.getDate("fecha"));
                if (rs.getString("Estado").equals("T")) {
                    fila[7] = "Activo";
                } else {
                    fila[7] = "Inactivo";
                }

                model.addRow(fila);
            }
            usuarios.setModel(model);
            formatotabla();
            this.bntGuardar.setEnabled(false);
            this.bntModificar.setEnabled(false);
            this.bntEliminar.setEnabled(false);
            this.bntNuevo.setEnabled(true);
            //conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en BD " + e.toString());
            //this.dispose();
        }
    }

    private void MostrarTodo(String Dato) {
        try {
            conn = BdConexion.getConexion();
            String sql = "";
            if (this.rbNit.isSelected()) {
                sql = sqlus.BUSCANIT + "'" + Dato + "'";
            }
            if (this.rbNombre.isSelected()) {
                sql = sqlus.BUSCANOMBRE + Dato + sqlus.CUALQUIERA;
            }
            removejtable();
            //model = new DefaultTableModel(null, titulos);//objeto TableModel para proporcionar los datos del objeto ResultSet al objeto JTable
            sent = conn.createStatement();// crea objeto Statement para consultar la base de datos
            ResultSet rs = sent.executeQuery(sql);// especifica la consulta y la ejecuta

            if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                int count = 0;
                rs.beforeFirst();//regresa el puntero al primer registro
                String[] fila = new String[8];
                while (rs.next()) {

                    fila[0] = rs.getString("idusuario");
                    fila[1] = rs.getString("nombre");
                    fila[2] = rs.getString("apellido");
                    fila[3] = rs.getString("nombreusuario");
                    fila[4] = rs.getString("contrasenia");
                    fila[5] = rs.getString("puesto");
                    fila[6] = sdf.format(rs.getDate("fecha"));
                    if (rs.getString("Estado").equals("T")) {
                        fila[7] = "Activo";
                    } else {
                        fila[7] = "Inactivo";
                    }

                    model.addRow(fila);
                    count = count + 1;
                }
                usuarios.setModel(model);
                formatotabla();
                this.bntGuardar.setEnabled(false);
                this.bntModificar.setEnabled(false);
                this.bntEliminar.setEnabled(false);
                this.bntNuevo.setEnabled(true);
                //JOptionPane.showInternalMessageDialog(this, "Se encontraron " + count + " registros");
                Limpiar();
                Desabilitar();

            } else {
                JOptionPane.showInternalMessageDialog(this, "El dato no fue encontrado");
            }
            //conn.close();
        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(this, "El dato no fue encontrado");
            System.out.print(e.getMessage());
        }
    }

    private void filaseleccionada() {
        int fila = usuarios.getSelectedRow();
        if (usuarios.getValueAt(fila, 0) != null) {
            try {
                conn = BdConexion.getConexion();
                Habilitar();
                String sql = sqlus.LLENAR + sqlus.WHERE + sqlus.ID + sqlus.IGUAL + usuarios.getValueAt(fila, 0);
                sent = conn.createStatement();
                ResultSet rs = sent.executeQuery(sql);
                rs.next();

                nombre.setText(rs.getString("nombre"));
                correo.setText(rs.getString("apellido"));
                usuario.setText(rs.getString("nombreusuario"));
                password.setText(rs.getString("contrasenia"));
                puesto.setText(rs.getString("puesto"));
                if (rs.getString("Estado").equals("T")) {
                    rbEstado.setSelected(true);
                    rbEstado.setBackground(new java.awt.Color(102, 204, 0));
                } else {
                    rbEstado.setSelected(false);
                    rbEstado.setBackground(Color.red);
                }
                dcFecha.setDate(rs.getDate("fecha"));

                this.bntGuardar.setEnabled(false);
                this.bntModificar.setEnabled(true);
                this.bntEliminar.setEnabled(true);
                this.bntNuevo.setEnabled(false);
                //conn.close();
            } catch (SQLException e) {
                JOptionPane.showInternalMessageDialog(this, "Error al cargar los datos", "Error BD", JOptionPane.ERROR_MESSAGE);
                System.out.print(e.getMessage());
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

        perfi = new javax.swing.JDialog();
        jcMousePanel1 = new jcMousePanel.jcMousePanel();
        nameusuario = new javax.swing.JTextField();
        idusuario = new javax.swing.JTextField();
        crearusuario = new javax.swing.JCheckBox();
        ventas = new javax.swing.JCheckBox();
        compras = new javax.swing.JCheckBox();
        categoria = new javax.swing.JCheckBox();
        medida = new javax.swing.JCheckBox();
        marca = new javax.swing.JCheckBox();
        proveedores = new javax.swing.JCheckBox();
        productos = new javax.swing.JCheckBox();
        Clientes = new javax.swing.JCheckBox();
        ventaR = new javax.swing.JCheckBox();
        ventacontado = new javax.swing.JCheckBox();
        ventacredito = new javax.swing.JCheckBox();
        Ganancia = new javax.swing.JCheckBox();
        inventariolote = new javax.swing.JCheckBox();
        inventario = new javax.swing.JCheckBox();
        pedidocompra = new javax.swing.JCheckBox();
        Rcompra = new javax.swing.JCheckBox();
        reimpresion = new javax.swing.JCheckBox();
        corte = new javax.swing.JCheckBox();
        ccredito = new javax.swing.JCheckBox();
        pcredito = new javax.swing.JCheckBox();
        corteusuario = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        pnlPaginador1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        buttonMostrar2 = new elaprendiz.gui.button.ButtonRect();
        AtrasoPagos = new javax.swing.JCheckBox();
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
        usuario = new elaprendiz.gui.textField.TextField();
        correo = new elaprendiz.gui.textField.TextField();
        password = new elaprendiz.gui.textField.TextField();
        puesto = new elaprendiz.gui.textField.TextField();
        dcFecha = new com.toedter.calendar.JDateChooser();
        rbEstado = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        usuarios = new javax.swing.JTable();
        pnlPaginador = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtDato = new elaprendiz.gui.textField.TextField();
        rbNit = new javax.swing.JRadioButton();
        rbNombre = new javax.swing.JRadioButton();
        buttonMostrar = new elaprendiz.gui.button.ButtonRect();
        buttonMostrar1 = new elaprendiz.gui.button.ButtonRect();

        perfi.setTitle("perfiles de usuario");
        perfi.setBounds(new java.awt.Rectangle(100, 100, 400, 400));

        jcMousePanel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fondo/índice.jpg"))); // NOI18N

        nameusuario.setVisible(false);
        nameusuario.setEditable(false);
        nameusuario.setEnabled(false);
        nameusuario.setOpaque(false);

        idusuario.setVisible(false);
        idusuario.setEnabled(false);

        crearusuario.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        crearusuario.setForeground(new java.awt.Color(255, 255, 255));
        crearusuario.setText("Crear usuario");
        crearusuario.setContentAreaFilled(false);
        crearusuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                crearusuarioActionPerformed(evt);
            }
        });

        ventas.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        ventas.setForeground(new java.awt.Color(255, 255, 255));
        ventas.setText("Ventas");
        ventas.setOpaque(false);
        ventas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ventasActionPerformed(evt);
            }
        });

        compras.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        compras.setForeground(new java.awt.Color(255, 255, 255));
        compras.setText("Compras");
        compras.setOpaque(false);
        compras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comprasActionPerformed(evt);
            }
        });

        categoria.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        categoria.setForeground(new java.awt.Color(255, 255, 255));
        categoria.setText("Categoria");
        categoria.setOpaque(false);
        categoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoriaActionPerformed(evt);
            }
        });

        medida.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        medida.setForeground(new java.awt.Color(255, 255, 255));
        medida.setText("Unidad de Medida");
        medida.setOpaque(false);
        medida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                medidaActionPerformed(evt);
            }
        });

        marca.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        marca.setForeground(new java.awt.Color(255, 255, 255));
        marca.setText("Marca");
        marca.setOpaque(false);
        marca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                marcaActionPerformed(evt);
            }
        });

        proveedores.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        proveedores.setForeground(new java.awt.Color(255, 255, 255));
        proveedores.setText("Proveedores");
        proveedores.setOpaque(false);
        proveedores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                proveedoresActionPerformed(evt);
            }
        });

        productos.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        productos.setForeground(new java.awt.Color(255, 255, 255));
        productos.setText("Productos");
        productos.setOpaque(false);
        productos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productosActionPerformed(evt);
            }
        });

        Clientes.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Clientes.setForeground(new java.awt.Color(255, 255, 255));
        Clientes.setText("Clientes");
        Clientes.setOpaque(false);
        Clientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClientesActionPerformed(evt);
            }
        });

        ventaR.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        ventaR.setForeground(new java.awt.Color(255, 255, 255));
        ventaR.setText("Reporte Venta");
        ventaR.setOpaque(false);
        ventaR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ventaRActionPerformed(evt);
            }
        });

        ventacontado.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        ventacontado.setForeground(new java.awt.Color(255, 255, 255));
        ventacontado.setText("Ventas Contado");
        ventacontado.setOpaque(false);
        ventacontado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ventacontadoActionPerformed(evt);
            }
        });

        ventacredito.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        ventacredito.setForeground(new java.awt.Color(255, 255, 255));
        ventacredito.setText("Ventas credito");
        ventacredito.setContentAreaFilled(false);
        ventacredito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ventacreditoActionPerformed(evt);
            }
        });

        Ganancia.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Ganancia.setForeground(new java.awt.Color(255, 255, 255));
        Ganancia.setText("Venta Ganancia");
        Ganancia.setOpaque(false);
        Ganancia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GananciaActionPerformed(evt);
            }
        });

        inventariolote.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        inventariolote.setForeground(new java.awt.Color(255, 255, 255));
        inventariolote.setText("Inventario por lote");
        inventariolote.setOpaque(false);
        inventariolote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inventarioloteActionPerformed(evt);
            }
        });

        inventario.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        inventario.setForeground(new java.awt.Color(255, 255, 255));
        inventario.setText("Inventario");
        inventario.setOpaque(false);
        inventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inventarioActionPerformed(evt);
            }
        });

        pedidocompra.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        pedidocompra.setForeground(new java.awt.Color(255, 255, 255));
        pedidocompra.setText("Pedido de compra");
        pedidocompra.setOpaque(false);
        pedidocompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pedidocompraActionPerformed(evt);
            }
        });

        Rcompra.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Rcompra.setForeground(new java.awt.Color(255, 255, 255));
        Rcompra.setText("Reporte compra");
        Rcompra.setOpaque(false);
        Rcompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RcompraActionPerformed(evt);
            }
        });

        reimpresion.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        reimpresion.setForeground(new java.awt.Color(255, 255, 255));
        reimpresion.setText("Reimpresiones");
        reimpresion.setOpaque(false);
        reimpresion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reimpresionActionPerformed(evt);
            }
        });

        corte.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        corte.setForeground(new java.awt.Color(255, 255, 255));
        corte.setText("Corte Efectivo");
        corte.setOpaque(false);
        corte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                corteActionPerformed(evt);
            }
        });

        ccredito.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        ccredito.setForeground(new java.awt.Color(255, 255, 255));
        ccredito.setText("Cobro creditos");
        ccredito.setOpaque(false);
        ccredito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ccreditoActionPerformed(evt);
            }
        });

        pcredito.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        pcredito.setForeground(new java.awt.Color(255, 255, 255));
        pcredito.setText("Pago creditos");
        pcredito.setOpaque(false);
        pcredito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pcreditoActionPerformed(evt);
            }
        });

        corteusuario.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        corteusuario.setForeground(new java.awt.Color(255, 255, 255));
        corteusuario.setText("Corte Usuario");
        corteusuario.setOpaque(false);

        pnlPaginador1.setBackground(new java.awt.Color(0, 153, 204));
        pnlPaginador1.setPreferredSize(new java.awt.Dimension(786, 40));
        pnlPaginador1.setLayout(new java.awt.GridBagLayout());

        jLabel9.setFont(new java.awt.Font("Script MT Bold", 1, 32)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("<--Perfil de Usuario-->");
        pnlPaginador1.add(jLabel9, new java.awt.GridBagConstraints());

        buttonMostrar2.setBackground(new java.awt.Color(102, 204, 0));
        buttonMostrar2.setText("Modificar Permisos");
        buttonMostrar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonMostrar2ActionPerformed(evt);
            }
        });

        AtrasoPagos.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        AtrasoPagos.setForeground(new java.awt.Color(255, 255, 255));
        AtrasoPagos.setText("Dias de atraso");
        AtrasoPagos.setOpaque(false);

        javax.swing.GroupLayout jcMousePanel1Layout = new javax.swing.GroupLayout(jcMousePanel1);
        jcMousePanel1.setLayout(jcMousePanel1Layout);
        jcMousePanel1Layout.setHorizontalGroup(
            jcMousePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jcMousePanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jcMousePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlPaginador1, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                    .addComponent(nameusuario))
                .addGap(459, 459, 459))
            .addGroup(jcMousePanel1Layout.createSequentialGroup()
                .addGroup(jcMousePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jcMousePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jcMousePanel1Layout.createSequentialGroup()
                            .addGap(149, 149, 149)
                            .addComponent(buttonMostrar2, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jcMousePanel1Layout.createSequentialGroup()
                            .addGap(26, 26, 26)
                            .addGroup(jcMousePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jcMousePanel1Layout.createSequentialGroup()
                                    .addGroup(jcMousePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(crearusuario, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(ventas, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(compras, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(categoria, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(medida, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(marca, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(proveedores, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(productos, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(Clientes, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(ventaR, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(ventacontado, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(AtrasoPagos, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(67, 67, 67)
                                    .addGroup(jcMousePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(pcredito, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(corte, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(reimpresion, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(Rcompra, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(pedidocompra, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(inventario, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(inventariolote, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(Ganancia, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(ventacredito, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(ccredito, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(corteusuario, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(idusuario, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jcMousePanel1Layout.setVerticalGroup(
            jcMousePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jcMousePanel1Layout.createSequentialGroup()
                .addGroup(jcMousePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jcMousePanel1Layout.createSequentialGroup()
                        .addComponent(pnlPaginador1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(nameusuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jcMousePanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(idusuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jcMousePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jcMousePanel1Layout.createSequentialGroup()
                        .addComponent(crearusuario)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ventas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(compras)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(categoria)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(medida)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(marca)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(proveedores)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(productos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Clientes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ventaR))
                    .addGroup(jcMousePanel1Layout.createSequentialGroup()
                        .addComponent(ventacredito)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Ganancia)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(inventariolote)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(inventario)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pedidocompra)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Rcompra)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(reimpresion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(corte)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ccredito)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pcredito)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jcMousePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ventacontado)
                    .addComponent(corteusuario))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AtrasoPagos)
                .addGap(9, 9, 9)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(buttonMostrar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout perfiLayout = new javax.swing.GroupLayout(perfi.getContentPane());
        perfi.getContentPane().setLayout(perfiLayout);
        perfiLayout.setHorizontalGroup(
            perfiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jcMousePanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        perfiLayout.setVerticalGroup(
            perfiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jcMousePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
        );

        setBackground(new java.awt.Color(0, 0, 0));
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setForeground(new java.awt.Color(0, 0, 0));
        setTitle("Usuarios");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFocusTraversalPolicyProvider(true);
        setName("Usuarios"); // NOI18N
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
        jLabel3.setText("*Puesto:");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(45, 90, 80, 20);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("*Password:");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(390, 65, 90, 20);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("*Usuario:");
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

        usuario.setEditable(false);
        usuario.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        usuario.setPreferredSize(new java.awt.Dimension(120, 21));
        jPanel1.add(usuario);
        usuario.setBounds(490, 40, 150, 21);

        correo.setEditable(false);
        correo.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        correo.setInputVerifier(new VerificadorEntrada(125,VerificadorEntrada.EMAIL));
        correo.setPreferredSize(new java.awt.Dimension(120, 21));
        jPanel1.add(correo);
        correo.setBounds(140, 65, 250, 21);

        password.setEditable(false);
        password.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        password.setPreferredSize(new java.awt.Dimension(120, 21));
        jPanel1.add(password);
        password.setBounds(490, 65, 150, 21);

        puesto.setEditable(false);
        puesto.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        puesto.setPreferredSize(new java.awt.Dimension(120, 21));
        jPanel1.add(puesto);
        puesto.setBounds(140, 90, 250, 21);

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

        panelImage1.add(jPanel1);
        jPanel1.setBounds(0, 40, 880, 140);

        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(786, 402));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        usuarios.setModel(model = new DefaultTableModel(null, titulos)
            {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
            usuarios.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            usuarios.setFocusCycleRoot(true);
            usuarios.setRowHeight(24);
            usuarios.setSurrendersFocusOnKeystroke(true);
            usuarios.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    usuariosMouseClicked(evt);
                }
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    usuariosMouseClicked(evt);
                }
            });
            jScrollPane1.setViewportView(usuarios);
            usuarios.getAccessibleContext().setAccessibleName("");

            jPanel3.add(jScrollPane1, java.awt.BorderLayout.CENTER);

            panelImage1.add(jPanel3);
            jPanel3.setBounds(0, 250, 880, 180);

            pnlPaginador.setBackground(new java.awt.Color(0, 153, 204));
            pnlPaginador.setPreferredSize(new java.awt.Dimension(786, 40));
            pnlPaginador.setLayout(new java.awt.GridBagLayout());

            jLabel8.setFont(new java.awt.Font("Script MT Bold", 1, 32)); // NOI18N
            jLabel8.setForeground(new java.awt.Color(255, 255, 255));
            jLabel8.setText("<--Usuario-->");
            pnlPaginador.add(jLabel8, new java.awt.GridBagConstraints());

            panelImage1.add(pnlPaginador);
            pnlPaginador.setBounds(0, 0, 880, 40);

            jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            jPanel4.setOpaque(false);
            jPanel4.setLayout(null);

            jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            jLabel7.setText("Buscar Por:");
            jPanel4.add(jLabel7);
            jLabel7.setBounds(167, 12, 80, 17);

            txtDato.setPreferredSize(new java.awt.Dimension(250, 27));
            txtDato.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    buttonBuscarActionPerformed(evt);
                }
            });
            jPanel4.add(txtDato);
            txtDato.setBounds(252, 7, 250, 27);

            rbNit.setBackground(new java.awt.Color(51, 153, 255));
            rbNit.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            rbNit.setForeground(new java.awt.Color(255, 255, 255));
            rbNit.setText("Usuario");
            rbNit.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    rbNitActionPerformed(evt);
                }
            });
            jPanel4.add(rbNit);
            rbNit.setBounds(280, 40, 80, 25);

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
            rbNombre.setBounds(400, 40, 81, 25);

            buttonMostrar.setBackground(new java.awt.Color(102, 204, 0));
            buttonMostrar.setText("Mostrar Todo");
            buttonMostrar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    buttonMostrarActionPerformed(evt);
                }
            });
            jPanel4.add(buttonMostrar);
            buttonMostrar.setBounds(580, 10, 126, 25);

            buttonMostrar1.setBackground(new java.awt.Color(102, 204, 0));
            buttonMostrar1.setText("Perfiles");
            buttonMostrar1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    buttonMostrar1ActionPerformed(evt);
                }
            });
            jPanel4.add(buttonMostrar1);
            buttonMostrar1.setBounds(710, 10, 86, 25);

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
        if (nombre.getText().equals("") || usuario.getText().equals("") || puesto.getText().equals("") || usuario.getText().equals("") || password.getText().equals("") || getFecha() == null) {
            JOptionPane.showInternalMessageDialog(this, "Complete los campos obligatorios");
        } else {
            int resp;
            resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Grabar el Registro?", "Pregunta", 0);
            if (resp == 0) {

                try {
                    conn = BdConexion.getConexion();
                    //PreparedStatement nos permite crear instrucciones SQL compiladas, que se ejecutan con más efi ciencia que los objetos Statement
                    //también pueden especifi car parámetros,lo cual las hace más fl exibles que las instrucciones Statement
                    PreparedStatement ps = conn.prepareStatement(sqlus.NUEVOC, PreparedStatement.RETURN_GENERATED_KEYS);

                    ps.setString(1, nombre.getText());
                    ps.setString(2, correo.getText());
                    ps.setString(3, usuario.getText());
                    ps.setString(4, password.getText());
                    ps.setString(5, puesto.getText());
                    ps.setString(6, getFecha());
                    int n = ps.executeUpdate();
                    ResultSet rs = ps.getGeneratedKeys();
                    int id = 0;
                    while (rs.next()) {
                        id = rs.getInt(1);
                    }

                    PreparedStatement ps1 = conn.prepareStatement(sqlus.NUEVOPER);
                    for (int i = 0; i <= 22; i++) {
                        ps1.setString(1, "" + i);
                        ps1.setString(2, "F");
                        ps1.setString(3, "" + id);
                        int n2 = ps1.executeUpdate();
                    }

                    if (n > 0) {
                        Llenar();
                        Desabilitar();
                        Limpiar();
                        txtDato.requestFocus();
                        JOptionPane.showInternalMessageDialog(this, "Datos guardados correctamente");
                        
                        abrir();
                        perfi.setVisible(true);
                        perfi.setSize(447, 487);
                        perfi.toFront();
                        perfi.setLocationRelativeTo(null);
                    }
                    //conn.close();
                } catch (SQLException e) {
                    if (e.getErrorCode() == 1062) {
                        JOptionPane.showInternalMessageDialog(this,
                                "Error al guardar \n El usuario ya existe ingrese uno diferente", "Error ", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showInternalMessageDialog(this,
                                "Error al guardar \n Verifique los datos e intente nuevamente", "Error ", JOptionPane.ERROR_MESSAGE);
                    }
                    System.out.print(e.getMessage() + "\n");
                }

            }
        }
    }//GEN-LAST:event_bntGuardarActionPerformed

    private void bntSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntSalirActionPerformed
        cerrarVentana();
    }//GEN-LAST:event_bntSalirActionPerformed

    private void usuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usuariosMouseClicked
        // TODO add your handling code here:
        if (evt.getButton() == 1) {
            int fila = usuarios.getSelectedRow();
            if (usuarios.getValueAt(fila, 0) != null) {
                try {
                    conn = BdConexion.getConexion();
                    Habilitar();
                    String sql = sqlus.LLENAR + sqlus.WHERE + sqlus.ID + sqlus.IGUAL + usuarios.getValueAt(fila, 0);
                    sent = conn.createStatement();
                    ResultSet rs = sent.executeQuery(sql);
                    rs.next();

                    nombre.setText(rs.getString("nombre"));
                    correo.setText(rs.getString("apellido"));
                    usuario.setText(rs.getString("nombreusuario"));
                    password.setText(rs.getString("contrasenia"));
                    puesto.setText(rs.getString("puesto"));
                    if (rs.getString("Estado").equals("T")) {
                        rbEstado.setSelected(true);
                        rbEstado.setBackground(new java.awt.Color(102, 204, 0));
                    } else {
                        rbEstado.setSelected(false);
                        rbEstado.setBackground(Color.red);
                    }
                    dcFecha.setDate(rs.getDate("fecha"));

                    this.bntGuardar.setEnabled(false);
                    this.bntModificar.setEnabled(true);
                    this.bntEliminar.setEnabled(true);
                    this.bntNuevo.setEnabled(false);
                    //conn.close();
                } catch (SQLException e) {
                    JOptionPane.showInternalMessageDialog(this, "Error al cargar los datos", "Error BD", JOptionPane.ERROR_MESSAGE);
                    System.out.print(e.getMessage());
                }
            }
        }
    }//GEN-LAST:event_usuariosMouseClicked

    private void bntEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntEliminarActionPerformed
        // TODO add your handling code here:
        int resp;
        resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Eliminar el Registro?", "Pregunta", 0);
        if (resp == 0) {
            try {
                conn = BdConexion.getConexion();
                int fila = usuarios.getSelectedRow();
                String sql = sqlus.DELETEC + sqlus.WHERE + sqlus.ID + sqlus.IGUAL + usuarios.getValueAt(fila, 0);
                sent = conn.createStatement();//El programa utiliza al objeto Statement para enviar instrucciones de SQL a la base de datos.
                int n = sent.executeUpdate(sql);
                if (n > 0) {
                    Llenar();
                    Limpiar();
                    Desabilitar();
                    txtDato.requestFocus();
                    JOptionPane.showInternalMessageDialog(this, "Datos eliminados correctamente");

                }
                //conn.close();
            } catch (SQLException e) {
                if (e.getErrorCode() == 1451) {
                    JOptionPane.showInternalMessageDialog(this,
                            "Error al eliminar \n El usuario esta siendo utilizado y no puede ser eliminado ", "Error ", JOptionPane.ERROR_MESSAGE);
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
        if (nombre.getText().equals("") || usuario.getText().equals("") || puesto.getText().equals("") || usuario.getText().equals("") || password.getText().equals("") || getFecha() == null) {
            JOptionPane.showInternalMessageDialog(this, "Complete los campos obligatorios");
        } else {
            int resp;
            resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Modidicar el Registro?", "Pregunta", 0);
            if (resp == 0) {
                try {
                    conn = BdConexion.getConexion();
                    int fila = usuarios.getSelectedRow();
                    String dao = (String) usuarios.getValueAt(fila, 0);

                    //PreparedStatement nos permite crear instrucciones SQL compiladas, que se ejecutan con más efi ciencia que los objetos Statement
                    //también pueden especifi car parámetros,lo cual las hace más fl exibles que las instrucciones Statement
                    PreparedStatement ps = conn.prepareCall(sqlus.UPDATEC);
                    ps.setString(1, nombre.getText());
                    ps.setString(2, correo.getText());
                    ps.setString(3, usuario.getText());
                    ps.setString(4, password.getText());
                    ps.setString(5, puesto.getText());
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
                        Llenar();
                        txtDato.requestFocus();
                        JOptionPane.showInternalMessageDialog(this, "Datos modificados correctamente");
                    }
                    //conn.close();
                } catch (SQLException e) {
                    if (e.getErrorCode() == 1062) {
                        JOptionPane.showInternalMessageDialog(this,
                                "Error al modificar \n El usuario ya existe ingrese uno diferente", "Error ", JOptionPane.ERROR_MESSAGE);
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
        Llenar();
        txtDato.requestFocus();
    }//GEN-LAST:event_bntCancelarActionPerformed

    private void buttonMostrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonMostrarActionPerformed
        // TODO add your handling code here:
        Llenar();
        Limpiar();
        Desabilitar();
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

    private void buttonBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonBuscarActionPerformed
        // TODO add your handling code here:
//        if (!txtDato.getText().isEmpty()) {
//            {
        MostrarTodo(txtDato.getText());
//            }
//        } else {
//            JOptionPane.showInternalMessageDialog(this, "No hay dato que buscar  ", "Error", JOptionPane.ERROR_MESSAGE);
//        }

    }//GEN-LAST:event_buttonBuscarActionPerformed

    private void rbEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbEstadoActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_rbEstadoActionPerformed
    public void abrir() {
        try {
            sinchek();
            // Se obtiene una conexión con la base de datos. Hay que
            // cambiar el Login "root" y la clave "la_clave" por las
            // adecuadas a la base de datos que estemos usando.
            //Connection conexion = DriverManager.getConnection(LeePropiedades.leeID("url"), LeePropiedades.leeID("usuario"), LeePropiedades.leeID("password"));
            conn = BdConexion.getConexion();
            // Se crea un Statement, para realizar la consulta
            com.mysql.jdbc.Statement s = (com.mysql.jdbc.Statement) conn.createStatement();
            int fila = usuarios.getSelectedRow();

            idusuario.setText(usuarios.getValueAt(fila, 0) + "");
            nameusuario.setText(usuarios.getValueAt(fila, 1) + "");
            String sql = "select estado,menu from perfilusu where idusuario='" + usuarios.getValueAt(fila, 0) + "' ";

            // Se realiza la consulta. Los resultados se guardan en el 
            // ResultSet rs
            ResultSet rs = s.executeQuery(sql);

            // Se recorre el ResultSet, mostrando por pantalla los resultados.
            while (rs.next()) {
                if (rs.getString("menu").equals("0") && rs.getString("estado").equals("T")) {
                    crearusuario.setSelected(true);

                }
                if (rs.getString("menu").equals("1") && rs.getString("estado").equals("T")) {
                    ventas.setSelected(true);
                }
                if (rs.getString("menu").equals("2") && rs.getString("estado").equals("T")) {
                    compras.setSelected(true);
                }

                if (rs.getString("menu").equals("3") && rs.getString("estado").equals("T")) {
                    categoria.setSelected(true);
                }
                if (rs.getString("menu").equals("4") && rs.getString("estado").equals("T")) {
                    medida.setSelected(true);
                }
                if (rs.getString("menu").equals("5") && rs.getString("estado").equals("T")) {
                    marca.setSelected(true);
                }
                if (rs.getString("menu").equals("6") && rs.getString("estado").equals("T")) {
                    proveedores.setSelected(true);
                }
                if (rs.getString("menu").equals("7") && rs.getString("estado").equals("T")) {
                    productos.setSelected(true);
                }
                if (rs.getString("menu").equals("8") && rs.getString("estado").equals("T")) {
                    Clientes.setSelected(true);
                }
                if (rs.getString("menu").equals("9") && rs.getString("estado").equals("T")) {
                    ventaR.setSelected(true);
                }
                if (rs.getString("menu").equals("10") && rs.getString("estado").equals("T")) {
                    ventacontado.setSelected(true);
                }
                if (rs.getString("menu").equals("11") && rs.getString("estado").equals("T")) {
                    ventacredito.setSelected(true);
                }
                if (rs.getString("menu").equals("12") && rs.getString("estado").equals("T")) {
                    Ganancia.setSelected(true);
                }
                if (rs.getString("menu").equals("13") && rs.getString("estado").equals("T")) {
                    inventariolote.setSelected(true);
                }
                if (rs.getString("menu").equals("14") && rs.getString("estado").equals("T")) {
                    inventario.setSelected(true);
                }
                if (rs.getString("menu").equals("15") && rs.getString("estado").equals("T")) {
                    pedidocompra.setSelected(true);
                }
                if (rs.getString("menu").equals("16") && rs.getString("estado").equals("T")) {
                    Rcompra.setSelected(true);
                }
                if (rs.getString("menu").equals("17") && rs.getString("estado").equals("T")) {
                    reimpresion.setSelected(true);
                }
                if (rs.getString("menu").equals("18") && rs.getString("estado").equals("T")) {
                    corte.setSelected(true);
                }
                if (rs.getString("menu").equals("19") && rs.getString("estado").equals("T")) {
                    ccredito.setSelected(true);
                }
                if (rs.getString("menu").equals("20") && rs.getString("estado").equals("T")) {
                    pcredito.setSelected(true);
                }
                if (rs.getString("menu").equals("21") && rs.getString("estado").equals("T")) {
                    corteusuario.setSelected(true);
                }
                if (rs.getString("menu").equals("22") && rs.getString("estado").equals("T")) {
                    AtrasoPagos.setSelected(true);
                }
            }

            // Se cierra la conexión con la base de datos.
            //conexion.close();
        } catch (NullPointerException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }
    private void buttonMostrar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonMostrar1ActionPerformed
        // TODO add your handling code here:
        abrir();
        perfi.setVisible(true);
        perfi.setSize(447, 487);
        perfi.toFront();
        perfi.setLocationRelativeTo(null);
    }//GEN-LAST:event_buttonMostrar1ActionPerformed

    private void crearusuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_crearusuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_crearusuarioActionPerformed

    private void ventasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ventasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ventasActionPerformed

    private void comprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comprasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comprasActionPerformed

    private void categoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_categoriaActionPerformed

    private void medidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_medidaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_medidaActionPerformed

    private void marcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_marcaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_marcaActionPerformed

    private void proveedoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_proveedoresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_proveedoresActionPerformed

    private void productosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_productosActionPerformed

    private void ClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClientesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ClientesActionPerformed

    private void ventaRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ventaRActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ventaRActionPerformed

    private void ventacontadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ventacontadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ventacontadoActionPerformed

    private void ventacreditoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ventacreditoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ventacreditoActionPerformed

    private void GananciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GananciaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_GananciaActionPerformed

    private void inventarioloteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inventarioloteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inventarioloteActionPerformed

    private void inventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inventarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inventarioActionPerformed

    private void pedidocompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pedidocompraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pedidocompraActionPerformed

    private void RcompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RcompraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RcompraActionPerformed

    private void reimpresionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reimpresionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reimpresionActionPerformed

    private void corteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_corteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_corteActionPerformed
    public void sinchek() {
        crearusuario.setSelected(false);
        ventas.setSelected(false);
        compras.setSelected(false);
        categoria.setSelected(false);
        medida.setSelected(false);
        marca.setSelected(false);
        proveedores.setSelected(false);
        productos.setSelected(false);
        Clientes.setSelected(false);
        ventaR.setSelected(false);
        ventacontado.setSelected(false);
        ventacredito.setSelected(false);
        Ganancia.setSelected(false);
        inventariolote.setSelected(false);
        inventario.setSelected(false);
        pedidocompra.setSelected(false);
        Rcompra.setSelected(false);
        reimpresion.setSelected(false);
        corte.setSelected(false);
        ccredito.setSelected(false);
        pcredito.setSelected(false);
        corteusuario.setSelected(false);
        AtrasoPagos.setSelected(false);

    }

    public void llenado() {
        String[] estados = new String[24];
        estados[0] = "F";
        estados[1] = "F";
        estados[2] = "F";
        estados[3] = "F";
        estados[4] = "F";
        estados[5] = "F";
        estados[6] = "F";
        estados[7] = "F";
        estados[8] = "F";
        estados[9] = "F";
        estados[10] = "F";
        estados[11] = "F";
        estados[12] = "F";
        estados[13] = "F";
        estados[14] = "F";
        estados[15] = "F";
        estados[16] = "F";
        estados[17] = "F";
        estados[18] = "F";
        estados[19] = "F";
        estados[20] = "F";
        estados[21] = "F";
        estados[22] = "F";

        if (crearusuario.isSelected() == true) {
            estados[0] = "T";
        }
        if (ventas.isSelected() == true) {
            estados[1] = "T";
        }
        if (compras.isSelected() == true) {
            estados[2] = "T";
        }

        if (categoria.isSelected() == true) {
            estados[3] = "T";
        }
        if (medida.isSelected() == true) {
            estados[4] = "T";
        }
        if (marca.isSelected() == true) {
            estados[5] = "T";
        }
        if (proveedores.isSelected() == true) {
            estados[6] = "T";
        }
        if (productos.isSelected() == true) {
            estados[7] = "T";
        }
        if (Clientes.isSelected() == true) {
            estados[8] = "T";
        }
        if (ventaR.isSelected() == true) {
            estados[9] = "T";
        }
        if (ventacontado.isSelected() == true) {
            estados[10] = "T";
        }
        if (ventacredito.isSelected() == true) {
            estados[11] = "T";
        }
        if (Ganancia.isSelected() == true) {
            estados[12] = "T";
        }
        if (inventariolote.isSelected() == true) {
            estados[13] = "T";
        }
        if (inventario.isSelected() == true) {
            estados[14] = "T";
        }
        if (pedidocompra.isSelected() == true) {
            estados[15] = "T";
        }
        if (Rcompra.isSelected() == true) {
            estados[16] = "T";
        }
        if (reimpresion.isSelected() == true) {
            estados[17] = "T";
        }
        if (corte.isSelected() == true) {
            estados[18] = "T";
        }
        if (ccredito.isSelected() == true) {
            estados[19] = "T";
        }
        if (pcredito.isSelected() == true) {
            estados[20] = "T";
        }
        if (corteusuario.isSelected() == true) {
            estados[21] = "T";
        }
        if (AtrasoPagos.isSelected() == true) {
            estados[22] = "T";
        }

        try {
            conn = BdConexion.getConexion();
            int fila = usuarios.getSelectedRow();
            String dao = idusuario.getText();
            //PreparedStatement nos permite crear instrucciones SQL compiladas, que se ejecutan con más efi ciencia que los objetos Statement
            //también pueden especifi car parámetros,lo cual las hace más fl exibles que las instrucciones Statement
            PreparedStatement ps = conn.prepareCall(sqlus.UPDATPERFIL);
            int n = 0;
            for (int i = 0; i <= 22; i++) {

                ps.setString(1, estados[i]);
                ps.setString(2, dao);
                ps.setString(3, "" + i);

                n = ps.executeUpdate();
            }
            if (n > 0) {
                JOptionPane.showInternalMessageDialog(this, "Datos modificados correctamente");
                abrir();
            }
            //conn.close();
        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(this,
                    "Error al modificar \n Verifique los datos e intente nuevamente", "Error ", JOptionPane.ERROR_MESSAGE);
            //System.out.print(e.getMessage());
        }

    }
    private void ccreditoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ccreditoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ccreditoActionPerformed

    private void pcreditoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pcreditoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pcreditoActionPerformed

    private void buttonMostrar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonMostrar2ActionPerformed
        // TODO add your handling code here:
        llenado();
    }//GEN-LAST:event_buttonMostrar2ActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        cerrarVentana();
    }//GEN-LAST:event_formInternalFrameClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox AtrasoPagos;
    private javax.swing.JCheckBox Clientes;
    private javax.swing.JCheckBox Ganancia;
    private javax.swing.JCheckBox Rcompra;
    private elaprendiz.gui.button.ButtonRect bntCancelar;
    private elaprendiz.gui.button.ButtonRect bntEliminar;
    private elaprendiz.gui.button.ButtonRect bntGuardar;
    private elaprendiz.gui.button.ButtonRect bntModificar;
    private elaprendiz.gui.button.ButtonRect bntNuevo;
    private elaprendiz.gui.button.ButtonRect bntSalir;
    private elaprendiz.gui.button.ButtonRect buttonMostrar;
    private elaprendiz.gui.button.ButtonRect buttonMostrar1;
    private elaprendiz.gui.button.ButtonRect buttonMostrar2;
    private javax.swing.JCheckBox categoria;
    private javax.swing.JCheckBox ccredito;
    private javax.swing.JCheckBox compras;
    private elaprendiz.gui.textField.TextField correo;
    private javax.swing.JCheckBox corte;
    private javax.swing.JCheckBox corteusuario;
    private javax.swing.JCheckBox crearusuario;
    private com.toedter.calendar.JDateChooser dcFecha;
    private javax.swing.JTextField idusuario;
    private javax.swing.JCheckBox inventario;
    private javax.swing.JCheckBox inventariolote;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private jcMousePanel.jcMousePanel jcMousePanel1;
    private javax.swing.JCheckBox marca;
    private javax.swing.JCheckBox medida;
    private javax.swing.JTextField nameusuario;
    private elaprendiz.gui.textField.TextField nombre;
    private elaprendiz.gui.panel.PanelImage panelImage1;
    private elaprendiz.gui.textField.TextField password;
    private javax.swing.JCheckBox pcredito;
    private javax.swing.JCheckBox pedidocompra;
    private javax.swing.JDialog perfi;
    private javax.swing.JPanel pnlActionButtons;
    private javax.swing.JPanel pnlPaginador;
    private javax.swing.JPanel pnlPaginador1;
    private javax.swing.JCheckBox productos;
    private javax.swing.JCheckBox proveedores;
    private elaprendiz.gui.textField.TextField puesto;
    private javax.swing.JRadioButton rbEstado;
    private javax.swing.JRadioButton rbNit;
    private javax.swing.JRadioButton rbNombre;
    private javax.swing.JCheckBox reimpresion;
    private elaprendiz.gui.textField.TextField txtDato;
    private elaprendiz.gui.textField.TextField usuario;
    private javax.swing.JTable usuarios;
    private javax.swing.JCheckBox ventaR;
    private javax.swing.JCheckBox ventacontado;
    private javax.swing.JCheckBox ventacredito;
    private javax.swing.JCheckBox ventas;
    // End of variables declaration//GEN-END:variables
}
