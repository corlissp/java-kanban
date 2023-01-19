package tasks;

import enums.Status;
import enums.Type;

public class SingleTask extends Task {
    public SingleTask(int id, String name, Status status, String description) {
        super(id, name, status, description);
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
    public String toString() {
        return "tasks.SingleTask{" +
                "id = " + getId() +
                ", name = '" + getName() + '\'' +
                ", description = '" + getDescription() + '\'' +
                ", status = '" + getStatus() + '\'' +
                "}";
    }

}
