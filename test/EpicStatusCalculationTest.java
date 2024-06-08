package test;

import manager.ManagerSaveException;
import manager.TaskManager;
import manager.InMemoryTaskManager;
import moduls.Epic;
import moduls.SubTask;
import moduls.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicStatusCalculationTest {
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void testCalculateEpicStatus_AllNew() throws ManagerSaveException {
        Epic epic = new Epic("Epic", "Description");
        taskManager.addEpic(epic);

        SubTask subTask1 = new SubTask("SubTask 1", "Description 1", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now(), epic.getId());
        SubTask subTask2 = new SubTask("SubTask 2", "Description 2", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(30), epic.getId());

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        taskManager.updateEpic(epic);

        assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    void testCalculateEpicStatus_AllDone() throws ManagerSaveException {
        Epic epic = new Epic("Epic", "Description");
        taskManager.addEpic(epic);

        SubTask subTask1 = new SubTask("SubTask 1", "Description 1", TaskStatus.DONE, Duration.ofMinutes(30), LocalDateTime.now(), epic.getId());
        SubTask subTask2 = new SubTask("SubTask 2", "Description 2", TaskStatus.DONE, Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(30), epic.getId());

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        taskManager.updateEpic(epic);

        assertEquals(TaskStatus.DONE, epic.getStatus());
    }

    @Test
    void testCalculateEpicStatus_NewAndDone() throws ManagerSaveException {
        Epic epic = new Epic("Epic", "Description");
        taskManager.addEpic(epic);

        SubTask subTask1 = new SubTask("SubTask 1", "Description 1", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now(), epic.getId());
        SubTask subTask2 = new SubTask("SubTask 2", "Description 2", TaskStatus.DONE, Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(30), epic.getId());

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        taskManager.updateEpic(epic);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void testCalculateEpicStatus_AllInProgress() throws ManagerSaveException {
        Epic epic = new Epic("Epic", "Description");
        taskManager.addEpic(epic);

        SubTask subTask1 = new SubTask("SubTask 1", "Description 1", TaskStatus.IN_PROGRESS, Duration.ofMinutes(30), LocalDateTime.now(), epic.getId());
        SubTask subTask2 = new SubTask("SubTask 2", "Description 2", TaskStatus.IN_PROGRESS, Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(30), epic.getId());

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        taskManager.updateEpic(epic);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }
}
