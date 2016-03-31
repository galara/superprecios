package BD;

import java.sql.*;

public class Conectiondb {

    //static Connection conn = null;
    static java.sql.Connection conn;//getConnection intentara establecer una conexión.
    static Statement st = null;
    static ResultSet rs = null;

//    public static Connection Enlace(Connection conn) throws SQLException {
//        try {
//            LeePropiedades.archivoRecurso = "controlador-bd";
//            DriverManager.registerDriver(new org.gjt.mm.mysql.Driver());
//            conn = DriverManager.getConnection(LeePropiedades.leeID("url"), LeePropiedades.leeID("usuario"), LeePropiedades.leeID("password"));
//
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, e);
//        }
//        return conn;
//    }

    public static Statement sta(Statement st) throws SQLException {
        //conn = Enlace(conn);
        conn = BdConexion.getConexion();
        
        st = conn.createStatement();
        return st;
    }

    public static ResultSet EnlCliente(ResultSet rs) throws SQLException {
        st = sta(st);
        rs = st.executeQuery("select * from proveedor");
        return rs;
    }

    public static ResultSet EnlSalidas(ResultSet rs,String fecha1, String fecha2) throws SQLException {
        st = sta(st);
        rs = st.executeQuery("SELECT\n"
                + "     salida.idsalida AS salida_idsalida,\n"
                + "     salida.salida AS salida_salida,\n"
                + "     salida.fecha AS salida_fecha,\n"
                + "     salida.total AS salida_total,\n"
                + "     clientes.nombre AS clientes_nombres,\n"
                + "     usuario.nombreusuario AS usuario_nombreusuario,\n"
                + "     salida.tipopago_idtipopago AS tipopago,\n"
                + "     salida.clientes_idclientes AS salida_clientes_idclientes\n"
                + "FROM\n"
                + "     clientes INNER JOIN  salida ON clientes.idclientes = salida.clientes_idclientes\n"
                + "     INNER JOIN usuario ON salida.usuario_idusuario = usuario.idusuario where salida.fecha BETWEEN '" + fecha1 + "' and '" + fecha2 + "' order BY salida.salida desc");
        return rs;
    }

    public static ResultSet EnlDSalidas(ResultSet rs, String f) throws SQLException {
        String f2 = f;
        st = sta(st);
        //rs=st.executeQuery("select * from detallesalida where iddetallesalida="+f2);
        rs = st.executeQuery("SELECT\n"
                + "     detallesalida.iddetallesalida AS detallesalida_iddetallesalida,\n"
                + "     detallesalida.cantidad AS detallesalida_cantidad,\n"
                + "     detallesalida.precio AS detallesalida_precio,\n"
                + "     detallesalida.devolucion AS detallesalida_devolucion,\n"
                + "     producto.nombre AS producto_nombre,\n"
                + "     salida.salida AS salida_salida\n"
                + "FROM\n"
                + "     producto INNER JOIN lote ON producto.idproducto = lote.producto_idproducto\n"
                + "     INNER JOIN  detallesalida ON lote.idlote = detallesalida.lote_idlote\n"
                + "     INNER JOIN  salida ON detallesalida.salida_idsalida = salida.idsalida where salida_idsalida ='" + f2 + "'");
        return rs;
    }

    public static ResultSet EnlIngresos(ResultSet rs,String fecha1, String fecha2) throws SQLException {
        st = sta(st);
        rs = st.executeQuery("SELECT\n"
                + "     compra.idcompra AS compra_idcompra,\n"
                + "     compra.numdoc  AS compra_Nofactura,\n"
                + "     compra.fecha AS compra_fecha,\n"
                + "     compra.total AS compra_total,\n"
                + "     usuario.nombreusuario AS usuario_nombreusuario\n"
                + "     \n"
                + "FROM     usuario INNER JOIN compra ON usuario.idusuario = compra.usuario_idusuario\n"
                + "     INNER JOIN proveedor ON compra.proveedor_idproveedor = proveedor.idproveedor where compra.fecha BETWEEN '" + fecha1 + "' and '" + fecha2 + "' order BY compra.idcompra desc");
        return rs;
    }

    public static ResultSet EnlDIngresos(ResultSet rs, String f) throws SQLException {
        String f2 = f;
        st = sta(st);
        //rs=st.executeQuery("select * from detallesalida where iddetallesalida="+f2);
        rs = st.executeQuery("SELECT\n"
                + "     compra.idcompra,producto.codigo,producto.nombre,\n"
                + "         lote.cantidad,lote.precio,compra.numdoc,compra.fecha,usuario.nombreusuario\n"
                + "FROM\n"
                + "     producto INNER JOIN lote ON producto.idproducto = lote.producto_idProducto\n"
                + "     INNER JOIN compra ON lote.compra_idcompra = compra.idcompra\n"
                + "     INNER JOIN usuario ON compra.usuario_idusuario = usuario.idusuario\n"
                + "WHERE\n"
                + "     compra_idcompra='" + f2 + "'");
        return rs;
    }

    public static ResultSet VerCliente(ResultSet rs) throws SQLException {
        st = sta(st);
        PreparedStatement pstmt = conn.prepareStatement("{select proveedor from proveedor}");
        pstmt.executeUpdate();
        rs = pstmt.getResultSet();
        return rs;
    }
}
