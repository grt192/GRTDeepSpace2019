/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.fieldmap;

import frc.fieldmap.geometry.Circle;
import frc.fieldmap.geometry.Polygon;
import frc.fieldmap.geometry.Vector;
import frc.robot.Robot;

/**
 * Add your docs here.
 */
public class FieldMap {

    private Polygon[] obstacles;
    private VisionTarget[] visionTargets;

    public FieldMap() {
        // buildMap();
        testMap();
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

    public VisionTarget getNearestTarget(Vector robotPos, Vector image) {
        double min = Double.POSITIVE_INFINITY;
        VisionTarget best = null;
        for (VisionTarget vt : visionTargets) {
            double dist = vt.pos.pos.add(image.rotate(vt.pos.angle)).distanceSquaredTo(robotPos);
            if (dist < min) {
                dist = min;
                best = vt;
            }
        }
        return best;
    }

    private void buildMap() {
        Polygon habZoneClose = new Polygon(new Vector(48, 73.6291), new Vector(0, 73.6291), new Vector(0, 251.2433),
                new Vector(48, 251.2433));
        Polygon leftRocketClose = new Polygon(new Vector(209.5727, 0), new Vector(209.5727, 6.6388),
                new Vector(219.678, 26.2055), new Vector(238.8411, 0), new Vector(238.8411, 26.2055));
        Polygon cargoBayClose = new Polygon(new Vector(222.8754, 139.1743), new Vector(222.8754, 185.11),
                new Vector(251.3519, 133.8589), new Vector(251.3519, 189.5825), new Vector(324, 133.8589),
                new Vector(324, 189.5825));
        Polygon rightRocketClose = new Polygon(new Vector(209.5727, 324), new Vector(209.527, 314.125),
                new Vector(219.6789, 303.9730), new Vector(238.8411, 295.5230), new Vector(238.8411, 324));
        obstacles[0] = habZoneClose;
        obstacles[1] = leftRocketClose;
        obstacles[2] = cargoBayClose;
        obstacles[3] = rightRocketClose;
    }

    private void testMap() {
        obstacles = new Polygon[1];
        Polygon table = new Polygon(new Vector(41, 53), new Vector(41, 74), new Vector(119, 74), new Vector(119, 53));
        obstacles[0] = table;

        visionTargets = new VisionTarget[1];
        visionTargets[0] = new VisionTarget(new Vector(139, 142.5), -Math.PI / 2, false);
    }
}
