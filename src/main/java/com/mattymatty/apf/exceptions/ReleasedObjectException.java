package com.mattymatty.apf.exceptions;

public class ReleasedObjectException extends RuntimeException {
    public ReleasedObjectException() {
    }

    public ReleasedObjectException(String message) {
        super(message);
    }

    public ReleasedObjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReleasedObjectException(Throwable cause) {
        super(cause);
    }

    public ReleasedObjectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
