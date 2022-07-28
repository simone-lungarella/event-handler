package it.os.event.handler.exception;

public class EventNotFoundException extends RuntimeException {

    public EventNotFoundException(String msg, Exception e) {
        super(msg, e);
    }

    public EventNotFoundException(Exception e) {
        super(null, e);
    }

    public EventNotFoundException(String msg) {
        super(msg, null);
    }

}
