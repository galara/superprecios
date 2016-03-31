/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modelos;

/**
 *
 * @author Glara
 */

/**
 *Clase co metodo para calcular el interes de un peridoso o varios
 *resive como parametros el (monto, tasa de interes y los peridos en meses) 
 *devuelve un float=monto de interes
 */
public class CalculoInteres {
    
    static float interesSimple(float monto, float tasainteres, int períodos){
     for(int n = 1; n <= períodos; n++ ){
        monto = monto * tasainteres / 100;
        monto=(float) (Math.round((monto) * 100.0) / 100.0);
     }
     return monto;
  }
    
}
