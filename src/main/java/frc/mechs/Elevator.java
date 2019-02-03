package frc.mechs;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.config.Config;

public class Elevator {

    private TalonSRX winch;
    private TalonSRX winchFollower;

    public int rocketTop = 448228;
    public int rocketMiddle;
    public int rocketBottom = 209000;
    public int cargoShip;
    public final int pickup = 0;

    /*
     * Code for elevator mech which carries a ball up to the third level of the
     * rocket
     */
    public Elevator() {
        winch = new TalonSRX(Config.getInt("winch"));
        winchFollower = new TalonSRX(Config.getInt("winch_follower"));
        Config.defaultConfigTalon(winchFollower);
        configTalon(winch);
        winchFollower.follow(winch);
    }

    /*
     * Sets the power of the winch
     * 
     * @param power
     */
    public void setPower(double power) {
        if (power >= 0.0)
            power = Math.max(power, 0.13);
        winch.set(ControlMode.PercentOutput, power);

    }

    /*
     * Sets the position of the winch
     *
     * @param position
     */
    public void setPosition(int position) {
        winch.set(ControlMode.Position, position);
    }

    /*
    * 
    */
    private void configTalon(TalonSRX talon) {
        Config.defaultConfigTalon(talon);
        talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        talon.setSensorPhase(true);
        talon.configReverseSoftLimitThreshold(pickup);
        talon.configReverseSoftLimitEnable(true);
        talon.configForwardSoftLimitThreshold(rocketTop);
        talon.configForwardSoftLimitEnable(true);
        talon.config_kP(0, 0);
        talon.config_kI(0, 0);
        talon.config_kD(0, 0);
        talon.config_kF(0, 0);
    }
}