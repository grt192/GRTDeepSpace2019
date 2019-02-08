/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.config.Config;
import frc.fieldmap.FieldMap;
import frc.input.Input;
import frc.input.JoystickProfile;
import frc.mechs.BottomIntake;
import frc.mechs.Climber;
import frc.mechs.Elevator;
import frc.mechs.Hatches;
import frc.mechs.TopIntake;
import frc.modes.Mode;
import frc.positiontracking.KalmanFilterPositionTracker;
import frc.positiontracking.PositionTracker;
import frc.sequence.Sequence;
import frc.swerve.NavXGyro;
import frc.swerve.Swerve;
import frc.vision.Camera;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

    private NetworkTableEntry mode;

    public static Swerve SWERVE;
    public static NavXGyro GYRO;
    public static PositionTracker POS_TRACKER;
    public static FieldMap FIELD_MAP;
    public static Hatches HATCHES;
    public static BottomIntake BOTTOM_INTAKE;
    public static Elevator ELEVATOR;
    public static TopIntake TOP_INTAKE;
    public static Climber CLIMBER;
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
        ELEVATOR = new Elevator();
        TOP_INTAKE = new TopIntake();
        BOTTOM_INTAKE = new BottomIntake();
        HATCHES = new Hatches();
        HATCH_JEVOIS = new Camera("hatch_cam");
        POS_TRACKER = new KalmanFilterPositionTracker();
        POS_TRACKER.set(ROBOT_HEIGHT / 2, ROBOT_WIDTH / 2);
        SWERVE = new Swerve();
        Sequence.initSequneces();
        Mode.initModes();
        mode = NetworkTableInstance.getDefault().getTable("Robot").getEntry("mode");
        mode.setNumber(0);
    }

    private void loop() {
        ELEVATOR.dontKillRoller(); // pls
        // handle mode switching
        int i = mode.getNumber(0).intValue();
        if (manualOverride()) {
            mode.setNumber(0);
            i = 0;
        }
        if (!Mode.getMode(i).loop()) {
            mode.setNumber(0);
        }
    }

    private boolean manualOverride() {
        double x = JoystickProfile.applyDeadband(-Input.SWERVE_XBOX.getY(Hand.kLeft));
        double y = JoystickProfile.applyDeadband(Input.SWERVE_XBOX.getX(Hand.kLeft));
        if (x != 0 || y != 0)
            return true;
        return false;
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
