package Manager;

import Moduls.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager { //класс реализует систему хранения истории
    // собственная реализация связного списка
    private static class Node {
        Task task;
        Node next;
        Node prev;
        Node(Task task) {
            this.task = task;
        }
    }
    private Node head;
    private Node tail;
    private Map<Integer, Node> map = new HashMap<>();
    private List<Task> history = new ArrayList<>();

    //добавление задачи в историю
    @Override
    public void add(Task task) {
        remove(task.getId());  // Удаляем узел, если он уже есть в истории
        Node newNode = new Node(task);
        linkLast(newNode);
        map.put(task.getId(), newNode);
    }

    //метод добавляет новый узел в конец двусвязного списка
    private void linkLast(Node newNode) {
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
    }
    //удаление задачи по id
    @Override
    public void remove(int id) {
        Node node = map.get(id);
        if (node != null) {
            removeNode(node);
            map.remove(id);
        }
    }
    //Метод удаляет узел из списка
    private void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
    }
    //возвращение списка истории
    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        Node current = head;
        while (current != null) {
            history.add(current.task);
            current = current.next;
        }
        return history;
    }
}
