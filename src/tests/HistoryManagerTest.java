package tests;

import enums.Status;
import manager.history.InMemoryHistoryManager;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import tasks.SingleTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HistoryManagerTest {

    protected InMemoryHistoryManager manager;

    public HistoryManagerTest() {
        this.manager = new InMemoryHistoryManager();
    }

    @Order(1)
    @Test
    void add() {
        SingleTask singleTask = new SingleTask(1, "Task 1", Status.NEW, "Task id 1");
        manager.add(singleTask);
        final List<Task> history = manager.getHistory();
        List<Task> list = new ArrayList<>();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
        list.add(singleTask) ;
        assertEquals(list, manager.getHistory());
    }

    @Order(2)
    @Test
    void remove() {
        SingleTask singleTask = new SingleTask(1, "Task 1", Status.NEW, "Task id 1");
        List<Task> list = new ArrayList<>();
        manager.add(singleTask);
        list.add(singleTask);
        singleTask = new SingleTask(2, "Task 2", Status.NEW, "Task id 2");
        manager.add(singleTask);
        list.add(singleTask);
        assertEquals(list, manager.getHistory());
        assertEquals(2, manager.getHistory().size());
        manager.remove(1);
        assertEquals(1, manager.getHistory().size());
        assertEquals(singleTask, manager.getHistory().get(0));
    }

    @Order(3)
    @Test
    void getHistory() {
        SingleTask singleTask = new SingleTask(1, "Task 1", Status.NEW, "Task id 1");
        List<Task> list = new ArrayList<>();
        manager.add(singleTask);
        list.add(singleTask);
        singleTask = new SingleTask(2, "Task 2", Status.NEW, "Task id 2");
        manager.add(singleTask);
        list.add(singleTask);
        assertEquals(list, manager.getHistory());
        assertEquals(list.size(), manager.getHistory().size());
    }
}