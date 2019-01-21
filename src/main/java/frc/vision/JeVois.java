package frc.vision;

import edu.wpi.first.wpilibj.SerialPort;

public class JeVois extends Thread {

    private boolean enabled = false;

    private SerialPort camera;
    public String lastString;
    public volatile JeVoisMessage lastMessage = null;
    public volatile long lastReceivedTimestamp;

    public JeVois() {
        this(SerialPort.Port.kUSB);
    }

    public JeVois(SerialPort.Port port) { // port should be kUSB, kUSB1, or kUSB2
        this.camera = new SerialPort(115200, port);
    }

    @Override
    public void run() {
        while (true) {
            if (this.enabled) {
                try {
                    this.lastString = camera.readString().trim();
                    System.out.println(lastString);
                    if (!lastString.equals("")) {
                        this.lastMessage = new JeVoisMessage(lastString, System.currentTimeMillis());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public JeVoisMessage getLastMessage() {
        return lastMessage;
    }

    public long getLastReceivedTimestamp() {
        return lastReceivedTimestamp;
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

}