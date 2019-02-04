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
public class Circle {

    private double radius;
    private Vector position;

    public Circle(Vector pos, double radius) {
        this.radius = radius;
        position = pos;
    }

    public boolean intersects(Polygon p) {
        return p.getClosestDistance(position) <= radius;
    }

    public boolean outsideBounds(Vector bounds) {
        return (position.x - radius <= 0 || position.x + radius >= bounds.x || position.y - radius <= 0
                || position.x + radius >= bounds.y);
    }
}
