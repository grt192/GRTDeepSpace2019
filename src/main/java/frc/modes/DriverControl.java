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

    private int pov = -1;
    private int lastPov;

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
        double intakePower = JoystickProfile.applyDeadband(Input.XBOX.getY(Hand.kRight), 0.3);
        if (Input.XBOX.getBButton()) {
            Robot.BOTTOM_INTAKE.out();
            intakePower = 1.0;
        }
        if (Input.XBOX.getBButtonReleased()) {
            Sequence.INTAKE_SEQUENCE.start();
        }
        if (!Sequence.INTAKE_SEQUENCE.isRunning()) {
            Robot.BOTTOM_INTAKE.setPower(intakePower);
            Robot.TOP_INTAKE.setPower(intakePower);
        }
        double elevatorPower = JoystickProfile.applyDeadband(-Input.MECH_XBOX.getY(Hand.kLeft));
        Robot.ELEVATOR.setPower(elevatorPower);
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
        boolean buttonPressed = false;
        if (pov == -1) {
            buttonPressed = true;
        }
        pov = Input.XBOX.getPOV();
        if (Input.XBOX.getBumperPressed(Hand.kLeft)) {
            pov = lastPov - 45;
        }
        if (Input.XBOX.getBumperPressed(Hand.kRight)) {
            pov = lastPov + 45;
        }
        if (buttonPressed) {
            if (pov == -1) {
            } else if (pov == 45) {
                Robot.SWERVE.setAngle(Math.toRadians(52));
                lastPov = pov;
            } else if (pov == 135) {
                Robot.SWERVE.setAngle(Math.toRadians(142));
                lastPov = pov;
            } else if (pov == 235) {
                Robot.SWERVE.setAngle(Math.toRadians(302));
                lastPov = pov;
            } else if (pov == 325) {
                Robot.SWERVE.setAngle(Math.toRadians(212));
                lastPov = pov;
            } else {
                Robot.SWERVE.setAngle(Math.toRadians(pov));
                lastPov = pov;
            }

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
