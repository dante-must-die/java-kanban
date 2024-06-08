package test;

import manager.InMemoryTaskManager;
import manager.ManagerSaveException;
import moduls.Epic;
import moduls.SubTask;
import moduls.Task;
import moduls.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private InMemoryTaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void shouldAddAndRetrieveTaskById() throws ManagerSaveException {
        Task task = new Task("Task", "Task description", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.addTask(task);
        Task retrievedTask = taskManager.getTaskById(task.getId());
        assertNotNull(retrievedTask, "The task should be retrieved by ID");
        assertEquals(task, retrievedTask, "The retrieved task should match the added task");
    }

    @Test
    void shouldAddAndRetrieveSubTaskById() throws ManagerSaveException {
        Epic epic = new Epic("Epic", "Epic description");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("SubTask", "SubTask description", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(30), epic.getId());
        taskManager.addSubTask(subTask);
        SubTask retrievedSubTask = (SubTask) taskManager.getSubTaskById(subTask.getId());
        assertNotNull(retrievedSubTask, "The subTask should be retrieved by ID");
        assertEquals(subTask, retrievedSubTask, "The retrieved subTask should match the added subTask");
    }

    @Test
    void shouldAddAndRetrieveEpicById() throws ManagerSaveException {
        Epic epic = new Epic("Epic", "Epic description");
        taskManager.addEpic(epic);
        Epic retrievedEpic = (Epic) taskManager.getEpicById(epic.getId());
        assertNotNull(retrievedEpic, "The epic should be retrieved by ID");
        assertEquals(epic, retrievedEpic, "The retrieved epic should match the added epic");
    }

    @Test
    void tasksWithManualAndGeneratedIdsDoNotConflict() throws ManagerSaveException {
        Task autoIdTask = new Task("Auto ID Task", "Task description", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.addTask(autoIdTask);
        int autoGeneratedId = autoIdTask.getId();

        int manualId = autoGeneratedId + 1;
        Task manualIdTask = new Task("Manual ID Task", "Task description", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(60));
        manualIdTask.setId(manualId);
        taskManager.addTask(manualIdTask);

        Task retrievedAutoIdTask = taskManager.getTaskById(autoGeneratedId);
        Task retrievedManualIdTask = taskManager.getTaskById(manualId);

        assertNotNull(retrievedAutoIdTask, "Task with auto-generated ID should exist.");
        assertNotNull(retrievedManualIdTask, "Task with manually set ID should exist.");
        assertNotEquals(retrievedAutoIdTask.getId(), retrievedManualIdTask.getId(), "Tasks should have unique IDs.");
    }

    @Test
    void addedTaskShouldRemainUnchanged() throws ManagerSaveException {
        Task originalTask = new Task("Original Task", "Task Description", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.addTask(originalTask);
        int taskId = originalTask.getId();

        Task retrievedTask = taskManager.getTaskById(taskId);

        assertEquals(originalTask.getId(), retrievedTask.getId(), "ID should match");
        assertEquals(originalTask.getTitle(), retrievedTask.getTitle(), "Title should match");
        assertEquals(originalTask.getDescription(), retrievedTask.getDescription(), "Description should match");
        assertEquals(originalTask.getStatus(), retrievedTask.getStatus(), "Status should match");
    }

    @Test
    void deletingSubTaskRemovesItFromParentEpic() throws ManagerSaveException {
        Epic epic = new Epic("Epic for Subtasks", "Epic Description");
        taskManager.addEpic(epic);
        int epicId = epic.getId();

        SubTask subTask1 = new SubTask("SubTask 1", "Description for SubTask 1", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(30), epicId);
        taskManager.addSubTask(subTask1);
        int subTaskId1 = subTask1.getId();

        SubTask subTask2 = new SubTask("SubTask 2", "Description for SubTask 2", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(60), epicId);
        taskManager.addSubTask(subTask2);
        int subTaskId2 = subTask2.getId();

        taskManager.deleteSubTask(subTaskId1);

        Epic updatedEpic = (Epic) taskManager.getEpicById(epicId);
        assertFalse(updatedEpic.getSubTaskIds().contains(subTaskId1), "Deleted subtask's ID should not be in the epic's subtask list.");
        assertTrue(updatedEpic.getSubTaskIds().contains(subTaskId2), "Other subtask's ID should still be present.");
    }

    @Test
    void deletingEpicAlsoDeletesItsSubTasks() throws ManagerSaveException {
        Epic epic = new Epic("Complex Epic", "Contains several subtasks");
        taskManager.addEpic(epic);
        int epicId = epic.getId();

        SubTask subTask1 = new SubTask("SubTask 1 for Epic", "Details for subtask 1", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(30), epicId);
        taskManager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("SubTask 2 for Epic", "Details for subtask 2", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(60), epicId);
        taskManager.addSubTask(subTask2);

        taskManager.deleteEpic(epicId);

        assertNull(taskManager.getSubTaskById(subTask1.getId()), "SubTask 1 should be deleted when the parent epic is deleted.");
        assertNull(taskManager.getSubTaskById(subTask2.getId()), "SubTask 2 should be deleted when the parent epic is deleted.");
    }
}
