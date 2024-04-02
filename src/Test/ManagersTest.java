package Test;

import Manager.Managers;
import Manager.TaskManager;
import Moduls.Epic;
import Moduls.SubTask;
import Moduls.Task;
import Moduls.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    //утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров;
    @Test
    public void getDefaultTaskManager_ShouldReturnInitializedInstance() {
        TaskManager taskManager = Managers.getDefault();

        Task task = new Task("Test Task", "Description", TaskStatus.NEW);
        taskManager.addTask(task);
        assertFalse(taskManager.getTasks().isEmpty(), "TaskManager should be able to add tasks");
    }


}