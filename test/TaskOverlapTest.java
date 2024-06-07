package test;

import manager.TaskManager;
import manager.InMemoryTaskManager;
import manager.ManagerSaveException;
import moduls.Task;
import moduls.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TaskOverlapTest {
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void testTaskOverlap() throws ManagerSaveException {
        Task task1 = new Task("Task 1", "Description 1", TaskStatus.NEW, Duration.ofMinutes(60), LocalDateTime.now());
        taskManager.addTask(task1);

        Task overlappingTask = new Task("Task 2", "Description 2", TaskStatus.NEW, Duration.ofMinutes(60), task1.getStartTime().plusMinutes(30));
        assertThrows(ManagerSaveException.class, () -> taskManager.addTask(overlappingTask), "Overlapping task should throw exception");

        Task nonOverlappingTask = new Task("Task 3", "Description 3", TaskStatus.NEW, Duration.ofMinutes(60), task1.getStartTime().plusMinutes(60));
        assertDoesNotThrow(() -> taskManager.addTask(nonOverlappingTask), "Non-overlapping task should not throw exception");
    }
}
