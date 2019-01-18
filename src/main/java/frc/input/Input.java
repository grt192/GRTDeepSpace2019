/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.input;

import edu.wpi.first.wpilibj.XboxController;
import frc.networking.FieldGUIServer;
import frc.sequence.RumbleSeguence;
import frc.sequence.PlaceHatch;

/**
 * Add your docs here.
 */
public class Input {

    public static final XboxController XBOX = new XboxController(0);
    public static final FieldGUIServer GUI = new FieldGUIServer();
    public static final RumbleSeguence RUMBLE = new RumbleSeguence();
    public static final PlaceHatch HATCHES_SEQUENCE = new PlaceHatch();
}
