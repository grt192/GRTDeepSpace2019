/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.modes;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.input.Input;
import frc.input.JoystickProfile;
import frc.robot.Robot;

/**
 * Add your docs here.
 */
class DriverControl extends Mode {

    @Override
    public void enter() {
        Input.RUMBLE.start();
    }

    @Override
    public boolean loop() {
        driveSwerve();
        return true;
    }

    private void driveSwerve() {
        double x = JoystickProfile.clipAndSquare(-Input.XBOX.getY(Hand.kLeft));
        double y = JoystickProfile.clipAndSquare(Input.XBOX.getX(Hand.kLeft));
        double lTrigger = Input.XBOX.getTriggerAxis(Hand.kLeft);
        double rTrigger = Input.XBOX.getTriggerAxis(Hand.kRight);
        double rotate = 0;
        if (lTrigger + rTrigger > 0.05) {
            rotate = rTrigger * rTrigger - lTrigger * lTrigger;
            // } else {
            // double rx = -Input.XBOX.getY(Hand.kRight);
            // double ry = Input.XBOX.getX(Hand.kRight);
            // if (rx * rx + ry * ry > 0.7) {
            // double theta = Math.atan2(ry, rx);
            // Robot.SWERVE.setAngle(theta);
            // }
        }
        Robot.SWERVE.drive(x, y, rotate);
    }

}
