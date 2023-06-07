package manager.task.test;

import enums.Status;
import manager.task.TaskManager;
import org.junit.jupiter.api.*;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
abstract class TaskManagerTest<T extends TaskManager> {
    private final T manager;
    TaskManagerTest(T manager) {
        this.manager = manager;
    }

    @BeforeEach
    public void beforeEach() {
        manager.clearAll();
        manager.saveNewEpicTask("Epic(1)", "Empty", "13:00 10.01.24"); // id 1
        manager.saveNewEpicTask("Epic(2)", "All new", "13:00 11.01.24"); // id 2
        manager.saveNewSubTask("Sub 1 by Epic(2)", "New status", 2, "13:10 11.01.24", 180);  // id 3
        manager.saveNewSubTask("Sub 2 by Epic(2)", "New status", 2, "13:00 12.01.24", 180);  // id 4
        manager.saveNewEpicTask("Epic(3)", "All done", "13:00 13.01.24"); // id 5
        manager.saveNewSubTask("Sub 1 by Epic(3)", "Done status", 5, "13:10 13.01.24", 180);  // id 6
        manager.saveNewSubTask("Sub 2 by Epic(3)", "Done status", 5, "13:00 14.01.24", 180);  // id 7
        manager.saveNewEpicTask("Epic(4)", "All done", "13:00 15.01.24"); // id 8
        manager.saveNewSubTask("Sub 1 by Epic(4)", "New status", 8, "13:10 15.01.24", 180);  // id 9
        manager.saveNewSubTask("Sub 2 by Epic(4)", "Done status", 8, "13:00 16.01.24", 180);  // id 10
        manager.saveNewEpicTask("Epic(5)", "All done", "13:00 17.01.24"); // id 11
        manager.saveNewSubTask("Sub 1 by Epic(5)", "New status", 11, "13:10 17.01.24", 180);  // id 12
        manager.saveNewSubTask("Sub 2 by Epic(5)", "In progress status", 11, "13:00 18.01.24", 180);  // id 13
        manager.saveNewSingleTask("Single", "Single id 14", "13:00 19.01.24", 180); // id 14
    }

    @Order(1)
    @Test
    void getHistory() {
        List<Task> list = new ArrayList<>();
        list.add(manager.getSingleTaskById(14));
        list.add(manager.getSubTaskById(12));
        list.add(manager.getEpicTaskById(2));
        Assertions.assertEquals(manager.getHistory().size(), list.size());
        Assertions.assertEquals(manager.getHistory(), list);
        list.add(manager.getSingleTaskById(14));
        list.remove(0);
        Assertions.assertEquals(manager.getHistory(), list);
    }

    @Test
    void saveNewSingleTask() {
        manager.saveNewSingleTask("Single", "Single id 15", "13:00 20.01.24", 180); // id 15
        Assertions.assertEquals(manager.getSingleTaskById(15).getName(), "Single");
        Assertions.assertEquals(manager.getSingleTaskById(15).getStatus(), Status.NEW);
        Assertions.assertEquals(manager.getSingleTaskById(15).getDescription(), "Single id 15");
        Assertions.assertEquals(manager.getSingleTaskById(15).getId(), 15);
        manager.deleteSingleById(15);
        manager.clearAll();
    }

    @Test
    void saveNewSubTask() {
        manager.saveNewSubTask("Sub test", "Epic id 11", 11, "13:00 20.01.24", 180); // id 15
        Assertions.assertEquals(manager.getSubTaskById(15).getName(), "Sub test");
        Assertions.assertEquals(manager.getSubTaskById(15).getEpicTaskId(), 11);
        Assertions.assertEquals(manager.getSubTaskById(15).getDescription(), "Epic id 11");
        Assertions.assertEquals(manager.getSubTaskById(15).getStatus(), Status.NEW);
        manager.saveNewSubTask("Sub test 2", "No id Epic", 0, "13:00 21.01.24", 180); // id 16
        Assertions.assertNull(manager.getSubTaskById(16), "Неверно сохраняет с несуществующем Epic");
        manager.deleteSubById(15);
        manager.clearAll();
    }

    @Test
    void saveNewEpicTask() {
        manager.saveNewEpicTask("Epic test", "id 15, no sub", "13:00 20.01.24"); // id 15
        Assertions.assertEquals(manager.getEpicTaskById(15).getName(), "Epic test");
        Assertions.assertEquals(manager.getEpicTaskById(15).getDescription(), "id 15, no sub");
        Assertions.assertEquals(manager.getEpicTaskById(15).getStatus(), Status.NEW);
        Assertions.assertEquals(manager.getEpicTaskById(15).getId(), 15);
        Assertions.assertTrue(manager.getEpicTaskById(15).getSubTasks().isEmpty());
        manager.deleteEpicById(15);
        manager.clearAll();
    }

    @Test
    void updateSingleStatus() {
        manager.updateSingleStatus(manager.getSingleTaskById(14), Status.DONE);
        Assertions.assertEquals(manager.getSingleTaskById(14).getStatus(), Status.DONE);
        manager.clearAll();
    }

    @Test
    void updateSubStatus() {
        manager.updateSubStatus(manager.getSubTaskById(6), Status.DONE);
        Assertions.assertEquals(manager.getSubTaskById(6).getStatus(), Status.DONE);
        manager.clearAll();
    }

