/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.modes;

public abstract class Mode {

    public static Mode DRIVER_CONTROL;
    public static PathfindingControl PATHFINDING_CONTROL;
    public static ClimbMode CLIMB_MODE;
    public static ScoreMode SCORE_MODE;
    private static Mode[] modes;

    public static void initModes() {
        DRIVER_CONTROL = new DriverControl();
        PATHFINDING_CONTROL = new PathfindingControl();
        SCORE_MODE = new ScoreMode();
        modes = new Mode[3];
        modes[0] = DRIVER_CONTROL;
        modes[1] = PATHFINDING_CONTROL;
        modes[2] = SCORE_MODE;
    }

    public abstract boolean loop();

    public static Mode getMode(int i) {
        return modes[i];
    }

}
