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
public class IntakeSequence extends Sequence {

    @Override
    public void runSequence() {
        Robot.BOTTOM_INTAKE.setPower(1.0);
        Robot.BOTTOM_INTAKE.in();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Robot.BOTTOM_INTAKE.setPower(0.0);
    }
}
