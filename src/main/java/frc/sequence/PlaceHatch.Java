/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.sequence;

import frc.mechs.Hatches;
import frc.robot.Robot;

/**
 * Add your docs here.
 */
public class PlaceHatch extends Sequence {

    @Override
    public void run() {
        Robot.HATCHES.setOut();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Robot.HATCHES.setIn();
    }
}
