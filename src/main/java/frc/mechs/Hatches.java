package frc.mechs;

import edu.wpi.first.wpilibj.Solenoid;
import frc.config.Config;

public class Hatches {

    private Solenoid top;
    private Solenoid bottom;

    public Hatches() {
        top = new Solenoid(Config.getInt("hatch_top"));
        bottom = new Solenoid(Config.getInt("hatch_bottom"));
    }

    public void setTop(boolean on) {
        top.set(on);
    }

    public void setBottom(boolean on) {
        bottom.set(on);
    }
}