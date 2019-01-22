/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.config.Config;
import frc.fieldmap.FieldMap;
import frc.input.Input;
import frc.mechs.Hatches;
import frc.modes.Mode;
import frc.modes.PathfindingControl;
import frc.positiontracking.BasicPositionTracker;
import frc.positiontracking.KalmanFilterPositionTracker;
import frc.positiontracking.PositionTracker;
import frc.sequence.Sequence;
import frc.swerve.NavXGyro;
import frc.swerve.Swerve;
import frc.vision.Camera;
import frc.vision.JeVois;;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

    private Mode DEFAULT_MODE;
    private Mode currentMode;

    public static Swerve SWERVE;
    public static NavXGyro GYRO;
    public static PositionTracker POS_TRACKER;
    public static FieldMap FIELD_MAP;
    public static Hatches HATCHES;
    public static double ROBOT_WIDTH;
    public static double ROBOT_HEIGHT;
    public static double ROBOT_RADIUS;
    public static Camera HATCH_JEVOIS;

    @Override
    public void robotInit() {
        Config.start();
        ROBOT_WIDTH = Config.getDouble("robot_width");
        ROBOT_HEIGHT = Config.getDouble("robot_height");
        ROBOT_RADIUS = Math.sqrt(ROBOT_WIDTH * ROBOT_WIDTH + ROBOT_HEIGHT * ROBOT_HEIGHT) / 2;
        FIELD_MAP = new FieldMap();
        GYRO = new NavXGyro();
        HATCH_JEVOIS = new Camera("hatch_cam");
        HATCHES = new Hatches();
        POS_TRACKER = new KalmanFilterPositionTracker();
        POS_TRACKER.set(ROBOT_HEIGHT / 2, ROBOT_WIDTH / 2);
        SWERVE = new Swerve();
        Sequence.initSequneces();
        Mode.initModes();
        DEFAULT_MODE = Mode.DRIVER_CONTROL;
        currentMode = DEFAULT_MODE;
        Input.GUI.start();
        // CameraServer.getInstance().startAutomaticCapture(0);
    }

    private void loop() {
        // handle mode switching
        String line = Input.GUI.readLine();
        while (line != "") {
            System.out.println(line);
            String[] message = line.split(" ");
            switch (message[0]) {
            case "move":
                double x = Double.parseDouble(message[1]);
                double y = Double.parseDouble(message[2]);
                PathfindingControl.PATHFINDING_CONTROL.setTarget(x, y);
            case "resume":
                changeMode(Mode.PATHFINDING_CONTROL);
                break;
            case "pause":
                changeMode(DEFAULT_MODE);
                break;
            }
            line = Input.GUI.readLine();
        }
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
