/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modelos;

/**
 *
 * @author 30234
 */
public class unidad {
    
    private String nombre;
    private String id;

    public unidad(String nombre, String id) {
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
