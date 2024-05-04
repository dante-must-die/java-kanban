package Manager;

import Moduls.Task;

import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {
    //добавление задачи
    void add(Task task);

    //удаление задачи по id
    void remove(int id);

    //получение списка истории
    List<Task> getHistory();
}
