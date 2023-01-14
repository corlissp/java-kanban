import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        // Create 2 new single task
        manager.saveNewSingleTask(new SingleTask.Creator("First single task"));  // id 1
        manager.saveNewSingleTask(new SingleTask.Creator("Second single task"));  // id 2

        // Change status single task
        SingleTask singleTask = (SingleTask) manager.getTaskById(1);
        manager.update(singleTask.updateStatus(Status.IN_PROGRESS));

        // Output single tasks
        System.out.println("\tSingleTask");
        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getTaskById(2));

        // Create first epic task
        manager.saveNewEpicTask(new EpicTask.Creator("Epic(1)"));  // id 3
        EpicTask epicTask = (EpicTask) manager.getTaskById(3);

        // Create two subtasks by Epic(1)
        manager.saveNewSubTask(new SubTask.Creator("Sub 1 by Epic(1)", epicTask));  // id 4
        manager.saveNewSubTask(new SubTask.Creator("Sub 2 by Epic(1)", epicTask));  // id 5

        // Change status first subtask
        SubTask subTask = (SubTask) manager.getTaskById(4);
        manager.update(subTask.updateStatus(Status.DONE));
        manager.update(epicTask.updateStatus());

        // Change status second subtask
        subTask = (SubTask) manager.getTaskById(5);
        manager.update(subTask.updateStatus(Status.IN_PROGRESS));
        manager.update(epicTask.updateStatus());

        // Create second epic task
        manager.saveNewEpicTask(new EpicTask.Creator("Epic(2)"));  // id 6
        epicTask = (EpicTask) manager.getTaskById(6);

        // Create one subtask by Epic(2)
        manager.saveNewSubTask(new SubTask.Creator("Sub 1 by Epic(2)", epicTask));  // id 7

        // Change status subtask
        subTask = (SubTask) manager.getTaskById(7);
        manager.update(subTask.updateStatus(Status.DONE));
        manager.update(epicTask.updateStatus());

        // Output epic tasks
        System.out.println("\tEpicTask");
        System.out.println(manager.getTaskById(3));
        System.out.println(manager.getTaskById(6));

        // Delete single task
        manager.deleteById(2);

        /*
            Delete epic task
            If delete an epic, the subtasks of this epic is also deleted
         */
        manager.deleteById(6);

        // Delete one subtask by Epic(1)
        manager.deleteById(5);

        // Output
        System.out.println("\tAfter delete single task, Epic(2) and Sub 2 by Epic(1)");
        System.out.println(manager.getAllTasks());

        /*
            Delete last subtask by Epic(1)
            If epic haven`t god subs, epic status becomes NEW
         */
        manager.deleteById(4);

        // Output
        System.out.println("\tFinal");
        System.out.println(manager.getAllTasks());
    }
}
