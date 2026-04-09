package bo.com.bg.app.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractController {

    protected Logger logger() {
        return LogManager.getLogger(getClass());
    }
}
