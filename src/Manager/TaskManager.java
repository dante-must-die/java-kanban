package Manager;

import Moduls.Epic;
import Moduls.SubTask;
import Moduls.Task;
import Moduls.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private int currentId = 0;

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public void addTask(Task task) {
        int id = generateId();
        task.setId(id);
        tasks.put(id, task);
    }
    public void addSubTask(SubTask subTask) {
        int id = generateId();
        subTask.setId(id);
        subTasks.put(id, subTask);
        Epic epic = epics.get(subTask.getEpicId());
        if (epic != null) {
            epic.addSubTaskId(id);
        }

    }
    public void addEpic(Epic epic) {
        int id = generateId();
        epic.setId(id);
        epics.put(id, epic);
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
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            updateEpicStatus(subTask.getEpicId());
        }
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic.getId());
        }
    }
    public void deleteAllTasks() {
        tasks.clear();
        subTasks.clear();
        epics.clear();
    }
    public void deleteTask(int id) {
        tasks.remove(id);
    }
    public void deleteSubTask(int id) {
        SubTask removedSubTask = subTasks.remove(id);
        Epic epic = epics.get(removedSubTask.getEpicId());
        epic.getSubTaskIds().remove(Integer.valueOf(id));
        updateEpicStatus(epic.getId());
    }
    public void deleteEpic(int id) {
        Epic removedEpic = epics.remove(id);
        for (Integer subTaskId : new ArrayList<>(removedEpic.getSubTaskIds())) {
            subTasks.remove(subTaskId);
        }
    }
    public Task getTaskById(int id) {
        return tasks.get(id);
    }
    public Task getSubTaskById(int id) {
        return subTasks.get(id);
    }
    public Task getEpicById(int id) {
        return epics.get(id);
    }
    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return;

        int total = epic.getSubTaskIds().size();
        int done = 0;
        int inProgress = 0;

        for (Integer subTaskId : epic.getSubTaskIds()) {
            SubTask subTask = subTasks.get(subTaskId);
            if (subTask != null && subTask.getStatus() == TaskStatus.DONE) {
                done++;
            } else if (subTask != null && subTask.getStatus() == TaskStatus.IN_PROGRESS) {
                inProgress++;
            }
        }

        if (done == total) {
            epic.setStatus(TaskStatus.DONE);
        } else if (inProgress > 0 || (total > done && done > 0)) {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        } else {
            epic.setStatus(TaskStatus.NEW);
        }
    }
    private int generateId() {
        return ++currentId;
    }
}

