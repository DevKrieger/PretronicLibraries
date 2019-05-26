package net.prematic.libraries.utility.io;

import java.io.IOException;

public class IORuntimeException extends RuntimeException{

    public IORuntimeException(String message) {
        super(message);
    }

    public IORuntimeException(IOException cause) {
        super(cause.getMessage(),cause);
    }

    public IORuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
