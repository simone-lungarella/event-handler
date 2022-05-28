package it.os.event.handler.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String msg, Exception e) {
        super(msg, e);
    }

    public BusinessException(Exception e) {
        super(null, e);
    }

}
