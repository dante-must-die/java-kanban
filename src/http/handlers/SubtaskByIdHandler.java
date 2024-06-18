package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import http.HttpTaskServer;
import manager.TaskManager;
import moduls.SubTask;

import java.io.IOException;

public class SubtaskByIdHandler extends BaseHttpHandler { // класс для эндпоинтов SubtaskById
    private static final Gson gson = HttpTaskServer.getGson();
    private final TaskManager taskManager;

    public SubtaskByIdHandler(TaskManager taskManager) {
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
                handleGetSubTaskById(exchange, id);
                break;
            case "DELETE":
                handleDeleteSubTask(exchange, id);
                break;
            default:
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        }
    }

    private void handleGetSubTaskById(HttpExchange exchange, int id) throws IOException {
        SubTask subTask = (SubTask) taskManager.getSubTaskById(id);
        if (subTask == null) {
            sendNotFound(exchange);
            return;
        }
        String response = gson.toJson(subTask);
        sendText(exchange, response, 200);
    }

    private void handleDeleteSubTask(HttpExchange exchange, int id) throws IOException {
        try {
            taskManager.deleteSubTask(id);
            sendText(exchange, "SubTask deleted successfully", 200);
        } catch (Exception e) {
            exchange.sendResponseHeaders(500, -1); // Internal Server Error
        }
    }
}
