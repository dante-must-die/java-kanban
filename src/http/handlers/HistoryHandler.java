package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import http.HttpTaskServer;
import manager.TaskManager;
import moduls.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {
    // класс для эндпоинтов History
    private static final Gson GSON = HttpTaskServer.getGson();
    private final TaskManager taskManager;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            handleGetHistory(exchange);
        } else {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        }
    }

    private void handleGetHistory(HttpExchange exchange) throws IOException {
        List<Task> history = taskManager.getHistory();
        String response = GSON.toJson(history);
        sendText(exchange, response, 200);
    }
}
