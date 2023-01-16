package manager;

import enums.Status;
import enums.Type;
import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private final TaskIdGenerator taskIdGenerator;
    private final HashMap<Integer, Task> tasks;  // Для храниния всех данных из 3-х таблиц
    private final HashMap<Integer, SingleTask> singleTasks;
    private final HashMap<Integer, SubTask> subTasks;
    private final HashMap<Integer, EpicTask> epicTasks;

    public Manager() {
        this.taskIdGenerator = new TaskIdGenerator();
        this.singleTasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epicTasks = new HashMap<>();
        this.tasks = new HashMap<>();
    }

    public void saveNewSingleTask(SingleTask.Creator singleTaskCreator) {
        int freeId = taskIdGenerator.getNextId();
        SingleTask singleTask = new SingleTask(freeId, singleTaskCreator.getName(), Status.NEW, singleTaskCreator.getDescription());
        singleTasks.put(singleTask.getId(), singleTask);
        tasks.put(singleTask.getId(),singleTask);
    }

    public void saveNewEpicTask(EpicTask.Creator epicTaskCreator) {
        int freeId = taskIdGenerator.getNextId();
        ArrayList<SubTask> subTasks = new ArrayList<>();
        EpicTask epicTask = new EpicTask(freeId, epicTaskCreator.getName(), subTasks, Status.NEW, epicTaskCreator.getDescription());
        epicTasks.put(epicTask.getId(), epicTask);
        tasks.put(epicTask.getId(), epicTask);
    }

    public void saveNewSubTask(SubTask.Creator subTaskCreator) {
        int freeId = taskIdGenerator.getNextId();
        SubTask subTask = new SubTask(freeId, subTaskCreator.getName(), Status.NEW, subTaskCreator.getEpicTask(), subTaskCreator.getDescription());
        subTasks.put(subTask.getId(), subTask);
        tasks.put(subTask.getId(), subTask);
        if (subTaskCreator.getEpicTask() != null) {
            EpicTask epicTask = subTaskCreator.getEpicTask();
            epicTask.addSubTask(subTask);
        }
    }

    public void updateSingleStatus(SingleTask singleTask, Status status) {
        SingleTask newSingleTask = new SingleTask(singleTask.getId(), singleTask.getName(), status, singleTask.getDescription());
        singleTasks.put(newSingleTask.getId(), newSingleTask);
        tasks.put(newSingleTask.getId(), newSingleTask);
    }

    public void updateSubStatus(SubTask subTask, Status newStatus) {
        SubTask newSubTask = new SubTask(subTask.getId(), subTask.getName(), newStatus, subTask.getEpicTask(), subTask.getDescription());
        for (int i = 0; i < subTask.getEpicTask().getSizeSubTasks(); i++) {
            int key = subTask.getEpicTask().getIdSubTask(i);
            if (key == newSubTask.getId()) {
                subTask.getEpicTask().setStatusSubTask(i, newStatus);
            }
        }
        subTasks.put(newSubTask.getId(), newSubTask);
        tasks.put(newSubTask.getId(), newSubTask);
        updateEpicStatus(newSubTask.getEpicTask());
    }

    private void updateEpicStatus(EpicTask epicTask) {
        Status status = Status.NEW;
        int counterDone = 0;
        for (int i = 0; i < epicTask.getSizeSubTasks(); i++) {
            if (epicTask.getSubTask(i).getStatus() == Status.IN_PROGRESS) {
                status = epicTask.getSubTask(i).getStatus();
                break;
            } else if (epicTask.getSubTask(i).getStatus() == Status.DONE) {
                counterDone++;
            }
        }
        if (counterDone == epicTask.getSizeSubTasks() && epicTask.getSizeSubTasks() != 0) {
            status = Status.DONE;
        } else if (counterDone > 0 && counterDone < epicTask.getSizeSubTasks()) {
            status = Status.IN_PROGRESS;
        }
        EpicTask newEpicTask = new EpicTask(epicTask.getId(), epicTask.getName(), epicTask.getSubTasks(), status, epicTask.getDescription());
        epicTasks.put(newEpicTask.getId(), newEpicTask);
        tasks.put(newEpicTask.getId(), newEpicTask);
    }

    public ArrayList<SubTask> getSubFromEpic(EpicTask epicTask) {
        return epicTask.getSubTasks();
    }

    public EpicTask getEpicFromSub(SubTask subTask) {
        return subTask.getEpicTask();
    }

    public void deleteByIdAndUpdate(int id) {
        if (tasks.get(id).getType() == Type.EPIC) {
            EpicTask taskToRemove = (EpicTask) tasks.get(id);
            for (int i = 0; i < taskToRemove.getSizeSubTasks(); i++) {
                int idSubTask = taskToRemove.getIdSubTask(i);
                tasks.remove(idSubTask);
                subTasks.remove(idSubTask);
            }
            epicTasks.remove(id);
        } else if (tasks.get(id).getType() == Type.SUB) {
            SubTask taskToRemove = (SubTask) tasks.get(id);
            for (int i = 0; i < taskToRemove.getEpicTask().getSizeSubTasks(); i++) {
                if (taskToRemove.getEpicTask().getIdSubTask(i) == id) {
                    taskToRemove.getEpicTask().removeSubTask(i);
                }
            }
            subTasks.remove(id);
            updateEpicStatus(taskToRemove.getEpicTask());
        } else {
            singleTasks.remove(id);
        }
        tasks.remove(id);
    }

    public void clearAll() {
        tasks.clear();
        subTasks.clear();
        singleTasks.clear();
        epicTasks.clear();
    }

    public void clearSubTasks() {
        for(Task task: tasks.values()) {
            if (task.getType() == Type.SUB) {
                tasks.remove(task.getId());
                SubTask subTask = getSubTaskById(task.getId());
                int index = subTask.getEpicTask().getIndexInSubTasksById(task.getId());
                subTask.getEpicTask().getSubTasks().remove(index);
            }
        }
        subTasks.clear();
    }

    public void clearEpicTasks() {
        for(Task task: tasks.values()) {
            if (task.getType() == Type.SUB || task.getType() == Type.EPIC)
                tasks.remove(task.getId());
        }
        subTasks.clear();
        epicTasks.clear();
    }

    public void clearSingleTasks() {
        for(Task task: tasks.values()) {
            if (task.getType() == Type.SINGLE)
                tasks.remove(task.getId());
        }
        singleTasks.clear();
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Task task : this.tasks.values()) {
            tasks.add(task);
        }
        return tasks;
    }

    public ArrayList<SingleTask> getAllSingleTasks() {
        ArrayList<SingleTask> tasks = new ArrayList<>();
        for (SingleTask task : singleTasks.values()) {
            tasks.add(task);
        }
        return tasks;
    }

    public ArrayList<SubTask> getAllSubTasks() {
        ArrayList<SubTask> tasks = new ArrayList<>();
        for (SubTask task : subTasks.values()) {
            tasks.add(task);
        }
        return tasks;
    }

    public ArrayList<EpicTask> getAllEpicTasks() {
        ArrayList<EpicTask> tasks = new ArrayList<>();
        for (EpicTask task : epicTasks.values()) {
            tasks.add(task);
        }
        return tasks;
    }

    public SingleTask getSingleTaskById(int id) {
        return singleTasks.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    public EpicTask getEpicTaskById(int id) {
        return epicTasks.get(id);
    }

    public static final class TaskIdGenerator {
        private int nextId = 1;
        public int getNextId(){
            return nextId++;
        }
    }
}
