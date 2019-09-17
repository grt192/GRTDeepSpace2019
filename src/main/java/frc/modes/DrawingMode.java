/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.modes;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.fieldmap.geometry.Vector;
import frc.robot.Robot;
import frc.drawing.Path;

/**
 * Add your docs here.
 */
public class DrawingMode extends Mode {

    private Vector target;
    private volatile Vector tempTarget;
    private volatile boolean newTarget;
    private NetworkTableEntry targetEntry;

    private boolean premadePath;
    private List<Vector> points = new ArrayList<Vector>();
    private Path path;

    public static final double SPEED = 0.5;

    public DrawingMode() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("Pathfinding");
        targetEntry = table.getEntry("target");
        targetEntry.setString("0.0 0.0");
        targetEntry.addListener(event -> {
            String data = event.value.getString();
            String[] split = data.split(" ");
            tempTarget = new Vector(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
            newTarget = true;
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate | EntryListenerFlags.kLocal);

        path = new Path();
    }

    @Override
    public boolean loop() {
        Robot.SWERVE.setRobotCentric(false);
        if (premadePath) {
            points = path.getAction(1);
            if (points != null) {
                for (int i = 0; i < points.size(); i++) {
                    move(points.get(i));
                }
            }
        } else {
            if (newTarget) {
                target = tempTarget;
                newTarget = false;
            }
            if (target == null)
                return false;
            move(target);
        }
        return true;
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
