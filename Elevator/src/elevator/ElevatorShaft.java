/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elevator;
import javax.swing.JToggleButton;
import java.util.ArrayList;

/**
 *
 * @author Mark Masone
 */
public class ElevatorShaft {
    
    private final boolean[] up;
    private final boolean[] down;
    private JToggleButton[] upButtons;
    private JToggleButton[] downButtons;
    private final ArrayList<FloorRequestListener> floorButtonListeners;
    
    public ElevatorShaft() {
        up = new boolean[]{false,false,false,false,false,false};
        down = new boolean[]{false,false,false,false,false,false};
        floorButtonListeners = new ArrayList();
    }
    
    public void setButtons(JToggleButton[] upButtons,JToggleButton[] downButtons) {
        this.upButtons = upButtons;
        this.downButtons = downButtons;
    }
    
    public void addFloorButtonListener(FloorRequestListener fbl) {
        floorButtonListeners.add(fbl);
    }
    
    public void buttonPressed(int floor,boolean direction) {
        if(direction) {
            upButtons[floor - 1].setEnabled(false);
        } else {
            downButtons[floor - 2].setEnabled(false);
        }
        requestFloor(floor,direction);
    }
    
    public void requestFloor(int floor, boolean direction) {
        if(direction) {
            up[floor - 1] = true;
        } else {
            down[floor - 1] = true;
        }
        for(FloorRequestListener floorButtonListener : floorButtonListeners)
            floorButtonListener.floorRequested(floor);
    }
    
    public boolean upRequested(int floor) {
        return up[floor - 1];
    }
    
    public boolean downRequested(int floor) {
        return down[floor - 1];
    }
    
    public void resetButtons(int floor) {
        if(floor < 6) {
            up[floor - 1] = false;
            upButtons[floor - 1].setSelected(false);
            upButtons[floor - 1].setEnabled(true);
        }
        if(floor > 1) {
            down[floor - 1] = false;
            downButtons[floor - 2].setSelected(false);
            downButtons[floor - 2].setEnabled(true);
        }
    }
    
    public void draw(java.awt.Graphics2D g2d) {
        for(int i = 0; i < 6; i++) {
            int y = 80 + i * 64;
            g2d.drawString("up: " + up[5 - i], 150, y - 10);
            g2d.drawString("down: " + down[5 - i], 150, y + 10);
            g2d.drawString((6 - i) + "", 110, y);
        }
    }
}
