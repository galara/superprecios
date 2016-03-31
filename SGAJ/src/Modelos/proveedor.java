/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;
/**
 *
 * @author Otto
 */
public class proveedor {

    private String nombre;
    private String id;

    public proveedor(String nombre, String id) {
        this.nombre = nombre;
        this.id = id;
    }

    public String getID() {
        return id;
    }

    public String toString() {
        return nombre;
    }

}
