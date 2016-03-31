/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BD;

/**
 *
 * @author GLARA
 */
public class sqlprod {
    public static final String LLENAR =      "select producto.idproducto,producto.codigo,producto.nombre,categoria.nombre,producto.PrecioCompra,producto.Precioventa,producto.preciocredito,producto.preciodistribuidor,producto.precioespecial,producto.pivote,producto.comprometido,producto.cantidad,producto.fecha,producto.Estado,producto.Descripcion,unidad.nombre,marca.nombre from  producto INNER JOIN categoria on producto.Categoria_idCategoria=categoria.idCategoria INNER JOIN unidad on producto.idunidad=unidad.idunidad INNER JOIN marca on producto.idmarca=marca.idmarca";
    public static final String BUSCANIT =    "select producto.idproducto,producto.codigo,producto.nombre,categoria.nombre,producto.PrecioCompra,producto.Precioventa,producto.preciocredito,producto.preciodistribuidor,producto.precioespecial,producto.pivote,producto.comprometido,producto.cantidad,producto.fecha,producto.Estado,producto.Descripcion,unidad.nombre,marca.nombre from  producto INNER JOIN categoria on producto.Categoria_idCategoria=categoria.idCategoria INNER JOIN unidad on producto.idunidad=unidad.idunidad INNER JOIN marca on producto.idmarca=marca.idmarca where producto.codigo=";
    public static final String BUSCANOMBRE = "select producto.idproducto,producto.codigo,producto.nombre,categoria.nombre,producto.PrecioCompra,producto.Precioventa,producto.preciocredito,producto.preciodistribuidor,producto.precioespecial,producto.pivote,producto.comprometido,producto.cantidad,producto.fecha,producto.Estado,producto.Descripcion,unidad.nombre,marca.nombre from  producto INNER JOIN categoria on producto.Categoria_idCategoria=categoria.idCategoria INNER JOIN unidad on producto.idunidad=unidad.idunidad INNER JOIN marca on producto.idmarca=marca.idmarca where producto.nombre like '%";
    public static final String BMARCA =      "select producto.idproducto,producto.codigo,producto.nombre,categoria.nombre,producto.PrecioCompra,producto.Precioventa,producto.preciocredito,producto.preciodistribuidor,producto.precioespecial,producto.pivote,producto.comprometido,producto.cantidad,producto.fecha,producto.Estado,producto.Descripcion,unidad.nombre,marca.nombre from  producto INNER JOIN categoria on producto.Categoria_idCategoria=categoria.idCategoria INNER JOIN unidad on producto.idunidad=unidad.idunidad INNER JOIN marca on producto.idmarca=marca.idmarca where marca.nombre like '%";
    public static final String BCAT =        "select producto.idproducto,producto.codigo,producto.nombre,categoria.nombre,producto.PrecioCompra,producto.Precioventa,producto.preciocredito,producto.preciodistribuidor,producto.precioespecial,producto.pivote,producto.comprometido,producto.cantidad,producto.fecha,producto.Estado,producto.Descripcion,unidad.nombre,marca.nombre from  producto INNER JOIN categoria on producto.Categoria_idCategoria=categoria.idCategoria INNER JOIN unidad on producto.idunidad=unidad.idunidad INNER JOIN marca on producto.idmarca=marca.idmarca where categoria.nombre like '%";
    public static final String NUEVOC =     "insert into producto (codigo,nombre,categoria_idcategoria,PrecioCompra,Precioventa,preciocredito,preciodistribuidor,precioespecial,pivote,cantidad,fecha,Descripcion,idunidad,idmarca) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public static final String NUEVOL =     "insert into lote (cantidad,precio,fecha,stock,producto_idProducto,tipoentrada,compra_idcompra) values (?,?,?,?,?,?,?)";
    
    public static final String DELETEC = "delete from producto";
    public static final String UPDATEC = "update producto set  codigo=?,nombre=?,producto.categoria_idcategoria=?,PrecioCompra=?,Precioventa=?,preciocredito=?,preciodistribuidor=?,precioespecial=?,pivote=?,cantidad=?,fecha=?,Estado=?,Descripcion=?,idunidad=?,idmarca=? where idproducto=?";
                                    
    public static final String ID = "producto.idproducto";
    //public static final String COMBOPROV = "select idproveedor,nombre from proveedor where idproveedor!='1'";
    //public static final String COMBOCAT = "select idcategoria,nombre from categoria where idcategoria !='1'";
    
    //public static final String COMBOPROV = "select idproveedor,nombre from proveedor";
    public static final String COMBOCAT = "select idcategoria,nombre from categoria where estado='T'";
    public static final String COMBOUNI = "select idunidad,nombre from unidad where estado='T'";
    public static final String COMBOMAR = "select idmarca,nombre from marca where estado='T'";
    public static final String COMBOTP = "select idtipopago,descripcion,dias from tipopago";
  
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
    public static final String ORDER_BY = " order by producto.idproducto asc";
    public static final String WHERE = " where ";
    public static final String ASCENDENTE = " asc ";
    public static final String DESCENDENTE = " desc ";
    public static final String LIMIT = " limit ";
    
}
