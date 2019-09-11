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
            Robot.HATCHES.setMiddle(false);
        }

        // Swerve Driver: Place Hatch
        if (Input.SWERVE_XBOX.getAButtonPressed()) {
            Sequence.PLACE_HATCH.start();
        }

        // Swerve Driver: Right joystick to intake power
        intakePower = -JoystickProfile.applyDeadband(Input.SWERVE_XBOX.getY(Hand.kRight), 0.3);
        if (Input.SWERVE_XBOX.getBButton())
            intakePower = -1.0;

        // Swerve Driver: Activate roller
        if (Robot.BOTTOM_INTAKE.getPosition()) {
            Robot.BOTTOM_INTAKE.setPower(intakePower);
        }
        Robot.TOP_INTAKE.setPower(-intakePower);

        // Mech Driver: Get manual elevator power (from controller)
        elevatorPower = JoystickProfile.applyDeadband(-Input.MECH_XBOX.getY(Hand.kLeft), 0.2);

        // If no elevator sequence is running, give manual control to height
        if (!Robot.ELEVATOR.isClosedLoop() || elevatorPower != 0) {
            Robot.ELEVATOR.setPower(elevatorPower);
            System.out.println("elevator power: " + elevatorPower);
        }

        // Mech Driver: goto elevator position

        switch (Input.MECH_XBOX.getPOV()) {
        // case 0:
        // Robot.ELEVATOR.setPosition(Elevator.ROCKET_TOP); //
        // System.out.println("Rocket Top");
        // break;
        case 90:
            Robot.ELEVATOR.setPosition(Elevator.ROCKET_MIDDLE); //
            System.out.println("Rocket Middle");
            break;
        case 180:
            Robot.ELEVATOR.setPosition(Elevator.ROCKET_BOTTOM); //
            System.out.println("Rocket Bottom");
            break;
        case 270:
            Robot.ELEVATOR.setPosition(Elevator.CARGO_SHIP); //
            System.out.println("Cargo Ship");
            break;
        }

        if (Input.MECH_XBOX.getBackButtonPressed()) {
            Robot.ELEVATOR.setPosition(Elevator.PICKUP);
        }

        if (Input.MECH_XBOX.getStartButtonPressed()) {
            Robot.BOTTOM_INTAKE.toggle();
        }

        // Experimental stuff

        if(Input.MECH_XBOX.getAButtonPressed()){
            
        }
        // if (Input.MECH_XBOX.getAButtonPressed()) {
        //     Sequence.EXTEND_HOOK.start();
        // } else if (Input.MECH_XBOX.getBButtonPressed()) {
        //     Sequence.RETRACT_HOOK.start();
        // }
        // if (Input.MECH_XBOX.getBumperPressed(Hand.kLeft)) {
        // Robot.CLIMBER.toggleFront();
        // }
        // if (Input.MECH_XBOX.getBumperPressed(Hand.kRight)) {
        // Robot.CLIMBER.toggleBack();
        // }

    }

    private void driveSwerve() {
        if (Input.SWERVE_XBOX.getStartButtonPressed())
            Robot.SWERVE.setRobotCentric(true);
        if (Input.SWERVE_XBOX.getBackButtonPressed())
            Robot.SWERVE.setRobotCentric(false);

        double x = JoystickProfile.applyDeadband(-Input.SWERVE_XBOX.getY(Hand.kLeft));
        double y = JoystickProfile.applyDeadband(Input.SWERVE_XBOX.getX(Hand.kLeft));
        // double y =
        // JoystickProfile.applyDeadband(Input.SWERVE_XBOX.getX(Hand.kRight));
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
                Robot.SWERVE.setAngle(Math.toRadians(-60));
                lastPov = pov;
            } else if (pov == 135) {
                Robot.SWERVE.setAngle(Math.toRadians(-150));
                lastPov = pov;
            } else if (pov == 235) {
                Robot.SWERVE.setAngle(Math.toRadians(-300));
                lastPov = pov;
            } else if (pov == 325) {
                Robot.SWERVE.setAngle(Math.toRadians(-210));
                lastPov = pov;
            } else {
                Robot.SWERVE.setAngle(Math.toRadians(-pov));
                lastPov = pov;
            }

        }

        double lTrigger = Input.SWERVE_XBOX.getTriggerAxis(Hand.kLeft);
        double rTrigger = Input.SWERVE_XBOX.getTriggerAxis(Hand.kRight);
        double rotate = 0;
        if (lTrigger + rTrigger > 0.05) {
            rotate = -(rTrigger * rTrigger - lTrigger * lTrigger);
            System.out.println("rotate: " + rotate);
        }
        Robot.SWERVE.drive(x, y, rotate);
    }

}
