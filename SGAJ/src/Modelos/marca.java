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
public class marca {
    
    private String nombre;
    private String id;

    public marca(String nombre, String id) {
        this.nombre = nombre;
        this.id = id;
    }

    public marca() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getID() {
        return id;
    }

    public String toString() {
        return nombre;
    }
    
}
