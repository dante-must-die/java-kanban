package Manager;

import Moduls.Task;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{
    private ArrayList<Task> history = new ArrayList<>();
    @Override
    public void add(Task task) {
        history.remove(task);
        history.add(task);
        if (history.size() > 10) {
            history.remove(0);
        }
    }
    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
