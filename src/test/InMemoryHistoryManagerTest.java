package test;

import manager.InMemoryHistoryManager;
import moduls.Task;
import moduls.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private InMemoryHistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    //задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
    @Test
    void addedTasksAreRetainedAndDuplicatedAreRemoved() {
        Task task1 = new Task("Task 1", "Description for Task 1", TaskStatus.NEW);
        Task task2 = new Task("Task 2", "Description for Task 2", TaskStatus.NEW);
        task1.setId(1);
        task2.setId(2);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1); // Повторное добавление Task 1

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "History should contain exactly two tasks.");
        assertSame(task2, history.get(0), "Task 2 should be the first in history after re-adding Task 1.");
        assertSame(task1, history.get(1), "Task 1 should be moved to the end of history after being re-added.");
    }

    //тест на проверку неограниченного размера
    @Test
    void historyGrowsIndefinitely() {
        int numberOfTasks = 100;  // Тестируем добавление большого количества задач
        for (int i = 0; i < numberOfTasks; i++) {
            Task task = new Task("Task " + i, "Description for Task " + i, TaskStatus.NEW);
            task.setId(i);
            historyManager.add(task);
        }

        List<Task> history = historyManager.getHistory();
        assertEquals(numberOfTasks, history.size(), "History should grow indefinitely and contain all added tasks.");
    }
}