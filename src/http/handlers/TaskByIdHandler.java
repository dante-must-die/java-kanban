package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import http.HttpTaskServer;
import manager.TaskManager;
import moduls.Task;

import java.io.IOException;

public class TaskByIdHandler extends BaseHttpHandler {
    private static final Gson gson = HttpTaskServer.getGson();
    private final TaskManager taskManager;

    public TaskByIdHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        if (pathParts.length < 3) {
            sendNotFound(exchange);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(pathParts[2]);
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
            return;
        }

        switch (method) {
            case "GET":
                handleGetTaskById(exchange, id);
                break;
            case "DELETE":
                handleDeleteTask(exchange, id);
                break;
            default:
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        }
    }

    private void handleGetTaskById(HttpExchange exchange, int id) throws IOException {
        Task task = taskManager.getTaskById(id);
        if (task == null) {
            sendNotFound(exchange);
            return;
        }
        String response = gson.toJson(task);
        sendText(exchange, response, 200);
    }

    private void handleDeleteTask(HttpExchange exchange, int id) throws IOException {
        try {
            taskManager.deleteTask(id);
            sendText(exchange, "Task deleted successfully", 200);
        } catch (Exception e) {
            exchange.sendResponseHeaders(500, -1); // Internal Server Error
        }
    }
}
