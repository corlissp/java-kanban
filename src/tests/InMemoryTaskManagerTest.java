package tests;

import manager.Managers;
import manager.task.InMemoryTaskManager;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    InMemoryTaskManagerTest() {
        super(Managers.taskManager());
    }

    @Order(1)
    @Test
    void getHistory() {
        super.getHistory();
    }

    @Test
    void getSingleTaskById() {
        super.getSingleTaskById();
    }

    @Test
    void saveNewSingleTask() {
        super.saveNewSingleTask();
    }

    @Test
    void saveNewSubTask() {
        super.saveNewSubTask();
    }

    @Test
    void saveNewEpicTask() {
        super.saveNewEpicTask();
    }

    @Test
    void updateSingleStatus() {
        super.updateSingleStatus();
    }

    @Test
    void updateSubStatus() {
        super.updateSubStatus();
    }

    @Test
    void updateEpicStatus() {
        super.updateEpicStatus();
    }

    @Test
    void getSubTaskById() {
        super.getSubTaskById();
    }

    @Test
    void getEpicTaskById() {
        super.getEpicTaskById();
    }

    @Test
    void getAllTasks() {
        super.getAllTasks();
    }

    @Test
    void getAllSingleTasks() {
        super.getAllSingleTasks();
    }

    @Test
    void getAllSubTasks() {
        super.getAllSubTasks();
    }

    @Test
    void getAllEpicTasks() {
        super.getAllEpicTasks();
    }

    @Test
    void getSubFromEpic() {
        super.getSubFromEpic();
    }

    @Test
    void getEpicFromSub() {
        super.getEpicFromSub();
    }

    @Test
    void deleteSingleById() {
        super.deleteSingleById();
    }

    @Test
    void deleteSubById() {
        super.deleteSubById();
    }

    @Test
    void deleteEpicById() {
        super.deleteEpicById();
    }

    @Test
    void clearSubTasks() {
        super.clearSubTasks();
    }

    @Test
    void clearSingleTasks() {
        super.clearSingleTasks();
    }

    @Test
    void clearEpicTasks() {
        super.clearEpicTasks();
    }

    @Test
    void clearAll() {
        super.clearAll();
    }

}