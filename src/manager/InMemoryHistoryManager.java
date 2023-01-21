package manager;

import tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int HISTORY_LENGTH = 10;
    private final List<Task> historyTasks;

    public InMemoryHistoryManager() {
        historyTasks = new LinkedList<>();
    }

    @Override
    public void add(Task task) {
        if (this.historyTasks.size() >= HISTORY_LENGTH) {
            this.historyTasks.remove(0);
        }
        this.historyTasks.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return this.historyTasks;
    }
}
