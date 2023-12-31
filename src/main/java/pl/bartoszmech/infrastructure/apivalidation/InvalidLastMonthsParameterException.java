package pl.bartoszmech.infrastructure.apivalidation;

import java.security.InvalidParameterException;

public class InvalidLastMonthsParameterException extends InvalidParameterException {
    InvalidLastMonthsParameterException(String message) {
        super(message);
    }
}
