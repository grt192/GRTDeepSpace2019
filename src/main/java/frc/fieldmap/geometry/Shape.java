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
public abstract class Shape {

    public abstract double getMin(Vector axis);

    public abstract double getMax(Vector axis);

    public abstract Vector[] getAxes();

    public boolean intersects(Shape other) {
        Vector[] axes = getAxes();
        for (int i = 0; i < axes.length; i++) {
            double min1 = getMin(axes[i]);
            double max1 = getMax(axes[i]);
            double min2 = other.getMin(axes[i]);
            double max2 = other.getMax(axes[i]);
            if (!(max1 >= min2 && max2 >= min1)) {
                return false;
            }
        }
        Vector[] otherAxes = other.getAxes();
        for (int i = 0; i < otherAxes.length; i++) {
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

}
