package it.os.event.handler.exception;

public class AdminRequiredException extends RuntimeException {

    public AdminRequiredException(String msg, Exception e) {
        super(msg, e);
    }

    public AdminRequiredException(Exception e) {
        super(null, e);
    }

    public AdminRequiredException(String msg) {
        super(msg, null);
    }

}
