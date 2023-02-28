package manager.history;

import tasks.Task;
import java.util.List;

public final class LinkedList {
    private Node head;
    private Node tail;
    private int size;

    public LinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public Node LinkLast(Task elem) {
        final Node last = tail;
        final Node newNode = new Node(elem);
        tail = newNode;
        if (last == null) {
            head = newNode;
        } else {
            last.next = newNode;
            newNode.prev = last;
        }
        size++;
        return newNode;
    }

    public List<Task> getTasks() {
        List<Task> resulted = new java.util.LinkedList<>();
        Node currentElement = head;
        while (currentElement != null) {
            resulted.add(currentElement.elem);
            currentElement = currentElement.next;
        }
        return resulted;
    }

    /*
    В параметр принимает Node, а не id, т.к. это прописано в задании:
    "Добавьте метод removeNode в класс.
    В качестве параметра этот метод должен принимать объект Node — узел связного списка и вырезать его."
     */
    public void removeNode(Node element) {
        if (element.equals(head)) {
            head = element.next;
            element.next.prev = null;
        } else if (element.equals(tail)) {
            tail = element.prev;
            element.prev.next = null;
        } else {
            Node nextElement = element.next;
            element.prev.next = nextElement;
            nextElement.prev = element.prev;
        }
        size--;
    }

    public int size() {
        return size;
    }

    final static class Node {
        private Node prev;
        private Node next;
        private final Task elem;

        Node(Task elem) {
            this.prev = null;
            this.next = null;
            this.elem = elem;
        }
    }
}
