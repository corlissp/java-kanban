import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import enums.Status;
import http.KVServer;
import manager.history.HistoryManager;
import manager.Managers;
import manager.task.TaskManager;
import tasks.SingleTask;

public class Main {
    public static void main(String[] args) {
        KVServer server;
        try {
            Gson gson = new GsonBuilder().create();
            server = new KVServer();
            server.start();
            HistoryManager historyManager = Managers.historyManager();
            TaskManager httpTaskManager = Managers.getDefault(historyManager);

            SingleTask task = new SingleTask("First single task", Status.NEW,
                    "", 60, "10:00 10.01.24");
            httpTaskManager.addTask(task);  // id 1
            httpTaskManager.saveNewEpicTask("Epic(1)", "",
                    "11:00 11.11.23");  // id 2
            httpTaskManager.saveNewSubTask("Sub 1", "Sub by epic 1", 2,
                    "11:10 11.11.23", 60); // id 3

            System.out.println("Печать задач по id");
            System.out.println(gson.toJson(httpTaskManager.getEpicTaskById(2)));
            System.out.println(gson.toJson(httpTaskManager.getSubTaskById(3)));
            System.out.println(gson.toJson(httpTaskManager.getSingleTaskById(1)));
            System.out.println("Печать всех задач");
            System.out.println(gson.toJson(httpTaskManager.getAllTasks()));
            System.out.println("Печать всех эпиков");
            System.out.println(gson.toJson(httpTaskManager.getAllEpicTasks()));
            System.out.println("Печать всех подзадач");
            System.out.println(gson.toJson(httpTaskManager.getAllSubTasks()));

            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
