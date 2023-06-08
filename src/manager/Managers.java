package manager;

import manager.history.InMemoryHistoryManager;
import manager.task.FileBackedTasksManager;
import manager.task.InMemoryTaskManager;
import tasks.Task;

import java.util.List;

public abstract class Managers {
    private static InMemoryHistoryManager HISTORY_MANAGER;

    public static FileBackedTasksManager fileManager() {
        return new FileBackedTasksManager(historyManager());
    }

    public static InMemoryTaskManager taskManager() {
        return new InMemoryTaskManager(historyManager());
    }

    public static InMemoryHistoryManager historyManager() {
        if (HISTORY_MANAGER == null) {
            HISTORY_MANAGER = new InMemoryHistoryManager();
        }
        return new InMemoryHistoryManager();
    }

    public static List<Task> getDefaultHistory() {
        return HISTORY_MANAGER.getHistory();
    }
}
