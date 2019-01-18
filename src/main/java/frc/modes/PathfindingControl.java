/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.modes;

import frc.fieldmap.geometry.Vector;
import frc.pathfinding.Pathfinding;
import frc.robot.Robot;

/**
 * Add your docs here.
 */
public class PathfindingControl extends Mode {

    private Vector target;
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
        Vector pos = new Vector(xPos, yPos);
        Vector endPos = pathfinding.search(xPos, yPos);
        if (endPos == null) {
            System.out.println("NO PATH FOUND");
            return false;
        }
        double d = pos.distanceTo(target);
        double speed = 0.5 * Math.min((d - 36) / 36.0, 1);
        Vector velocity = endPos.subtract(pos).multiply(speed / d);
        if (d < 4) {
            return false;
        }
        Robot.SWERVE.drive(velocity.x, velocity.y, 0);
        return true;

    }

    public void setTarget(double x, double y) {
        System.out.println("Setting target to " + x + ", " + y);
        target = new Vector(x, y);
        pathfinding.setTargetNode(x, y);

    }

}
