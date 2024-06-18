package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import http.HttpTaskServer;
import manager.TaskManager;
import moduls.Task;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandler { // класс для эндпоинтов Task
    private static final Gson gson = HttpTaskServer.getGson();
    private final TaskManager taskManager;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                handleGetTasks(exchange);
                break;
            case "POST":
                handlePostTask(exchange);
                break;
            default:
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        List<Task> tasks = taskManager.getTasks();
        String response = gson.toJson(tasks);
        sendText(exchange, response, 200);
    }

    private void handlePostTask(HttpExchange exchange) throws IOException {
        try (InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {
            Task task = gson.fromJson(isr, Task.class);
            taskManager.addTask(task);
            sendText(exchange, "Task added successfully", 201);
        } catch (Exception e) {
            exchange.sendResponseHeaders(500, -1); // Internal Server Error
        }
    }
}
