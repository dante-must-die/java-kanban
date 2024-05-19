package manager;

import moduls.Epic;
import moduls.SubTask;
import moduls.Task;

import java.util.List;

public interface TaskManager {

    void save() throws ManagerSaveException;

    List<SubTask> getSubTasks();

    //получение списка всех задач
    List<Task> getTasks();

    List<Epic> getEpics();

    //добавление задачи
    void addTask(Task task) throws ManagerSaveException;

    void addSubTask(SubTask subTask) throws ManagerSaveException;

    void addEpic(Epic epic) throws ManagerSaveException;

    //вывод всех задач
    void printAllTasks();

    void printAllSubTasks();

    void printAllEpics();

    //получение списка всех подзадач эпика
    List<SubTask> getSubTaskOfEpic(int epicId);

    //обновление задачи
    void updateTask(Task task) throws ManagerSaveException;

    void updateSubTask(SubTask subTask) throws ManagerSaveException;

    void updateEpic(Epic epic) throws ManagerSaveException;

    //удаление задачи
    void deleteAllTasks();

    void deleteTask(int id) throws ManagerSaveException;

    void deleteSubTask(int id) throws ManagerSaveException;

    void deleteEpic(int id) throws ManagerSaveException;

    //получение задачи по Id
    Task getTaskById(int id);

    Task getSubTaskById(int id);

    Task getEpicById(int id);


}
