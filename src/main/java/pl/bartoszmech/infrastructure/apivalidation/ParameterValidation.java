package pl.bartoszmech.infrastructure.apivalidation;

public class ParameterValidation {

    public static void validateLastMonths(int value) throws IllegalArgumentException {
        if (value <= 0) {
            throw new InvalidLastMonthsParameterException("Invalid value: " + value + ". Value must be a positive integer.");
        }
    }

}
