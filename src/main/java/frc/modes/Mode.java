/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.modes;

public abstract class Mode {

    public static final Mode DRIVER_CONTROL = new DriverControl();

    public void enter() {
    }

    public void exit() {

    }

    public abstract boolean loop();

}
