package dev.lebenkov.meets.api.util.exc;

public class MaxParticipantsReachedException extends RuntimeException {
    public MaxParticipantsReachedException(Long eventId) {
        super("Max participants reached for event with id " + eventId);
    }
}

