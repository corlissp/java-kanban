import manager.task.FileBackedTasksManager;
import manager.task.InMemoryTaskManager;
import manager.Managers;
import tasks.EpicTask;
import tasks.SingleTask;
import enums.Status;
import tasks.SubTask;

public class Main {
    public static void main(String[] args) {

        FileBackedTasksManager manager = Managers.fileManager();
        // InMemoryTaskManager manager = Managers.taskManager();

        // Create 2 new single task
        manager.saveNewSingleTask("First single task", "", "10:00 10.01.24", 60);  // id 1
        manager.saveNewSingleTask("Second single task", "", "10:30 10.01.24", 60);
        manager.saveNewSingleTask("Second single task", "", "11:30 10.01.24", 60);  // id 2

        // Change status single task
        SingleTask singleTask = manager.getSingleTaskById(1);
        manager.updateSingleStatus(singleTask, Status.DONE);

        // Output single tasks
        System.out.println("\ttasks.SingleTask");
        System.out.println(manager.getAllSingleTasks());
        System.out.println("\tHistory");
        System.out.println(manager.getHistory());

        // Create first epic task
        manager.saveNewEpicTask("Epic(1)", "", "11:00 11.11.23");  // id 3
        EpicTask epicTask = manager.getEpicTaskById(3);

        // Create two subtasks by Epic(1)
        manager.saveNewSubTask("Sub 1 by Epic(1)", "", epicTask.getId(), "11:10 11.11.23", 120);  // id 4
        manager.saveNewSubTask("Sub 2 by Epic(1)", "", epicTask.getId(), "13:10 11.11.23", 180);  // id 5

        // Change status first subtask
        SubTask subTask = manager.getSubTaskById(4);
        manager.updateSubStatus(subTask, Status.DONE);

        // Change status second subtask
        subTask = manager.getSubTaskById(5);
        manager.updateSubStatus(subTask, Status.IN_PROGRESS);

        // Test repeats in history
        subTask = manager.getSubTaskById(4);
        subTask = manager.getSubTaskById(5);
        subTask = manager.getSubTaskById(5);

        System.out.println("\tHistory");
        System.out.println(manager.getHistory());

        manager.deleteSingleById(1);
        System.out.println("\tHistory after delete id 1");
        System.out.println(manager.getHistory());

        // Create second epic task
        manager.saveNewEpicTask("Epic(2)", "", "13:00 11.02.23");  // id 6
        epicTask = manager.getEpicTaskById(6);

        // Create one subtask by Epic(2)
        manager.saveNewSubTask("Sub 1 by Epic(2)", "", epicTask.getId(), "13:00 12.02.23", 180);  // id 7

        // Change status subtask
        subTask = manager.getSubTaskById(7);
        manager.updateSubStatus(subTask, Status.DONE);

        // Output epic tasks
        System.out.println("\ttasks.EpicTask");
        System.out.println(manager.getAllEpicTasks());

        System.out.println("\tSort");
        System.out.println(manager.getPrioritizedTasks());

        // Delete single task
        manager.deleteSingleById(2);
        System.out.println("\tHistory after delete id 2");
        System.out.println(manager.getHistory());

        /*
            Delete epic task
            If delete an epic, the subtasks of this epic is also deleted
         */
        manager.deleteEpicById(6);

        System.out.println("\tHistory after delete id 6");
        System.out.println(manager.getHistory());

        // Output
        System.out.println("\tAfter delete single task, Epic(2) and Sub 2 by Epic(1)");
        System.out.println("\tSingle Tasks");
        System.out.println(manager.getAllSingleTasks());
        System.out.println("\tEpic Tasks");
        System.out.println(manager.getAllEpicTasks());
        System.out.println("\tSubtasks");
        System.out.println(manager.getAllSubTasks());

        // Output
        System.out.println("\tFinal");
        System.out.println(manager.getAllTasks());

        System.out.println("\tHistory");
        System.out.println(manager.getHistory() + "\n");

        System.out.println("\tSort");
        System.out.println(manager.getPrioritizedTasks());

        manager.clearAll();

        manager.openFile("Test.scv");
        manager.saveNewSingleTask("Single new", "After file", "13:00 12.07.23", 180);
        singleTask = manager.getSingleTaskById(1);
        manager.updateSingleStatus(singleTask, Status.IN_PROGRESS);
        manager.saveNewEpicTask("Epic new after file", "Epic for save", "13:00 12.08.23");
        manager.deleteSingleById(4);
    }
}
