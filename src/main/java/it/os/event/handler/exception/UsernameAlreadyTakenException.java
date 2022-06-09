package it.os.event.handler.exception;

public class UsernameAlreadyTakenException extends RuntimeException {

    public UsernameAlreadyTakenException(String msg, Exception e) {
        super(msg, e);
    }

    public UsernameAlreadyTakenException(String msg) {
        super(msg, null);
    }

    public UsernameAlreadyTakenException(Exception e) {
        super(null, e);
    }

}
