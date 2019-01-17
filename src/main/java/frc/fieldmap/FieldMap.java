/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.fieldmap;

import frc.fieldmap.geometry.Circle;
import frc.fieldmap.geometry.*;
import frc.robot.Robot;

/**
 * Add your docs here.
 */
public class FieldMap {

    private Polygon[] obstacles;

    public FieldMap() {
        buildMap();
    }

    public boolean lineOfSight(Vector v1, Vector v2) {
        Vector dif = v2.subtract(v1);
        double d = v1.distanceTo(v2);
        if (d == 0.0)
            return true;
        Vector norm = dif.multiply(Robot.ROBOT_RADIUS / d).normal();
        Polygon rect = new Polygon(v1.add(norm), v2.add(norm), v2.subtract(norm), v1.subtract(norm));
        Circle endCircle = new Circle(v2, Robot.ROBOT_RADIUS);
        if (shapeIntersects(rect))
            return false;
        if (shapeIntersects(endCircle))
            return false;
        return true;
    }

    public boolean shapeIntersects(Polygon p) {
        for (Polygon poly : obstacles) {
            if (p.intersects(poly))
                return true;
        }
        return false;
    }

    public boolean shapeIntersects(Circle c) {
        for (Polygon poly : obstacles) {
            if (c.intersects(poly))
                return true;
        }
        return false;
    }

    private void buildMap() {

    }
}
