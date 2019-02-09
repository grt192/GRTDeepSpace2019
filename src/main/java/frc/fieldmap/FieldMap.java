/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.fieldmap;

import java.util.HashSet;
import java.util.Set;

import frc.fieldmap.geometry.Circle;
import frc.fieldmap.geometry.Polygon;
import frc.fieldmap.geometry.Vector;
import frc.robot.Robot;

/**
 * Add your docs here.
 */
public class FieldMap {

    private double FIELD_WIDTH, FIELD_HEIGHT;
    private Vector bounds;
    private Polygon wall;
    private Polygon[] obstacles;
    private VisionTarget[] visionTargets;

    public FieldMap() {
        // buildMap();
        // testMap();
        // wall = new Polygon(new Vector(0, 0), new Vector(FIELD_HEIGHT, 0), new
        // Vector(FIELD_HEIGHT, FIELD_WIDTH),
        // new Vector(0, FIELD_WIDTH));
        testMapShop();
    }

    public boolean lineOfSight(Vector v1, Vector v2) {
        Vector dif = v2.subtract(v1);
        double d = v1.distanceTo(v2);
        if (d == 0.0)
            return true;
        Vector norm = dif.multiply(Robot.ROBOT_RADIUS / d).normal();
        Polygon rect = new Polygon(v1.add(norm), v2.add(norm), v2.subtract(norm), v1.subtract(norm));
        Circle startCircle = new Circle(v1, Robot.ROBOT_RADIUS);
        Circle endCircle = new Circle(v2, Robot.ROBOT_RADIUS);
        if (shapeIntersects(rect))
            return false;
        if (shapeIntersects(startCircle))
            return false;
        if (shapeIntersects(endCircle))
            return false;
        return true;
    }

    public boolean shapeIntersects(Polygon p) {
        if (p.outsideBounds(bounds))
            return true;
        for (Polygon poly : obstacles) {
            if (p.intersects(poly))
                return true;
        }
        return false;
    }

    public boolean shapeIntersects(Circle c) {
        if (c.outsideBounds(bounds))
            return true;
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

    public Vector closestWallPoint(Vector p) {
        return wall.closestPoint(p);
    }

    public Polygon[] getObstacles() {
        return obstacles;
    }

    public Set<Vector> generateNodes() {
        double radius = Robot.ROBOT_RADIUS + 1.0;
        double bigRadius = radius + 0.5;
        Set<Vector> nodeSet = new HashSet<>();
        for (Polygon p : obstacles) {
            Vector[] nodes = p.getPossibleNodes(bigRadius);
            for (Vector v : nodes) {
                Circle c = new Circle(v, radius);
                if (!shapeIntersects(c))
                    nodeSet.add(v);
            }
        }
        return nodeSet;
    }

    private void buildMap() {
        FIELD_WIDTH = 27 * 12;
        FIELD_HEIGHT = 54 * 12;
        bounds = new Vector(FIELD_HEIGHT, FIELD_WIDTH);
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
        FIELD_WIDTH = 14 * 12;
        FIELD_HEIGHT = 14 * 12;
        bounds = new Vector(FIELD_HEIGHT, FIELD_WIDTH);
        obstacles = new Polygon[2];
        Polygon table = new Polygon(new Vector(48, 72), new Vector(48, 96), new Vector(120, 96), new Vector(120, 72));
        Polygon cargoShip = new Polygon(new Vector(84, 168), new Vector(84, 151), new Vector(108, 151),
                new Vector(108, 168));
        obstacles[0] = table;
        obstacles[1] = cargoShip;

        visionTargets = new VisionTarget[1];
        visionTargets[0] = new VisionTarget(new Vector(96, 151), -Math.PI / 2, false);
    }

    private void testMapShop() {
        FIELD_WIDTH = 14 * 12 + 10;
        FIELD_HEIGHT = 16 * 12;
        bounds = new Vector(FIELD_HEIGHT, FIELD_WIDTH);
        obstacles = new Polygon[2];
        Polygon rocket = new Polygon(new Vector(105, FIELD_WIDTH), new Vector(105, FIELD_WIDTH - 3.5),
                new Vector(105 + 9, FIELD_WIDTH - 22), new Vector(105 + 18.5 + 9, FIELD_WIDTH - 22),
                new Vector(105 + 9 + 9 + 18.5, FIELD_WIDTH - 3.5), new Vector(105 + 9 + 9 + 18.5, FIELD_WIDTH));
        Polygon habzone = new Polygon(new Vector(0, 65.5), new Vector(67, 65.5), new Vector(67, 116),
                new Vector(0, 116));
        obstacles[0] = rocket;
        obstacles[1] = habzone;

        visionTargets = new VisionTarget[4];
        VisionTarget intake = new VisionTarget(new Vector(0, 33.5), 0, false);
        VisionTarget rightSideRocket = new VisionTarget(
                new Vector((2 * (105 + 18.5 + 9) + 9) / 2, (2 * FIELD_WIDTH - 3.5 - 22) / 2), -2.0944 + Math.PI / 2,
                false);
        VisionTarget centerSideRocket = new VisionTarget(new Vector((114 + 114 + 18.5) / 2, FIELD_WIDTH - 22),
                -Math.PI / 2, true);
        VisionTarget leftSideRocket = new VisionTarget(
                new Vector((2 * (105) + 9) / 2, (2 * FIELD_WIDTH - 3.5 - 22) / 2), -1.0472 - Math.PI / 2, false);
        visionTargets[0] = intake;
        visionTargets[1] = leftSideRocket;
        visionTargets[2] = rightSideRocket;
        visionTargets[3] = centerSideRocket;
    }
}
