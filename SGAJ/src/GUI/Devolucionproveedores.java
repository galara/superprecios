/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import BD.BdConexion;
import static GUI.MenuPrincipal.panel_center;
import Modelos.AccesoUsuario;
import excepciones.VerificadorEntrada;
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
public class Devolucionproveedores extends javax.swing.JInternalFrame {

    private String archivoRecurso = "controlador-bd";
    DefaultTableModel model, model2;
    Connection conn;//getConnection intentara establecer una conexión.
    Statement sent;
    String[] titulos = {"Id", "Codigo", "Descripción", "Cantidad", "Precio", "Subtotal", "idlote", "idcompra", "Devolucion"};
    String[] titulos2 = {"idp", "idc", "Fecha", "No.Doc", "Total", "saldo", "FechaPago"};
    String idproveedor, idcompras;
    SimpleDateFormat formatof = new SimpleDateFormat("dd/MM/yyyy");
    //java.sql.Connection conn;//getConnection intentara establecer una conexión.
    /**
     * Creates new form pedido
     */
    public Devolucionproveedores() {

        initComponents();
        //Llenar();
        addEscapeKey();
        //addEscapeKey2();
        formatotabla2();
        formatotabla();

        tabladetallecompra.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent arg0) {
                int key = arg0.getKeyCode();
                if (key == java.awt.event.KeyEvent.VK_SPACE) {
                    devolverproductos();
                }
            }
        });

        tablacomprasporpagar.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent arg0) {
                int key = arg0.getKeyCode();
                if (key == java.awt.event.KeyEvent.VK_SPACE) {
                    seleccionarcompra();
                }
                if (key == java.awt.event.KeyEvent.VK_DOWN || key == java.awt.event.KeyEvent.VK_UP) {
                    removejtable();
                }
            }
        });

        tablacomprasporpagar.getModel().addTableModelListener((TableModelEvent e) -> {
            formatotabla2();
        });

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
        idproveedor = (datos[5]);
    }

    private String getFecha() {

        try {
            String fecha;
            int años = dcFechaini1.getCalendar().get(Calendar.YEAR);
            int dias = dcFechaini1.getCalendar().get(Calendar.DAY_OF_MONTH);
            int mess = dcFechaini1.getCalendar().get(Calendar.MONTH) + 1;
//            int hours = dcFechaini1.getCalendar().get(Calendar.HOUR_OF_DAY);
//            int minutes = dcFechaini1.getCalendar().get(Calendar.MINUTE);
//            int seconds = dcFechaini1.getCalendar().get(Calendar.SECOND);

            fecha = "" + años + "-" + mess + "-" + dias;// + " " + hours + ":" + minutes + ":" + seconds;
            return fecha;
        } catch (Exception e) {
            JOptionPane.showInternalMessageDialog(this, "Verifique la fecha");
            System.out.print(e.getMessage());
        }
        return null;

    }

    private String getFecha2() {

        try {
            String fecha;
            int años = dcFechafin1.getCalendar().get(Calendar.YEAR);
            int dias = dcFechafin1.getCalendar().get(Calendar.DAY_OF_MONTH);
            int mess = dcFechafin1.getCalendar().get(Calendar.MONTH) + 1;
//            int hours = dcFechafin1.getCalendar().get(Calendar.HOUR_OF_DAY);
//            int minutes = dcFechafin1.getCalendar().get(Calendar.MINUTE);
//            int seconds = dcFechafin1.getCalendar().get(Calendar.SECOND);

            fecha = "" + años + "-" + mess + "-" + dias;//+ " " + hours + ":" + minutes + ":" + seconds;
            return fecha;
        } catch (Exception e) {
            JOptionPane.showInternalMessageDialog(this, "Verifique la fecha");
            System.out.print(e.getMessage());
        }
        return null;

    }

    private String getFecha3() {

        try {
            Calendar dcFech = Calendar.getInstance();
            String fecha;
            int años = dcFech.get(Calendar.YEAR);
            int dias = dcFech.get(Calendar.DAY_OF_MONTH);
            int mess = dcFech.get(Calendar.MONTH) + 1;
            fecha = "" + años + "-" + mess + "-" + dias;
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
        for (int i = 0; i < 9; i++) {
            column = tabladetallecompra.getColumnModel().getColumn(i);
            if (i == 0 || i == 6 || i == 7) {
                column.setMaxWidth(0);
                column.setMinWidth(0);
            } else if (i == 1) {
                column.setPreferredWidth(20); //Difine el ancho de la columna
                tabladetallecompra.getColumnModel().getColumn(i).setCellRenderer(modelocentrar);
            } else if (i == 2) {
                column.setPreferredWidth(228); //Difine el ancho de la columna
            } else if (i > 2 & i <= 5 || i == 8) {
                column.setPreferredWidth(39);//Difine el ancho de la columna
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
            if (i == 0 || i == 1 || i == 7) {
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

    private boolean existendevoluciones() {
        try {
            int fila = tablacomprasporpagar.getSelectedRow();
            String Dato = (String) tablacomprasporpagar.getValueAt(fila, 1).toString();
            conn = BdConexion.getConexion();
            String sql = "select devoluciones.cantidad,devoluciones.precio,devoluciones.idcompra,devoluciones.entradasalida from devoluciones INNER JOIN producto on producto.idproducto=devoluciones.idproducto INNER JOIN compra on compra.idcompra=devoluciones.idcompra INNER JOIN usuario on usuario.idusuario=devoluciones.idusuario where devoluciones.idcompra=" + Dato + " and devoluciones.entradasalida='PROVEEDOR' order by devoluciones.fecha asc";
            //String sql = "select xcobrarclientes.idxcobrarclientes,xcobrarclientes.fecha,xcobrarclientes.monto,salida.idsalida,salida.salida,usuario.nombreusuario from xcobrarclientes INNER JOIN salida on salida.idsalida=xcobrarclientes.salida_idsalida INNER JOIN usuario on usuario.idusuario=xcobrarclientes.usuario_idusuario where xcobrarclientes.salida_idsalida=" + Dato + " group by xcobrarclientes.idxcobrarclientes order by xcobrarclientes.idxcobrarclientes asc";
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
            String fech1 = getFecha();
            String fech2 = getFecha2();
            //System.out.print(fech1 + "  -  " + fech2);
            String sql = "";
            if (this.jRadioButton3.isSelected()) {
                sql = "select * from compra where compra.fecha BETWEEN '" + fech1 + "' and '" + fech2 + "' and compra.proveedor_idproveedor='" + Dato + "'" + " order by compra.idcompra";
            }
            if (this.jRadioButton5.isSelected()) {
                sql = "select * from compra where numdoc=" + numerofactura.getText() + " and compra.proveedor_idproveedor='" + Dato + "'" + " order by compra.idcompra";
            }
            //String sql = "select * from compra where proveedor_idproveedor=" + Dato;
            ResultSet rs = sent.executeQuery(sql);// especifica la consulta y la ejecuta

            if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                rs.beforeFirst();//regresa el puntero al primer registro

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
                formatotabla2();
            } else {
                JOptionPane.showInternalMessageDialog(this, "No se encontraron datos con las condiciones ingresadas \n"
                        + "         Verifique los datos e intente denuevo          ");
            }
            //conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en BD " + e.toString());
            this.dispose();
        }
    }

    private void MostrarTodo(String Dato) {
        try {
            conn = BdConexion.getConexion();
            String sql = "select producto_idproducto,producto.codigo, producto.nombre,lote.cantidad,lote.precio,lote.cantidad*lote.precio,lote.idlote,lote.compra_idcompra,lote.devolucion  from lote INNER JOIN producto on producto.idproducto=lote.producto_idproducto where lote.compra_idcompra=" + Dato + " order by idlote asc";
            removejtable();
            sent = conn.createStatement();// crea objeto Statement para consultar la base de datos
            ResultSet rs = sent.executeQuery(sql);// especifica la consulta y la ejecuta

            if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                int count = 0;
                float subt, cant, prec;
                rs.beforeFirst();//regresa el puntero al primer registro
                String[] fila = new String[9];
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
                    fila[6] = rs.getString("lote.idlote");
                    fila[7] = rs.getString("lote.compra_idcompra");
                    fila[8] = rs.getString("lote.devolucion");

                    model.addRow(fila);
                    count = count + 1;
                }
                tabladetallecompra.setModel(model);
                formatotabla();
            } else {
                JOptionPane.showInternalMessageDialog(this, "El dato no fue encontrado");
            }
            //conn.close();
        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(this, "El dato no fue encontrado");
            System.out.print(e.getMessage());
        }
    }

    public void devuelve() {
        String Cant = "0";
        float value = 0;

        try {

            Cant = JOptionPane.showInputDialog(null, "Cantidad de devolucion");
            value = Float.parseFloat(Cant);

        } catch (Exception e) {

            JOptionPane.showInternalMessageDialog(this, "La cantidad deve ser mayor a cero");
            Cant = "0";
            System.out.print(e.toString());

        }

        if (Cant != null || !Cant.equals("") || !Cant.equals(" ") || !Cant.equals("0")) {
            if (value > 0) {
                try {
                    // Se crea un Statement, para realizar la consulta
                    conn = BdConexion.getConexion();
                    Statement s = (Statement) conn.createStatement();
                    int p = tabladetallecompra.getSelectedRow(); float cantidad = 0;

                    String idcompraseleccionada = (String) model.getValueAt(p, 7);
                    //String Cant = JOptionPane.showInputDialog(null, "Cantidad de devolucion");
                    cantidad = Float.parseFloat(Cant);
                    cantidad=(float) (Math.round(cantidad * 100.0) / 100.0);
                    ResultSet rs = null;

                    rs = s.executeQuery("SELECT idProducto, cantidad FROM producto WHERE idproducto='" + model.getValueAt(p, 0) + "'");
                    float cant = 0;
                    while (rs.next()) {
                        cant = rs.getFloat("cantidad");
                    }

                    rs = s.executeQuery("SELECT  cantidad FROM lote WHERE idlote='" + model.getValueAt(p, 6) + "'");
                    float cant2 = 0;
                    while (rs.next()) {
                        cant2 = rs.getFloat("cantidad");
                    }

                    rs = s.executeQuery("SELECT  cantidad,devolucion,stock FROM lote WHERE idlote='" + model.getValueAt(p, 6) + "'");
                    float cant1 = 0, cant22 = 0, stock = 0;
                    while (rs.next()) {
                        cant22 = rs.getFloat("cantidad");
                        cant1 = rs.getFloat("devolucion");
                        stock = rs.getFloat("stock");
                    }
                    rs = s.executeQuery("SELECT  total,saldo FROM compra WHERE idcompra='" + model.getValueAt(p, 7) + "'");
                    float total = 0, saldo = 0, total2 = 0, saldo2 = 0;
                    while (rs.next()) {
                        total = rs.getFloat("total");
                        saldo = rs.getFloat("saldo");
                    }

                    rs = s.executeQuery("SELECT  SUM(monto) FROM xpagardetalle WHERE compra_idcompra='" + model.getValueAt(p, 7) + "'");
                    float monto = 0, nmonto = 0;
                    while (rs.next()) {
                        monto = (float) (Math.round((rs.getFloat("SUM(monto)")) * 100.0) / 100.0);
                        //System.out.print("suma monto" + monto);
                    }

                    if (cantidad <= stock) {

                        float SumaP = cant - cantidad;
                        SumaP=(float) (Math.round(SumaP * 100.0) / 100.0);
                        float sumaL = cant2 - cantidad;
                        sumaL=(float) (Math.round(sumaL * 100.0) / 100.0);
                        float devolucion = cant1 + cantidad;
                        devolucion=(float) (Math.round(devolucion * 100.0) / 100.0);
                        float sumalote = stock - cantidad;
                        sumalote=(float) (Math.round(sumalote * 100.0) / 100.0);
                                
                        String estado1 = "T", fecha = "", estado2 = "F";
                        if (sumalote > 0) {
                            estado1 = "T";
                        }
                        if (sumalote == 0) {
                            estado1 = "F";
                        }
                        //inicio calcular nuevo saldo, nuevo stock, y nuevo estado**********
                        float pr = Float.parseFloat((String) model.getValueAt(p, 4));
                        float ntotal = (float) (Math.round((cantidad * pr) * 100.0) / 100.0);
                        float nuevototal = (float) (Math.round((total - ntotal) * 100.0) / 100.0);

                        if (nuevototal >= 0) {
                            total2 = nuevototal;
                            //saldo2=(saldo-ntotal);
                        }
                        if ((saldo - ntotal) == 0) {
                            estado2 = "F";
                            saldo2 = (float) (Math.round((saldo - ntotal) * 100.0) / 100.0);
                        }
                        if ((saldo - ntotal) > 0) {
                            estado2 = "T";
                            saldo2 = (float) (Math.round((saldo - ntotal) * 100.0) / 100.0);
                        }
                //fin calcular nuevo saldo, nuevo stock, y nuevo estado***************

                        // calcular el valor del monto que nos devolveran
                        if (saldo2 < monto) {
                            nmonto = (float) (Math.round((total2 - monto) * 100.0) / 100.0);
                            //System.out.print("monto " + nmonto);
                        }

                        try {
                            s.executeUpdate("UPDATE producto SET cantidad='" + SumaP + "' WHERE idProducto =" + model.getValueAt(p, 0));
                            s.executeUpdate("UPDATE compra SET total='" + total2 + "', saldo='" + saldo2 + "', status='" + estado2 + "' WHERE idcompra =" + model.getValueAt(p, 7));
                            s.executeUpdate("UPDATE lote SET cantidad='" + sumaL + "', stock='" + sumalote + "', devolucion='" + devolucion + "', estado='" + estado1 + "' WHERE idlote =" + model.getValueAt(p, 6));
                            //{"Id", "Codigo", "Descripción", "Cantidad", "Precio", "Subtotal", "idlote", "idcompra", "Devolucion"};
                            //Login entrar = new Login();
                            PreparedStatement ps1 = conn.prepareCall("insert into devoluciones (idproducto,cantidad,precio,subtotal,idlote,idcompra,devolucion,fecha,entradasalida,idusuario) values (?,?,?,?,?,?,?,?,?,?)");
                            ps1.setString(1, (String) model.getValueAt(p, 0));
                            ps1.setString(2, (String) "" + cantidad);
                            ps1.setString(3, (String) model.getValueAt(p, 4));
                            ps1.setString(4, (String) ("" + Math.round((cantidad * pr) * 100.0) / 100.0));
                            ps1.setString(5, (String) model.getValueAt(p, 6));
                            ps1.setString(6, (String) model.getValueAt(p, 7));
                            ps1.setString(7, (String) model.getValueAt(p, 8));
                            ps1.setString(8, (String) getFecha3());
                            ps1.setString(9, (String) "PROVEEDOR");
                            ps1.setString(10, (String) "" + AccesoUsuario.idusu());
                            int n2 = ps1.executeUpdate();

                            if (nmonto < 0) {
                                PreparedStatement ps = conn.prepareCall("insert into xpagardetalle (fecha,monto,compra_idcompra,usuario_idusuario,observacion) values (?,?,?,?,?)");
                                ps.setString(1, getFecha3());
                                ps.setString(2, "" + nmonto);
                                ps.setString(3, (String) model.getValueAt(p, 7));
                                //Login entrar = new Login();
                                ps.setString(4, "" + AccesoUsuario.idusu());
                                ps.setString(5, "Por Devolución de productos al proveedor ");
                                int n = ps.executeUpdate();
                            }
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, "Error No se puede hacer la devolucion");
                            System.out.print("Error " + e);
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "Error No se puede hacer la devolucion no hay productos en el Lote\n"
                                + "Solo tiene "+stock+" Productos de "+cant22+" que compro");
                    }

                    for (int h = model.getRowCount(); h > 0; h--) {
                        model.removeRow(h - 1);
                    }
                    Llenar(idproveedor);
                    MostrarTodo(idcompraseleccionada);
                    //conn.close();
                    JOptionPane.showMessageDialog(null, "Devolución operada exitosamente");

                } catch (SQLException ex) {
                    //Logger.getLogger(devoluciones.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, "Error No se puede hacer la devolucion");
                    System.out.print("Error " + ex);
                }
            }
        } else {
            JOptionPane.showInternalMessageDialog(this, "La cantidad no puede ser menor o igual a cero");
        }
    }

    private void devolverproductos() {

        if (tabladetallecompra.getRowCount() == 0 && tabladetallecompra.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "La tabla no contiene datos que Devolver");
        } else {
            if (tabladetallecompra.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(null, "No se ha seleccionado un registro");
            } else {
                int p = tabladetallecompra.getSelectedRow();
                float cant = Float.parseFloat((String) model.getValueAt(p, 3));
                if (cant > 0) {
                    devuelve();
                } else if (cant == 0) {
                    JOptionPane.showInternalMessageDialog(this, "No hay productos que devolver Cantidad = " + cant);
                }

            }
        }
    }

    private void seleccionarcompra() {
        int fila = tablacomprasporpagar.getSelectedRow();
        String dato = tablacomprasporpagar.getValueAt(fila, 1).toString();
        MostrarTodo(dato);
        idcompras = dato;
    }

    private void detalledevoluciones() {
        if (tablacomprasporpagar.getRowCount() == 0 && tablacomprasporpagar.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "La tabla no contiene datos que mostrar");
        } else {
            if (tablacomprasporpagar.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(null, "No se ha seleccionado un registro");
            } else {

                if (existendevoluciones() == true) {

                    Detalledevoluvionesproveedor nuevasol = new Detalledevoluvionesproveedor();
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
                    if (existendevoluciones() == false) {
                        JOptionPane.showInternalMessageDialog(this, " No existen Devoluciones de la Compra seleccionada ");
                    }
                }
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
                        JOptionPane.showInternalMessageDialog(this, " No existen Abonos de la Compra seleccionada ");
                    }
                }
            }
        }
    }

    private void abrircomprobante2(int idabono) {
        if (idabono > 0) {
            try {
                String ids = "";
                ids = ("" + idabono);
                String fech1 = getFecha();
                String fech2 = getFecha2();
                int tipo = Integer.parseInt(ids);

                String archivo = "";
                archivo = "Estadocuentaproveedor.jasper";
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
                parametro.put("defecha", fech1);
                parametro.put("afecha", fech2);
                JasperPrint impresor = JasperFillManager.fillReport(masterReport, parametro, conn);
                JasperViewer jviewer = new JasperViewer(impresor, false);
                jviewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
                jviewer.setTitle("Estado de Cuenta Cliente");
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
        java.awt.GridBagConstraints gridBagConstraints;

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
        jPanel6 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jRadioButton3 = new javax.swing.JRadioButton();
        dcFechaini1 = new org.freixas.jcalendar.JCalendarCombo();
        dcFechafin1 = new org.freixas.jcalendar.JCalendarCombo();
        jRadioButton5 = new javax.swing.JRadioButton();
        numerofactura = new javax.swing.JTextField();
        btnbuscarproducto1 = new elaprendiz.gui.button.ButtonRect();
        jcMousePanel7 = new jcMousePanel.jcMousePanel();
        jScrollPanedetallecompra = new javax.swing.JScrollPane();
        tabladetallecompra = new javax.swing.JTable();
        jScrollPanedetallecompra2 = new javax.swing.JScrollPane();
        tablacomprasporpagar = new javax.swing.JTable();
        pnlActionButtons = new javax.swing.JPanel();
        btndevolcerproducto = new elaprendiz.gui.button.ButtonRect();
        btndetalleabonos = new elaprendiz.gui.button.ButtonRect();
        btnimprimirestado = new elaprendiz.gui.button.ButtonRect();
        bntSalir2 = new elaprendiz.gui.button.ButtonRect();
        pnlPaginador = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();

        setMaximizable(true);
        setTitle("Devolucion Proveedores");
        setToolTipText("");
        setName("Devolucionproveedores"); // NOI18N

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
        jLabel4.setBounds(40, 20, 80, 20);

        nombre.setEditable(false);
        nombre.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        nombre.setPreferredSize(new java.awt.Dimension(120, 21));
        jcMousePanel6.add(nombre);
        nombre.setBounds(140, 20, 250, 21);

        nit.setEditable(false);
        nit.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        nit.setPreferredSize(new java.awt.Dimension(120, 21));
        jcMousePanel6.add(nit);
        nit.setBounds(490, 20, 150, 21);

        telefono.setEditable(false);
        telefono.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        telefono.setPreferredSize(new java.awt.Dimension(120, 21));
        jcMousePanel6.add(telefono);
        telefono.setBounds(490, 50, 150, 21);

        correo.setEditable(false);
        correo.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        correo.setInputVerifier(new VerificadorEntrada(125,VerificadorEntrada.EMAIL));
        correo.setPreferredSize(new java.awt.Dimension(120, 21));
        jcMousePanel6.add(correo);
        correo.setBounds(140, 50, 250, 21);

        direccion.setEditable(false);
        direccion.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        direccion.setPreferredSize(new java.awt.Dimension(120, 21));
        jcMousePanel6.add(direccion);
        direccion.setBounds(140, 80, 500, 21);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel6.setText("Correo:");
        jcMousePanel6.add(jLabel6);
        jLabel6.setBounds(40, 50, 80, 20);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel11.setText("*Dirección:");
        jcMousePanel6.add(jLabel11);
        jLabel11.setBounds(40, 80, 80, 20);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel12.setText("Telefono:");
        jcMousePanel6.add(jLabel12);
        jLabel12.setBounds(400, 50, 80, 20);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel13.setText("*Nit:");
        jcMousePanel6.add(jLabel13);
        jLabel13.setBounds(400, 20, 80, 20);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Condiciones de busqueda   -  Enter o F5 para buscar", 0, 0, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel6.setOpaque(false);
        jPanel6.setLayout(null);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Del:");
        jPanel6.add(jLabel18);
        jLabel18.setBounds(110, 17, 40, 20);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Al:");
        jPanel6.add(jLabel19);
        jLabel19.setBounds(340, 17, 30, 20);

        jRadioButton3.setText("Filtro Fecha");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });
        jPanel6.add(jRadioButton3);
        jRadioButton3.setBounds(20, 17, 90, 23);

        dcFechaini1.setEnabled(false);
        dcFechaini1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        dcFechaini1.setName(""); // NOI18N
        dcFechaini1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dcFechaini1ActionPerformed(evt);
            }
        });
        dcFechaini1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dcFechaini1KeyPressed(evt);
            }
        });
        jPanel6.add(dcFechaini1);
        dcFechaini1.setBounds(160, 17, 180, 21);

        dcFechafin1.setEnabled(false);
        dcFechafin1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        dcFechafin1.setName(""); // NOI18N
        dcFechafin1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dcFechafin1ActionPerformed(evt);
            }
        });
        dcFechafin1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dcFechafin1KeyPressed(evt);
            }
        });
        jPanel6.add(dcFechafin1);
        dcFechafin1.setBounds(380, 17, 180, 21);

        jRadioButton5.setText("Filtro Documento");
        jRadioButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton5ActionPerformed(evt);
            }
        });
        jPanel6.add(jRadioButton5);
        jRadioButton5.setBounds(609, 17, 110, 23);

        numerofactura.setEditable(false);
        numerofactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numerofacturaActionPerformed(evt);
            }
        });
        jPanel6.add(numerofactura);
        numerofactura.setBounds(730, 17, 110, 23);

        jcMousePanel6.add(jPanel6);
        jPanel6.setBounds(10, 110, 910, 50);

        btnbuscarproducto1.setBackground(new java.awt.Color(102, 204, 0));
        btnbuscarproducto1.setText("Estado de cuenta");
        btnbuscarproducto1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscarproducto1ActionPerformed(evt);
            }
        });
        jcMousePanel6.add(btnbuscarproducto1);
        btnbuscarproducto1.setBounds(710, 50, 154, 25);

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
        jScrollPanedetallecompra.setViewportBorder(javax.swing.BorderFactory.createTitledBorder("Espacio=Devolver Articulo"));

        tabladetallecompra.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
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
                        .addComponent(jScrollPanedetallecompra2, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPanedetallecompra, javax.swing.GroupLayout.PREFERRED_SIZE, 579, javax.swing.GroupLayout.PREFERRED_SIZE))
                );
                jcMousePanel7Layout.setVerticalGroup(
                    jcMousePanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPanedetallecompra, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                    .addComponent(jScrollPanedetallecompra2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                );

                jcMousePanelprincipal.add(jcMousePanel7);
                jcMousePanel7.setBounds(0, 210, 930, 230);

                pnlActionButtons.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 153, 255), 1, true));
                pnlActionButtons.setOpaque(false);
                pnlActionButtons.setPreferredSize(new java.awt.Dimension(786, 52));
                java.awt.GridBagLayout pnlActionButtonsLayout = new java.awt.GridBagLayout();
                pnlActionButtonsLayout.columnWidths = new int[] {70};
                pnlActionButtons.setLayout(pnlActionButtonsLayout);

                btndevolcerproducto.setBackground(new java.awt.Color(51, 153, 255));
                btndevolcerproducto.setText("Devolver Producto");
                btndevolcerproducto.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        btndevolcerproductoActionPerformed(evt);
                    }
                });
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 2;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                gridBagConstraints.insets = new java.awt.Insets(13, 5, 12, 0);
                pnlActionButtons.add(btndevolcerproducto, gridBagConstraints);

                btndetalleabonos.setBackground(new java.awt.Color(51, 153, 255));
                btndetalleabonos.setText("Detalle de Devoluciones");
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

                btnimprimirestado.setBackground(new java.awt.Color(51, 153, 255));
                btnimprimirestado.setText("Detalle de Abonos");
                btnimprimirestado.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        btnimprimirestadoActionPerformed(evt);
                    }
                });
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 4;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                gridBagConstraints.insets = new java.awt.Insets(13, 5, 12, 0);
                pnlActionButtons.add(btnimprimirestado, gridBagConstraints);

                bntSalir2.setBackground(new java.awt.Color(51, 153, 255));
                bntSalir2.setText("Salir    ");
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
                jLabel15.setText("<--Devolucion a Proveedores-->");
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

    private void tablacomprasporpagarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablacomprasporpagarMouseClicked
        // TODO add your handling code here:
        int fila = tablacomprasporpagar.getSelectedRow();
        String dato = tablacomprasporpagar.getValueAt(fila, 1).toString();
        MostrarTodo(dato);
        idcompras = dato;
    }//GEN-LAST:event_tablacomprasporpagarMouseClicked

    private void bntSalir2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntSalir2ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_bntSalir2ActionPerformed

    private void btndevolcerproductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndevolcerproductoActionPerformed
        devolverproductos();
    }//GEN-LAST:event_btndevolcerproductoActionPerformed

    private void buttonRect1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRect1ActionPerformed
        // TODO add your handling code here:
        detalledevoluciones();
    }//GEN-LAST:event_buttonRect1ActionPerformed

    private void numerofacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numerofacturaActionPerformed
        // TODO add your handling code here:

        Llenar(idproveedor);
        removejtable();
