package frc.mechs;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.config.Config;
import frc.robot.Robot;

public class Elevator {

    private TalonSRX winch;
    private TalonSRX winchFollower;

    private boolean closedLoop;

    public static int rocketTop = 434890;
    public static int rocketMiddle = 360962;
    public static int rocketBottom = 209000;
    public static int cargoShip = 295877;
    public static final int pickup = 0;

    private static int rollerBottom = 0;
    private static int rollerTop = 1566700;
    private static int rollerThreshold;

    public Elevator() {
        winch = new TalonSRX(Config.getInt("winch"));
        winchFollower = new TalonSRX(Config.getInt("winch_follower"));
        Config.defaultConfigTalon(winchFollower);
        configTalon(winch);
        winchFollower.follow(winch);
    }

    public void setPower(double power) {
        closedLoop = false;
        winch.set(ControlMode.PercentOutput, power);

    }

    public void setPosition(int position) {
        closedLoop = true;
        if (position != pickup)
            winch.set(ControlMode.Position, position);
        else
            winch.set(ControlMode.PercentOutput, -0.3);
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
            if (speed > 0) {
                danger = true;
            }
        } else if (pos <= rollerTop - rollerThreshold && pos >= rollerTop) {
            if (speed < 0) {
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