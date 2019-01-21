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
public class ShootBall extends Sequence {

    @Override
    public void run() {
        Robot.BOTTOMINTAKE.out();
        Robot.ELEVATOR.down();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Robot.BOTTOMINTAKE.in();
        Robot.ELEVATOR.up();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
