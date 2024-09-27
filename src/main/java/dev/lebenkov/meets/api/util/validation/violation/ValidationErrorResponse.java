package dev.lebenkov.meets.api.util.validation.violation;

import java.util.List;

public record ValidationErrorResponse(List<Violation> violations) {
}
