package manager.task;

import enums.Status;
import manager.history.InMemoryHistoryManager;
import tasks.EpicTask;
import tasks.SingleTask;
import tasks.SubTask;
import tasks.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final TaskIdGenerator taskIdGenerator;
    protected static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");
    protected static HashMap<Integer, SingleTask> singleTasks;
    protected static HashMap<Integer, SubTask> subTasks;
    protected static HashMap<Integer, EpicTask> epicTasks;
    protected static InMemoryHistoryManager historyManager;

    public InMemoryTaskManager(InMemoryHistoryManager historyManager) {
        this.taskIdGenerator = new TaskIdGenerator();
        singleTasks = new HashMap<>();
        subTasks = new HashMap<>();
        epicTasks = new HashMap<>();
        InMemoryTaskManager.historyManager = historyManager;
    }

    @Override
    public boolean isTimeFree(String time) {
        LocalDateTime dateTime = LocalDateTime.parse(time, DATE_TIME_FORMATTER);
        List<Task> list = getAllTasks();
        for (Task task: list) {
            if (dateTime.isAfter(task.getStartTime()) && dateTime.isBefore(task.getEndTime()))
                return false;
        }
        return true;
    }

    @Override
    public void saveNewSingleTask(String name, String description, String startTime, int duration) {
        int freeId = taskIdGenerator.getNextId();
        if (isTimeFree(startTime)) {
            SingleTask singleTask = new SingleTask(freeId, name, Status.NEW, description, duration, startTime);
            singleTasks.put(singleTask.getId(), singleTask);
        } else {
            System.out.println("На это время уже есть задача!");
        }
    }

    @Override
    public void saveNewEpicTask(String name, String description, String startTime) {
        int duration = 0;
        int freeId = taskIdGenerator.getNextId();
        if (isTimeFree(startTime)) {
            ArrayList<Integer> subTasks = new ArrayList<>();
            EpicTask epicTask = new EpicTask(freeId, name, subTasks, Status.NEW, description, duration, startTime);
            epicTasks.put(epicTask.getId(), epicTask);
        } else {
            System.out.println("На это время уже есть задача!");
        }
    }

    @Override
    public void saveNewSubTask(String name, String description, int epicId, String startTime, int duration) {
        int freeId = taskIdGenerator.getNextId();
        if (isTimeFree(startTime)) {
            if (epicTasks.get(epicId) != null) {
                SubTask subTask = new SubTask(freeId, name, Status.NEW, epicId, description, duration, startTime);
                subTasks.put(subTask.getId(), subTask);
                EpicTask epicTask = epicTasks.get(epicId);
                epicTask.addSubTask(freeId);
                epicTask.setDuration(epicTask.getDuration() + subTask.getDuration());
            }
        } else {
            System.out.println("На это время уже есть задача!");
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
        }
        return list;
    }

    @Override
    public EpicTask getEpicFromSub(SubTask subTask) {
        return epicTasks.get(subTask.getEpicTaskId());
    }

    @Override
    public void deleteSingleById(int id) {
        if (singleTasks.get(id) != null) {
            singleTasks.remove(id);
            historyManager.remove(id);
        }
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
            historyManager.remove(id);
            updateEpicStatus(epic);
        }
    }

    @Override
    public void deleteEpicById(int id) {
        if (epicTasks.get(id) != null) {
            EpicTask epic = epicTasks.get(id);
            ArrayList<Integer> subsId = epic.getSubTasks();
            for (Integer subId : subsId) {
                subTasks.remove(subId);
                historyManager.remove(subId);
            }
            epicTasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void clearAll() {
        subTasks.clear();
        singleTasks.clear();
        epicTasks.clear();
        taskIdGenerator.setNextId(1);
        // historyManager.clear();
    }

    @Override
    public void clearSubTasks() {
        subTasks.clear();
        for (EpicTask epic : epicTasks.values()) {
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
        tasks.addAll(singleTasks.values());
        tasks.addAll(epicTasks.values());
        tasks.addAll(subTasks.values());
        return tasks;
    }

    @Override
    public ArrayList<SingleTask> getAllSingleTasks() {
        return new ArrayList<>(singleTasks.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public ArrayList<EpicTask> getAllEpicTasks() {
        return new ArrayList<>(epicTasks.values());
    }

    @Override
    public SingleTask getSingleTaskById(int id) {
        if (singleTasks.containsKey(id)) {
            historyManager.add(singleTasks.get(id));
            return singleTasks.get(id);
        }
        return null;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        if (subTasks.containsKey(id)) {
            historyManager.add(subTasks.get(id));
            return subTasks.get(id);
        }
        return null;
    }

    @Override
    public EpicTask getEpicTaskById(int id) {
        if (epicTasks.containsKey(id)) {
            historyManager.add(epicTasks.get(id));
            return epicTasks.get(id);
        }
        return null;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        List<Task> list = getAllTasks();
        Set<Task> set = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        set.addAll(list);
        return set;
    }

    public static final class TaskIdGenerator {
        private int nextId = 1;

        public int getNextId() {
            int flag = 0;
            while (flag == 0) {
                if (singleTasks.containsKey(nextId) || epicTasks.containsKey(nextId) || subTasks.containsKey(nextId)) {
                    nextId++;
                } else {
                    flag = 1;
                }
            }
            return nextId;
        }

        public void setNextId(int id) {
            nextId = id;
        }
    }
}
