package tasks;

import enums.Status;
import enums.Type;

import java.time.LocalDateTime;

public class SingleTask extends Task {
    public SingleTask(int id, String name, Status status, String description, int durationMinutes, String startTime) {
        super(id, name, status, description, durationMinutes, startTime);
    }

    public SingleTask(int id, String name, Status status, String description) {
        super(id, name, status, description);
    }

    public SingleTask(String name, Status status, String description, int durationMinutes, String startTime) {
        super(name, status, description, durationMinutes, startTime);
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public Type getType() {
        return Type.SINGLE;
    }

    @Override
    public LocalDateTime getStartTime() {
        return super.getStartTime();
    }

    public LocalDateTime getEndTime() {
        return super.getEndTime();
    }

    @Override
    public String toString() {
        return getId() + "," + getType() + "," + getName() + "," + getStatus() + ","
                + getDescription() + "," + getStartTime().format(DATE_TIME_FORMATTER) + "," +
                getEndTime().format(DATE_TIME_FORMATTER) + "\n";
    }

}
