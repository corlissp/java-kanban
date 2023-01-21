package tasks;

import enums.Status;
import enums.Type;

public abstract class Task {
    private final int id;
    private final String name;
    private final String description;
    protected Status status;

    public Task(int id, String name, Status status, String description) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.description = description;
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
}
