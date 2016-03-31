package BD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author GLARA
 */
public class BdConexion {

    public static Connection cns = null;
    private static ResultSet rs = null;
    private static Statement st = null;
    private static PreparedStatement ps = null;
    //***********AGROFATIMA************************************************
    //private static String host = "192.168.0.10";
    //public static String dataBase = "agrofatima"; //nombre base de datos
    //public static String user = "agropecuaria"; // usuario de la base de datos
    //public static String pass = "rdwLWVx9cWcyyPFe"; // contraseña de la base de datos
    
//    //***********SAN MIGUEL************************************************
    private static String host = "localhost";//"192.168.0.20";
    public static String dataBase = "superprecios";//"agrofatima2"; //nombre base de datos
    public static String user = "root";//"agrofatima2"; // usuario de la base de datos
    public static String pass = "superprecios";//"rdwLWVx9cWcyyPFe"; 
    
//    ***********otro************************************************
//    private static String host = "localhost";//"192.168.0.20";
//    public static String dataBase = "agrofatima";//"agrofatima2"; //nombre base de datos
//    public static String user = "agropecuaria";//"agrofatima2"; // usuario de la base de datos
//    public static String pass = /*"agrofatima2";//*/"rdwLWVx9cWcyyPFe"; 
    
    //***********LOCAL  ************************************************
    //private static String host = "localhost";//"192.168.0.20";
    //public static String dataBase = "agrofatima";//"agrofatima2"; //nombre base de datos
    //public static String user = "root";//"agrofatima2"; // usuario de la base de datos
    //public static String pass = "adolfo123";//"rdwLWVx9cWcyyPFe"; 
    
    public static int TODO = 1;
    public static int SOLO_STATEMENT = 2;
    public static int SOLO_RESULTSET = 3;
    public static int SOLO_PREPAREDSTATEMENT = 4;
    public static int CONNECION = 5;

    public static void init() {
        try {
            if (cns == null) {
                Class.forName("com.mysql.jdbc.Driver");
                String url = "jdbc:mysql://" + host + ":3306/" + dataBase;
                cns = DriverManager.getConnection(url, user, pass);
            }
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Fue imposible conectarse al servidor.", host, JOptionPane.ERROR_MESSAGE);
            //ex.printStackTrace(System.err);
        } catch (SQLException ex) {
            System.out.println("No se pudo establecer la conexión" + ex);
            JOptionPane.showMessageDialog(null, "no se pudo conectar con el servidor : Error\n" + ex
                    + "jdbc:mysql://" + host + ":3306/" + dataBase, "Error de Conexión", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(null, "Fue imposible conectarse al servidor.", host, JOptionPane.ERROR_MESSAGE);
            //ex.printStackTrace(System.err);
            System.exit(0);
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(null, "se esta pasando un objeto nulo ", host, JOptionPane.ERROR_MESSAGE);
            //System.out.println("se esta pasando un objeto nulo");
        }

    }

    /**
     * antes de llamar a este metodo, debe llamar al metodo init.
     *
     * @return
     * @throws NullPointerException
     */
    public static Connection getConexion() throws NullPointerException {
        if (cns == null) {
            init();
        }
        return cns;
    }

    public static ResultSet getResultSet(String sql) {
        if (cns == null) {
            init();
        }
        try {
            rs = getStatement().executeQuery(sql);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
            //ex.printStackTrace();
        }

        return rs;
    }

    public static Statement getStatement() {
        if (cns == null) {
            init();
        }
        try {
            st = cns.createStatement();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        return st;
    }

    public static PreparedStatement getPreparedStatement(String sql) {
        if (cns == null) {
            init();
        }

        if (ps == null) {
            try {
                ps = cns.prepareStatement(sql);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        } else if (ps != null) {
            try {
                if (ps.isClosed()) {
                    ps = cns.prepareStatement(sql);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
        return ps;
    }

    /**
     *
     * @param tipoCierre -
     * TODO,SOLO_STATEMENT,SOLO_RESULTSET,SOLO_PREPAREDSTATEMENT,CONNECION
     */
    public static void cerrarEnlacesConexion(int tipoCierre) {
        switch (tipoCierre) {
            case 1:
                cerrarConexion();
                cerrarResultSet();
                cerrarStatement();
                cerrarPreparedStatement();
                break;
            case 2:
                cerrarStatement();
                break;
            case 3:
                cerrarResultSet();
                break;
            case 4:
                cerrarPreparedStatement();
                break;
            case 5:
                cerrarConexion();
                break;
            default:
        }
    }

    private static void cerrarResultSet() {
        if (rs != null) {
            try {
                if (!rs.isClosed()) {
                    rs.close();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    private static void cerrarStatement() {
        if (st != null) {
            try {
                if (!st.isClosed()) {
                    st.close();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    private static void cerrarPreparedStatement() {
        if (ps != null) {
            try {
                if (!ps.isClosed()) {
                    ps.close();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    private static void cerrarConexion() {
        if (cns != null) {
            try {
                if (!cns.isClosed()) {
                    cns.close();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

}
