package manager;

import manager.history.InMemoryHistoryManager;
import manager.task.InMemoryTaskManager;
import tasks.Task;

import java.util.List;

public abstract class Managers {
    private static InMemoryTaskManager TASK_MANAGER;
    private static InMemoryHistoryManager HISTORY_MANAGER;

    public static InMemoryTaskManager taskManager() {
        if (TASK_MANAGER == null) {
            TASK_MANAGER = new InMemoryTaskManager(historyManager());
        }
        return TASK_MANAGER;
    }

    public static InMemoryHistoryManager historyManager() {
        if (HISTORY_MANAGER == null) {
            HISTORY_MANAGER = new InMemoryHistoryManager();
        }
        return HISTORY_MANAGER;
    }

    public static List<Task> getDefaultHistory() {
        return HISTORY_MANAGER.getHistory();
    }
}
