package frc.mechs;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.config.Config;
import frc.robot.Robot;

public class Elevator {

    private TalonSRX winch;
    private TalonSRX winchFollower;

    private NetworkTableEntry desiredPos;

    private boolean closedLoop;

    public static final int ROCKET_TOP = 3;
    public static final int ROCKET_MIDDLE = 2;
    public static final int ROCKET_BOTTOM = 0;
    public static final int CARGO_SHIP = 1;
    public static final int PICKUP = -1;

    public static final int rocketTop = 434890;
    public static final int rocketMiddle = 360962;
    public static final int rocketBottom = 209000;
    public static final int cargoShip = 295877;
    public static final int pickup = 0;
    private int[] positions;

    private static int rollerBottom = 500;
    private static int rollerTop = 156670;
    private static int rollerThreshold = 206670;
    private static int dangerSpeed = 15;

    public Elevator() {
        winch = new TalonSRX(Config.getInt("winch"));
        winchFollower = new TalonSRX(Config.getInt("winch_follower"));
        positions = new int[4];
        positions[ROCKET_BOTTOM] = Config.getInt("rocket_1");
        positions[ROCKET_MIDDLE] = Config.getInt("rocket_2");
        positions[ROCKET_TOP] = Config.getInt("rocket_3");
        positions[CARGO_SHIP] = Config.getInt("cargo_ship");
        Config.defaultConfigTalon(winchFollower);
        configTalon(winch);
        winchFollower.follow(winch);
        this.setPower(0);
        desiredPos = NetworkTableInstance.getDefault().getTable("Robot").getSubTable("Elevator").getEntry("target");
        desiredPos.setNumber(-2);
        desiredPos.addListener((event) -> {
            int val = (int) event.value.getDouble();
            if (val < -1)
                return;
            closedLoop = true;
            if ((val == PICKUP && winch.getSelectedSensorPosition() > rollerBottom)
                    || winch.getSelectedSensorPosition() < rollerTop)
                Robot.BOTTOM_INTAKE.forceOut();
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (val == PICKUP)
                winch.set(ControlMode.PercentOutput, -0.3);
            else
                winch.set(ControlMode.Position, positions[val]);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Robot.BOTTOM_INTAKE.setToDesiredPos();
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    }

    public void setPower(double power) {
        closedLoop = false;
        desiredPos.setNumber(-2);
        winch.set(ControlMode.PercentOutput, power);
    }

    public void setPosition(int position) {
        desiredPos.setNumber(position);
    }

    public boolean isClosedLoop() {
        return closedLoop;
    }

    public void dontKillRoller() {
        int pos = winch.getSelectedSensorPosition();
        int speed = winch.getSelectedSensorVelocity();
        boolean danger = false;
        if (pos >= rollerBottom && pos <= rollerTop) {
            danger = true;
        } else if (pos >= rollerBottom - rollerThreshold && pos <= rollerBottom) {
            if (speed > dangerSpeed) {
                danger = true;
            }
        } else if (pos <= rollerTop - rollerThreshold && pos >= rollerTop) {
            if (speed < -dangerSpeed) {
                danger = true;
            }
        }
        if (danger)
            Robot.BOTTOM_INTAKE.forceOut();
        else
            Robot.BOTTOM_INTAKE.setToDesiredPos();
    }

    private void configTalon(TalonSRX talon) {
        Config.defaultConfigTalon(talon);
        talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        talon.setSensorPhase(true);
        talon.configReverseSoftLimitThreshold(pickup);
        talon.configReverseSoftLimitEnable(true);
        talon.configForwardSoftLimitThreshold(rocketTop);
        talon.configForwardSoftLimitEnable(true);
        talon.config_kP(0, 1024.0 / 4000);
        talon.config_kI(0, 0);
        talon.config_kD(0, 1024.0 / 10000);
        talon.config_kF(0, 0);
    }
}