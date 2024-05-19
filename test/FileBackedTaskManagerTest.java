package test;

import manager.FileBackedTaskManager;
import manager.ManagerSaveException;
import manager.TaskManager;
import manager.Managers;
import moduls.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    private File tempFile;
    private TaskManager manager;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("tasks", ".csv");
        manager = new FileBackedTaskManager(tempFile);
    }

    @AfterEach
    void tearDown() {
        tempFile.delete();
    }

    @Test
    void shouldSaveAndLoadEmptyFile() throws IOException, ManagerSaveException {
        manager.save();
        TaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);
        assertTrue(loadedManager.getTasks().isEmpty());
        assertTrue(loadedManager.getEpics().isEmpty());
        assertTrue(loadedManager.getSubTasks().isEmpty());
    }

    @Test
    void shouldSaveAndLoadMultipleTasks() throws IOException, ManagerSaveException {
        Task task1 = new Task("Task 1", "Description 1", TaskStatus.NEW);
        Task task2 = new Task("Task 2", "Description 2", TaskStatus.IN_PROGRESS);
        Epic epic = new Epic("Epic 1", "Epic Description");
        SubTask subTask = new SubTask("SubTask 1", "SubTask Description", TaskStatus.DONE, epic.getId());

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic);
        manager.addSubTask(subTask);

        manager.save();

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
    void shouldRestoreManagerStateFromFile() throws IOException, ManagerSaveException {
        Task task = new Task("Task 1", "Description", TaskStatus.NEW);
        Epic epic = new Epic("Epic 1", "Epic Description");
        SubTask subTask = new SubTask("SubTask 1", "SubTask Description", TaskStatus.NEW, epic.getId());

        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubTask(subTask);

        manager.save();

        TaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertNotNull(loadedManager.getTaskById(task.getId()));
        assertNotNull(loadedManager.getEpicById(epic.getId()));
        assertNotNull(loadedManager.getSubTaskById(subTask.getId()));
    }
}
