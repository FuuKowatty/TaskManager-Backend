package pl.bartoszmech.infrastructure.auth.error;

public record AuthErrorResponseBody(

        String type,
        String message

) {}
