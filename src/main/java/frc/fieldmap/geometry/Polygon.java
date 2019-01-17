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
public class Polygon extends Shape {

    private Vector[] points;
    private Vector[] axes;

    public Polygon(Vector... points) {
        this.points = points;
        axes = new Vector[points.length];
        Vector v;
        for (int i = 0; i < points.length - 1; ++i) {
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

}
