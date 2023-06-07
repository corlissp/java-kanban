package tasks;

import enums.Status;
import enums.Type;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Task implements Comparable<LocalDateTime>{
    protected static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");
    private final int id;
    private final String name;
    protected Duration duration;
    private final LocalDateTime startTime;
    private final String description;
    protected Status status;

    public Task(int id, String name, Status status, String description, int durationMinutes, String startTime) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.description = description;
        this.duration = Duration.ofMinutes(durationMinutes);
        this.startTime = LocalDateTime.parse(startTime, DATE_TIME_FORMATTER);
    }

    public Task(int id, String name, Status status, String description) {
        this.id = id;
        this.name = name;
        this.duration = null;
        this.status = status;
        this.startTime = null;
        this.description = description;
    }

    public long getDuration() {
        return duration.toMinutes();
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        if (getStartTime() != null)
            return startTime.plus(duration);
        return null;
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public abstract Status getStatus();

    public abstract void setStatus(Status status);

    public abstract Type getType();

    @Override
    public int compareTo(LocalDateTime o) {
        return this.startTime.compareTo(o);
    }
}
