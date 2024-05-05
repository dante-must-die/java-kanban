package moduls;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {  //модульный класс типа задания
    private List<Integer> subTaskIds = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description, TaskStatus.NEW);
    }

    //добавления сабтаска по id
    public void addSubTaskId(int subTaskId) {
        subTaskIds.add(subTaskId);
    }

    //получение списка id сабтасков
    public List<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    //получение списка сабтасков
    public List<Integer> getSubtasks() {
        return subTaskIds;
    }

    @Override
    public String toString() {
        return "Moduls.Epic{" +
                "subTaskIds=" + subTaskIds +
                '}';
    }
}
