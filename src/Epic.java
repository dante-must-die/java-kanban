import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList<Integer> subTaskIds = new ArrayList<>();
    private ArrayList<SubTask> subTasks;
    public Epic(int id, String title, String description) {
        super(id, title, description, TaskStatus.NEW);
    }
    public void addSubTaskId(int subTaskId) {
        subTaskIds.add(subTaskId);
    }

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }
    public ArrayList<SubTask> getSubtasks() {
        return new ArrayList<>(subTasks); // Возвращаем копию списка, чтобы избежать изменений снаружи
    }
    @Override
    public String toString() {
        return "Epic{" +
                "subTaskIds=" + subTaskIds +
                '}';
    }
}
