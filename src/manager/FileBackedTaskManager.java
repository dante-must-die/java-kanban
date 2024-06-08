package manager;

import moduls.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File fileToSave;

    private FileBackedTaskManager(File fileToSave) {
        this.fileToSave = fileToSave;
    }

    public static FileBackedTaskManager createClass(File fileToSave) {
        return new FileBackedTaskManager(fileToSave);
    }

    // метод для загрузки из файла
    public static FileBackedTaskManager loadFromFile(File file) throws ManagerSaveException {
        FileBackedTaskManager manager = createClass(file);
        try {
            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            for (String line : lines.subList(1, lines.size())) {
                Task task = taskFromString(line);
                if (task instanceof Epic) {
                    manager.addEpic((Epic) task);
                } else if (task instanceof SubTask) {
                    manager.addSubTask((SubTask) task);
                } else {
                    manager.addTask(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
        return manager;
    }

    // метод для преобразования string в task
    private static Task taskFromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        TaskType type = TaskType.valueOf(fields[1]);
        String title = fields[2];
        TaskStatus status = TaskStatus.valueOf(fields[3]);
        String description = fields[4];
        Duration duration = fields[5].equals("null") ? null : Duration.ofMinutes(Long.parseLong(fields[5]));
        LocalDateTime startTime = fields[6].equals("null") ? null : LocalDateTime.parse(fields[6]);

        switch (type) {
            case TASK:
                return new Task(title, description, status, duration, startTime);
            case EPIC:
                return new Epic(title, description);
            case SUBTASK:
                int epicId = Integer.parseInt(fields[7]);
                return new SubTask(title, description, status, duration, startTime, epicId);
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи");
        }
    }

    // переопределение методов интерфейса
    @Override
    public void addTask(Task task) throws ManagerSaveException {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask) throws ManagerSaveException {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public void addEpic(Epic epic) throws ManagerSaveException {
        super.addEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) throws ManagerSaveException {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) throws ManagerSaveException {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) throws ManagerSaveException {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteTask(int id) throws ManagerSaveException {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteSubTask(int id) throws ManagerSaveException {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) throws ManagerSaveException {
        super.deleteEpic(id);
        save();
    }

    public void savePublic() throws ManagerSaveException {
        save();
    }

    // метод для сохранения данных
    private void save() throws ManagerSaveException {
        try (FileWriter writer = new FileWriter(fileToSave, StandardCharsets.UTF_8)) {
            writer.write("id,type,name,status,description,duration,startTime,epic\n");

            for (Task task : getTasks()) {
                writer.write(taskToString(task) + "\n");
            }

            for (Epic epic : getEpics()) {
                writer.write(taskToString(epic) + "\n");
            }

            for (SubTask subTask : getSubTasks()) {
                writer.write(taskToString(subTask) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    // метод для перевода task в string
    private String taskToString(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getId()).append(",");
        if (task instanceof SubTask) {
            sb.append(TaskType.SUBTASK).append(",");
        } else if (task instanceof Epic) {
            sb.append(TaskType.EPIC).append(",");
        } else {
            sb.append(TaskType.TASK).append(",");
        }
        sb.append(task.getTitle()).append(",");
        sb.append(task.getStatus()).append(",");
        sb.append(task.getDescription()).append(",");
        sb.append(task.getDuration() == null ? "null" : task.getDuration().toMinutes()).append(",");
        sb.append(task.getStartTime() == null ? "null" : task.getStartTime().toString()).append(",");
        if (task instanceof SubTask) {
            sb.append(((SubTask) task).getEpicId());
        }
        return sb.toString();
    }
}
