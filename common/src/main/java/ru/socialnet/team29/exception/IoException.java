package ru.socialnet.team29.exception;

import java.io.IOException;

public class IoException extends IOException {
    public IoException(String message) {
        super("Input/Output file with " + message + " not found.");
    }

    public IoException(IOException e) {

    }
}
