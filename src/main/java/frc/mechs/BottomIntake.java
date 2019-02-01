package frc.mechs;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import frc.config.Config;

public class BottomIntake {

    private Solenoid intake;
    private TalonSRX intakeMotor;

    public BottomIntake() {
        this.intake = new Solenoid(Config.getInt("roller_sol"));
        this.intakeMotor = new TalonSRX(Config.getInt("roller"));
        Config.defaultConfigTalon(intakeMotor);
        intakeMotor.setNeutralMode(NeutralMode.Coast);
    }

    public void setPower(double power) {
        intakeMotor.set(ControlMode.PercentOutput, power);
    }

    public void in() {
        setPower(0.0);
        intake.set(false);

    }

    public void out() {
        intake.set(true);
    }

    public void toggle() {
        setPower(0);
        intake.set(!intake.get());
    }
}