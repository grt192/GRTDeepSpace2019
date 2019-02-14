/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.modes;

import frc.fieldmap.VisionTarget;
import frc.fieldmap.geometry.Vector;
import frc.robot.Robot;

/**
 * Add your docs here.
 */
public class ScoreMode extends Mode {

    private static final double ALLOWABLE_Y_ERROR = 1.5;

    private VisionTarget target;

    @Override
    public boolean loop() {
        Robot.SWERVE.setRobotCentric(false);
        if (target == null)
            return false;
        Vector pos = new Vector(Robot.POS_TRACKER.getX(), Robot.POS_TRACKER.getY());
        double angle = target.pos.angle;
        Vector displacement = pos.subtract(target.pos.pos).rotate(-angle)
                .subtract(new Vector((Robot.ROBOT_HEIGHT / 2) + 6.0, 0));
        if (displacement.x <= -2.0) {
            Robot.SWERVE.drive(0, 0, 0);
            return false;
        }
        Vector velocity;
        if (Math.abs(displacement.y) < ALLOWABLE_Y_ERROR) {
            velocity = new Vector(-0.2, -0.1 * displacement.y / ALLOWABLE_Y_ERROR);
        } else {
            double d = displacement.magnitude();
            double speed = Math.max(0.1, d / 120.0);
            velocity = displacement.multiply(-speed / d);
        }
        velocity = velocity.rotate(angle);
        Robot.SWERVE.drive(velocity.x, velocity.y, 0);
        return true;
    }

    public void setTarget(VisionTarget vt) {
        target = vt;
        Robot.SWERVE.setAngle(vt.pos.angle);
    }
}
