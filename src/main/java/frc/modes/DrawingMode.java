/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.modes;

import java.util.ArrayList;
import java.util.List;

import frc.fieldmap.geometry.Vector;
import frc.robot.Robot;
import frc.drawing.Path;

/**
 * Add your docs here.
 */
public class DrawingMode extends Mode {

    private List<Vector> points = new ArrayList<Vector>();
    private Path path = new Path();

    private double xPos, yPos;

    public static final double SPEED = 0.5;

    public DrawingMode() {

    }

    @Override
    public boolean loop() {
        Robot.SWERVE.setRobotCentric(false);
        points = path.getAction();

        xPos = Robot.POS_TRACKER.getX();
        yPos = Robot.POS_TRACKER.getY();
        Vector pos = new Vector(xPos, yPos);

        for (int i = 0; i < points.size(); i++) {
            points.set(i, points.get(i).add(pos));
        }

        if (!points.isEmpty()) {
            for (int i = 0; i < points.size(); i++) {
                move(points.get(i));
            }
        }
        return false;
    }

    private void move(Vector target) {

        while (Math.abs(xPos - target.x) < 2 && Math.abs(yPos - target.y) < 2) {
            Vector pos = new Vector(xPos, yPos);

            Vector velocity = new Vector(target.x - xPos, target.y - yPos);

            double distance = pos.distanceTo(target);
            System.out.println("position: " + xPos + ", " + yPos);
            System.out.println("distance: " + distance);
            double speed = SPEED * Math.min(distance / 36.0, 1);
            velocity = velocity.multiply(speed);
            System.out.println("velocity: " + velocity.x + ", " + velocity.y);
            Robot.SWERVE.drive(velocity.x, velocity.y, 0);

            xPos = Robot.POS_TRACKER.getX();
            yPos = Robot.POS_TRACKER.getY();
        }
    }
}
