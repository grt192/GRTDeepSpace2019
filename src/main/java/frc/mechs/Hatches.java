package frc.mechs;

import edu.wpi.first.wpilibj.Solenoid;
import frc.config.Config;

/*
* Code for hatches mech which move in and out to pick up and place the hatches 
*/
public class Hatches {

    private Solenoid hatch1;
    private Solenoid hatch2;

    public Hatches() {
        this.hatch1 = new Solenoid(Config.getInt("hatch1"));
        this.hatch2 = new Solenoid(Config.getInt("hatch2"));
    }

    /*
     * Moves mech in
     */
    public void setIn() {
        hatch1.set(false);
        hatch2.set(false);
    }

    /*
     * Moves mech out
     */
    public void setOut() {
        hatch1.set(true);
        hatch2.set(true);
    }
}