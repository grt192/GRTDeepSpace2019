/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.config.Config;
import frc.input.Input;
import frc.modes.Mode;
import frc.swerve.Swerve;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

    private static final Mode DEFAULT_MODE = Mode.DRIVER_CONTROL;
    private Mode currentMode;

    public static Swerve SWERVE;

    @Override
    public void robotInit() {
        Config.start();
        Input.GUI.start();
        SWERVE = new Swerve();
        currentMode = DEFAULT_MODE;
    }

    private void loop() {
        // handle mode switching
        String line = Input.GUI.readLine();
        System.out.println(line);
        if (!currentMode.loop()) {
            changeMode(DEFAULT_MODE);
        }
    }

    private void changeMode(Mode newMode) {
        if (currentMode == newMode)
            return;
        currentMode.exit();
        newMode.enter();
        currentMode = newMode;
    }

    @Override
    public void autonomousPeriodic() {
        loop();
    }

    @Override
    public void teleopPeriodic() {
        loop();
    }
}
