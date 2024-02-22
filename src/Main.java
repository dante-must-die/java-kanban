
public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        Task task1 = new Task(manager.generateId(), "Task 1", "Description 1", TaskStatus.NEW);
        Task task2 = new Task(manager.generateId(), "Task 2", "Description 2", TaskStatus.NEW);
        manager.addTask(task1);
        manager.addTask(task2);

        Epic epic1 = new Epic(manager.generateId(), "Epic 1", "Epic Description 1");
        Epic epic2 = new Epic(manager.generateId(), "Epic 2", "Epic Description 2");
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        SubTask subTask1 = new SubTask(manager.generateId(), "SubTask 1", "SubTask Description 1", TaskStatus.NEW, epic1.getId());
        SubTask subTask2 = new SubTask(manager.generateId(), "SubTask 2", "SubTask Description 2", TaskStatus.NEW, epic1.getId());
        SubTask subTask3 = new SubTask(manager.generateId(), "SubTask 3", "SubTask Description 3", TaskStatus.NEW, epic2.getId());
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.addSubTask(subTask3);

        manager.printAllTasks();
        manager.printAllSubTasks();
        manager.printAllEpics();

        subTask1.setStatus(TaskStatus.DONE);
        manager.updateTask(subTask1);
        subTask2.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateTask(subTask2);
        subTask3.setStatus(TaskStatus.DONE);
        manager.updateTask(subTask3);

        System.out.println("\nAfter status updates:");
        manager.printAllTasks();
        manager.printAllSubTasks();
        manager.printAllEpics();

        manager.deleteTask(task1.getId());
        manager.deleteTask(epic1.getId());

        System.out.println("\nAfter deletion:");
        manager.printAllTasks();
        manager.printAllSubTasks();
        manager.printAllEpics();
    }
}