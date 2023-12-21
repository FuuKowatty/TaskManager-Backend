package pl.bartoszmech.infrastructure.apivalidation;

import java.util.List;

public record ValidationResponse(List<String> messages) {
}
