

public class Main {
    public static void main(String[] args) {
        /*InMemoryTaskManager manager = new InMemoryTaskManager();

        Epic epic = new Epic("Epic Title", "Epic Description");

        int epicId = epic.getId();

        SubTask subTask = new SubTask("Epic Title", "Epic Description", TaskStatus.NEW, epicId);

        manager.addSubTask(subTask);

        manager.printAllSubTasks();
        manager.printAllEpics();*/

        /*Task task1 = new Task("Moduls.Task 1", "Description 1", TaskStatus.NEW);
        Task task2 = new Task("Moduls.Task 2", "Description 2", TaskStatus.NEW);
        manager.addTask(task1);
        manager.addTask(task2);

        Epic epic1 = new Epic("Moduls.Epic 1", "Moduls.Epic Description 1");
        Epic epic2 = new Epic("Moduls.Epic 2", "Moduls.Epic Description 2");
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        SubTask subTask1 = new SubTask("Moduls.SubTask 1", "Moduls.SubTask Description 1", TaskStatus.NEW, epic1.getId());
        SubTask subTask2 = new SubTask("Moduls.SubTask 2", "Moduls.SubTask Description 2", TaskStatus.NEW, epic1.getId());
        SubTask subTask3 = new SubTask("Moduls.SubTask 3", "Moduls.SubTask Description 3", TaskStatus.NEW, epic2.getId());
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.addSubTask(subTask3);

        manager.printAllTasks();
        manager.printAllSubTasks();
        manager.printAllEpics();


        subTask1.setStatus(TaskStatus.DONE);
        manager.updateSubTask(subTask1);
        subTask2.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubTask(subTask2);
        subTask3.setStatus(TaskStatus.DONE);
        manager.updateSubTask(subTask3);

        System.out.println("\nAfter status updates:");
        manager.printAllTasks();
        manager.printAllSubTasks();
        manager.printAllEpics();

        manager.deleteTask(task1.getId());
        manager.deleteEpic(epic1.getId());

        System.out.println("\nAfter deletion:");
        manager.printAllTasks();
        manager.printAllSubTasks();
        manager.printAllEpics();*/
    }
}
