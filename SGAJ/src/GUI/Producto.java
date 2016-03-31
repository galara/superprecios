/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.https://www.youtube.com/watch?v=ICF-RldvSIo
 */
package GUI;

import BD.BdConexion;
import BD.sqlprod;
import Modelos.categorias;
import Modelos.marca;
import Modelos.unidad;
import excepciones.FiltraEntrada;
import excepciones.FormatoDecimal;
import excepciones.Helper;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.Calendar;
import java.util.Hashtable;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
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
public class Producto extends javax.swing.JInternalFrame {

    Hashtable<String, String> hashmarca = new Hashtable<String, String>();
    Hashtable<String, String> hashunidad = new Hashtable<String, String>();
    Hashtable<String, String> hashcategoria = new Hashtable<String, String>();
    DefaultTableModel model;
    Connection conn;//getConnection intentara establecer una conexión.
    Statement sent;
    String[] titulos = {"id", "Codigo", "Nombre", "Categoria", "PrecioC", "PrecioV", "Stock Min", "Unidad", "Existencia", /*"Fecha",*/ "Estado", "Marca"};

    /**
     * Creates new form Productos
     */
    public Producto() {
        initComponents();
        Desabilitar();
        Llenar();
        Llenarcombos();
        setFiltroTexto();
        addEscapeKey();

        tproductos.addKeyListener(new java.awt.event.KeyAdapter() {
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

    private void Llenarcombos() {
        DefaultComboBoxModel value1, value2, value3;
        value2 = new DefaultComboBoxModel();
        categoria.setModel(value2);
        value1 = new DefaultComboBoxModel();
        unidad.setModel(value1);
        value3 = new DefaultComboBoxModel();
        marca.setModel(value3);

        try {
            conn = BdConexion.getConexion();
            Statement s = (Statement) conn.createStatement();
            ResultSet rs = s.executeQuery(sqlprod.COMBOUNI);

            value1.addElement(new unidad(" ", "0"));
            int conta = 0;
            while (rs.next()) {
                conta++;
                value1.addElement(new unidad(rs.getString("Nombre"), "" + rs.getInt("idunidad")));
                hashunidad.put(rs.getString("Nombre"), "" + conta);
            }
            //conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurio un Error al cargar los datos\n" + ex.toString());
        }

        try {
            conn = BdConexion.getConexion();
            Statement s = (Statement) conn.createStatement();
            ResultSet rs = s.executeQuery(sqlprod.COMBOCAT);

            value2.addElement(new categorias(" ", "0"));
            int conta = 0;
            while (rs.next()) {
                conta++;
                value2.addElement(new categorias(rs.getString("Nombre"), "" + rs.getInt("idcategoria")));
                hashcategoria.put(rs.getString("Nombre"), "" + conta);
            }
            //conn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ocurio un Error al cargar los datos\n" + ex.toString());
        }

        try {
            conn = BdConexion.getConexion();
            Statement s = (Statement) conn.createStatement();
            ResultSet rs = s.executeQuery(sqlprod.COMBOMAR);

            value3.addElement(new marca(" ", "0"));
            int conta = 0;
            while (rs.next()) {
                conta++;
                value3.addElement(new marca(rs.getString("Nombre"), "" + rs.getInt("idmarca")));
                hashmarca.put(rs.getString("Nombre"), "" + conta);
            }
            //conn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ocurio un Error al cargar los datos\n" + ex.toString());
        }
    }

    public void removejtable() {
        while (tproductos.getRowCount() != 0) {
            model.removeRow(0);
        }
    }

    private void Desabilitar() {
        nombre.setEditable(false);
        codigo.setEditable(false);
        categoria.setEditable(false);
        precioC.setEditable(false);
        precioV.setEditable(false);
        precioCred.setEditable(false);
        precioDist.setEditable(false);
        precioEsp.setEditable(false);
        stockMin.setEditable(false);
        //stock.setEditable(false);
        dcFecha.setEnabled(false);
        rbEstado.setSelected(false);
        rbEstado.setEnabled(false);
        unidad.setEditable(false);
        marca.setEditable(false);
        descripcion.setEditable(false);
    }

    private void Limpiar() {
        nombre.setText("");
        codigo.setText("");
        precioC.setText("");
        precioV.setValue(null);
        precioCred.setValue(null);
        precioDist.setValue(null);
        precioEsp.setValue(null);
        //txtDato.setText("");
        stockMin.setText("");
        stock.setText("");
        rbEstado.setSelected(false);
        rbEstado.setEnabled(false);
        rbEstado.setBackground(new java.awt.Color(51, 153, 255));
        dcFecha.setDate(Calendar.getInstance().getTime());
        descripcion.setText("");
        Llenarcombos();
    }

    private void Habilitar() {
        nombre.setEditable(true);
        codigo.setEditable(true);
        precioC.setEditable(true);
        precioV.setEditable(true);
        precioCred.setEditable(true);
        precioDist.setEditable(true);
        precioEsp.setEditable(true);
        stockMin.setEditable(true);
        //stock.setEditable(true);
        dcFecha.setEnabled(true);
        rbEstado.setSelected(true);
        rbEstado.setEnabled(true);
        //categoria.setEditable(true);
        //unidad.setEditable(true);
        //marca.setEditable(true);
        descripcion.setEditable(true);
        //nombre.requestFocus();
    }

    public void setFiltroTexto() {
        Helper.setFiltraEntrada(nombre.getDocument(), FiltraEntrada.NUM_LETRAS, 100, true);
        Helper.setFiltraEntrada(codigo.getDocument(), FiltraEntrada.NUM_LETRAS, 50, true);
        Helper.setFiltraEntrada(descripcion.getDocument(), FiltraEntrada.DEFAULT, 255, true);
        Helper.setFiltraEntrada(txtDato.getDocument(), FiltraEntrada.NUM_LETRAS, 100, true);
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
        for (int i = 0; i < 11; i++) {
            column = tproductos.getColumnModel().getColumn(i);
            if (i == 0 || i == 1) {
                column.setPreferredWidth(20); //Difine el ancho de la columna
                tproductos.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);
            } else if (i == 2 || i == 3) {
                column.setPreferredWidth(145); //Difine el ancho de la columna
            } else if (i > 3 & i <= 9 || i == 10) {
                column.setPreferredWidth(36);//Difine el ancho de la columna
                tproductos.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);
            } else if (i == 9) {
                column.setPreferredWidth(3); //Difine el ancho de la columna
                tproductos.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);
            }
        }
    }

