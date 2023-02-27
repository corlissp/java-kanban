package manager.history;

import tasks.Task;
import java.util.HashMap;
import java.util.List;
import manager.history.LinkedList.Node;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList historyTasks;
    private final HashMap<Integer, Node> map;

    public InMemoryHistoryManager() {
        historyTasks = new LinkedList();
        map = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        if (map.containsKey(task.getId())) {
            historyTasks.removeNode(map.get(task.getId()));
        }
        Node node = historyTasks.LinkLast(task);
        map.put(task.getId(), node);
    }

    @Override
    public void remove(int id) {
        if (map.containsKey(id)) {
            historyTasks.removeNode(map.get(id));
            map.remove(id);
        }
    }
    @Override
    public List<Task> getHistory() {
        return this.historyTasks.getTasks();
    }
}
