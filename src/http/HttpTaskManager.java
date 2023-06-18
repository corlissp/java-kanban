package http;

import com.google.gson.*;
import manager.history.HistoryManager;
import manager.task.FileBackedTasksManager;
import tasks.EpicTask;
import tasks.SingleTask;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {

    final static String KEY_TASKS = "task";
    final static String KEY_SUBTASKS = "subtasks";
    final static String KEY_EPICS = "epics";
    final static String KEY_HISTORY = "history";
    final KVTaskClient client;
    private static final Gson gson = new GsonBuilder().create();

    public HttpTaskManager(HistoryManager historyManager, String path) throws IOException, InterruptedException {
        super(historyManager);
        client = new KVTaskClient(path);

        JsonElement jsonTasks = JsonParser.parseString(client.load(KEY_TASKS));
        if (!jsonTasks.isJsonNull()) {
            JsonArray jsonTasksArray = jsonTasks.getAsJsonArray();
            for (JsonElement jsonTask : jsonTasksArray) {
                SingleTask task = gson.fromJson(jsonTask, SingleTask.class);
                this.addTask(task);
            }
        }

        JsonElement jsonEpics = JsonParser.parseString(client.load(KEY_EPICS));
        if (!jsonEpics.isJsonNull()) {
            JsonArray jsonEpicsArray = jsonEpics.getAsJsonArray();
            for (JsonElement jsonEpic : jsonEpicsArray) {
                EpicTask task = gson.fromJson(jsonEpic, EpicTask.class);
                this.addEpicTask(task);
            }
        }

        JsonElement jsonSubtasks = JsonParser.parseString(client.load(KEY_SUBTASKS));
        if (!jsonSubtasks.isJsonNull()) {
            JsonArray jsonSubtasksArray = jsonSubtasks.getAsJsonArray();
            for (JsonElement jsonSubtask : jsonSubtasksArray) {
                SubTask task = gson.fromJson(jsonSubtask, SubTask.class);
                this.addSubTask(task);
            }
        }

        JsonElement jsonHistoryList = JsonParser.parseString(client.load(KEY_HISTORY));
        if (!jsonHistoryList.isJsonNull()) {
            JsonArray jsonHistoryArray = jsonHistoryList.getAsJsonArray();
            for (JsonElement jsonTaskId : jsonHistoryArray) {
                int taskId = jsonTaskId.getAsInt();
                if (this.subTasks.containsKey(taskId)) {
                    this.getSubTaskById(taskId);
                } else if (this.epicTasks.containsKey(taskId)) {
                    this.getEpicTaskById(taskId);
                } else if (this.singleTasks.containsKey(taskId)) {
                    this.getSingleTaskById(taskId);
                }
            }
        }
    }

    @Override
    public void save() {
        client.put(KEY_TASKS, gson.toJson(singleTasks.values()));
        client.put(KEY_SUBTASKS, gson.toJson(subTasks.values()));
        client.put(KEY_EPICS, gson.toJson(epicTasks.values()));
        client.put(KEY_HISTORY, gson.toJson(this.getHistory()
                .stream()
                .map(Task::getId)
                .collect(Collectors.toList())));
    }
}
