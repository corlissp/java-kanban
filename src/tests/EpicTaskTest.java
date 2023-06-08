package tests;

import enums.Status;
import manager.Managers;
import manager.task.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.EpicTask;

class EpicTaskTest {

    private final InMemoryTaskManager manager;

    {
        manager = Managers.taskManager();
    }

    @BeforeEach
    public void beforeEach() {
        manager.saveNewEpicTask("Epic(1)", "Empty", "13:00 10.01.22"); // id 1
        manager.saveNewEpicTask("Epic(2)", "All new", "13:00 11.01.22"); // id 2
        manager.saveNewSubTask("Sub 1 by Epic(2)", "New status", 2, "13:10 11.01.22", 180);  // id 3
        manager.saveNewSubTask("Sub 2 by Epic(2)", "New status", 2, "13:00 12.01.22", 180);  // id 4
        manager.saveNewEpicTask("Epic(3)", "All done", "13:00 13.01.22"); // id 5
        manager.saveNewSubTask("Sub 1 by Epic(3)", "Done status", 5, "13:10 13.01.22", 180);  // id 6
        manager.saveNewSubTask("Sub 2 by Epic(3)", "Done status", 5, "13:00 14.01.22", 180);  // id 7
        manager.saveNewEpicTask("Epic(4)", "All done", "13:00 15.01.22"); // id 8
        manager.saveNewSubTask("Sub 1 by Epic(4)", "New status", 8, "13:10 15.01.22", 180);  // id 9
        manager.saveNewSubTask("Sub 2 by Epic(4)", "Done status", 8, "13:00 16.01.22", 180);  // id 10
        manager.saveNewEpicTask("Epic(5)", "All done", "13:00 17.01.22"); // id 11
        manager.saveNewSubTask("Sub 1 by Epic(5)", "New status", 11, "13:10 17.01.22", 180);  // id 12
        manager.saveNewSubTask("Sub 2 by Epic(5)", "In progress status", 11, "13:00 18.01.22", 180);  // id 13
    }
    @Test
    public void emptyEpicTest() {
        EpicTask epicTask = manager.getEpicTaskById(1);
        Assertions.assertEquals(epicTask.getSizeSubTasks(), 0);
        manager.clearAll();
    }

    @Test
    public void allNewEpicTest() {
        EpicTask epicTask = manager.getEpicTaskById(2);
        for (Integer idSubTask : epicTask.getSubTasks()) {
            Status statusSub = manager.getSubTaskById(idSubTask).getStatus();
            Assertions.assertEquals(statusSub, Status.NEW);
        }
        Assertions.assertEquals(epicTask.getStatus(), Status.NEW, "Epic status not NEW");
        manager.clearAll();
    }

    @Test
    public void allDoneEpicTest() {
        EpicTask epicTask = manager.getEpicTaskById(5);
        manager.updateSubStatus(manager.getSubTaskById(6), Status.DONE);
        manager.updateSubStatus(manager.getSubTaskById(7), Status.DONE);
        for (Integer idSubTask : epicTask.getSubTasks()) {
            Status statusSub = manager.getSubTaskById(idSubTask).getStatus();
            Assertions.assertEquals(statusSub, Status.DONE);
        }
        Assertions.assertEquals(epicTask.getStatus(), Status.DONE, "Epic status not DONE");
        manager.clearAll();
    }

    @Test
    public void newAndDoneEpicTest() {
        EpicTask epicTask = manager.getEpicTaskById(8);
        manager.updateSubStatus(manager.getSubTaskById(10), Status.DONE);
        Assertions.assertEquals(epicTask.getStatus(), Status.IN_PROGRESS, "Epic status not IN_PROGRESS");
        manager.clearAll();
    }

    @Test
    public void inProgressEpicTest() {
        EpicTask epicTask = manager.getEpicTaskById(11);
        manager.updateSubStatus(manager.getSubTaskById(13), Status.IN_PROGRESS);
        Assertions.assertEquals(epicTask.getStatus(), Status.IN_PROGRESS, "Epic status not IN_PROGRESS");
        manager.clearAll();
    }
}