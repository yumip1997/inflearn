package com.example.demo.exception;

public class MyDBDuplicateKeyException extends MyDBException{

    public MyDBDuplicateKeyException() {
    }

    public MyDBDuplicateKeyException(String message) {
        super(message);
    }

    public MyDBDuplicateKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyDBDuplicateKeyException(Throwable cause) {
        super(cause);
    }

    public MyDBDuplicateKeyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
