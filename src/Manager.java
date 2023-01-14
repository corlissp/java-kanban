import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private TaskIdGenerator taskIdGenerator;
    private HashMap<Integer, Task> tasks;

    public Manager() {
        this.taskIdGenerator = new TaskIdGenerator();
        this.tasks = new HashMap<>();
    }

    public void saveNewSingleTask(SingleTask.Creator singleTaskCreator) {
        int freeId = taskIdGenerator.getNextId();
        SingleTask singleTask = new SingleTask(freeId, singleTaskCreator.getName(), Status.NEW);
        tasks.put(singleTask.getId(), singleTask);
    }

    public void saveNewEpicTask(EpicTask.Creator epicTaskCreator) {
        int freeId = taskIdGenerator.getNextId();
        ArrayList<SubTask> subTasks = new ArrayList<>();
        EpicTask epicTask = new EpicTask(freeId, epicTaskCreator.getName(), subTasks, Status.NEW);
        tasks.put(epicTask.getId(), epicTask);
    }
    public void saveNewSubTask(SubTask.Creator subTaskCreator) {
        int freeId = taskIdGenerator.getNextId();
        SubTask subTask = new SubTask(freeId, subTaskCreator.getName(), Status.NEW, subTaskCreator.getEpicTask());
        tasks.put(subTask.getId(), subTask);
        EpicTask epicTask = subTaskCreator.getEpicTask();
        epicTask.addSubTask(subTask);
    }

    public void deleteById(int id) {
        if (tasks.get(id).getType() == Type.EPIC) {
            EpicTask taskToRemove = (EpicTask) tasks.get(id);
            for (int i = 0; i < taskToRemove.getSizeSubTasks(); i++) {
                int idSubTask = taskToRemove.getIdSubTask(i);
                tasks.remove(idSubTask);
            }
        } else if (tasks.get(id).getType() == Type.SUB) {
            SubTask taskToRemove = (SubTask) tasks.get(id);
            for (int i = 0; i < taskToRemove.getEpicTask().getSizeSubTasks(); i++) {
                if (taskToRemove.getEpicTask().getIdSubTask(i) == id) {
                    taskToRemove.getEpicTask().removeSubTask(i);
                }
            }
            update(taskToRemove.getEpicTask().updateStatus());
        }
        tasks.remove(id);
    }

    public void clearAll() {
        tasks.clear();
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Task task : this.tasks.values()) {
            tasks.add(task);
        }
        return tasks;
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }
    public void update(Task task) {
        tasks.put(task.getId(), task);
    }

    public static final class TaskIdGenerator {
        private int nextId = 1;
        public int getNextId(){
            return nextId++;
        }
    }
}
