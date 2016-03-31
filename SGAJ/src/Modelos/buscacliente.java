/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

/**
 *
 * @author Otto
 */
public class buscacliente {

    private String nombre;
    private int id;

    public buscacliente(String nombre, int id) {
        this.nombre = nombre;
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public String toString() {
        return nombre;
    }

}
