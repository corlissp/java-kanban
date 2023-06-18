package manager;

import http.HttpTaskManager;
import http.KVServer;
import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManager;
import manager.task.FileBackedTasksManager;
import manager.task.InMemoryTaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public abstract class Managers {

    public static FileBackedTasksManager fileManager() {
        return new FileBackedTasksManager(historyManager());
    }

    public static InMemoryTaskManager taskManager() {
        return new InMemoryTaskManager(historyManager());
    }

    public static HistoryManager historyManager() {
        return new InMemoryHistoryManager();
    }

    public static HttpTaskManager getDefault(HistoryManager historyManager) throws IOException, InterruptedException {
        return new HttpTaskManager(historyManager, "http://localhost:" + KVServer.PORT);
    }


    public static List<Task> getDefaultHistory() {
        return historyManager().getHistory();
    }
}
