package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> historyTasks;
    public InMemoryHistoryManager() {
        historyTasks = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        if (this.historyTasks.size() < 10) {
            this.historyTasks.add(task);
        } else {
            this.historyTasks.remove(0);
            this.historyTasks.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return this.historyTasks;
    }
}
