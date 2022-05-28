package it.os.event.handler.exception;

public class StepNotFoundException extends RuntimeException {

    public StepNotFoundException(String msg, Exception e) {
        super(msg, e);
    }

    public StepNotFoundException(Exception e) {
        super(null, e);
    }

    public StepNotFoundException(String msg) {
        super(msg, null);
    }

}
