/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.sequence;

import frc.robot.Robot;

/**
 * Add your docs here.
 */
public class ClimbSequence extends Sequence {

    @Override
    public void runSequence() {
        Robot.SWERVE.setAngle(0);
        Robot.CLIMBER.deployRails();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Robot.SWERVE.setRobotCentric(true);
        Robot.SWERVE.drive(-0.25, 0, 0);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Robot.SWERVE.drive(0, 0, 0);
        while (!Robot.CLIMBER.isUp()) {
            Robot.CLIMBER.ascend();
            if (Robot.CLIMBER.getPercentUp() > 0.5) {
                Robot.SWERVE.drive(-0.25, 0, 0);
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Robot.CLIMBER.stop();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Robot.SWERVE.drive(0, 0, 0);
        while (Robot.CLIMBER.getPercentUp() > 0.5) {
            Robot.CLIMBER.pullUp();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Robot.CLIMBER.stop();
    }
}
