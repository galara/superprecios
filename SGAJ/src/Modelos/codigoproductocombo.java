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
public class codigoproductocombo {
    
    private String descripcion;
    private String id;
    //private int dias;

    public codigoproductocombo (String descripcion, String id) {
        this.descripcion = descripcion;
        this.id = id;
        //this.dias=dia;
    }
    
//    public codigoproductocombo (String descripcion, String id) {
//        this.descripcion = descripcion;
//        this.id = id;
//    }

    public String getID() {
        return id;
    }
    
    public String toString() {
        return descripcion;
    }
//    public int todia() {
//        return dias;
//    }
}
