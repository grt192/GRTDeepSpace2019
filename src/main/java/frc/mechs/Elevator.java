package frc.mechs;

import com.ctre.phoenix.motorcontrol.ControlMode;
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
        this.winch = new TalonSRX(Config.getInt("winch_motor"));
        this.winchFollower = new TalonSRX(Config.getInt("winch_motor_follower"));

        winchFollower.follow(winch);
    }

    public void setPower(Double power) {
        winch.set(ControlMode.PercentOutput, power);
    }

    public void setPosition(int position) {
        winch.set(ControlMode.Position, position);
    }
}