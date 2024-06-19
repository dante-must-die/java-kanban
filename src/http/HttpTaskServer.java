package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import http.adapters.DurationTypeAdapter;
import http.adapters.LocalDateTimeTypeAdapter;
import http.handlers.*;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer { // основной класс для API
    private static final int PORT = 8080;
    private static final Gson GSON = new GsonBuilder() // создание Gson и использование адаптеров
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .create();
    private final TaskManager taskManager;
    private HttpServer server;

    public HttpTaskServer(TaskManager taskManager) throws IOException { // конструктор с хэндлерами
        this.taskManager = taskManager;
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/tasks", new TaskHandler(taskManager));
        server.createContext("/tasks/", new TaskByIdHandler(taskManager));
        server.createContext("/subtasks", new SubtaskHandler(taskManager));
        server.createContext("/subtasks/", new SubtaskByIdHandler(taskManager));
        server.createContext("/epics", new EpicHandler(taskManager));
        server.createContext("/epics/", new EpicByIdHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }

    public static Gson getGson() {
        return GSON;
    }

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer server = new HttpTaskServer(taskManager);
        server.start();
    }

    public void start() { // метод для удобного запуска сервера
        server.start();
        System.out.println("Server started on port " + PORT);
    }

    public void stop() { // метод для закрытия сервера
        server.stop(0);
        System.out.println("Server stopped");
    }
}
