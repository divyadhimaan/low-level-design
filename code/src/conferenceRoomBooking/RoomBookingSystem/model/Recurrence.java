package RoomBookingSystem.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Recurrence {
    private final UUID recurrenceId;
    private final int numberOfWeeks;
    private final int startSlot;
    private final int durationInMinutes;
    private final int dayOfWeek;
    private final FrequencyType frequencyType;

    // Private constructor - use builder instead
    private Recurrence(Builder builder) {
        this.recurrenceId = UUID.randomUUID();
        this.numberOfWeeks = builder.numberOfWeeks;
        this.startSlot = builder.startSlot;
        this.durationInMinutes = builder.durationInMinutes;
        this.dayOfWeek = builder.dayOfWeek;
        this.frequencyType = builder.frequencyType;
    }

    // Legacy constructor for backward compatibility
    public Recurrence(int numberOfWeeks, int startSlot, int durationInMinutes, int dayOfWeek, String frequency) {
        this.recurrenceId = UUID.randomUUID();
        this.numberOfWeeks = numberOfWeeks;
        this.startSlot = startSlot;
        this.durationInMinutes = durationInMinutes;
        this.dayOfWeek = dayOfWeek;
        this.frequencyType = FrequencyType.valueOf(frequency);
    }

    // ====== BUILDER PATTERN ======
    public static class Builder {
        // Required fields
        private final int numberOfWeeks;
        private final int startSlot;
        private final int durationInMinutes;
        private final int dayOfWeek;

        // Optional fields with defaults
        private FrequencyType frequencyType = FrequencyType.WEEKLY;

        /**
         * Creates a builder for recurrence with required parameters
         * @param numberOfWeeks Number of weeks to repeat (1-52)
         * @param startSlot Starting time slot (1-10)
         * @param durationInMinutes Duration in minutes (1-600)
         * @param dayOfWeek Day of week (1=Monday, 6=Saturday)
         */
        public Builder(int numberOfWeeks, int startSlot, int durationInMinutes, int dayOfWeek) {
            this.numberOfWeeks = numberOfWeeks;
            this.startSlot = startSlot;
            this.durationInMinutes = durationInMinutes;
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
            if (startSlot < 1 || startSlot > 10) {
                throw new IllegalArgumentException("Start slot must be between 1 and 10, got: " + startSlot);
            }
            if (durationInMinutes <= 0 || durationInMinutes > 600) {
                throw new IllegalArgumentException("Duration must be between 1 and 600 minutes, got: " + durationInMinutes);
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
                ", startSlot=" + startSlot +
                ", durationInMinutes=" + durationInMinutes +
                ", dayOfWeek=" + dayOfWeek +
                ", frequencyType=" + frequencyType +
                '}';
    }
}
