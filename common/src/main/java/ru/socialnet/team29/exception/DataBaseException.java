package ru.socialnet.team29.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataBaseException extends Exception
{

    private Exception exception;
    private String classException;
    private String message;

    public DataBaseException(String classException, String message) {
        this.classException = classException;
        this.message = message;
    }
}
