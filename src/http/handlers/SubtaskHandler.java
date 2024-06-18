package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import http.HttpTaskServer;
import manager.TaskManager;
import moduls.SubTask;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler {
    // класс для эндпоинтов Subtask
    private static final Gson gson = HttpTaskServer.getGson();
    private final TaskManager taskManager;

    public SubtaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                handleGetSubTasks(exchange);
                break;
            case "POST":
                handlePostSubTask(exchange);
                break;
            default:
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        }
    }

    private void handleGetSubTasks(HttpExchange exchange) throws IOException {
        List<SubTask> subTasks = taskManager.getSubTasks();
        String response = gson.toJson(subTasks);
        sendText(exchange, response, 200);
    }

    private void handlePostSubTask(HttpExchange exchange) throws IOException {
        try (InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {
            SubTask subTask = gson.fromJson(isr, SubTask.class);
            taskManager.addSubTask(subTask);
            sendText(exchange, "SubTask added successfully", 201);
        } catch (Exception e) {
            exchange.sendResponseHeaders(500, -1); // Internal Server Error
        }
    }
}
