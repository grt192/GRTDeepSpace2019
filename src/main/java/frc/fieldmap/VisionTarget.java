/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.fieldmap;

import frc.fieldmap.geometry.Vector;
import frc.positiontracking.Position;

/**
 * Add your docs here.
 */
public class VisionTarget {

    private static final double HIGH_HEIGHT = 39.125;
    private static final double LOW_HEIGHT = 31.5;

    public final Position pos;
    public final double height;

    public VisionTarget(Vector pos, double angle, boolean high) {
        this.pos = new Position(pos, angle);
        if (high) {
            height = HIGH_HEIGHT;
        } else {
            height = LOW_HEIGHT;
        }
    }
}
