package tasks;

import enums.Status;
import enums.Type;

public class SubTask extends Task {
    private final EpicTask epicTask;

    public SubTask(int id, String name, Status status, EpicTask epicTask, String description) {
        super(id, name, status, description);
        this.epicTask = epicTask;
    }

    public EpicTask getEpicTask() {
        return epicTask;
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

    public static class Creator {
        private String name;
        private EpicTask epicTask;
        private String description;

        public Creator(String name, String description, EpicTask epicTask) {
            this.name = name;
            this.epicTask = epicTask;
            this.description = description;
        }

        public EpicTask getEpicTask() {
            return epicTask;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }
}
