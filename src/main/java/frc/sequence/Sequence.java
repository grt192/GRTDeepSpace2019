/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.sequence;

import edu.wpi.first.wpilibj.Notifier;

/**
 * Add your docs here.
 */
public abstract class Sequence implements Runnable {

    public static RumbleSeguence RUMBLE_SEQUENCE;

    public static void initSequneces() {
        RUMBLE_SEQUENCE = new RumbleSeguence();
    }

    private Notifier notifier;

    public Sequence() {
        notifier = new Notifier(this);
    }

    public final void start() {
        notifier.startSingle(0);
    }

    public final Notifier getNotifier() {
        return notifier;
    }
}
