/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BD;

/**
 *
 * @author GLARA
 */
public class sqlcat {
    public static final String LLENAR = "select * from categoria";
    public static final String BUSCANIT = "select * from categoria where nit=";
    public static final String BUSCANOMBRE = "select * from categoria where nombre like '%";
    public static final String NUEVOC = "insert into categoria (nombre,fec_reg) values (?,?)";
    public static final String DELETEC = "delete from categoria";
    public static final String UPDATEC = "update categoria set  nombre=?, fec_reg=?, Estado=? where idCategoria=?";
    public static final String ID = "idCategoria";
    
    public static final String IGUAL = " = ";
    public static final String DIFERENTE = " != ";
    public static final String MENOR_QUE = " < ";
    public static final String MAYOR_QUE = " > ";
    public static final String NO_NULO = " is not null ";
    public static final String ES_NULO = " is null ";
    public static final String ENTRE_BETWEEN = " between ";
    public static final String IN = " in ";
    public static final String NOT = " not ";
    public static final String AND = " and ";
    public static final String OR = " or ";
    public static final String XOR = " xor ";
    public static final String LIKE = " like ";
    public static final String CUALQUIERA = "%'";
    public static final String SOLO_UNO = "_";
    public static final String ORDER_BY = " order by idcategoria asc";
    public static final String WHERE = " where ";
    public static final String ASCENDENTE = " asc ";
    public static final String DESCENDENTE = " desc ";
    public static final String LIMIT = " limit ";
    
}
