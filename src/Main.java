import manager.task.FileBackedTasksManager;
import manager.task.InMemoryTaskManager;
import manager.Managers;
import tasks.EpicTask;
import tasks.SingleTask;
import enums.Status;
import tasks.SubTask;

public class Main {
    public static void main(String[] args) {
        // InMemoryTaskManager manager = Managers.taskManager();
        FileBackedTasksManager fileManager = Managers.fileManager();

        // Create 2 new single task
        fileManager.saveNewSingleTask("First single task", "");  // id 1
        fileManager.saveNewSingleTask("Second single task", "");  // id 2

        // Change status single task
        SingleTask singleTask = fileManager.getSingleTaskById(1);
        fileManager.updateSingleStatus(singleTask, Status.DONE);

        // Output single tasks
        System.out.println("\ttasks.SingleTask");
        System.out.println(fileManager.getAllSingleTasks());
        System.out.println("\tHistory");
        System.out.println(fileManager.getHistory());

        // Create first epic task
        fileManager.saveNewEpicTask("Epic(1)", "");  // id 3
        EpicTask epicTask = fileManager.getEpicTaskById(3);

        // Create two subtasks by Epic(1)
        fileManager.saveNewSubTask("Sub 1 by Epic(1)", "", epicTask.getId());  // id 4
        fileManager.saveNewSubTask("Sub 2 by Epic(1)", "", epicTask.getId());  // id 5

        // Change status first subtask
        SubTask subTask = fileManager.getSubTaskById(4);
        fileManager.updateSubStatus(subTask, Status.DONE);

        // Change status second subtask
        subTask = fileManager.getSubTaskById(5);
        fileManager.updateSubStatus(subTask, Status.IN_PROGRESS);

        // Test repeats in history
        subTask = fileManager.getSubTaskById(4);
        subTask = fileManager.getSubTaskById(5);
        subTask = fileManager.getSubTaskById(5);

        System.out.println("\ttasks.SubTasks by Epic(1)");
        System.out.println(fileManager.getSubFromEpic(epicTask));
        System.out.println("\tHistory");
        System.out.println(fileManager.getHistory());

        fileManager.deleteSingleById(1);
        System.out.println("\tHistory after delete id 1");
        System.out.println(fileManager.getHistory());

        // Create second epic task
        fileManager.saveNewEpicTask("Epic(2)", "");  // id 6
        epicTask = fileManager.getEpicTaskById(6);

        // Create one subtask by Epic(2)
        fileManager.saveNewSubTask("Sub 1 by Epic(2)", "", epicTask.getId());  // id 7

        // Change status subtask
        subTask = fileManager.getSubTaskById(7);
        fileManager.updateSubStatus(subTask, Status.DONE);

        // Output epic tasks
        System.out.println("\ttasks.EpicTask");
        System.out.println(fileManager.getAllEpicTasks());

        // Delete single task
        fileManager.deleteSingleById(2);
        System.out.println("\tHistory after delete id 2");
        System.out.println(fileManager.getHistory());

        /*
            Delete epic task
            If delete an epic, the subtasks of this epic is also deleted
         */
        fileManager.deleteEpicById(6);

        System.out.println("\tHistory after delete id 6");
        System.out.println(fileManager.getHistory());

        // Delete one subtask by Epic(1)
        fileManager.deleteSubById(4);

        // Output
        System.out.println("\tAfter delete single task, Epic(2) and Sub 2 by Epic(1)");
        System.out.println("\tSingle Tasks");
        System.out.println(fileManager.getAllSingleTasks());
        System.out.println("\tEpic Tasks");
        System.out.println(fileManager.getAllEpicTasks());
        System.out.println("\tSubtasks");
        System.out.println(fileManager.getAllSubTasks());

        /*
            Delete last subtask by Epic(1)
            If epic haven`t god subs, epic status becomes NEW
         */
        // manager.clearSubTasks();

        // Output
        System.out.println("\tFinal");
        System.out.println(fileManager.getAllTasks());

        System.out.println("\tHistory");
        System.out.println(fileManager.getHistory());

        //fileManager.clearAll();

        fileManager.openFile("Test.scv");
        fileManager.saveNewSingleTask("Single new", "After file");
        singleTask = fileManager.getSingleTaskById(7);
        fileManager.updateSingleStatus(singleTask, Status.IN_PROGRESS);
        fileManager.saveNewEpicTask("Epic new after file", "Epic for save");
        fileManager.deleteSingleById(1);
    }
}
