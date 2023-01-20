import manager.InMemoryTaskManager;
import manager.Managers;
import tasks.EpicTask;
import tasks.SingleTask;
import enums.Status;
import tasks.SubTask;

import java.sql.SQLOutput;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = Managers.taskManager();

        // Create 2 new single task
        manager.saveNewSingleTask("First single task", "");  // id 1
        manager.saveNewSingleTask("Second single task", "");  // id 2

        // Change status single task
        SingleTask singleTask = manager.getSingleTaskById(1);
        manager.updateSingleStatus(singleTask, Status.DONE);

        // Output single tasks
        System.out.println("\ttasks.SingleTask");
        System.out.println(manager.getAllSingleTasks());
        System.out.println("\tHistory");
        System.out.println(Managers.getDefaultHistory());

        // Create first epic task
        manager.saveNewEpicTask("Epic(1)", "");  // id 3
        EpicTask epicTask = manager.getEpicTaskById(3);

        // Create two subtasks by Epic(1)
        manager.saveNewSubTask("Sub 1 by Epic(1)", "", epicTask.getId());  // id 4
        manager.saveNewSubTask("Sub 2 by Epic(1)", "", epicTask.getId());  // id 5

        // Change status first subtask
        SubTask subTask = manager.getSubTaskById(4);
        manager.updateSubStatus(subTask, Status.DONE);

        // Change status second subtask
        subTask = manager.getSubTaskById(5);
        manager.updateSubStatus(subTask, Status.IN_PROGRESS);

        System.out.println("\ttasks.SubTasks by Epic(1)");
        System.out.println(manager.getSubFromEpic(epicTask));
        System.out.println("\tHistory");
        System.out.println(Managers.getDefaultHistory());

        // Create second epic task
        manager.saveNewEpicTask("Epic(2)", "");  // id 6
        epicTask = manager.getEpicTaskById(6);

        // Create one subtask by Epic(2)
        manager.saveNewSubTask("Sub 1 by Epic(2)", "", epicTask.getId());  // id 7

        // Change status subtask
        subTask = manager.getSubTaskById(7);
        manager.updateSubStatus(subTask, Status.DONE);

        // Output epic tasks
        System.out.println("\ttasks.EpicTask");
        System.out.println(manager.getAllEpicTasks());

        // Delete single task
        manager.deleteSingleById(2);

        /*
            Delete epic task
            If delete an epic, the subtasks of this epic is also deleted
         */
        manager.deleteEpicById(6);

        // Delete one subtask by Epic(1)
        manager.deleteSubById(5);

        // Output
        System.out.println("\tAfter delete single task, Epic(2) and Sub 2 by Epic(1)");
        System.out.println("\tSingle Tasks");
        System.out.println(manager.getAllSingleTasks());
        System.out.println("\tEpic Tasks");
        System.out.println(manager.getAllEpicTasks());
        System.out.println("\tSubtasks");
        System.out.println(manager.getAllSubTasks());

        /*
            Delete last subtask by Epic(1)
            If epic haven`t god subs, epic status becomes NEW
         */
        manager.clearSubTasks();

        // Output
        System.out.println("\tFinal");
        System.out.println(manager.getAllTasks());

        System.out.println("\tHistory");
        System.out.println(Managers.getDefaultHistory());

        manager.clearAll();
    }
}
