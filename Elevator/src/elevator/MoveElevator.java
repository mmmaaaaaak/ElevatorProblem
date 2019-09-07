/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elevator;
import java.util.TimerTask;

/**
 *
 * @author Mark Masone
 */
public class MoveElevator extends TimerTask {
    
    public MoveElevator(ElevatorPanel elevatorPanel) {
        this.elevatorPanel = elevatorPanel;
    }
    
    @Override
    public void run() {
        elevatorPanel.moveElevator();
    }
    
    private ElevatorPanel elevatorPanel;
}
