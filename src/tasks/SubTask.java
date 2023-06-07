package tasks;

import enums.Status;
import enums.Type;

public class SubTask extends Task {
    private final int epicTaskId;

    public SubTask(int id, String name, Status status, int epicTask, String description, int durationMinutes, String startTime) {
        super(id, name, status, description, durationMinutes, startTime);
        this.epicTaskId = epicTask;
    }

    public SubTask(int id, String name, Status status, int epicTask, String description) {
        super(id, name, status, description);
        this.epicTaskId = epicTask;
    }

    public int getEpicTaskId() {
        return epicTaskId;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public Type getType() {
        return Type.SUB;
    }

    @Override
    public String toString() {
        return getId() + "," + getType() + "," + getName() + "," + getStatus() + ","
                + getDescription() + "," + getEpicTaskId() + ","
                + getStartTime().format(DATE_TIME_FORMATTER) + "," +
                getEndTime().format(DATE_TIME_FORMATTER) + "\n";
    }
}