    private void Llenar() {
        try {
            conn = BdConexion.getConexion();
            removejtable();
            //model = new DefaultTableModel(null, titulos);//objeto TableModel para proporcionar los datos del objeto ResultSet al objeto JTable
            sent = conn.createStatement();// crea objeto Statement para consultar la base de datos
            ResultSet rs = sent.executeQuery(/*sql*/sqlprod.LLENAR + sqlprod.ORDER_BY);// especifica la consulta y la ejecuta

            String[] fila = new String[11];
            while (rs.next()) {

                fila[0] = rs.getString("idproducto");
                fila[1] = rs.getString("codigo");
                fila[2] = rs.getString("nombre");
                fila[3] = rs.getString("categoria.nombre");
                fila[4] = rs.getString("PrecioCompra");
                fila[5] = rs.getString("Precioventa");
                fila[6] = rs.getString("pivote");
                fila[7] = rs.getString("unidad.nombre");
                fila[8] = rs.getString("cantidad");
                //fila[9] = rs.getString("Estado");

                if (rs.getString("Estado").equals("T")) {
                    fila[9] = "Activo";
                } else {
                    fila[9] = "Inactivo";
                }
                fila[10] = rs.getString("marca.nombre");

                model.addRow(fila);
            }
            tproductos.setModel(model);
            formatotabla();
            this.bntGuardar.setEnabled(false);
            this.bntModificar.setEnabled(false);
            this.bntEliminar.setEnabled(false);
            this.bntNuevo.setEnabled(true);
            //conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en BD " + e.toString());
            this.dispose();
        }
    }

