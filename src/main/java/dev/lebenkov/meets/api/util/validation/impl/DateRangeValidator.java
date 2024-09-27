package dev.lebenkov.meets.api.util.validation.impl;

import dev.lebenkov.meets.api.util.validation.ValidDateRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

@Slf4j
public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    private String startField;
    private String endField;

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        this.startField = constraintAnnotation.startField();
        this.endField = constraintAnnotation.endField();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {
        try {
            Field startDateField = o.getClass().getDeclaredField(startField);
            Field endDateField = o.getClass().getDeclaredField(endField);
            startDateField.setAccessible(true);
            endDateField.setAccessible(true);

            Object startDate = startDateField.get(o);
            Object endDate = endDateField.get(o);

            if (startDate == null || endDate == null) {
                return true;
            }

            if (startDate instanceof LocalDateTime && endDate instanceof LocalDateTime) {
                boolean isValid = ((LocalDateTime) startDate).isBefore((LocalDateTime) endDate);

                if (!isValid) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                            .addPropertyNode(endField)
                            .addConstraintViolation();
                }

                return isValid;
            }

            return false;
        } catch (Exception e) {
            log.error("Error with date range in DateRangeValidator", e);
            return false;
        }
    }
}