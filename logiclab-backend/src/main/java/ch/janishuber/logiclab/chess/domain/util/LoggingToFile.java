package org.example.chess.backend.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggingToFile {

    public static Logger getLogger(String name) {
        Logger logger = Logger.getLogger(name);
        try {
            File logsDir = new File("logs");
            if (!logsDir.exists()) {
                System.out.println("Creating logs directory");
                logsDir.mkdirs();
            }
            FileHandler fileHandler = new FileHandler("logs/chessbot.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logger;
    }
}