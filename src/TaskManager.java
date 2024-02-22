import java.util.ArrayList;
import java.util.HashMap;
public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private int currentId = 0;
    public int generateId() {
        return ++currentId;
    }
    public void addTask(Task task) {
        task.setId(++currentId);
        tasks.put(task.getId(), task);
    }
    public void addSubTask(SubTask subTask) {
        subTask.setId(++currentId);
        tasks.put(subTask.getId(), subTask);
    }
    public void addEpic(Epic epic) {
        epic.setId(++currentId);
        tasks.put(epic.getId(), epic);
    }
    public void printAllTasks() {
        System.out.println("All Tasks:");
        for (Task task : tasks.values()) {
            System.out.println(task);
        }
    }

    public void printAllSubTasks() {
        System.out.println("\nAll SubTasks:");
        for (SubTask subTask : subTasks.values()) {
            System.out.println(subTask);
        }
    }

    public void printAllEpics() {
        System.out.println("\nAll Epics:");
        for (Epic epic : epics.values()) {
            System.out.println(epic);
        }
    }
    public Task getTasks(int id) {
        if(tasks.containsKey(id)) {
            return tasks.get(id);
        } else if(subTasks.containsKey(id)) {
            return subTasks.get(id);
        } else if(epics.containsKey(id)) {
            return epics.get(id);
        } else {
            return null;
        }
    }
    public ArrayList<SubTask> getSubTaskOfEpic(int epicId) {
        ArrayList<SubTask> subTaskOfEpic = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if(epic != null) {
            ArrayList<Integer> subTaskId = epic.getSubTaskIds();
            for(Integer id : subTaskId) {
                SubTask subTask = subTasks.get(id);
                if(subTask != null) {
                    subTaskOfEpic.add(subTask);
                }
            }
        }
        return subTaskOfEpic;
    }
    public void updateTask(Task task) {
        if (task instanceof Epic) {
            epics.put(task.getId(), (Epic) task);
            updateEpicStatus(task.getId());
        } else if (task instanceof SubTask) {
            subTasks.put(task.getId(), (SubTask) task);
            updateEpicStatus(((SubTask) task).getEpicId());
        } else {
            tasks.put(task.getId(), task);
        }
    }

    private void updateEpicStatus(int epicId) {
        if (!epics.containsKey(epicId)) {
            return;
        }
        Epic epic = epics.get(epicId);
        int countNew = 0, countDone = 0;
        ArrayList<SubTask> subtasksList = epic.getSubtasks();

        for (SubTask subtask : subtasksList) {
            if (subtask.getStatus() == TaskStatus.NEW) {
                countNew++;
            } else if (subtask.getStatus() == TaskStatus.DONE) {
                countDone++;
            }
        }

        if (countNew == subtasksList.size()) {
            epic.setStatus(TaskStatus.NEW);
        } else if (countDone == subtasksList.size()) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }
    public void deleteAllTasks() {
        tasks.clear();
        subTasks.clear();
        epics.clear();
    }
    public void deleteTask(int id) {
        if(tasks.containsKey(id)) {
            tasks.remove(id);
        } else if(subTasks.containsKey(id)) {

        }
    }
    public Task getTaskById(int id) {
        if(tasks.containsKey(id)) {
            return tasks.get(id);
        } else if(subTasks.containsKey(id)) {
            return subTasks.get(id);
        } else {
            return epics.get(id);
        }
    }
}

