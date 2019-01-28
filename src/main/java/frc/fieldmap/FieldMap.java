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

    private void buildMap() {
        Polygon habZoneClose = new Polygon(new Vector(48, 73.6291), new Vector(0, 73.6291), new Vector(0, 251.2433),
                new Vector(48, 251.2433));
        Polygon leftRocketClose = new Polygon(new Vector(209.5727, 0), new Vector(209.5727, 7.63),
                new Vector(219.179, 27.32), new Vector(238.01, 27.32), new Vector(246.452, 7.630),
                new Vector(246.452, 0));

        Polygon cargoBayClose = new Polygon(new Vector(325.01, 133.082), new Vector(250.072, 133.082),
                new Vector(220.216, 138.249), new Vector(220.216, 183.751), new Vector(250.011, 189),
                new Vector(325.01, 189));
        Polygon rightRocketClose = new Polygon(new Vector(209.073, 321.608), new Vector(209.073, 313.089),
                new Vector(219.51, 294.559), new Vector(238.01, 294.559), new Vector(248.447, 313.089),
                new Vector(248.447, 321.608));
        obstacles[0] = habZoneClose;
        obstacles[1] = leftRocketClose;
        obstacles[2] = cargoBayClose;
        obstacles[3] = rightRocketClose;
    }

    private void testMap() {
        obstacles = new Polygon[1];
        Polygon table = new Polygon(new Vector(41, 53), new Vector(41, 74), new Vector(119, 74), new Vector(119, 53));
        obstacles[0] = table;
    }
}
