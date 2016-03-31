/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

/**
 *
 * @author Glara
 */
public class Mainclass {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        // TODO code application logic here
        //Login1 usu = new Login1();
        //usu.setVisible(true);
        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {

                new Login1().setVisible(true);

            }
        }
        
        
        );

    }
}
