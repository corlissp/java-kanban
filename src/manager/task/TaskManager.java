package manager.task;

import enums.Status;
import tasks.EpicTask;
import tasks.SingleTask;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface TaskManager {
    void saveNewSingleTask(String name, String description, String startTime, int duration);
    void saveNewSubTask(String name, String description, int epicId, String startTime, int duration);
    void saveNewEpicTask(String name, String description, String startTime);
    void updateSingleStatus(SingleTask singleTask, Status newStatus);
    void updateSubStatus(SubTask subTask, Status newStatus);
    void updateEpicStatus(EpicTask epicTask);
    SingleTask getSingleTaskById(int id);
    SubTask getSubTaskById(int id);
    EpicTask getEpicTaskById(int id);
    ArrayList<Task> getAllTasks();
    ArrayList<SingleTask> getAllSingleTasks();
    ArrayList<SubTask> getAllSubTasks();
    ArrayList<EpicTask> getAllEpicTasks();
    ArrayList<SubTask> getSubFromEpic(EpicTask epicTask);
    EpicTask getEpicFromSub(SubTask subTask);
    void deleteSingleById(int id);
    void deleteSubById(int id);
    void deleteEpicById(int id);
    void clearSubTasks();
    void clearSingleTasks();
    void clearEpicTasks();
    void clearAll();
    List<Task> getHistory();
    boolean isTimeFree(String time);
    Set<Task> getPrioritizedTasks();
    void addTask(SingleTask task);
    void addSubTask(SubTask task);
    void addEpicTask(EpicTask task);
}
