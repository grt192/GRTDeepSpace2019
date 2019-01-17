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
public class Vector {

    public final double x, y;
    public double magnitude = -1;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector add(Vector v) {
        return new Vector(x + v.x, y + v.y);
    }

    public Vector subtract(Vector v) {
        return new Vector(x - v.x, y - v.y);
    }

    public Vector normal() {
        return new Vector(-y, x);
    }

    public double dot(Vector v) {
        return x * v.x + y * v.y;
    }

    public double getMagnitude() {
        if (magnitude < 0)
            magnitude = Math.sqrt(x * x + y * y);
        return magnitude;
    }

    public Vector multiply(double s) {
        return new Vector(x * s, y * s);
    }
}
