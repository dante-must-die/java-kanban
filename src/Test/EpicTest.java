package Test;

import Manager.InMemoryTaskManager;
import Manager.Managers;
import Manager.TaskManager;
import Moduls.Epic;
import Moduls.SubTask;
import Moduls.TaskStatus;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }

    //объект Epic нельзя добавить в самого себя в виде подзадачи;
    @Test
    void epicCannotBeSubtaskOfItself() {
        Epic epic = new Epic("Epic Title", "Epic Description");
        taskManager.addEpic(epic);
        int epicId = epic.getId();

        SubTask subTask = new SubTask("Epic SubTask", "Trying to add Epic as its own subtask.", TaskStatus.NEW, epicId);
        taskManager.addSubTask(subTask);
        assertTrue(taskManager.getSubTasks().isEmpty() || !taskManager.getSubTasks().contains(subTask), "The system should not contain the invalid subtask.");
    }

    //объект Subtask нельзя сделать своим же эпиком
    @Test
    void subTaskCannotBeItsOwnEpic() {

        Epic epic = new Epic("Epic for SubTask", "Description");
        taskManager.addEpic(epic);
        int epicId = epic.getId();

        SubTask subTask = new SubTask("SubTask Title", "SubTask Description", TaskStatus.NEW, epicId);
        taskManager.addSubTask(subTask);

        SubTask retrievedSubTask = (SubTask) taskManager.getSubTaskById(subTask.getId());
        assertNotEquals(retrievedSubTask.getEpicId(), retrievedSubTask.getId(), "A subtask should not be its own epic.");
    }
}