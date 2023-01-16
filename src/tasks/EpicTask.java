package tasks;

import enums.Status;
import enums.Type;

import java.util.ArrayList;

public class EpicTask extends Task {
    private ArrayList<SubTask> subTasks;

    public EpicTask(int id, String name, ArrayList<SubTask> subTasks, Status status, String description) {
        super(id, name, status, description);
        this.subTasks = subTasks;
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

    public int getIndexInSubTasksById(int id) {
        int i;
        for (i = 0; i < subTasks.size(); i++) {
            if (subTasks.get(i).getId() == id)
                break;
        }
        return i;
    }

    public SubTask getSubTask(int i) {
        return subTasks.get(i);
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setStatusSubTask(int i, Status newStatus) {
        subTasks.get(i).setStatus(newStatus);
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
        return "tasks.EpicTask{" +
                "id = " + getId() + "," +
                "status = " + getStatus() + "," +
                "subtasks = " + subTasks +
                "}";
    }

    public static class Creator {
        private String name;
        private String description;

        public Creator(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public String getName() {
            return name;
        }
    }
}
