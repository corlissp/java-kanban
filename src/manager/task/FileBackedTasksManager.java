package manager.task;

import enums.Status;
import manager.history.HistoryManager;
import tasks.EpicTask;
import tasks.SingleTask;
import tasks.SubTask;
import tasks.Task;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static enums.Status.*;
import static java.lang.Integer.parseInt;

public class FileBackedTasksManager extends InMemoryTaskManager {
    public FileBackedTasksManager(HistoryManager historyManager) {
        super(historyManager);
    }

    private static final FileBackedTasksManager fileManager = new FileBackedTasksManager(historyManager);

    public void save() {
        try {
            HashMap<Integer, Task> tasks = new HashMap<>();
            Path path = Paths.get("resources");
            Path absPath = path.toAbsolutePath();
            String separator = File.separator;
            File file = new File(absPath + separator + "TaskManager.scv");
            Writer fileWriter = new FileWriter(file);
            for (SingleTask singleTask : singleTasks.values()) {
                tasks.put(singleTask.getId(), singleTask);
            }
            for (EpicTask epicTask : epicTasks.values()) {
                tasks.put(epicTask.getId(), epicTask);
            }
            for (SubTask subTask : subTasks.values()) {
                tasks.put(subTask.getId(), subTask);
            }
            for (Task task : tasks.values()) {
                fileWriter.write(task.toString());
            }
            fileWriter.write("\n");
            fileWriter.write(historyToString());
            fileWriter.close();
        } catch (IOException ex) {
            System.out.println("Запись не выполнена!");
        }
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        try {
            Path path = Paths.get("resources");
            Path absPath = path.toAbsolutePath();
            String separator = File.separator;
            File file = new File(absPath + separator + "TaskManager.scv");
            Writer fileWriter = new FileWriter(file);
            for (Task task: super.getPrioritizedTasks()) {
                fileWriter.write(task.toString());
            }
            fileWriter.write("\n");
            fileWriter.write(historyToString());
            fileWriter.close();
        } catch (IOException ex) {
            System.out.println("Запись не выполнена!");
        }
        return super.getPrioritizedTasks();
    }

    private void saveNewSingleTask(String name, String description, int id, String startTime, int duration) {
        if (super.isTimeFree(startTime)) {
            SingleTask singleTask = new SingleTask(id, name, Status.NEW, description, duration, startTime);
            singleTasks.put(singleTask.getId(), singleTask);
            save();
            taskIdGenerator.setNextId(id + 1);
        } else {
            System.out.println("На это время уже есть задача!");
        }
    }

    @Override
    public void saveNewSingleTask(String name, String description, String startTime, int duration) {
        super.saveNewSingleTask(name, description, startTime, duration);
        save();
    }

    private void saveNewEpicTask(String name, String description, int id, String startTime) {
        if (super.isTimeFree(startTime)) {
            ArrayList<Integer> subTasks = new ArrayList<>();
            EpicTask epicTask = new EpicTask(id, name, subTasks, Status.NEW, description, 0, startTime);
            epicTasks.put(epicTask.getId(), epicTask);
            save();
            taskIdGenerator.setNextId(id + 1);
        } else {
            System.out.println("На это время уже есть задача!");
        }
    }

    @Override
    public void saveNewEpicTask(String name, String description, String startTime) {
        super.saveNewEpicTask(name, description, startTime);
        save();
    }

    private void saveNewSubTask(String name, String description, int epicId, int id, String startTime, int duration) {
        if (super.isTimeFree(startTime)) {
            if (epicTasks.get(epicId) != null) {
                SubTask subTask = new SubTask(id, name, Status.NEW, epicId, description, duration, startTime);
                subTasks.put(subTask.getId(), subTask);
                EpicTask epicTask = epicTasks.get(epicId);
                epicTask.addSubTask(id);
            }
            save();
            taskIdGenerator.setNextId(id + 1);
        } else {
            System.out.println("На это время уже есть задача!");
        }
    }

    @Override
    public void saveNewSubTask(String name, String description, int epicId, String startTime, int duration) {
        super.saveNewSubTask(name, description, epicId, startTime, duration);
        save();
    }

    @Override
    public void updateSingleStatus(SingleTask singleTask, Status newStatus) {
        super.updateSingleStatus(singleTask, newStatus);
        save();
    }

