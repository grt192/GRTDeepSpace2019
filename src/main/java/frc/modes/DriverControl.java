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
import frc.sequence.Sequence;

/**
 * Add your docs here.
 */
class DriverControl extends Mode {

    @Override
    public boolean loop() {
        driveMechs();
        driveSwerve();
        return true;
    }

    private void driveMechs() {
        if (Input.XBOX.getAButtonPressed()) {
            Sequence.PLACE_HATCH.start();
        }
        if (Input.XBOX.getBButtonPressed()) {
            Robot.BOTTOM_INTAKE.toggle();
        }
        double intakePower = JoystickProfile.applyDeadband(Input.XBOX.getY(Hand.kRight));
        Robot.BOTTOM_INTAKE.setPower(intakePower);
        Robot.TOP_INTAKE.setPower(intakePower);
    }

    private void driveSwerve() {
        if (Input.XBOX.getYButtonPressed())
            Robot.SWERVE.setRobotCentric(true);
        if (Input.XBOX.getXButtonPressed())
            Robot.SWERVE.setRobotCentric(false);
        double x = JoystickProfile.applyDeadband(-Input.XBOX.getY(Hand.kLeft));
        double y = JoystickProfile.applyDeadband(Input.XBOX.getX(Hand.kLeft));
        double mag = Math.sqrt(x * x + y * y);
        x *= mag;
        y *= mag;
        int pov = Input.XBOX.getPOV();
        if (pov >= 0) {
            Robot.SWERVE.setAngle(Math.toRadians(pov));
        }
        double lTrigger = Input.XBOX.getTriggerAxis(Hand.kLeft);
        double rTrigger = Input.XBOX.getTriggerAxis(Hand.kRight);
        double rotate = 0;
        if (lTrigger + rTrigger > 0.05) {
            rotate = rTrigger * rTrigger - lTrigger * lTrigger;
        }
        Robot.SWERVE.drive(x, y, rotate);
    }

}