    private void MostrarTodo(String Dato) {
        try {
            conn = BdConexion.getConexion();
            String sql = "";

            if (this.rbCodigo.isSelected()) {
                sql = sqlprod.BUSCANIT + "'" + Dato + "'";
            }
            if (this.rbNombre.isSelected()) {
                sql = sqlprod.BUSCANOMBRE + Dato + sqlprod.CUALQUIERA;
            }
            if (this.rbMarca.isSelected()) {
                sql = sqlprod.BMARCA + Dato + sqlprod.CUALQUIERA;
            }
            if (this.rbCategoria.isSelected()) {
                sql = sqlprod.BCAT + Dato + sqlprod.CUALQUIERA;
            }
            removejtable();
            //model = new DefaultTableModel(null, titulos);//objeto TableModel para proporcionar los datos del objeto ResultSet al objeto JTable
            sent = conn.createStatement();// crea objeto Statement para consultar la base de datos
            ResultSet rs = sent.executeQuery(sql);// especifica la consulta y la ejecuta

            if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                int count = 0;
                rs.beforeFirst();//regresa el puntero al primer registro
                String[] fila = new String[11];
                while (rs.next()) {

                    fila[0] = rs.getString("idproducto");
                    fila[1] = rs.getString("codigo");
                    fila[2] = rs.getString("nombre");
                    fila[3] = rs.getString("categoria.nombre");
                    fila[4] = rs.getString("PrecioCompra");
                    fila[5] = rs.getString("Precioventa");
                    fila[6] = rs.getString("pivote");
                    fila[7] = rs.getString("unidad.nombre");
                    fila[8] = rs.getString("cantidad");
                    //fila[9] = rs.getString("Estado");

                    if (rs.getString("Estado").equals("T")) {
                        fila[9] = "Activo";
                    } else {
                        fila[9] = "Inactivo";
                    }
                    fila[10] = rs.getString("marca.nombre");
                    model.addRow(fila);
                    count = count + 1;
                }
                tproductos.setModel(model);
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

        //if (evt.getButton() == 1) {
        int fila = tproductos.getSelectedRow();
        if (tproductos.getValueAt(fila, 0) != null) {
            try {
                categoria.setEditable(true);
                unidad.setEditable(true);
                marca.setEditable(true);
                stock.setEnabled(false);
                stock.setEditable(false);

                conn = BdConexion.getConexion();
                Habilitar();
                conn = BdConexion.getConexion();
                String sql = sqlprod.LLENAR + sqlprod.WHERE + sqlprod.ID + sqlprod.IGUAL + tproductos.getValueAt(fila, 0);
                sent = conn.createStatement();
                ResultSet rs = sent.executeQuery(sql);
                rs.next();

                nombre.setText(rs.getString("nombre"));
                codigo.setText(rs.getString("codigo"));
                int pr = 0;
                pr = Integer.parseInt(hashcategoria.get(rs.getString("categoria.nombre")));
                this.categoria.setSelectedIndex(pr);

                pr = Integer.parseInt(hashunidad.get(rs.getString("unidad.nombre")));
                this.unidad.setSelectedIndex(pr);

                precioC.setText(rs.getString("PrecioCompra"));
                precioV.setText(rs.getString("Precioventa"));
                precioCred.setText(rs.getString("preciocredito"));
                precioDist.setText(rs.getString("preciodistribuidor"));
                precioEsp.setText(rs.getString("precioespecial"));

                stockMin.setText(rs.getString("Pivote"));
                stock.setText(rs.getString("cantidad"));

                if (rs.getString("Estado").equals("T")) {
                    rbEstado.setSelected(true);
                    rbEstado.setBackground(new java.awt.Color(102, 204, 0));
                } else {
                    rbEstado.setSelected(false);
                    rbEstado.setBackground(Color.red);
                }
                dcFecha.setDate(rs.getDate("fecha"));
                descripcion.setText(rs.getString("Descripcion"));
                pr = Integer.parseInt(hashmarca.get(rs.getString("marca.nombre")));
                this.marca.setSelectedIndex(pr);

                this.bntGuardar.setEnabled(false);
                this.bntModificar.setEnabled(true);
                this.bntEliminar.setEnabled(true);
                this.bntNuevo.setEnabled(false);

                categoria.setEditable(false);
                unidad.setEditable(false);
                marca.setEditable(false);
                //conn.close();
            } catch (SQLException e) {
                JOptionPane.showInternalMessageDialog(this, "Error al cargar los datos", "Error BD", JOptionPane.ERROR_MESSAGE);
                System.out.print(e.getMessage());
            }
        }
        //}
    }

    private boolean existecodigo() {
        String d = codigo.getText();
        if (!d.equals("") || d != null || !d.isEmpty()) {
            try {
                conn = BdConexion.getConexion();
                String sql = "select producto.codigo from producto where producto.codigo=" + "'" + d + "'";
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
                JOptionPane.showInternalMessageDialog(this, "Ocurrio un error al verificar los datos");
                System.out.print(e.getMessage());
            }
        } else {
            JOptionPane.showInternalMessageDialog(this, "Ingrese un codigo para verificarlo");
        }
        return false;

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
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        nombre = new elaprendiz.gui.textField.TextField();
        dcFecha = new com.toedter.calendar.JDateChooser();
        rbEstado = new javax.swing.JRadioButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        codigo = new elaprendiz.gui.textField.TextField();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        descripcion = new javax.swing.JTextArea();
        jLabel13 = new javax.swing.JLabel();
        precioC = new javax.swing.JFormattedTextField();
        stockMin = new javax.swing.JFormattedTextField();
        stock = new javax.swing.JFormattedTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        precioV = new javax.swing.JFormattedTextField();
        jLabel15 = new javax.swing.JLabel();
        precioCred = new javax.swing.JFormattedTextField();
        jLabel14 = new javax.swing.JLabel();
        precioDist = new javax.swing.JFormattedTextField();
        precioEsp = new javax.swing.JFormattedTextField();
        jLabel16 = new javax.swing.JLabel();
        categoria = new javax.swing.JComboBox();
        unidad = new javax.swing.JComboBox();
        marca = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tproductos = new javax.swing.JTable();
        pnlPaginador = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtDato = new elaprendiz.gui.textField.TextField();
        rbCodigo = new javax.swing.JRadioButton();
        rbNombre = new javax.swing.JRadioButton();
        buttonMostrar = new elaprendiz.gui.button.ButtonRect();
        rbMarca = new javax.swing.JRadioButton();
        rbCategoria = new javax.swing.JRadioButton();
        jLabel17 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(0, 0, 0));
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setForeground(new java.awt.Color(0, 0, 0));
        setTitle("Productos");
        setToolTipText("");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFocusTraversalPolicyProvider(true);
        setName("Producto"); // NOI18N
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
        pnlActionButtons.setBounds(0, 500, 880, 50);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setOpaque(false);
        jPanel1.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("Nombre:");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(50, 20, 80, 20);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Categoria:");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(50, 49, 80, 20);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Descripción:");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(20, 170, 110, 20);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("Precio Comp:");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(405, 20, 100, 20);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel6.setText("*Fecha:");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(405, 110, 100, 21);

