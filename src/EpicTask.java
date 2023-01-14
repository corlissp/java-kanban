import java.util.ArrayList;

public class EpicTask extends Task {
    private ArrayList<SubTask> subTasks;
    private Status status;

    public EpicTask(int id, String name, ArrayList<SubTask> subTasks, Status status) {
        super(id, name);
        this.subTasks = subTasks;
        this.status = status;
    }

    public void removeSubTask(int i) {
        subTasks.remove(i);
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
    }
    public int getSizeSubTasks() {
        return subTasks.size();
    }

    public int getIdSubTask(int i) {
        return subTasks.get(i).getId();
    }

    public void setStatusSubTask(int i, Status newStatus) {
        subTasks.get(i).setStatus(newStatus);
    }
    public EpicTask updateStatus() {
        Status status = Status.NEW;
        int counterDone = 0;
        for (SubTask subTask: subTasks) {
            if (subTask.getStatus() == Status.IN_PROGRESS) {
                status = subTask.getStatus();
                break;
            } else if (subTask.getStatus() == Status.DONE) {
                counterDone++;
            }
        }
        if (counterDone == subTasks.size() && subTasks.size() != 0) {
            status = Status.DONE;
        } else if (counterDone > 0 && counterDone < subTasks.size()) {
            status = Status.IN_PROGRESS;
        }
        return new EpicTask(this.getId(), this.getName(), this.subTasks, status);
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public Type getType() {
        return Type.EPIC;
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "id = " + getId() + "," +
                "status = " + getStatus() + "," +
                "subtasks = " + subTasks +
                "}";
    }

    public static class Creator {
        private String name;

        public Creator(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
