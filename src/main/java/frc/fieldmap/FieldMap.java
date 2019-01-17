/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.fieldmap;

import frc.fieldmap.geometry.Circle;
import frc.fieldmap.geometry.Polygon;

/**
 * Add your docs here.
 */
public class FieldMap {

    private Polygon[] obstacles;

    public FieldMap() {
        buildMap();
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

    }
}
