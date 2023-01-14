public class SubTask extends Task {
    private Status status;
    private final EpicTask epicTask;

    public SubTask(int id, String name, Status status, EpicTask epicTask) {
        super(id, name);
        this.status = status;
        this.epicTask = epicTask;
    }

    public SubTask updateStatus(Status newStatus) {
        SubTask subTask = new SubTask(this.getId(), this.getName(), newStatus, this.epicTask);
        for (int i = 0; i < this.epicTask.getSizeSubTasks(); i++) {
            int key = this.epicTask.getIdSubTask(i);
            if (key == subTask.getId()) {
                this.epicTask.setStatusSubTask(i, newStatus);
            }
        }
        return subTask;
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
        return "SubTask{" +
                "id = " + getId() +
                ", status = " + getStatus() +
                ", name = '" + getName() + '\'' +
                "}";
    }

    public static class Creator {
        private String name;
        private  EpicTask epicTask;

        public Creator(String name, EpicTask epicTask) {
            this.name = name;
            this.epicTask = epicTask;
        }

        public EpicTask getEpicTask() {
            return epicTask;
        }

        public String getName() {
            return name;
        }
    }
}
