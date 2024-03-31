package Manager;

import Moduls.Epic;
import Moduls.SubTask;
import Moduls.Task;

import java.util.ArrayList;

public interface TaskManager {
    ArrayList<SubTask> getSubTasks();

    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    void addTask(Task task);

    void addSubTask(SubTask subTask);

    void addEpic(Epic epic);

    void printAllTasks();

    void printAllSubTasks();

    void printAllEpics();

    ArrayList<SubTask> getSubTaskOfEpic(int epicId);

    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);

    void deleteAllTasks();

    void deleteTask(int id);

    void deleteSubTask(int id);

    void deleteEpic(int id);

    Task getTaskById(int id);

    Task getSubTaskById(int id);

    Task getEpicById(int id);

    void updateEpicStatus(int epicId);

}