    @Override
    public void updateSubStatus(SubTask subTask, Status newStatus) {
        super.updateSubStatus(subTask, newStatus);
        save();
    }

    @Override
    public void updateEpicStatus(EpicTask epicTask) {
        super.updateEpicStatus(epicTask);
        save();
    }

    @Override
    public void deleteSingleById(int id) {
        super.deleteSingleById(id);
        save();
    }

    @Override
    public void deleteSubById(int id) {
        super.deleteSubById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void clearAll() {
        super.clearAll();
        save();
    }

    @Override
    public void clearSubTasks() {
        super.clearSubTasks();
        save();
    }

    @Override
    public void clearEpicTasks() {
        super.clearEpicTasks();
        save();
    }

    @Override
    public void clearSingleTasks() {
        super.clearSingleTasks();
        save();
    }

    public void openFile(String fileName) {
        Path path = Paths.get("resources");
        Path absPath = path.toAbsolutePath();
        String separator = File.separator;
        File file = new File(absPath + separator + fileName);
        loadFromFile(file);
    }

    @Override
    public void addTask(SingleTask task) {
        super.addTask(task);
    }

    @Override
    public void addSubTask(SubTask task) {
        super.addSubTask(task);
    }

    @Override
    public void addEpicTask(EpicTask task) {
        super.addEpicTask(task);
    }

    private static void loadFromFile(File file) {
        try {
            Scanner scanner = new Scanner(file);
            String line = scanner.nextLine();
            while (line != null) {
                if (line.isEmpty()) {
                    line = scanner.nextLine();
                    List<Integer> historyIdList = historyFromString(line);
                    saveHistoryFromString(historyIdList);
                    break;
                } else {
                    saveTaskFromString(line);
                    line = scanner.nextLine();
                }
            }
            scanner.close();
        } catch (IOException ex) {
            System.out.println("Не удалось считать файл: " + file);
        }
    }

    private static void saveHistoryFromString(List<Integer> list) {
        for (Integer id : list) {
            if (epicTasks.containsKey(id)) {
                historyManager.add(epicTasks.get(id));
            } else if (subTasks.containsKey(id)) {
                historyManager.add(subTasks.get(id));
            } else if (singleTasks.containsKey(id)) {
                historyManager.add(singleTasks.get(id));
            }
        }
        fileManager.save();
    }

    private static void saveTaskFromString(String value) {
        String[] split = value.split(",");
        int id = parseInt(split[0]);
        Status status = statusFromString(split[3]);
        switch (split[1]) {
            case "SINGLE":
                int duration = parseInt(split[6]);
                fileManager.saveNewSingleTask(split[2], split[4], id, split[5], duration);
                SingleTask singleTask = singleTasks.get(id);
                if (singleTask != null) {
                    singleTask.setStatus(status);
                    fileManager.save();
                }
                break;
            case "EPIC":
                fileManager.saveNewEpicTask(split[2], split[4], id, split[5]);
                EpicTask epic = epicTasks.get(id);
                if (epic != null) {
                    epic.setStatus(status);
                    fileManager.save();
                }
                break;
            case "SUB":
                int epicId = parseInt(split[5]);
                duration = parseInt(split[7]);
                fileManager.saveNewSubTask(split[2], split[4], epicId, id, split[6], duration);
                SubTask sub = subTasks.get(id);
                if (sub != null) {
                    sub.setStatus(status);
                    fileManager.save();
                }
                break;
        }
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> listId = new ArrayList<>();
        String[] split = value.split(",");
        for (String s : split) {
            listId.add(Integer.parseInt(s));
        }
        return listId;
    }

    private String historyToString() {
        String history;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < getHistory().size(); i++) {
            if (i == getHistory().size() - 1) {
                builder.append(getHistory().get(i).getId());
            } else {
                builder.append(getHistory().get(i).getId()).append(",");
            }
        }
        history = builder.toString();
        return history;
    }

    private static Status statusFromString(String line) {
        Status status = NEW;
        switch (line) {
            case "NEW":
                break;
            case "DONE":
                status = DONE;
                break;
            case "IN_PROGRESS":
                status = IN_PROGRESS;
                break;
            default:
        }
        return status;
    }
}