//        Devolucionproveedores nuevasol = new Devolucionproveedores();
//        //if (panel_center.getComponentCount() > 0 & panel_center.getComponentCount() < 3) //solo uno en t
//        {
//            int fila = proveedores.getSelectedRow();
//            String dao = (String) proveedores.getValueAt(fila, 0);
//            panel_center.add(nuevasol);
//            nuevasol.show();// ver interno
//            nuevasol.setClosable(true);// icono de cerrar
//            nuevasol.toFront();//aparece al frente
//
//            String[] datosprov = datosproveedor();
//            nuevasol.llenardatos(datosprov);
//            nuevasol.Llenar(dao);
//        }
    }//GEN-LAST:event_numerofacturaActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        // TODO add your handling code here:
        if (this.jRadioButton3.isSelected()) {
            numerofactura.setEditable(false);
            jRadioButton5.setSelected(false);
            dcFechaini1.setEnabled(true);
            dcFechafin1.setEnabled(true);
        } else {
            //numerofactura.setEditable(false);
            //jRadioButton5.setSelected(false);
            dcFechaini1.setEnabled(false);
            dcFechafin1.setEnabled(false);
        }
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jRadioButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton5ActionPerformed
        // TODO add your handling code here:
        if (this.jRadioButton5.isSelected()) {
            jRadioButton3.setSelected(false);
            dcFechaini1.setEnabled(false);
            dcFechafin1.setEnabled(false);
            //numerofactura.setEnabled(true);
            numerofactura.setEditable(true);
        } else {
            //jRadioButton3.setSelected(false);
            //dcFechaini1.setEnabled(false);
            //dcFechafin1.setEnabled(false);
            numerofactura.setEditable(false);
        }


    }//GEN-LAST:event_jRadioButton5ActionPerformed

    private void dcFechafin1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dcFechafin1ActionPerformed
        // TODO add your handling code here:

        if (!dcFechafin1.getDate().equals(null)) {
            Llenar(idproveedor);
            removejtable();
        }

    }//GEN-LAST:event_dcFechafin1ActionPerformed

    private void dcFechafin1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dcFechafin1KeyPressed
        // TODO add your handling code here:
        int key = evt.getKeyCode();
        if (key == java.awt.event.KeyEvent.VK_F5) {
            Llenar(idproveedor);
            removejtable();
        }
        if (key == java.awt.event.KeyEvent.VK_ENTER) {
            Llenar(idproveedor);
            removejtable();
        }

        // }
    }//GEN-LAST:event_dcFechafin1KeyPressed

    private void dcFechaini1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dcFechaini1ActionPerformed
        // TODO add your handling code here:
        if (!dcFechafin1.getDate().equals(null)) {
            Llenar(idproveedor);
            removejtable();
        }
    }//GEN-LAST:event_dcFechaini1ActionPerformed

    private void dcFechaini1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dcFechaini1KeyPressed
        // TODO add your handling code here:
        int key = evt.getKeyCode();
        if (key == java.awt.event.KeyEvent.VK_F5) {
            Llenar(idproveedor);
            removejtable();
        }
        if (key == java.awt.event.KeyEvent.VK_ENTER) {
            Llenar(idproveedor);
            removejtable();
        }
    }//GEN-LAST:event_dcFechaini1KeyPressed

    private void btnimprimirestadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnimprimirestadoActionPerformed
        // TODO add your handling code here:
        detalleabonos();
    }//GEN-LAST:event_btnimprimirestadoActionPerformed

    private void btnbuscarproducto1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarproducto1ActionPerformed
        // TODO add your handling code here:
        int x = Integer.parseInt(idproveedor);
        abrircomprobante2(x);
    }//GEN-LAST:event_btnbuscarproducto1ActionPerformed
    private void teclaPresionada(int tecla) {
        if (tecla == KeyEvent.VK_F1) {
            this.dispose();
        }

        if (tecla == 27) {
            this.dispose();
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private elaprendiz.gui.button.ButtonRect bntSalir2;
    private elaprendiz.gui.button.ButtonRect btnbuscarproducto1;
    private elaprendiz.gui.button.ButtonRect btndetalleabonos;
    private elaprendiz.gui.button.ButtonRect btndevolcerproducto;
    private elaprendiz.gui.button.ButtonRect btnimprimirestado;
    private elaprendiz.gui.textField.TextField correo;
    private org.freixas.jcalendar.JCalendarCombo dcFechafin1;
    private org.freixas.jcalendar.JCalendarCombo dcFechaini1;
    private elaprendiz.gui.textField.TextField direccion;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JScrollPane jScrollPanedetallecompra;
    private javax.swing.JScrollPane jScrollPanedetallecompra2;
    private jcMousePanel.jcMousePanel jcMousePanel6;
    private jcMousePanel.jcMousePanel jcMousePanel7;
    private jcMousePanel.jcMousePanel jcMousePanelprincipal;
    private elaprendiz.gui.textField.TextField nit;
    private elaprendiz.gui.textField.TextField nombre;
    private javax.swing.JTextField numerofactura;
    private javax.swing.JPanel pnlActionButtons;
    private javax.swing.JPanel pnlPaginador;
    public static javax.swing.JTable tablacomprasporpagar;
    public static javax.swing.JTable tabladetallecompra;
    private elaprendiz.gui.textField.TextField telefono;
    // End of variables declaration//GEN-END:variables
}
