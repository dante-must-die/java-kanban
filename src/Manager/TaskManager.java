package Manager;

import Moduls.Epic;
import Moduls.SubTask;
import Moduls.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    List<SubTask> getSubTasks();
    //получение списка всех задач
    List<Task> getTasks();

    List<Epic> getEpics();
    //добавление задачи
    void addTask(Task task);

    void addSubTask(SubTask subTask);

    void addEpic(Epic epic);
    //вывод всех задач
    void printAllTasks();

    void printAllSubTasks();

    void printAllEpics();
    //получение списка всех подзадач эпика
    List<SubTask> getSubTaskOfEpic(int epicId);
    //обновление задачи
    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);
    //удаление задачи
    void deleteAllTasks();

    void deleteTask(int id);

    void deleteSubTask(int id);

    void deleteEpic(int id);
    //получение задачи по Id
    Task getTaskById(int id);

    Task getSubTaskById(int id);

    Task getEpicById(int id);


}
