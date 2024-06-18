package test;

import com.google.gson.Gson;
import http.HttpTaskServer;
import manager.InMemoryTaskManager;
import manager.ManagerSaveException;
import manager.TaskManager;
import moduls.Epic;
import moduls.SubTask;
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

public class SubtaskHandlerTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public SubtaskHandlerTest() throws IOException {
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
    public void testAddSubTask() throws IOException, InterruptedException, ManagerSaveException {
        Epic epic = new Epic("Test Epic", "Testing epic");
        manager.addEpic(epic);

        SubTask subTask = new SubTask("Test SubTask", "Testing subtask",
                TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.now(), epic.getId());
        String subTaskJson = gson.toJson(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subTaskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<SubTask> subTasksFromManager = manager.getSubTasks();
        assertNotNull(subTasksFromManager, "Подзадачи не возвращаются");
        assertEquals(1, subTasksFromManager.size(), "Некорректное количество подзадач");
        assertEquals("Test SubTask", subTasksFromManager.get(0).getTitle(), "Некорректное имя подзадачи");
    }

    @Test
    public void testGetSubTasks() throws IOException, InterruptedException, ManagerSaveException {
        Epic epic = new Epic("Test Epic", "Testing epic");
        manager.addEpic(epic);

        SubTask subTask = new SubTask("Test SubTask", "Testing subtask",
                TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.now(), epic.getId());
        manager.addSubTask(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<SubTask> subTasksFromResponse = gson.fromJson(response.body(), List.class);
        assertNotNull(subTasksFromResponse, "Подзадачи не возвращаются");
        assertEquals(1, subTasksFromResponse.size(), "Некорректное количество подзадач");
    }

    @Test
    public void testDeleteSubTask() throws IOException, InterruptedException, ManagerSaveException {
        Epic epic = new Epic("Test Epic", "Testing epic");
        manager.addEpic(epic);

        SubTask subTask = new SubTask("Test SubTask", "Testing subtask",
                TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.now(), epic.getId());
        manager.addSubTask(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subTask.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<SubTask> subTasksFromManager = manager.getSubTasks();
        assertEquals(0, subTasksFromManager.size(), "Некорректное количество подзадач после удаления");
    }

    @Test
    public void testGetSubTaskById() throws IOException, InterruptedException, ManagerSaveException {
        Epic epic = new Epic("Test Epic", "Testing epic");
        manager.addEpic(epic);

        SubTask subTask = new SubTask("Test SubTask", "Testing subtask",
                TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.now(), epic.getId());
        manager.addSubTask(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subTask.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        SubTask subTaskFromResponse = gson.fromJson(response.body(), SubTask.class);
        assertNotNull(subTaskFromResponse, "Подзадача не возвращается");
        assertEquals(subTask.getId(), subTaskFromResponse.getId(), "Некорректный ID подзадачи");
    }

    @Test
    public void testGetNonExistentSubTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/999");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }
}

