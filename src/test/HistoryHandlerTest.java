package test;

import com.google.gson.Gson;
import http.HttpTaskServer;
import manager.InMemoryTaskManager;
import manager.ManagerSaveException;
import manager.TaskManager;
import moduls.Task;
import moduls.TaskStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryHandlerTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public HistoryHandlerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.deleteAllTasks();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testGetHistory() throws IOException, InterruptedException, ManagerSaveException {
        Task task = new Task("Test Task", "Testing task",
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        manager.addTask(task);
        manager.getTaskById(task.getId()); // Adding task to history

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> historyFromResponse = gson.fromJson(response.body(), List.class);
        assertNotNull(historyFromResponse, "История задач не возвращается");
        assertEquals(1, historyFromResponse.size(), "Некорректное количество задач в истории");
    }

    @Test
    public void testGetEmptyHistory() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> historyFromResponse = gson.fromJson(response.body(), List.class);
        assertNotNull(historyFromResponse, "История задач не возвращается");
        assertEquals(0, historyFromResponse.size(), "История задач не пуста");
    }
}

