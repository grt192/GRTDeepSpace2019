/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.vision;

import frc.config.Config;
import frc.fieldmap.VisionTarget;
import frc.fieldmap.geometry.Vector;
import frc.positiontracking.Position;
import frc.robot.*;

public class Camera {
    private Position relativePosition;
    private JeVois jeVois;

    public Camera(String name) {
        double x = Config.getDouble(name + "_x");
        double y = Config.getDouble(name + "_y");
        double angle = Config.getDouble(name + "_angle");
        relativePosition = new Position(new Vector(x, y), angle);
        jeVois = new JeVois();
        jeVois.start();
    }

    public Position getPositionEstimate(long maxAge) {
        if (System.currentTimeMillis() - jeVois.getLastReceivedTimestamp() > maxAge)
            return null;
        JeVoisMessage message = jeVois.getLastMessage();
        if (message == null)
            return null;
        Vector imageDisplacement = new Vector(message.translateZ, message.translateX);
        Vector myPosition = relativePosition.pos.rotate(Robot.GYRO.getAngle())
                .add(new Vector(Robot.POS_TRACKER.getX(), Robot.POS_TRACKER.getY()));
        VisionTarget target = Robot.FIELD_MAP.getNearestTarget(myPosition, imageDisplacement);
        double angleEstimate = message.rotateZ + Math.PI + target.pos.angle;
        Vector estimate = target.pos.pos.add(imageDisplacement.rotate(target.pos.angle))
                .subtract(relativePosition.pos.rotate(angleEstimate));
        Position pos = new Position(estimate, angleEstimate);
        System.out.println(estimate);
        return pos;
    }

}
