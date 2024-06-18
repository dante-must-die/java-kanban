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

public class PrioritizedHandlerTest {
    //тесты для проверки PrioritizedHandler
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public PrioritizedHandlerTest() throws IOException {
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
    public void testGetPrioritizedTasks() throws IOException, InterruptedException, ManagerSaveException {
        Task task1 = new Task("Test Task 1", "Testing task 1",
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now().plusMinutes(5));
        Task task2 = new Task("Test Task 2", "Testing task 2",
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        manager.addTask(task1);
        manager.addTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> tasksFromResponse = gson.fromJson(response.body(), List.class);
        assertNotNull(tasksFromResponse, "Приоритетные задачи не возвращаются");
        assertEquals(2, tasksFromResponse.size(), "Некорректное количество приоритетных задач");
    }

    @Test
    public void testGetEmptyPrioritizedTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> tasksFromResponse = gson.fromJson(response.body(), List.class);
        assertNotNull(tasksFromResponse, "Приоритетные задачи не возвращаются");
        assertEquals(0, tasksFromResponse.size(), "Список приоритетных задач не пуст");
    }
}

