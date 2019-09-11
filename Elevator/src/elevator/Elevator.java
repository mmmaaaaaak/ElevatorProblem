/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elevator;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import javax.swing.JToggleButton;

/**
 *
 * @author Mark Masone
 */
public class Elevator implements FloorRequestListener {
    
    private int highestFloor;
    private int lowestFloor;
    private boolean moving;
    private boolean direction;
    private int elevatorY;
    private Timer timer;
    private TimerTask task;
    private final ArrayList<ElevatorMovementListener> elevatorMovementListeners;
    private final ElevatorShaft elevatorShaft;
    private JToggleButton[] buttons;
    
    public Elevator(ElevatorShaft elevatorShaft) {
        moving = false;
        direction = true;
        highestFloor = 1;
        lowestFloor = 6;
        elevatorY = 375;
        elevatorMovementListeners = new ArrayList();
        this.elevatorShaft = elevatorShaft;
    }
    
    public void setButtons(JToggleButton[] buttons) {
        this.buttons = buttons;
    }
    
    public void addElevatorMovementListener(ElevatorMovementListener eml) {
        elevatorMovementListeners.add(eml);
    }
    
    private boolean floorReached() {
        return (elevatorY + 8) % 64 == 0;
    }
    
    private int getFloor() {
        return 7 - ((elevatorY + 8) / 64);
    }
    
    @Override
    public void floorRequested(int floor) {
        if(highestFloor < floor)
            highestFloor = floor;
        if(lowestFloor > floor)
            lowestFloor = floor;
        if(!moving) {
            direction = true;
            moving = true;
            start(0);
        }
    }
    
    public void buttonPressed(int floor) {
        buttons[floor - 1].setEnabled(false);
        if(floor == 1)
            elevatorShaft.requestFloor(floor, true);
        else if(floor == 6)
            elevatorShaft.requestFloor(floor, false);
        else if(floor > getFloor())
            elevatorShaft.requestFloor(floor, true);
        else
            elevatorShaft.requestFloor(floor, false);
    }
    
    private void resetButton(int floor) {
        buttons[floor - 1].setSelected(false);
        buttons[floor - 1].setEnabled(true);
    }
    
    private void adjustFloorLimits() {
        int h = 1;
        int l = 6;
        for(int floor = lowestFloor; floor <= highestFloor; floor++) {
            if(elevatorShaft.downRequested(floor) || elevatorShaft.upRequested(floor)) {
                h = floor;
                if(l == 6) {
                    l = floor;
                }
            }
        }
        highestFloor = h;
        lowestFloor = l;
    }
    
    private void start(int delay) {
        timer = new Timer(); 
        task = new TimerTask(){
            @Override
            public void run() {
                move();
            }
        };
        timer.schedule(task, delay, 20); 
    }
     
    private void stop() {
        task.cancel();
        timer.cancel();
    }
     
    public void move() {
        if(direction) { // Elevator will go up
            if(floorReached()) {
                int floor = getFloor();
                if(elevatorShaft.upRequested(floor) || floor == highestFloor && elevatorShaft.downRequested(floor)) {
                    stop();
                    start(2000);
                    resetButton(floor);
                    elevatorShaft.resetButtons(floor);
                } else { // elevator starts to move up
                    if(floor == highestFloor && floor == lowestFloor) {
                        // there are no more requests, go down to first floor
                        direction = false;
                        highestFloor = 1;
                        lowestFloor = 6;
                    } else if(floor == highestFloor) {
                        direction = false;
                        adjustFloorLimits(); // will adjust the highestFloor value
                    } else if(floor == lowestFloor) {
                        elevatorY -= 1;
                        adjustFloorLimits(); // will adjust the lowestFloor value
                        System.out.println("adjust limits");
                    } else { // move the elevator up
                        elevatorY -= 1;
                    }
                }
            } else { // move the elevator up
                elevatorY -= 1;
            }
        } else { // Elevator will go down
            if(floorReached()) {
                int floor = getFloor();
                if(elevatorShaft.downRequested(floor) || floor == lowestFloor && elevatorShaft.upRequested(floor) || floor == 1) { // down button pressed on this floor
                    stop();
                    resetButton(floor);
                    elevatorShaft.resetButtons(floor);
                    
                    // when elevator reaches floor 1, change its direction to up
                    if(floor == 1)
                        direction = true;
                    
                    /* If a button was pressed above the elevator as it was descending to the first floor,
                    then highestFloor will be greater than 1 and the elevator should start back up. 
                    Otherwise, it will stop moving. */
                    if(highestFloor == 1)
                        moving = false;
                    else
                        start(2000);
                } else { // elevator starts to move down
                    if(floor == highestFloor && floor == lowestFloor) {
                        // there are no more requests, go down to first floor
                        highestFloor = 1;
                        lowestFloor = 6;
                    } else if(floor == lowestFloor && floor != 6) { 
                        // excludes floor 6, can't go up if we're already at the top floor!
                        direction = true;
                        adjustFloorLimits(); // will adjust the lowestFloor value
                    } else if(floor == highestFloor) {
                        elevatorY += 1;
                        adjustFloorLimits(); // will adjust the highestFloor value
                    } else { // move the elevator down
                        elevatorY += 1;
                    }
                }
            } else { // move the elevator down
                elevatorY += 1;
            }
        }
        for(ElevatorMovementListener elevatorMovementListener : elevatorMovementListeners)
            elevatorMovementListener.elevatorMoved();
    }
    
    public void draw(java.awt.Graphics2D g2d) {
        g2d.setColor(Color.red);
        g2d.drawString("highest floor: " + highestFloor, 300, 60);
        g2d.drawString("lowest floor: " + lowestFloor, 300, 80);
        g2d.drawRect(100, elevatorY, 150, 40);
    }
}
