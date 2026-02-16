package loggingSystem.model;

public enum LogLevel {
    DEBUG(1), INFO(2), WARN(3), ERROR(4);

    private final int level;

    LogLevel(int level){
        this.level = level;
    }

    public boolean isGreaterOrEqual(LogLevel other){
        return this.level >= other.level;
    }

}
