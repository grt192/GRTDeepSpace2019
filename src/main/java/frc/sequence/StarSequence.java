/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.sequence;

import java.util.ArrayList;
import java.util.List;

import frc.fieldmap.geometry.Vector;
import frc.robot.Robot;
import frc.drawing.Path;

/**
 * Add your docs here.
 */
public class StarSequence extends Sequence {

    private List<Vector> points = new ArrayList<Vector>();
    private Path path;

    public static final double SPEED = 0.5;

    public void runSequence() {
        Robot.SWERVE.setRobotCentric(false);
        points = path.getAction();
        if (points != null) {
            for (int i = 0; i < points.size(); i++) {
                move(points.get(i));
            }
        }
    }

    private void move(Vector target) {
        double xPos = Robot.POS_TRACKER.getX();
        double yPos = Robot.POS_TRACKER.getY();
        Vector pos = new Vector(xPos, yPos);

        Vector velocity = new Vector(target.x - xPos, target.y - yPos);

        double distance = pos.distanceTo(target);
        System.out.println("distance: " + distance);
        double speed = SPEED * Math.min(distance / 36.0, 1);
        velocity = velocity.multiply(speed);

        Robot.SWERVE.drive(velocity.x, velocity.y, 0);
    }
}
