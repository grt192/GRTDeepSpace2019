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
class PathfindingControl extends Mode {

    public PathfindingControl() {
        double x;
        double y;
    }

    Pathfinding pathfinding = new Pathfinding();

    @Override
    public boolean loop() {
        double xPos = Robot.POS_TRACKER.getX();
        double yPos = Robot.POS_TRACKER.getY();

        pathfinding.search(xPos, yPos);

        // Robot.SWERVE.drive();
        return true;

    }

    public void setTarget(double x, double y) {
        pathfinding.setTargetNode(x, y);
    }

}
