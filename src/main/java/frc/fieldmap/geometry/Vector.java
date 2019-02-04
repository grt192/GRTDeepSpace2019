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

    public Vector multiply(double s) {
        return new Vector(x * s, y * s);
    }

    public double distanceSquaredTo(Vector v) {
        double dx = x - v.x;
        double dy = y - v.y;
        return dx * dx + dy * dy;
    }

    public double distanceTo(Vector v) {
        return Math.sqrt(distanceSquaredTo(v));
    }

    public Vector rotate(double angle) {
        return new Vector(x * Math.cos(angle) - y * Math.sin(angle), x * Math.sin(angle) + y * Math.cos(angle));
    }

    @Override
    public String toString() {
        return "Vector[" + x + ", " + y + "]";
    }

    @Override
    public int hashCode() {
        return (int) (x * 1000 + y * 1000 * 1000);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Vector))
            return false;
        Vector v = (Vector) other;
        return (x == v.x && y == v.y);
    }
}
