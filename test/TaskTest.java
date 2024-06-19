package test;

import moduls.Epic;
import moduls.SubTask;
import moduls.Task;
import moduls.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskTest {
    @Test
    void tasksWithSameIdShouldBeEqual() {
        Task task1 = new Task("Task 1", "Description for Task 1", TaskStatus.NEW);
        Task task2 = new Task("Task 1", "Description for Task 1", TaskStatus.NEW);
        task1.setId(1);
        task2.setId(1);
        Assertions.assertEquals(task1, task2, "Tasks with the same ID should be equal.");
    }

    @Test
    void taskDescendantsWithSameIdShouldBeConsideredEqual() {
        Epic epic = new Epic("Epic", "Description");
        SubTask subTask = new SubTask("SubTask", "Description", TaskStatus.NEW, 2);

        epic.setId(1);
        subTask.setId(1);

        Assertions.assertTrue(epic.getId() == subTask.getId(), "Epic and SubTask with the same ID should be considered equal.");
    }
}
