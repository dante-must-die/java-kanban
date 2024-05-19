package manager;

import moduls.Epic;
import moduls.SubTask;
import moduls.Task;
import moduls.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    //коллекции заданий
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private int currentId = 0;
    private HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public void save() throws ManagerSaveException{
    }

    //реализация функций интерфейса TaskManager
    @Override
    public List<SubTask> getSubTasks() {
        List<SubTask> subList = new ArrayList<>(subTasks.values());
        return subList;
    }

    @Override
    public List<Task> getTasks() {
        List<Task> taskList = new ArrayList<>(tasks.values());
        return taskList;
    }

    @Override
    public List<Epic> getEpics() {
        List<Epic> epicList = new ArrayList<>(epics.values());
        return epicList;
    }

    @Override
    public void addTask(Task task) throws ManagerSaveException {
        int id = generateId();
        task.setId(id);
        tasks.put(id, task);
    }

    @Override
    public void addSubTask(SubTask subTask) throws ManagerSaveException {

        int id = generateId();
        subTask.setId(id);
        subTasks.put(id, subTask);
        Epic epic = epics.get(subTask.getEpicId());
        if (epic != null) {
            epic.addSubTaskId(id);
        }

    }

    @Override
    public void addEpic(Epic epic) throws ManagerSaveException {
        int id = generateId();
        epic.setId(id);
        epics.put(id, epic);
    }

    @Override
    public void printAllTasks() {
        System.out.println("All Tasks:");
        for (Task task : tasks.values()) {
            System.out.println(task);
        }
    }

    @Override
    public void printAllSubTasks() {
        System.out.println("\nAll SubTasks:");
        for (SubTask subTask : subTasks.values()) {
            System.out.println(subTask);
        }
    }

    @Override
    public void printAllEpics() {
        System.out.println("\nAll Epics:");
        for (Epic epic : epics.values()) {
            System.out.println(epic);
        }
    }

    @Override
    public List<SubTask> getSubTaskOfEpic(int epicId) {
        List<SubTask> subTaskOfEpic = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic != null) {
            List<Integer> subTaskId = epic.getSubTaskIds();
            for (Integer id : subTaskId) {
                SubTask subTask = subTasks.get(id);
                if (subTask != null) {
                    subTaskOfEpic.add(subTask);
                }
            }
        }
        return subTaskOfEpic;
    }

    @Override
    public void updateTask(Task task) throws ManagerSaveException {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) throws ManagerSaveException {
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            updateEpicStatus(subTask.getEpicId());
        }
    }

    @Override
    public void updateEpic(Epic epic) throws ManagerSaveException {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic.getId());
        }
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
        subTasks.clear();
        epics.clear();
    }

    @Override
    public void deleteTask(int id) throws ManagerSaveException {
        tasks.remove(id);
    }

    @Override
    public void deleteSubTask(int id) throws ManagerSaveException {
        SubTask removedSubTask = subTasks.remove(id);
        Epic epic = epics.get(removedSubTask.getEpicId());
        epic.getSubTaskIds().remove(Integer.valueOf(id));
        updateEpicStatus(epic.getId());
    }

    @Override
    public void deleteEpic(int id) throws ManagerSaveException {
        Epic removedEpic = epics.remove(id);
        for (Integer subTaskId : new ArrayList<>(removedEpic.getSubTaskIds())) {
            subTasks.remove(subTaskId);
        }
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) historyManager.add(task);
        return task;
    }

    @Override
    public Task getSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        if (subTask != null) historyManager.add(subTask);
        return subTask;
    }

    @Override
    public Task getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) historyManager.add(epic);
        return epic;
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

