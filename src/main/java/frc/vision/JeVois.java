package frc.vision;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class JeVois extends Thread {

    private boolean enabled = true;

    private String name;
    private BufferedReader camera;
    public String lastString;
    public volatile JeVoisMessage lastMessage = null;
    public volatile long lastReceivedTimestamp;

    public JeVois(String device) {
        this.name = device;
        try {
            camera = new BufferedReader(new FileReader(device));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if (camera == null)
            return;
        while (true) {
            if (this.enabled) {
                try {
                    String line = camera.readLine();
                    if (line.split(" ").length < 8) {
                        continue;
                    }
                    this.lastString = line;
                    if (!lastString.equals("")) {
                        System.out.println(name + ": " + line);
                        lastReceivedTimestamp = System.currentTimeMillis();
                        this.lastMessage = new JeVoisMessage(lastString, lastReceivedTimestamp);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(10);
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