    @Test
    void updateEpicStatus() {
        Assertions.assertEquals(manager.getEpicTaskById(5).getStatus(), Status.NEW);
        manager.getSubTaskById(6).setStatus(Status.DONE);
        manager.updateEpicStatus(manager.getEpicTaskById(5));
        Assertions.assertEquals(manager.getEpicTaskById(5).getStatus(), Status.IN_PROGRESS);
        manager.getSubTaskById(7).setStatus(Status.DONE);
        manager.updateEpicStatus(manager.getEpicTaskById(5));
        Assertions.assertEquals(manager.getEpicTaskById(5).getStatus(), Status.DONE);
        manager.clearAll();
    }
    @Test
    void getSingleTaskById() {
        Assertions.assertEquals(manager.getSingleTaskById(14).getName(), "Single");
        Assertions.assertEquals(manager.getSingleTaskById(14).getStatus(), Status.NEW);
        Assertions.assertEquals(manager.getSingleTaskById(14).getDescription(), "Single id 14");
        Assertions.assertEquals(manager.getSingleTaskById(14).getId(), 14);
        Assertions.assertNull(manager.getSingleTaskById(0), "Неверно возвращается несуществующий SingleTask");
        manager.clearAll();
    }

    @Test
    void getSubTaskById() {
        Assertions.assertNull(manager.getSubTaskById(0), "Неверно возвращается несуществующий SubTask");
        Assertions.assertEquals(manager.getSubTaskById(3).getEpicTaskId(), 2);
        Assertions.assertEquals(manager.getSubTaskById(3).getName(), "Sub 1 by Epic(2)");
        Assertions.assertEquals(manager.getSubTaskById(3).getDescription(), "New status");
        Assertions.assertEquals(manager.getSubTaskById(3).getStatus(), Status.NEW);
        manager.deleteSubById(3);
        manager.clearAll();
    }

    @Test
    void getEpicTaskById() {
        Assertions.assertNull(manager.getEpicTaskById(0), "Неверно возвращается несуществующий EpicTask");
        Assertions.assertEquals(manager.getEpicTaskById(1).getName(), "Epic(1)");
        Assertions.assertEquals(manager.getEpicTaskById(1).getDescription(), "Empty");
        Assertions.assertEquals(manager.getEpicTaskById(1).getId(), 1);
        Assertions.assertEquals(manager.getEpicTaskById(1).getStatus(), Status.NEW);
        manager.deleteEpicById(1);
        manager.clearAll();
    }

    @Test
    void getAllTasks() {
        Assertions.assertEquals(manager.getAllTasks().size(), 14);
        manager.clearAll();
    }

    @Test
    void getAllSingleTasks() {
        Assertions.assertEquals(manager.getAllSingleTasks().size(), 1);
        manager.clearAll();
    }

    @Test
    void getAllSubTasks() {
        Assertions.assertEquals(manager.getAllSubTasks().size(), 8);
        manager.clearAll();
    }

    @Test
    void getAllEpicTasks() {
        Assertions.assertEquals(manager.getAllEpicTasks().size(), 5);
        manager.clearAll();
    }

    @Test
    void getSubFromEpic() {
        EpicTask epic = manager.getEpicTaskById(2);
        Assertions.assertEquals(manager.getSubFromEpic(epic).size(), 2);
        epic = manager.getEpicTaskById(1);
        Assertions.assertTrue(manager.getSubFromEpic(epic).isEmpty());
        manager.clearAll();
    }

    @Test
    void getEpicFromSub() {
        SubTask sub = manager.getSubTaskById(3);
        Assertions.assertEquals(manager.getEpicFromSub(sub).getId(), 2);
        sub = manager.getSubTaskById(6);
        Assertions.assertEquals(manager.getEpicFromSub(sub).getId(), 5);
        manager.clearAll();
    }

    @Test
    void deleteSingleById() {
        manager.deleteSingleById(14);
        Assertions.assertNull(manager.getSingleTaskById(14), "Неверно удаляет single");
        manager.clearAll();
    }

    @Test
    void deleteSubById() {
        manager.deleteSubById(6);
        Assertions.assertNull(manager.getSubTaskById(6), "Неверно удаляет sub");
        manager.clearAll();
    }

    @Test
    void deleteEpicById() {
        manager.deleteEpicById(5);
        Assertions.assertNull(manager.getEpicTaskById(5), "Неверно удаляет epic");
        Assertions.assertNull(manager.getSubTaskById(6), "Не удаляет sub после удаления epic");
        Assertions.assertNull(manager.getSubTaskById(7), "Не удаляет sub после удаления epic");
        manager.clearAll();
    }

    @Test
    void clearSubTasks() {
        manager.clearSubTasks();
        Assertions.assertTrue(manager.getAllSubTasks().isEmpty());
        manager.clearAll();
    }

    @Test
    void clearSingleTasks() {
        manager.clearSingleTasks();
        Assertions.assertTrue(manager.getAllSingleTasks().isEmpty());
        manager.clearAll();
    }

    @Test
    void clearEpicTasks() {
        manager.clearEpicTasks();
        Assertions.assertTrue(manager.getAllEpicTasks().isEmpty());
        manager.clearAll();
    }

    @Test
    void clearAll() {
        manager.clearAll();
        Assertions.assertTrue(manager.getAllTasks().isEmpty());
    }

}