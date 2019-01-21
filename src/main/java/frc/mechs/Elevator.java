package frc.mechs;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.config.Config;

public class Elevator {

    private TalonSRX winch;
    private TalonSRX winchFollower;

    public int rocketTop;
    public int rocketMiddle;
    public int rocketBottom;
    public int cargoShip;
    public int pickup;

    public Elevator() {
        winch = new TalonSRX(Config.getInt("winch"));
        winchFollower = new TalonSRX(Config.getInt("winch_follower"));
        Config.defaultConfigTalon(winchFollower);
        configTalon(winch);
        winchFollower.follow(winch);
    }

    public void setPower(double power) {
        winch.set(ControlMode.PercentOutput, power);
    }

    public void setPosition(int position) {
        winch.set(ControlMode.Position, position);
    }

    private void configTalon(TalonSRX talon) {
        Config.defaultConfigTalon(talon);
        talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        talon.config_kP(0, 0);
        talon.config_kI(0, 0);
        talon.config_kD(0, 0);
        talon.config_kF(0, 0);
    }
}