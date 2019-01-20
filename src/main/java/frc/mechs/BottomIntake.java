package frc.mechs;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import frc.config.Config;

public class BottomIntake {

    private Solenoid intake1;
    private Solenoid intake2;
    private TalonSRX intakeMotor;

    public BottomIntake() {
        this.intake1 = new Solenoid(Config.getInt("intake1"));
        this.intake2 = new Solenoid(Config.getInt("intake2"));
        this.intakeMotor = new TalonSRX(Config.getInt("bottom-intake-motor"));
    }

    public void setPower(Double power) {
        intakeMotor.set(ControlMode.PercentOutput, power);
    }

    public void in() {
        setPower(0.0);
        intake1.set(false);
        intake2.set(false);

    }

    public void out() {
        intake1.set(true);
        intake2.set(true);
    }
}