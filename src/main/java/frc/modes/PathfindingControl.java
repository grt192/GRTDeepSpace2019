/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.modes;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.fieldmap.geometry.Vector;
import frc.pathfinding.Pathfinding;
import frc.robot.Robot;

/**
 * Add your docs here.
 */
public class PathfindingControl extends Mode {

    private Vector target;
    private volatile boolean newTarget;
    private Pathfinding pathfinding;
    private NetworkTableEntry targetEntry;

    public PathfindingControl() {
        pathfinding = new Pathfinding();
        NetworkTable table = NetworkTableInstance.getDefault().getTable("Pathfinding");
        targetEntry = table.getEntry("target");
        targetEntry.setString("0.0 0.0");
        targetEntry.addListener(event -> {
            String data = event.value.getString();
            String[] split = data.split(" ");
            target = new Vector(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
            newTarget = true;
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    }

    @Override
    public boolean loop() {
        if (newTarget) {
            setTarget(target.x, target.y);
            newTarget = false;
        }
        double xPos = Robot.POS_TRACKER.getX();
        double yPos = Robot.POS_TRACKER.getY();
        Vector pos = new Vector(xPos, yPos);
        Vector endPos = pathfinding.search(xPos, yPos);
        if (endPos == null) {
            System.out.println("NO PATH FOUND");
            return false;
        }
        double d = pos.distanceTo(endPos);
        double distance = pos.distanceTo(target);
        double speed = 0.75 * Math.min(d / 36.0, 1);
        Vector velocity = endPos.subtract(pos).multiply(speed / d);
        if (distance < 4) {
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
