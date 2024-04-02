package Manager;

import Moduls.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    //класс реализует систему хранения истории
    private List<Task> history = new ArrayList<>();

    //добавление задачи в историю
    @Override
    public void add(Task task) {
        history.remove(task);
        history.add(task);
        if (history.size() > 10) {
            history.remove(0);
        }
    }

    //возвращение списка истории
    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
