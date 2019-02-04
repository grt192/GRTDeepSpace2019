/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.fieldmap.geometry;

/**
 * Add your docs here.
 */
public class Polygon {

    private Vector[] points;
    private Vector[] axes;

    public Polygon(Vector... points) {
        this.points = points;
        axes = new Vector[points.length];
        Vector v;
        for (int i = 0; i < points.length; ++i) {
            if (i < points.length - 1) {
                v = points[i].subtract(points[i + 1]);
                v = v.normal();
            } else {
                v = points[i].subtract(points[0]);
                v = v.normal();
            }
            axes[i] = v;
        }

    }

    public boolean outsideBounds(Vector bounds) {
        for (Vector p : points)
            if (p.x >= bounds.x || p.x <= 0 || p.y >= bounds.y || p.y <= 0)
                return true;
        return false;
    }

    public boolean intersects(Polygon other) {
        Vector[] axes = getAxes();
        for (int i = 0; i < axes.length; ++i) {
            double min1 = getMin(axes[i]);
            double max1 = getMax(axes[i]);
            double min2 = other.getMin(axes[i]);
            double max2 = other.getMax(axes[i]);
            if (!(max1 >= min2 && max2 >= min1)) {
                return false;
            }
        }
        Vector[] otherAxes = other.getAxes();
        for (int i = 0; i < otherAxes.length; ++i) {
            double min1 = getMin(otherAxes[i]);
            double max1 = getMax(otherAxes[i]);
            double min2 = other.getMin(otherAxes[i]);
            double max2 = other.getMax(otherAxes[i]);
            if (!(max1 >= min2 && max2 >= min1)) {
                return false;
            }
        }
        return true;
    }

    public Vector[] getAxes() {
        return axes;
    }

    public double getMin(Vector axis) {
        double min = points[0].dot(axis);
        for (int i = 1; i < points.length; ++i) {
            min = Math.min(min, points[i].dot(axis));
        }
        return min;
    }

    public double getMax(Vector axis) {
        double max = points[0].dot(axis);
        for (int i = 1; i < points.length; ++i) {
            max = Math.max(max, points[i].dot(axis));
        }
        return max;
    }

    public double getClosestDistance(Vector center) {
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < points.length; ++i)
            min = Math.min(min, center.distanceSquaredTo(points[i]));
        return Math.sqrt(min);
    }

    public Vector[] getPossibleNodes(double radius) {
        Vector[] nodes = new Vector[points.length * 2];
        double ROOT_2 = Math.sqrt(2.0);
        int j = 0;
        for (int i = 0; i < points.length; ++i) {
            Vector p = points[i];
            Vector p1, p2;
            if (i == 0)
                p1 = points[points.length - 1];
            else
                p1 = points[i - 1];
            if (i == points.length - 1)
                p2 = points[0];
            else
                p2 = points[i + 1];
            Vector d1 = p1.subtract(p).multiply(1 / p1.distanceTo(p));
            Vector d2 = p2.subtract(p).multiply(1 / p2.distanceTo(p));
            double d = radius / Math.sqrt((1 - d1.dot(d2)) / 2);
            Vector v = d1.add(d2).multiply(d / ROOT_2);
            nodes[j++] = p.add(v);
            nodes[j++] = p.subtract(v);
        }
        return nodes;
    }

}
