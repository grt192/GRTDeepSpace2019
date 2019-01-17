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
public class Circle extends Shape {

    private double radius;
    private Vector position;
    private Vector[] axes;

    public Circle(Vector pos, double radius) {
        this.radius = radius;
        position = pos;
        axes = new Vector[0];
    }

    @Override
    public double getMin(Vector axis) {
        return 0;
    }

    @Override
    public double getMax(Vector axis) {
        return 0;
    }

    @Override
    public Vector[] getAxes() {
        return axes;
    }
}
