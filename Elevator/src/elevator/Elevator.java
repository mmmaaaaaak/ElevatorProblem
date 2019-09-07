/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elevator;
import javax.swing.JFrame;

/**
 *
 * @author Mark Masone
 */
public class Elevator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame ef = new JFrame();
                ef.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ef.add(new ElevatorPanel());
                ef.setSize(800,600);
                ef.setVisible(true);
            }
        });
    }
    
}
