package me.drawlin.ember;

import java.util.logging.Level;

public class Logger {

    public static void log(Level level, String log) {
        System.out.println("[" + level.getName() + "] " + log);
    }

}
