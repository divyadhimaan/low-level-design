package RoomBookingSystem.model;

import lombok.Getter;

import java.time.LocalTime;
import java.util.UUID;


@Getter
public class Recurrence {
    private final UUID recurrenceId;
    private final int numberOfWeeks;
    private final LocalTime start;
    private final LocalTime end;
//    private final int durationInMinutes;
    private final int dayOfWeek;
    private final FrequencyType frequencyType;

    // Private constructor - use builder instead
    private Recurrence(Builder builder) {
        this.recurrenceId = UUID.randomUUID();
        this.numberOfWeeks = builder.numberOfWeeks;
        this.start = builder.start;
        this.end = builder.end;
//        this.durationInMinutes = builder.durationInMinutes;
        this.dayOfWeek = builder.dayOfWeek;
        this.frequencyType = builder.frequencyType;
    }

    // Legacy constructor for backward compatibility
    public Recurrence(int numberOfWeeks, LocalTime start, LocalTime end, int dayOfWeek, String frequency) {
        this.recurrenceId = UUID.randomUUID();
        this.numberOfWeeks = numberOfWeeks;
        this.start = start;
        this.end = end;
        this.dayOfWeek = dayOfWeek;
        this.frequencyType = FrequencyType.valueOf(frequency);
    }

    // ====== BUILDER PATTERN ======
    public static class Builder {
        private static final LocalTime BUSINESS_START = LocalTime.of(9, 0);
        private static final LocalTime BUSINESS_END   = LocalTime.of(19, 0);
        // Required fields
        private final int numberOfWeeks;
        private final LocalTime start;
        private final LocalTime end;
//        private final int durationInMinutes;
        private final int dayOfWeek;

        // Optional fields with defaults
        private FrequencyType frequencyType = FrequencyType.WEEKLY;

        /**
         * Creates a builder for recurrence with required parameters
         * @param numberOfWeeks Number of weeks to repeat (1-52)
         * @param start Starting time
         * @param end Ending time
         * @param dayOfWeek Day of week (1=Monday, 6=Saturday)
         */
        public Builder(int numberOfWeeks, LocalTime start, LocalTime end, int dayOfWeek) {
            this.numberOfWeeks = numberOfWeeks;
            this.start = start;
            this.end = end;
            this.dayOfWeek = dayOfWeek;
        }

        /**
         * Set the frequency type (DAILY, WEEKLY, BIWEEKLY, MONTHLY)
         * Default: WEEKLY
         */
        public Builder withFrequency(FrequencyType frequencyType) {
            if (frequencyType == null) {
                throw new IllegalArgumentException("Frequency type cannot be null");
            }
            this.frequencyType = frequencyType;
            return this;
        }

        /**
         * Set the frequency type using string
         */
        public Builder withFrequency(String frequency) {
            this.frequencyType = FrequencyType.valueOf(frequency.toUpperCase());
            return this;
        }

        /**
         * Build the Recurrence object
         */
        public Recurrence build() {
            validateFields();
            return new Recurrence(this);
        }

        /**
         * Validate all parameters
         */
        private void validateFields() {
            if (numberOfWeeks <= 0 || numberOfWeeks > 52) {
                throw new IllegalArgumentException("Number of weeks must be between 1 and 52, got: " + numberOfWeeks);
            }
            if (start == null || end == null) {
                throw new IllegalArgumentException("Start and end times must not be null.");
            }
            if (!start.isBefore(end)) {
                throw new IllegalArgumentException("Invalid time range: start (" + start + ") must be before end (" + end + ").");
            }
            if (start.isBefore(BUSINESS_START)) {
                throw new IllegalArgumentException("Invalid start time: " + start + " is before business hours (" + BUSINESS_START + ").");
            }
            if (end.isAfter(BUSINESS_END)) {
                throw new IllegalArgumentException("Invalid end time: " + end + " exceeds business hours (" + BUSINESS_END + ").");
            }
            if (dayOfWeek < 1 || dayOfWeek > 6) {
                throw new IllegalArgumentException("Day of week must be between 1 and 6, got: " + dayOfWeek);
            }
            if (frequencyType == null) {
                throw new IllegalArgumentException("Frequency type cannot be null");
            }
        }
    }

    @Override
    public String toString() {
        return "Recurrence{" +
                "numberOfWeeks=" + numberOfWeeks +
                ", startTime=" + start +
                ", endTime=" + end +
                ", dayOfWeek=" + dayOfWeek +
                ", frequencyType=" + frequencyType +
                '}';
    }
}
