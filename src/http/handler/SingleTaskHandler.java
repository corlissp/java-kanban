package http.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import enums.Status;
import manager.task.TaskManager;
import tasks.SingleTask;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class SingleTaskHandler implements HttpHandler {
    private final Gson gson = new GsonBuilder().create();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final TaskManager taskManager;

    public SingleTaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        int statusCode = 0;
        String response = "";
        String method = httpExchange.getRequestMethod();
        String path = String.valueOf(httpExchange.getRequestURI());
        System.out.println("Обрабатывается запрос " + path + " с методом " + method);
        switch (method) {
            case "GET":
                String query = httpExchange.getRequestURI().getQuery();
                if (query == null) {
                    statusCode = 200;
                    String jsonString = gson.toJson(taskManager.getAllTasks());
                    System.out.println("GET TASKS: " + jsonString);
                    response = gson.toJson(jsonString);
                } else {
                    try {
                        int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                        SingleTask task = taskManager.getSingleTaskById(id);
                        if (task != null) {
                            response = gson.toJson(task);
                        } else {
                            response = "Задача с данным id не найдена";
                        }
                        statusCode = 200;
                    } catch (StringIndexOutOfBoundsException e) {
                        statusCode = 400;
                        response = "В запросе отсутствует необходимый параметр id";
                    } catch (NumberFormatException e) {
                        statusCode = 400;
                        response = "Неверный формат id";
                    }
                }
                break;
            case "POST":
                String bodyRequest = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                try {
                    SingleTask task = gson.fromJson(bodyRequest, SingleTask.class);
                    int id = task.getId();
                    Status status = task.getStatus();
                    if (taskManager.getSingleTaskById(id) != null) {
                        taskManager.updateSingleStatus(task, status);
                        statusCode = 201;
                        response = "Задача с id=" + id + " обновлена";
                    }
                } catch (JsonSyntaxException e) {
                    statusCode = 400;
                    response = "Неверный формат запроса";
                }
                break;
            case "DELETE":
                response = "";
                query = httpExchange.getRequestURI().getQuery();
                if (query == null) {
                    taskManager.clearSingleTasks();
                    statusCode = 200;
                } else {
                    try {
                        int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                        taskManager.deleteSingleById(id);
                        statusCode = 200;
                    } catch (StringIndexOutOfBoundsException e) {
                        statusCode = 400;
                        response = "В запросе отсутствует необходимый параметр id";
                    } catch (NumberFormatException e) {
                        statusCode = 400;
                        response = "Неверный формат id";
                    }
                }
                break;
            default:
                statusCode = 400;
                response = "Некорректный запрос";
        }

        httpExchange.getResponseHeaders().set("Content-Type", "text/plain; charset=" + DEFAULT_CHARSET);
        httpExchange.sendResponseHeaders(statusCode, 0);

        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
