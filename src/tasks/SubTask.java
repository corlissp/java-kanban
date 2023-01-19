package tasks;

import enums.Status;
import enums.Type;

public class SubTask extends Task {
    private final int epicTaskId;

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
        return "tasks.SubTask{" +
                "id = " + getId() +
                ", status = " + getStatus() +
                ", name = '" + getName() + '\'' +
                ", description = '" + getDescription() + '\'' +
                "}";
    }
}