        nombre.setEditable(false);
        nombre.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        nombre.setPreferredSize(new java.awt.Dimension(120, 21));
        jPanel1.add(nombre);
        nombre.setBounds(140, 20, 250, 21);

        dcFecha.setDate(Calendar.getInstance().getTime());
        dcFecha.setDateFormatString("dd/MM/yy");
        dcFecha.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        dcFecha.setMaxSelectableDate(new java.util.Date(3093496470100000L));
        dcFecha.setMinSelectableDate(new java.util.Date(-62135744300000L));
        dcFecha.setPreferredSize(new java.awt.Dimension(120, 22));
        jPanel1.add(dcFecha);
        dcFecha.setBounds(510, 110, 110, 21);

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
        rbEstado.setBounds(510, 140, 110, 21);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel9.setText("Stock Minimo:");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(405, 50, 100, 20);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel10.setText("Stock Actual:");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(405, 80, 100, 20);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel11.setText("Código:");
        jPanel1.add(jLabel11);
        jLabel11.setBounds(30, 141, 100, 20);

        codigo.setEditable(false);
        codigo.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        codigo.setPreferredSize(new java.awt.Dimension(120, 21));
        codigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codigoActionPerformed(evt);
            }
        });
        jPanel1.add(codigo);
        codigo.setBounds(140, 141, 250, 21);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel12.setText("Unidad Medida:");
        jPanel1.add(jLabel12);
        jLabel12.setBounds(20, 80, 110, 20);

        descripcion.setColumns(20);
        descripcion.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        descripcion.setLineWrap(true);
        descripcion.setWrapStyleWord(true);
        descripcion.setAutoscrolls(false);
        jScrollPane2.setViewportView(descripcion);

        jPanel1.add(jScrollPane2);
        jScrollPane2.setBounds(140, 170, 730, 36);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel13.setText("Marca:");
        jPanel1.add(jLabel13);
        jLabel13.setBounds(60, 110, 70, 17);

        precioC.setEditable(false);
        precioC.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
        precioC.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        precioC.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        precioC.setName("precioalmayor"); // NOI18N
        precioC.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel1.add(precioC);
        precioC.setBounds(510, 20, 110, 23);

        stockMin.setEditable(false);
        stockMin.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
        stockMin.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        stockMin.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        stockMin.setName("precioalmayor"); // NOI18N
        stockMin.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel1.add(stockMin);
        stockMin.setBounds(510, 50, 110, 23);

        stock.setEditable(false);
        stock.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
        stock.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        stock.setEnabled(false);
        stock.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        stock.setName("precioalmayor"); // NOI18N
        stock.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel1.add(stock);
        stock.setBounds(510, 80, 110, 23);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Precios de Venta", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel2.setOpaque(false);
        jPanel2.setLayout(null);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("Contado:");
        jPanel2.add(jLabel4);
        jLabel4.setBounds(25, 20, 70, 20);

        precioV.setEditable(false);
        precioV.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
        precioV.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        precioV.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        precioV.setName("precioalmayor"); // NOI18N
        precioV.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel2.add(precioV);
        precioV.setBounds(100, 20, 110, 23);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel15.setText("Credito:");
        jPanel2.add(jLabel15);
        jLabel15.setBounds(25, 50, 70, 20);

        precioCred.setEditable(false);
        precioCred.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
        precioCred.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        precioCred.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        precioCred.setName("precioalmayor"); // NOI18N
        precioCred.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel2.add(precioCred);
        precioCred.setBounds(100, 50, 110, 23);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel14.setText("Distribuidor:");
        jPanel2.add(jLabel14);
        jLabel14.setBounds(5, 80, 90, 20);

        precioDist.setEditable(false);
        precioDist.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
        precioDist.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        precioDist.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        precioDist.setName("precioalmayor"); // NOI18N
        precioDist.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel2.add(precioDist);
        precioDist.setBounds(100, 80, 110, 23);

        precioEsp.setEditable(false);
        precioEsp.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
        precioEsp.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        precioEsp.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        precioEsp.setName("precioalmayor"); // NOI18N
        precioEsp.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel2.add(precioEsp);
        precioEsp.setBounds(100, 110, 110, 23);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel16.setText("Especial:");
        jPanel2.add(jLabel16);
        jLabel16.setBounds(25, 110, 70, 20);

        jPanel1.add(jPanel2);
        jPanel2.setBounds(640, 5, 230, 160);

        categoria.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel1.add(categoria);
        categoria.setBounds(140, 49, 250, 25);

        unidad.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel1.add(unidad);
        unidad.setBounds(140, 80, 250, 25);

        marca.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel1.add(marca);
        marca.setBounds(140, 110, 250, 25);

        panelImage1.add(jPanel1);
        jPanel1.setBounds(0, 40, 880, 211);

        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(786, 402));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tproductos.setModel(model = new DefaultTableModel(null, titulos)
            {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
            tproductos.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            tproductos.setFocusCycleRoot(true);
            tproductos.setRowHeight(24);
            tproductos.setSurrendersFocusOnKeystroke(true);
            tproductos.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    tproductosMouseClicked(evt);
                }
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    tproductosMouseClicked(evt);
                }
            });
            jScrollPane1.setViewportView(tproductos);
            tproductos.getAccessibleContext().setAccessibleName("");

            jPanel3.add(jScrollPane1, java.awt.BorderLayout.CENTER);

            panelImage1.add(jPanel3);
            jPanel3.setBounds(0, 320, 880, 180);

            pnlPaginador.setBackground(new java.awt.Color(0, 153, 204));
            pnlPaginador.setPreferredSize(new java.awt.Dimension(786, 40));
            pnlPaginador.setLayout(new java.awt.GridBagLayout());

            jLabel8.setFont(new java.awt.Font("Script MT Bold", 1, 32)); // NOI18N
            jLabel8.setForeground(new java.awt.Color(255, 255, 255));
            jLabel8.setText("<--Productos-->");
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
                    txtDatoActionPerformed(evt);
                }
            });
            jPanel4.add(txtDato);
            txtDato.setBounds(252, 7, 320, 27);

            rbCodigo.setBackground(new java.awt.Color(51, 153, 255));
            rbCodigo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            rbCodigo.setForeground(new java.awt.Color(255, 255, 255));
            rbCodigo.setText("Código");
            rbCodigo.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    rbCodigoActionPerformed(evt);
                }
            });
            jPanel4.add(rbCodigo);
            rbCodigo.setBounds(220, 40, 75, 25);

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
            rbNombre.setBounds(320, 40, 81, 25);

            buttonMostrar.setBackground(new java.awt.Color(102, 204, 0));
            buttonMostrar.setText("Mostrar Todo");
            buttonMostrar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    buttonMostrarActionPerformed(evt);
                }
            });
            jPanel4.add(buttonMostrar);
            buttonMostrar.setBounds(610, 10, 126, 25);

            rbMarca.setBackground(new java.awt.Color(51, 153, 255));
            rbMarca.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            rbMarca.setForeground(new java.awt.Color(255, 255, 255));
            rbMarca.setText("Marca");
            rbMarca.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    rbMarcaActionPerformed(evt);
                }
            });
            jPanel4.add(rbMarca);
            rbMarca.setBounds(430, 40, 67, 25);

            rbCategoria.setBackground(new java.awt.Color(51, 153, 255));
            rbCategoria.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            rbCategoria.setForeground(new java.awt.Color(255, 255, 255));
            rbCategoria.setText("Categoria");
            rbCategoria.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    rbCategoriaActionPerformed(evt);
                }
            });
            jPanel4.add(rbCategoria);
            rbCategoria.setBounds(530, 40, 100, 25);

            jLabel17.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabel17.setForeground(new java.awt.Color(255, 255, 255));
            jLabel17.setText("Espacio para seleccionar la fila");
            jPanel4.add(jLabel17);
            jLabel17.setBounds(10, 50, 190, 14);

            panelImage1.add(jPanel4);
            jPanel4.setBounds(0, 250, 880, 70);

            getContentPane().add(panelImage1, java.awt.BorderLayout.CENTER);

            setBounds(0, 0, 894, 581);
        }// </editor-fold>//GEN-END:initComponents

    private void bntNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntNuevoActionPerformed
        // TODO add your handling code here:
        Limpiar();
        Llenarcombos();
        Habilitar();
        nombre.requestFocus();
        stock.setEnabled(true);
        stock.setEditable(true);
        this.bntGuardar.setEnabled(true);
        this.bntModificar.setEnabled(false);
        this.bntEliminar.setEnabled(false);
        this.bntNuevo.setEnabled(false);
    }//GEN-LAST:event_bntNuevoActionPerformed

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
    private void bntGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntGuardarActionPerformed
        // TODO add your handling code here:
        //System.out.print(Float.parseFloat(Validar(precioC.getText())));
        //Float.parseFloat(Validar(precioC.getText())
        if (nombre.getText().equals("") || codigo.getText().equals("") || unidad.getSelectedIndex() <= 0 || marca.getSelectedIndex() <= 0 || categoria.getSelectedIndex() <= 0 || getFecha() == null || Float.parseFloat(Validar(precioC.getText())) <= 0 || Float.parseFloat(Validar(precioV.getText())) <= 0 || Float.parseFloat(Validar(precioCred.getText())) <= 0 || Float.parseFloat(Validar(precioDist.getText())) <= 0 || Float.parseFloat(Validar(precioEsp.getText())) <= 0) {
            JOptionPane.showInternalMessageDialog(this, "Complete los campos obligatorios");
        } else {
            int resp;
            resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Grabar el Registro?", "Pregunta", 0);

            if (resp == 0) {
                try {
                    conn = BdConexion.getConexion();
                    categorias cat = (categorias) categoria.getSelectedItem();
                    String idcat = cat.getID();
                    unidad un = (unidad) unidad.getSelectedItem();
                    String idun = un.getID();
                    marca mar = (marca) marca.getSelectedItem();
                    String marc = mar.getID();
                    int idproducto = 0;

                    PreparedStatement ps = conn.prepareStatement(sqlprod.NUEVOC, PreparedStatement.RETURN_GENERATED_KEYS);
                    ps.setString(1, codigo.getText());
                    ps.setString(2, nombre.getText());
                    ps.setInt(3, Integer.parseInt(idcat));
                    ps.setFloat(4, Float.parseFloat(Validar(precioC.getText())));
                    ps.setFloat(5, Float.parseFloat(Validar(precioV.getText())));
                    ps.setFloat(6, Float.parseFloat(Validar(precioCred.getText())));
                    ps.setFloat(7, Float.parseFloat(Validar(precioDist.getText())));
                    ps.setFloat(8, Float.parseFloat(Validar(precioEsp.getText())));
                    ps.setFloat(9, Float.parseFloat(Validar(stockMin.getText())));
                    ps.setFloat(10, Float.parseFloat(Validar(stock.getText())));
                    ps.setString(11, getFecha());
                    ps.setString(12, descripcion.getText());
                    ps.setInt(13, Integer.parseInt(idun));
                    ps.setInt(14, Integer.parseInt(marc));

                    int n = ps.executeUpdate();
                    if (n > 0) {
                        ResultSet rs = ps.getGeneratedKeys();
                        while (rs.next()) {
                            idproducto = rs.getInt(1);
                        }
                        /**
                         * agregar nuevo lote en tabla de lotes******
                         */
                        if ((Float.parseFloat(Validar(stock.getText()))) > 0) {

                            PreparedStatement ps2 = conn.prepareStatement(sqlprod.NUEVOL);
                            ps2.setFloat(1, Float.parseFloat(Validar(stock.getText())));
                            ps2.setFloat(2, Float.parseFloat(Validar(precioC.getText())));
                            ps2.setString(3, getFecha());
                            ps2.setFloat(4, Float.parseFloat(Validar(stock.getText())));
                            ps2.setInt(5, (idproducto));
                            ps2.setString(6, "INICIAL");
                            ps2.setInt(7, (1));
                            int n2 = ps2.executeUpdate();

                            if (n2 > 0) {
                                Llenar();
                                Desabilitar();
                                Limpiar();
                                stock.setEnabled(false);
                                stock.setEditable(false);
                                JOptionPane.showInternalMessageDialog(this, "Datos guardados correctamente");

                            }
                        }
                        if (n > 0) {
                            Llenar();
                            Desabilitar();
                            Limpiar();
                            stock.setEnabled(false);
                            stock.setEditable(false);
                            txtDato.requestFocus();
                            JOptionPane.showInternalMessageDialog(this, "Datos guardados correctamente");

                        }
                    }
                    //conn.close();
                } catch (SQLException e) {
                    if (e.getErrorCode() == 1062) {
                        JOptionPane.showInternalMessageDialog(this,
                                "Error al guardar \n El Codigo ya existe ingrese uno diferente", "Error ", JOptionPane.ERROR_MESSAGE);
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

    private void tproductosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tproductosMouseClicked
        // TODO add your handling code here:
        if (evt.getButton() == 1) {
            int fila = tproductos.getSelectedRow();
            if (tproductos.getValueAt(fila, 0) != null) {
                try {
                    categoria.setEditable(true);
                    unidad.setEditable(true);
                    marca.setEditable(true);
                    stock.setEnabled(false);
                    stock.setEditable(false);

                    conn = BdConexion.getConexion();
                    Habilitar();
                    conn = BdConexion.getConexion();
                    String sql = sqlprod.LLENAR + sqlprod.WHERE + sqlprod.ID + sqlprod.IGUAL + tproductos.getValueAt(fila, 0);
                    sent = conn.createStatement();
                    ResultSet rs = sent.executeQuery(sql);
                    rs.next();

                    nombre.setText(rs.getString("nombre"));
                    codigo.setText(rs.getString("codigo"));
                    int pr = 0;
                    pr = Integer.parseInt(hashcategoria.get(rs.getString("categoria.nombre")));
                    this.categoria.setSelectedIndex(pr);

                    pr = Integer.parseInt(hashunidad.get(rs.getString("unidad.nombre")));
                    this.unidad.setSelectedIndex(pr);

                    precioC.setText(rs.getString("PrecioCompra"));
                    precioV.setText(rs.getString("Precioventa"));
                    precioCred.setText(rs.getString("preciocredito"));
                    precioDist.setText(rs.getString("preciodistribuidor"));
                    precioEsp.setText(rs.getString("precioespecial"));

                    stockMin.setText(rs.getString("Pivote"));
                    stock.setText(rs.getString("cantidad"));

                    if (rs.getString("Estado").equals("T")) {
                        rbEstado.setSelected(true);
                        rbEstado.setBackground(new java.awt.Color(102, 204, 0));
                    } else {
                        rbEstado.setSelected(false);
                        rbEstado.setBackground(Color.red);
                    }
                    dcFecha.setDate(rs.getDate("fecha"));
                    descripcion.setText(rs.getString("Descripcion"));
                    pr = Integer.parseInt(hashmarca.get(rs.getString("marca.nombre")));
                    this.marca.setSelectedIndex(pr);

                    this.bntGuardar.setEnabled(false);
                    this.bntModificar.setEnabled(true);
                    this.bntEliminar.setEnabled(true);
                    this.bntNuevo.setEnabled(false);

                    categoria.setEditable(false);
                    unidad.setEditable(false);
                    marca.setEditable(false);
                    //tproductos.setRequestFocusEnabled(true);
                    //conn.close();
                } catch (SQLException e) {
                    JOptionPane.showInternalMessageDialog(this, "Error al cargar los datos", "Error BD", JOptionPane.ERROR_MESSAGE);
                    System.out.print(e.getMessage());
                }
            }
        }
    }//GEN-LAST:event_tproductosMouseClicked

    private void bntEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntEliminarActionPerformed
        // TODO add your handling code here:
        int resp;
        resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Eliminar el Registro?", "Pregunta", 0);
        if (resp == 0) {
            try {
                conn = BdConexion.getConexion();
                int fila = tproductos.getSelectedRow();
                conn = BdConexion.getConexion();
                String sql = sqlprod.DELETEC + sqlprod.WHERE + sqlprod.ID + sqlprod.IGUAL + tproductos.getValueAt(fila, 0);
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
                            "Error al eliminar \n El material esta siendo utilizado en Compras y Ventas\n y no puede ser eliminado", "Error ", JOptionPane.ERROR_MESSAGE);
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
        if (nombre.getText().equals("") || codigo.getText().equals("") || unidad.getSelectedIndex() == 0 || marca.getSelectedIndex() == 0 || categoria.getSelectedIndex() == 0 || getFecha() == null) {
            JOptionPane.showInternalMessageDialog(this, "Complete los campos obligatorios");
        } else {
            int resp;
            resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Modidicar el Registro?", "Pregunta", 0);
            if (resp == 0) {
                try {
                    conn = BdConexion.getConexion();
                    int fila = tproductos.getSelectedRow();
                    conn = BdConexion.getConexion();
                    String dao = (String) tproductos.getValueAt(fila, 0);

                    categorias cat = (categorias) categoria.getSelectedItem();
                    String idcat = cat.getID();
                    unidad un = (unidad) unidad.getSelectedItem();
                    String idun = un.getID();
                    marca mar = (marca) marca.getSelectedItem();
                    String marc = mar.getID();
                    //PreparedStatement nos permite crear instrucciones SQL compiladas, que se ejecutan con más efi ciencia que los objetos Statement
                    //también pueden especifi car parámetros,lo cual las hace más fl exibles que las instrucciones Statement
                    PreparedStatement ps = conn.prepareCall(sqlprod.UPDATEC);

                    ps.setString(1, codigo.getText());
                    ps.setString(2, nombre.getText());
                    ps.setString(3, (String) idcat);
                    ps.setFloat(4, Float.parseFloat(Validar(precioC.getText())));
                    ps.setFloat(5, Float.parseFloat(Validar(precioV.getText())));
                    ps.setFloat(6, Float.parseFloat(Validar(precioCred.getText())));
                    ps.setFloat(7, Float.parseFloat(Validar(precioDist.getText())));
                    ps.setFloat(8, Float.parseFloat(Validar(precioEsp.getText())));
                    ps.setFloat(9, Float.parseFloat(Validar(stockMin.getText())));
                    ps.setFloat(10, Float.parseFloat(Validar(stock.getText())));
                    ps.setString(11, getFecha());

                    if (this.rbEstado.isSelected()) {
                        ps.setString(12, "T");
                    } else {
                        ps.setString(12, "F");
                    }
                    ps.setString(13, descripcion.getText());
                    ps.setString(14, (String) idun);
                    ps.setString(15, (String) marc);
                    ps.setString(16, dao);

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
                                "Error al modificar \n El Codigo ya existe ingrese uno diferente", "Error ", JOptionPane.ERROR_MESSAGE);
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
        stock.setEnabled(false);
        stock.setEditable(false);
        txtDato.requestFocus();

    }//GEN-LAST:event_bntCancelarActionPerformed

    private void buttonMostrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonMostrarActionPerformed
        // TODO add your handling code here:
        Llenar();
        Limpiar();
        Desabilitar();
        txtDato.requestFocus();
    }//GEN-LAST:event_buttonMostrarActionPerformed

    private void rbCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCodigoActionPerformed
        // TODO add your handling code here:
        rbNombre.setSelected(false);
        rbMarca.setSelected(false);
        rbCategoria.setSelected(false);
        txtDato.requestFocus();

    }//GEN-LAST:event_rbCodigoActionPerformed

    private void rbNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbNombreActionPerformed
        // TODO add your handling code here:
        rbCodigo.setSelected(false);
        rbMarca.setSelected(false);
        rbCategoria.setSelected(false);
        txtDato.requestFocus();
    }//GEN-LAST:event_rbNombreActionPerformed

    private void rbEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbEstadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbEstadoActionPerformed

    private void rbMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbMarcaActionPerformed
        // TODO add your handling code here:
        rbNombre.setSelected(false);
        rbCodigo.setSelected(false);
        rbCategoria.setSelected(false);
        txtDato.requestFocus();
    }//GEN-LAST:event_rbMarcaActionPerformed

    private void rbCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCategoriaActionPerformed
        // TODO add your handling code here:
        rbNombre.setSelected(false);
        rbMarca.setSelected(false);
        rbCodigo.setSelected(false);
        txtDato.requestFocus();
    }//GEN-LAST:event_rbCategoriaActionPerformed

    private void txtDatoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDatoActionPerformed
        // TODO add your handling code here:
        MostrarTodo(txtDato.getText());
    }//GEN-LAST:event_txtDatoActionPerformed

    private void codigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codigoActionPerformed
        // TODO add your handling code here:
        if (existecodigo() == true) {
            JOptionPane.showInternalMessageDialog(this, "Error el codigo ingresado ya existe \n Porfavor ingrese un codigo diferente", "Error ", JOptionPane.ERROR_MESSAGE);
        } else if (existecodigo() == false) {
            JOptionPane.showInternalMessageDialog(this, "El codigo ingresado no existe puede continuar");
        }
    }//GEN-LAST:event_codigoActionPerformed

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
    private javax.swing.JComboBox categoria;
    private elaprendiz.gui.textField.TextField codigo;
    private com.toedter.calendar.JDateChooser dcFecha;
    private javax.swing.JTextArea descripcion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox marca;
    private elaprendiz.gui.textField.TextField nombre;
    private elaprendiz.gui.panel.PanelImage panelImage1;
    private javax.swing.JPanel pnlActionButtons;
    private javax.swing.JPanel pnlPaginador;
    private javax.swing.JFormattedTextField precioC;
    private javax.swing.JFormattedTextField precioCred;
    private javax.swing.JFormattedTextField precioDist;
    private javax.swing.JFormattedTextField precioEsp;
    private javax.swing.JFormattedTextField precioV;
    private javax.swing.JRadioButton rbCategoria;
    private javax.swing.JRadioButton rbCodigo;
    private javax.swing.JRadioButton rbEstado;
    private javax.swing.JRadioButton rbMarca;
    private javax.swing.JRadioButton rbNombre;
    private javax.swing.JFormattedTextField stock;
    private javax.swing.JFormattedTextField stockMin;
    private javax.swing.JTable tproductos;
    private elaprendiz.gui.textField.TextField txtDato;
    private javax.swing.JComboBox unidad;
    // End of variables declaration//GEN-END:variables
}
