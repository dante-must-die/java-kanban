package manager;

import moduls.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private int currentId = 0;
    private HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void addTask(Task task) throws ManagerSaveException {
        if (isOverlapping(task)) {
            throw new ManagerSaveException();
        }
        int id = generateId();
        task.setId(id);
        tasks.put(id, task);
    }

    @Override
    public void addSubTask(SubTask subTask) throws ManagerSaveException {
        if (isOverlapping(subTask)) {
            throw new ManagerSaveException();
        }
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
        return epics.get(epicId)
                .getSubTaskIds()
                .stream()
                .map(subTasks::get)
                .collect(Collectors.toList());
    }

    @Override
    public void updateTask(Task task) throws ManagerSaveException {
        if (tasks.containsKey(task.getId()) && !isOverlapping(task)) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) throws ManagerSaveException {
        if (subTasks.containsKey(subTask.getId()) && !isOverlapping(subTask)) {
            subTasks.put(subTask.getId(), subTask);
            Epic epic = epics.get(subTask.getEpicId());
            if (epic != null) {
                updateEpicStatus(epic.getId());
            }
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

    @Override
    public void savePublic() throws ManagerSaveException {

    }

    public List<Task> getPrioritizedTasks() {
        return tasks.values().stream()
                .sorted((t1, t2) -> {
                    LocalDateTime t1Start = t1.getStartTime();
                    LocalDateTime t2Start = t2.getStartTime();
                    if (t1Start == null && t2Start == null) {
                        return 0;
                    }
                    if (t1Start == null) {
                        return 1;
                    }
                    if (t2Start == null) {
                        return -1;
                    }
                    return t1Start.compareTo(t2Start);
                })
                .collect(Collectors.toList());
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

    private boolean isOverlapping(Task newTask) {
        LocalDateTime newTaskStart = newTask.getStartTime();
        LocalDateTime newTaskEnd = newTask.getEndTime();

        if (newTaskStart == null || newTaskEnd == null) {
            return false;
        }

        return tasks.values().stream()
                .anyMatch(existingTask -> {
                    LocalDateTime existingStart = existingTask.getStartTime();
                    LocalDateTime existingEnd = existingTask.getEndTime();
                    return existingStart != null && existingEnd != null &&
                            newTaskStart.isBefore(existingEnd) && newTaskEnd.isAfter(existingStart);
                });
    }


}
