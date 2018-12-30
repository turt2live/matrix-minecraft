package io.t2l.mc.matrix;

import java.util.logging.Logger;

public class LogService {

    private static Logger logger;

    private LogService() {
    }

    public static void setLogger(Logger logger) {
        LogService.logger = logger;
    }

    public static Logger get() {
        return logger;
    }
}
