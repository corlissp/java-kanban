package manager;

import enums.Status;
import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private final TaskIdGenerator taskIdGenerator;
    private final HashMap<Integer, SingleTask> singleTasks;
    private final HashMap<Integer, SubTask> subTasks;
    private final HashMap<Integer, EpicTask> epicTasks;
    private final InMemoryHistoryManager historyManager;

    public InMemoryTaskManager(InMemoryHistoryManager historyManager) {
        this.taskIdGenerator = new TaskIdGenerator();
        this.singleTasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epicTasks = new HashMap<>();
        this.historyManager = historyManager;
    }

    @Override
    public void saveNewSingleTask(String name, String description) {
        int freeId = taskIdGenerator.getNextId();
        SingleTask singleTask = new SingleTask(freeId, name, Status.NEW, description);
        singleTasks.put(singleTask.getId(), singleTask);
    }

    @Override
    public void saveNewEpicTask(String name, String description) {
        int freeId = taskIdGenerator.getNextId();
        ArrayList<Integer> subTasks = new ArrayList<>();
        EpicTask epicTask = new EpicTask(freeId, name, subTasks, Status.NEW, description);
        epicTasks.put(epicTask.getId(), epicTask);
    }

    @Override
    public void saveNewSubTask(String name, String description, int epicId) {
        int freeId = taskIdGenerator.getNextId();
        if (epicTasks.get(epicId) != null) {
            SubTask subTask = new SubTask(freeId, name, Status.NEW, epicId, description);
            subTasks.put(subTask.getId(), subTask);
            EpicTask epicTask = epicTasks.get(epicId);
            epicTask.addSubTask(freeId);
        }
    }

    @Override
    public void updateSingleStatus(SingleTask singleTask, Status newStatus) {
        singleTask.setStatus(newStatus);
    }

    @Override
    public void updateSubStatus(SubTask subTask, Status newStatus) {
        subTask.setStatus(newStatus);
        EpicTask epic = epicTasks.get(subTask.getEpicTaskId());
        updateEpicStatus(epic);
    }

    @Override
    public void updateEpicStatus(EpicTask epicTask) {
        Status status = Status.NEW;
        int counterDone = 0;
        for (int i = 0; i < epicTask.getSizeSubTasks(); i++) {
            int idSub = epicTask.getSubTaskId(i);
            if (subTasks.get(idSub).getStatus() == Status.IN_PROGRESS) {
                status = subTasks.get(idSub).getStatus();
                break;
            } else if (subTasks.get(idSub).getStatus() == Status.DONE) {
                counterDone++;
            }
        }
        if (counterDone == epicTask.getSizeSubTasks() && epicTask.getSizeSubTasks() != 0) {
            status = Status.DONE;
        } else if (counterDone > 0 && counterDone < epicTask.getSizeSubTasks()) {
            status = Status.IN_PROGRESS;
        }
        epicTask.setStatus(status);
    }

    @Override
    public ArrayList<SubTask> getSubFromEpic(EpicTask epicTask) {
        ArrayList<SubTask> list = new ArrayList<>();
        for (int i = 0; i < epicTask.getSizeSubTasks(); i++) {
            SubTask subTask = subTasks.get(epicTask.getSubTaskId(i));
            list.add(subTask);
            historyManager.add(subTask);
        }
        return list;
    }

    @Override
    public EpicTask getEpicFromSub(SubTask subTask) {
        EpicTask epicTask = epicTasks.get(subTask.getEpicTaskId());
        historyManager.add(epicTask);
        return epicTask;
    }

    @Override
    public void deleteSingleById(int id) {
        if (singleTasks.get(id) != null)
            singleTasks.remove(id);
    }

    @Override
    public void deleteSubById(int id) {
        if (subTasks.get(id) != null) {
            int epicId = subTasks.get(id).getEpicTaskId();
            EpicTask epic = epicTasks.get(epicId);
            for (int i = 0; i < epic.getSizeSubTasks(); i++) {
                if (id == epic.getSubTaskId(i)) {
                    epic.removeSubTask(i);
                    break;
                }
            }
            subTasks.remove(id);
            updateEpicStatus(epic);
        }
    }

    @Override
    public void deleteEpicById(int id) {
        if (epicTasks.get(id) != null) {
            EpicTask epic = epicTasks.get(id);
            ArrayList<Integer> subsId = epic.getSubTasks();
            for (int i = 0; i < subsId.size(); i++) {
                int subId = subsId.get(i);
                subTasks.remove(subId);
            }
            epicTasks.remove(id);
        }
    }

    @Override
    public void clearAll() {
        subTasks.clear();
        singleTasks.clear();
        epicTasks.clear();
    }

    @Override
    public void clearSubTasks() {
        subTasks.clear();
        for (EpicTask epic: epicTasks.values()) {
            epic.clearSubtasks();
            updateEpicStatus(epic);
        }
    }

    @Override
    public void clearEpicTasks() {
        subTasks.clear();
        epicTasks.clear();
    }

    @Override
    public void clearSingleTasks() {
        singleTasks.clear();
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Task task : singleTasks.values()) {
            tasks.add(task);
            historyManager.add(task);

        }
        for (Task task : epicTasks.values()) {
            tasks.add(task);
            historyManager.add(task);
        }
        for (Task task : subTasks.values()) {
            tasks.add(task);
            historyManager.add(task);
        }
        return tasks;
    }
    @Override
    public ArrayList<SingleTask> getAllSingleTasks() {
        ArrayList<SingleTask> tasks = new ArrayList<>();
        for (SingleTask task : singleTasks.values()) {
            tasks.add(task);
            historyManager.add(task);
        }
        return tasks;
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        ArrayList<SubTask> tasks = new ArrayList<>();
        for (SubTask task : subTasks.values()) {
            tasks.add(task);
            historyManager.add(task);
        }
        return tasks;
    }

    @Override
    public ArrayList<EpicTask> getAllEpicTasks() {
        ArrayList<EpicTask> tasks = new ArrayList<>();
        for (EpicTask task : epicTasks.values()) {
            tasks.add(task);
            historyManager.add(task);
        }
        return tasks;
    }

    @Override
    public SingleTask getSingleTaskById(int id) {
        historyManager.add(singleTasks.get(id));
        return singleTasks.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public EpicTask getEpicTaskById(int id) {
        historyManager.add(epicTasks.get(id));
        return epicTasks.get(id);
    }

    public static final class TaskIdGenerator {
        private int nextId = 1;
        public int getNextId(){
            return nextId++;
        }
    }
}
