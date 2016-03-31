/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import BD.BdConexion;
import excepciones.Helper;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author GLARA
 */
public class CreditosVencidos {

    Connection conn;//getConnection intentara establecer una conexión.
    Statement sent;
    SimpleDateFormat formatof = new SimpleDateFormat("dd-MM-yyyy");

    /*
     * metodo para convertir String a Calendar y poderlo manipular
     * devuelve un Calendar 
     */
    private Calendar convierteacalendar(String fecha) {

        try {
            String dateStr = fecha;
            SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
            Date dateObj = curFormater.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateObj);
            return calendar;
        } catch (ParseException ex) {
            Logger.getLogger(CreditosVencidos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*
     * metodo para sumar 30 dias a un Calendar 
     * devuelve un Calendar 
     */
    private Calendar sumar1diascalendar(Calendar fecha) {
        fecha.add(Calendar.DATE, 1);
        return fecha;
    }

    /*
     * metodo para sumar 1 dia a un Calendar 
     * devuelve un Calendar 
     */
    private Calendar sumar30diascalendar(Calendar fecha) {
        fecha.add(Calendar.DATE, 30);
        return fecha;
    }

    /*
     * metodo para verificar si ya existe la mora para una fecha especificada
     * devuelve un Boolean
     */
    private boolean existeinteres(Calendar fechafin, int idsalida) {
        try {
            Calendar dcFech = fechafin;
            String fecha = Helper.getFechaFormateada(dcFech.getTime(), Helper.ANIO_MES_DIA);

            conn = BdConexion.getConexion();
            String sql = "select fechaal from detalleinteres where detalleinteres.salida_idsalida='" + idsalida + "' and detalleinteres.fechaal='" + fecha + "'";
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
            JOptionPane.showMessageDialog(null, "Ocurrio un error al cargar los datos" + e);
        }
        return false;
    }

    /*
     * metodo principal es llamado desde el Login para comenzar el proceso, establece la consulta y la
     * fecha actual del sistema=(PC) y se los envia como parametros al metodo listacreditos().
     */
    public void vencidos() {

        Calendar dcFech = Calendar.getInstance();
        String fechaactual = Helper.getFechaFormateada(dcFech.getTime(), Helper.ANIO_MES_DIA);
        String sql;
        sql = "SELECT clientes.nombre,clientes.direccion,clientes.nit,salida.idsalida,salida.saldo,salida.fechapago,usuario.nombreusuario,salida.fecha,\n"
                + "     salida.total,salida.interesactual,tipopago.descripcion,salida.clientes_idclientes,salida.estado\n"
                + "FROM clientes INNER JOIN salida ON clientes.idClientes = salida.clientes_idclientes\n"
                + "     INNER JOIN usuario ON salida.usuario_idusuario = usuario.idusuario\n"
                + "     INNER JOIN tipopago ON salida.tipopago_idtipopago = tipopago.idtipopago\n"
                + "WHERE salida.fechapago < '" + fechaactual + "' and  salida.estado='T' and salida.tipopago_idtipopago > 1 order by salida.salida asc";

        listacreditos(sql, dcFech);
    }

    /*
     * Se ejecuta la consulta y filtra los creditos vencidos para
     * posteriormente enviar los parametros necesarios al metodo calcularinteres().
     */
    public void listacreditos(String sql, Calendar fechaactual) {
        try {
            conn = BdConexion.getConexion();
            sent = conn.createStatement();// crea objeto Statement para consultar la base de datos
            ResultSet rs = sent.executeQuery(sql);// especifica la consulta y la ejecuta
            if (rs.next()) {
                rs.beforeFirst();//regresa el puntero al primer registro
                while (rs.next()) {
                    Object[] fila = new String[6];

                    fila[0] = rs.getString("salida.idsalida");
                    fila[1] = rs.getString("salida.clientes_idclientes");
                    fila[2] = Helper.getFechaFormateada(rs.getDate("salida.fechapago"), Helper.ANIO_MES_DIA); //formatof.format(rs.getDate("salida.fechapago"));
                    fila[3] = rs.getString("salida.saldo");
                    fila[4] = Helper.getFechaFormateada(fechaactual.getTime(), Helper.ANIO_MES_DIA);
                    if (rs.getString("salida.interesactual") == null) {
                        fila[5] = "0";
                    } else {
                        fila[5] = rs.getString("salida.interesactual");
                    }

                    calcularinteres(fila);
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron Intereses");
            }
            //JOptionPane.showMessageDialog(null, "Se han calculado intereses");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los datos " + e, "Error ", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * El metodo listacreditos(), envia los parametos, para comenzar el proceso de
     * calculo y guardar en la BD los intereses en periodos de 30 dias hasta que la fecha de interes
     * sea menor a la fehca del dia=(PC) y actualizar los saldos por intereses.
     */
    public void calcularinteres(Object[] fila) throws SQLException {
        try {
            boolean estado = true; // estado para ejecutar el while

            //Variables para el calculo de intereses
            int idsalida = 0, idcliente = 0;
            Calendar fechadepago, fechactual, fechainteres, fechadel;
            float saldo, interesactual, tasa = (float) 3.00;

            //Obtener los datos y pasarlos a las variables
            idsalida = Integer.parseInt((String) fila[0]);
            idcliente = Integer.parseInt((String) fila[1]);
            fechadepago = convierteacalendar((String) fila[2]);
            fechadel = sumar1diascalendar(convierteacalendar((String) fila[2]));
            fechainteres = sumar30diascalendar(fechadepago);
            saldo = Float.parseFloat((String) fila[3]);
            saldo = (float) (Math.round((saldo) * 100.0) / 100.0);
            fechactual = convierteacalendar((String) fila[4]);
            interesactual = Float.parseFloat((String) fila[5]);
            interesactual = (float) (Math.round((interesactual) * 100.0) / 100.0);
            float nsaldointeres = interesactual;

            //Inicio del while********************
            while (true == estado) {
                if (existeinteres(fechainteres, idsalida) != false) {
                    if (fechainteres.after(fechactual)) {
                        estado = false;
                    } else {
                        fechainteres = sumar30diascalendar(fechainteres);
                        fechadel = sumar30diascalendar(fechadel);
                        estado = true;
                    }
                } else if (existeinteres(fechainteres, idsalida) != true) {
                    try {
                        //Login entrar = new Login();//Usuario quien realiza el calculo de interes
                        float montointeres = CalculoInteres.interesSimple(saldo, tasa, 1);
                        nsaldointeres += (float) (Math.round((/*interesactual + */montointeres) * 100.0) / 100.0);
                        int n, n2;
                        //System.out.print("Monto " + montointeres + "  fecha del " + (Helper.getFechaFormateada(fechadel.getTime(), Helper.ANIO_MES_DIA)) + "  fecha interes " + Helper.getFechaFormateada(fechainteres.getTime(), Helper.ANIO_MES_DIA) + "  id salida " + idsalida + "  id cliente " + idcliente + "  fecha actual " + Helper.getFechaFormateada(fechactual.getTime(), Helper.ANIO_MES_DIA) + "\n");
                        conn = BdConexion.getConexion();
                        String sql = "insert into detalleinteres (salida_idsalida,fechadel,fechaal,montointeres,saldoactual,interesactual,nsaldointeres,fecharegistro,usuario_idusuario) values (?,?,?,?,?,?,?,?,?)";

                        PreparedStatement ps = conn.prepareStatement(sql/*,PreparedStatement.RETURN_GENERATED_KEYS*/);
                        ps.setString(1, "" + idsalida);
                        ps.setString(2, Helper.getFechaFormateada(fechadel.getTime(), Helper.ANIO_MES_DIA));
                        ps.setString(3, Helper.getFechaFormateada(fechainteres.getTime(), Helper.ANIO_MES_DIA));
                        ps.setFloat(4, montointeres);
                        ps.setFloat(5, saldo);
                        ps.setFloat(6, /*interesactual*/ (nsaldointeres - montointeres));
                        ps.setFloat(7, nsaldointeres);
                        ps.setString(8, Helper.getFechaFormateada(fechactual.getTime(), Helper.ANIO_MES_DIA));
                        ps.setString(9, "" + AccesoUsuario.idusu());

                        n = ps.executeUpdate();
                        if (n > 0) {
                            String sql2 = "update salida set  interesactual=? where salida.idsalida=?";
                            PreparedStatement ps2 = conn.prepareStatement(sql2);
                            ps2.setFloat(1, nsaldointeres);
                            ps2.setString(2, "" + idsalida);
                            n2 = ps2.executeUpdate();

                            if (n2 > 0) {
                                if (fechainteres.after(fechactual)) {
                                    estado = false;
                                } else {
                                    fechainteres = sumar30diascalendar(fechainteres);
                                    fechadel = sumar30diascalendar(fechadel);
                                    estado = true;
                                }
                            } else {
                                estado = false;
                                JOptionPane.showMessageDialog(null, "Ocurrio un error al Guardar");
                            }
                        }
                        //conn.close();
                    } catch (HeadlessException | SQLException e) {
                        estado = false;
                        JOptionPane.showMessageDialog(null, "Ocurrio un error : " + e);
                    }
                }
            }//Fin while ***************************
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ocurio un error : " + e);
        }

    }

}
