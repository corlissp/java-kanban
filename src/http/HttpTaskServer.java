package http;

import com.sun.net.httpserver.HttpServer;
import http.handler.EpicTaskHandler;
import http.handler.HistoryHandler;
import http.handler.SingleTaskHandler;
import http.handler.SubTaskHandler;
import manager.Managers;
import manager.history.HistoryManager;
import manager.task.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final HttpServer httpServer;
    private static final int PORT = 8080;

    public HttpTaskServer() throws IOException, InterruptedException {
        HistoryManager historyManager = Managers.historyManager();
        TaskManager taskManager = Managers.getDefault(historyManager);
        this.httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task", new SingleTaskHandler(taskManager));
        httpServer.createContext("/tasks/epic/", new EpicTaskHandler(taskManager));
        httpServer.createContext("/tasks/subtask/", new SubTaskHandler(taskManager));
        httpServer.createContext("/tasks/history/", new HistoryHandler(taskManager));
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(1);
    }
}
