package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import http.HttpTaskServer;
import manager.TaskManager;
import moduls.Epic;

import java.io.IOException;

public class EpicByIdHandler extends BaseHttpHandler {
    // класс для EpicById
    private static final Gson gson = HttpTaskServer.getGson();
    private final TaskManager taskManager;

    public EpicByIdHandler(TaskManager taskManager) {
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
                handleGetEpicById(exchange, id);
                break;
            case "DELETE":
                handleDeleteEpic(exchange, id);
                break;
            default:
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        }
    }

    private void handleGetEpicById(HttpExchange exchange, int id) throws IOException {
        Epic epic = (Epic) taskManager.getEpicById(id);
        if (epic == null) {
            sendNotFound(exchange);
            return;
        }
        String response = gson.toJson(epic);
        sendText(exchange, response, 200);
    }

    private void handleDeleteEpic(HttpExchange exchange, int id) throws IOException {
        try {
            taskManager.deleteEpic(id);
            sendText(exchange, "Epic deleted successfully", 200);
        } catch (Exception e) {
            exchange.sendResponseHeaders(500, -1); // Internal Server Error
        }
    }
}
