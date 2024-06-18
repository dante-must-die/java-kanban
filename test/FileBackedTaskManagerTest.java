package test;

import manager.FileBackedTaskManager;
import manager.ManagerSaveException;
import manager.TaskManager;
import moduls.Epic;
import moduls.SubTask;
import moduls.Task;
import moduls.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    private File tempFile;
    private TaskManager manager;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("tasks", ".csv");
        manager = FileBackedTaskManager.createClass(tempFile);
    }

    @AfterEach
    void tearDown() {
        tempFile.delete();
    }

    @Test
    void shouldSaveAndLoadEmptyFile() throws IOException, ManagerSaveException {
        ((FileBackedTaskManager) manager).savePublic();

        TaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertTrue(loadedManager.getTasks().isEmpty());
        assertTrue(loadedManager.getEpics().isEmpty());
        assertTrue(loadedManager.getSubTasks().isEmpty());
    }

    @Test
    void shouldSaveAndLoadMultipleTasks() throws ManagerSaveException {
        Task task1 = new Task("Task 1", "Description 1", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        Task task2 = new Task("Task 2", "Description 2", TaskStatus.IN_PROGRESS, Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(30));
        Epic epic = new Epic("Epic 1", "Epic Description");
        SubTask subTask = new SubTask("SubTask 1", "SubTask Description", TaskStatus.DONE, Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(60), epic.getId());

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic);
        manager.addSubTask(subTask);

        TaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(2, loadedManager.getTasks().size());
        assertEquals(1, loadedManager.getEpics().size());
        assertEquals(1, loadedManager.getSubTasks().size());

        assertEquals(task1, loadedManager.getTaskById(task1.getId()));
        assertEquals(task2, loadedManager.getTaskById(task2.getId()));
        assertEquals(epic, loadedManager.getEpicById(epic.getId()));
        assertEquals(subTask, loadedManager.getSubTaskById(subTask.getId()));
    }

    @Test
    void shouldRestoreManagerStateFromFile() throws ManagerSaveException {
        Task task = new Task("Task 1", "Description", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        Epic epic = new Epic("Epic 1", "Epic Description");
        SubTask subTask = new SubTask("SubTask 1", "SubTask Description", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(30), epic.getId());

        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubTask(subTask);

        TaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertNotNull(loadedManager.getTaskById(task.getId()));
        assertNotNull(loadedManager.getEpicById(epic.getId()));
        assertNotNull(loadedManager.getSubTaskById(subTask.getId()));
    }
}
