package it.os.event.handler.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String msg, Exception e) {
        super(msg, e);
    }

    public UserNotFoundException(String msg) {
        super(msg, null);
    }

    public UserNotFoundException(Exception e) {
        super(null, e);
    }

}
