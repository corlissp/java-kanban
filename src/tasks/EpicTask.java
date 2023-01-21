package tasks;

import enums.Status;
import enums.Type;

import java.util.ArrayList;

public class EpicTask extends Task {
    private final ArrayList<Integer> subTasks;

    public EpicTask(int id, String name, ArrayList<Integer> subTasks, Status status, String description) {
        super(id, name, status, description);
        this.subTasks = subTasks;
    }

    public void removeSubTask(int i) {
        subTasks.remove(i);
    }

    public void addSubTask(int id) {
        subTasks.add(id);
    }

    public int getSizeSubTasks() {
        return subTasks.size();
    }

    public Integer getSubTaskId(int i) {
        return subTasks.get(i);
    }

    public ArrayList<Integer> getSubTasks() {
        return subTasks;
    }

    public void clearSubtasks() {
        this.subTasks.clear();
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
        return Type.EPIC;
    }

    @Override
    public String toString() {
        return "tasks.EpicTask{" +
                "id = " + getId() + ", " +
                "status = " + getStatus() + ", " +
                "subtasksId = " + subTasks +
                "}";
    }
}
