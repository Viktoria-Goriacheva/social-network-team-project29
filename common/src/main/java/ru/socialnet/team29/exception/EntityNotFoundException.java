package ru.socialnet.team29.exception;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String message) {
        super("Entity with " + message + " not found.");
    }
}
