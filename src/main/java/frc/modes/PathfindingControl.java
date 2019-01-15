/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.modes;

import frc.pathfinding.*;
import frc.robot.Robot;

/**
 * Add your docs here.
 */
public class PathfindingControl extends Mode {

    private double x;
    private double y;
    private Pathfinding pathfinding;

    public PathfindingControl() {
        pathfinding = new Pathfinding();
    }

    public void enter() {
        System.out.println("pathfinding");
    }

    @Override
    public boolean loop() {

        double xPos = Robot.POS_TRACKER.getX();
        double yPos = Robot.POS_TRACKER.getY();
        // Node endPos = pathfinding.search(xPos, yPos);
        double x = this.x;// endPos.x;
        double y = this.y;// endPos.y;
        double d = 4 * Math.sqrt((x - xPos) * (x - xPos) + (y - yPos) * (y - yPos));
        double vx = (x - xPos) / d;
        double vy = (y - yPos) / d;
        Robot.SWERVE.drive(vx, vy, 0);
        if (Math.sqrt((this.x - xPos) * (this.x - xPos) + (this.y - yPos) * (this.y - yPos)) < 0.1) {
            return false;
        }
        return true;

    }

    public void setTarget(double x, double y) {
        System.out.println("Setting target to " + x + ", " + y);
        this.x = x;
        this.y = y;
        // pathfinding.setTargetNode(x, y);

    }

}
