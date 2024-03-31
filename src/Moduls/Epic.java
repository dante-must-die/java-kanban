package Moduls;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTaskIds = new ArrayList<>();
    public Epic(String title, String description) {
        super(title, description, TaskStatus.NEW);
    }
    public void addSubTaskId(int subTaskId) {
        subTaskIds.add(subTaskId);
    }

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }
    public ArrayList<Integer> getSubtasks() {
        return subTaskIds;
    }
    @Override
    public String toString() {
        return "Moduls.Epic{" +
                "subTaskIds=" + subTaskIds +
                '}';
    }
}
