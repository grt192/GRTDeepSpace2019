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
import frc.mechs.Elevator;
import frc.robot.Robot;
import frc.sequence.Sequence;

/**
 * Add your docs here.
 */
class DriverControl extends Mode {

    private int pov = -1;
    private int lastPov;
    private double intakePower;
    private double elevatorPower;

    @Override
    public boolean loop() {
        driveMechs();
        driveSwerve();
        return true;
    }

    private void driveMechs() {

        // Swerve Driver: Intake hatch
        if (Input.SWERVE_XBOX.getXButtonPressed()) {
            Sequence.INTAKE_HATCH.start();
        }

        // Swerve Driver: Place Hatch
        if (Input.SWERVE_XBOX.getAButtonPressed()) {
            Sequence.PLACE_HATCH.start();
        }

        // Swerve Driver: Right joystick to intake power
        intakePower = JoystickProfile.applyDeadband(Input.SWERVE_XBOX.getY(Hand.kRight), 0.3);

        // Swerve Driver: Activate roller
        if (Input.SWERVE_XBOX.getBButton()) {
            Robot.BOTTOM_INTAKE.out();
            intakePower = 1.0;
        }

        // Swerve Driver: Intake sequence, pull ball into inner roller
        if (Input.SWERVE_XBOX.getBButtonReleased()) {
            Sequence.INTAKE_SEQUENCE.start();
        }

        // If no sequence is running, give manual control to rollers
        if (!Sequence.INTAKE_SEQUENCE.isRunning()) {
            Robot.BOTTOM_INTAKE.setPower(intakePower);
            Robot.TOP_INTAKE.setPower(intakePower);
        }

        // Mech Driver: Get manual elevator power (from controller)
        elevatorPower = JoystickProfile.applyDeadband(-Input.MECH_XBOX.getY(Hand.kLeft), 0.2);

        // If no elecator sequence is running, give manual control to height
        if (!Robot.ELEVATOR.isClosedLoop() || elevatorPower != 0) {
            Robot.ELEVATOR.setPower(elevatorPower);
        }

        // Mech Driver: goto elevator position
        switch (Input.MECH_XBOX.getPOV()) {
        case 0:
            Robot.ELEVATOR.setPosition(Elevator.rocketTop);
            System.out.println("Rocket Top");
            break;
        case 90:
            Robot.ELEVATOR.setPosition(Elevator.rocketMiddle);
            System.out.println("Rocket Middle");
            break;
        case 180:
            Robot.ELEVATOR.setPosition(Elevator.rocketBottom);
            System.out.println("Rocket Bottom");
            break;
        case 270:
            Robot.ELEVATOR.setPosition(Elevator.cargoShip);
            System.out.println("Cargo Ship");
            break;
        }
        if (Input.MECH_XBOX.getBackButtonPressed()) {
            Robot.ELEVATOR.setPosition(Elevator.pickup);
        }

        if (Input.MECH_XBOX.getAButtonPressed()) {
            Robot.BOTTOM_INTAKE.out();
        } else if (Input.MECH_XBOX.getBButtonPressed()) {
            Robot.BOTTOM_INTAKE.in();
        }
    }

    private void driveSwerve() {
        if (Input.SWERVE_XBOX.getStartButtonPressed())
            Robot.SWERVE.setRobotCentric(true);
        if (Input.SWERVE_XBOX.getBackButtonPressed())
            Robot.SWERVE.setRobotCentric(false);

        double x = JoystickProfile.applyDeadband(-Input.SWERVE_XBOX.getY(Hand.kLeft));
        double y = JoystickProfile.applyDeadband(Input.SWERVE_XBOX.getX(Hand.kLeft));
        double mag = Math.sqrt(x * x + y * y);
        x *= mag;
        y *= mag;
        boolean buttonPressed = false;
        if (pov == -1) {
            buttonPressed = true;
        }
        pov = Input.SWERVE_XBOX.getPOV();
        if (Input.SWERVE_XBOX.getBumperPressed(Hand.kLeft)) {
            pov = lastPov - 45;
        }
        if (Input.SWERVE_XBOX.getBumperPressed(Hand.kRight)) {
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

        double lTrigger = Input.SWERVE_XBOX.getTriggerAxis(Hand.kLeft);
        double rTrigger = Input.SWERVE_XBOX.getTriggerAxis(Hand.kRight);
        double rotate = 0;
        if (lTrigger + rTrigger > 0.05) {
            rotate = rTrigger * rTrigger - lTrigger * lTrigger;
        }
        Robot.SWERVE.drive(x, y, rotate);
    }

}
