package dev.lebenkov.meets.api.util.validation.impl;

import dev.lebenkov.meets.api.util.validation.ValidDateRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    private String startField;
    private String endField;

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        this.startField = constraintAnnotation.startField();
        this.endField = constraintAnnotation.endField();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Field startDateField = o.getClass().getDeclaredField(startField);
            Field endDateField = o.getClass().getDeclaredField(endField);
            startDateField.setAccessible(true);
            endDateField.setAccessible(true);

            Object startDate = startDateField.get(o);
            Object endDate = endDateField.get(o);

            if (startDate == null || endDate == null) {
                return true; // Пропускаем валидацию, если одно из полей null
            }

            if (startDate instanceof LocalDateTime && endDate instanceof LocalDateTime) {
                return ((LocalDateTime) startDate).isBefore((LocalDateTime) endDate);
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
