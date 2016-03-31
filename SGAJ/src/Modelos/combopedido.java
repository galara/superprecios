/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Modelos;
/**
 *
 * @author Otto
 */
public class combopedido {

private String nombre,cantidad;
private int id ;

public combopedido(String nombre , int id,String cant) {
this.nombre=nombre;
this.id=id;
this.cantidad=cant;

}
public int getID(){
return id ;
}

public String toString() {
return nombre ;
}
public String tocant() {
return cantidad ;
}
}

