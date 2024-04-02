package Test;

import Manager.InMemoryHistoryManager;
import Moduls.Task;
import Moduls.TaskStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
        historyManager.add(task1);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "History should contain exactly two tasks.");
        assertEquals(task2, history.get(0), "Task 2 should be the first in history after re-adding Task 1.");
        assertEquals(task1, history.get(1), "Task 1 should be moved to the end of history after being re-added.");
    }

    @Test
    void historyDoesNotExceedLimit() {
        // Добавляем 11 задач, чтобы проверить лимит истории
        for (int i = 0; i < 11; i++) {
            Task task = new Task("Task " + i, "Description for Task " + i, TaskStatus.NEW);
            task.setId(i);
            historyManager.add(task);
        }

        List<Task> history = historyManager.getHistory();
        assertEquals(10, history.size(), "History should not exceed 10 tasks.");
        assertNull(history.stream().filter(t -> t.getId() == 0).findFirst().orElse(null), "The first added task (id=0) should be removed from history.");
    }
}