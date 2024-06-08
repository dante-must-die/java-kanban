package test;

import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import moduls.Task;
import moduls.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void testEmptyHistory() {
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty(), "History should be empty initially.");
    }

    @Test
    void testAddTaskToHistory() {
        Task task = new Task("Task 1", "Description", TaskStatus.NEW);
        task.setId(1);
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "History should contain one task.");
        assertEquals(task, history.get(0), "History should contain the added task.");
    }

    @Test
    void testNoDuplicateInHistory() {
        Task task = new Task("Task 1", "Description", TaskStatus.NEW);
        task.setId(1);
        historyManager.add(task);
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "History should not contain duplicates.");
    }

    @Test
    void testRemoveTaskFromHistory() {
        Task task1 = new Task("Task 1", "Description", TaskStatus.NEW);
        Task task2 = new Task("Task 2", "Description", TaskStatus.NEW);
        Task task3 = new Task("Task 3", "Description", TaskStatus.NEW);
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task2.getId());

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "History should contain two tasks after removal.");
        assertFalse(history.contains(task2), "History should not contain the removed task.");
    }

    @Test
    void testRemoveFirstTaskFromHistory() {
        Task task1 = new Task("Task 1", "Description", TaskStatus.NEW);
        Task task2 = new Task("Task 2", "Description", TaskStatus.NEW);
        Task task3 = new Task("Task 3", "Description", TaskStatus.NEW);
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task1.getId());

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "History should contain two tasks after removal of the first task.");
        assertFalse(history.contains(task1), "History should not contain the removed first task.");
    }

    @Test
    void testRemoveLastTaskFromHistory() {
        Task task1 = new Task("Task 1", "Description", TaskStatus.NEW);
        Task task2 = new Task("Task 2", "Description", TaskStatus.NEW);
        Task task3 = new Task("Task 3", "Description", TaskStatus.NEW);
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task3.getId());

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "History should contain two tasks after removal of the last task.");
        assertFalse(history.contains(task3), "History should not contain the removed last task.");
    }
}
