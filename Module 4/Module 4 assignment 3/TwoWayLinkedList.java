import java.util.*;

public class TwoWayLinkedList<E> implements MyList<E> {
  private static class Node<E> {
    E element;
    Node<E> next;
    Node<E> previous;

    public Node(E e) {
      element = e;
    }
  }

  private Node<E> head, tail;
  private int size = 0;

  public TwoWayLinkedList() {
  }

  public TwoWayLinkedList(E[] objects) {
    for (E e : objects) {
      add(e);
    }
  }

  public void add(int index, E e) {
    if (index == 0) {
      addFirst(e);
    } else if (index >= size) {
      addLast(e);
    } else {
      Node<E> current = head;
      for (int i = 1; i < index; i++) {
        current = current.next;
      }
      Node<E> temp = current.next;
      current.next = new Node<>(e);
      current.next.previous = current;
      current.next.next = temp;
      if (temp != null) {
        temp.previous = current.next;
      }
      size++;
    }
  }

  public void addFirst(E e) {
    Node<E> newNode = new Node<>(e);
    newNode.next = head;
    if (head != null) {
      head.previous = newNode;
    }
    head = newNode;
    if (tail == null) {
      tail = head;
    }
    size++;
  }

  public void addLast(E e) {
    Node<E> newNode = new Node<>(e);
    if (tail == null) {
      head = tail = newNode;
    } else {
      tail.next = newNode;
      newNode.previous = tail;
      tail = newNode;
    }
    size++;
  }

  public E get(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException();
    }
    Node<E> current = head;
    for (int i = 0; i < index; i++) {
      current = current.next;
    }
    return current.element;
  }

  public int indexOf(Object e) {
    Node<E> current = head;
    for (int i = 0; i < size; i++) {
      if (current.element.equals(e)) {
        return i;
      }
      current = current.next;
    }
    return -1;
  }

  public int lastIndexOf(E e) {
    Node<E> current = tail;
    for (int i = size - 1; i >= 0; i--) {
      if (current.element.equals(e)) {
        return i;
      }
      current = current.previous;
    }
    return -1;
  }

  public E remove(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException();
    }
    E removed;
    if (index == 0) {
      removed = head.element;
      head = head.next;
      if (head != null) {
        head.previous = null;
      } else {
        tail = null;
      }
    } else if (index == size - 1) {
      removed = tail.element;
      tail = tail.previous;
      if (tail != null) {
        tail.next = null;
      } else {
        head = null;
      }
    } else {
      Node<E> current = head;
      for (int i = 1; i < index; i++) {
        current = current.next;
      }
      removed = current.next.element;
      current.next = current.next.next;
      current.next.previous = current;
    }
    size--;
    return removed;
  }

  public E set(int index, E e) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException();
    }
    Node<E> current = head;
    for (int i = 0; i < index; i++) {
      current = current.next;
    }
    E old = current.element;
    current.element = e;
    return old;
  }

  public int size() {
    return size;
  }

  public boolean contains(Object e) {
    return indexOf(e) != -1;
  }

  public boolean add(E e) {
    addLast(e);
    return true;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public boolean remove(Object e) {
    int index = indexOf(e);
    if (index != -1) {
      remove(index);
      return true;
    }
    return false;
  }

  public void clear() {
    head = tail = null;
    size = 0;
  }

  public Iterator<E> iterator() {
    return new LinkedListIterator();
  }

  public ListIterator<E> listIterator() {
    return new MyListIterator(0);
  }

  public ListIterator<E> listIterator(int index) {
    return new MyListIterator(index);
  }

  private class LinkedListIterator implements Iterator<E> {
    private Node<E> current = head;

    public boolean hasNext() {
      return current != null;
    }

    public E next() {
      E e = current.element;
      current = current.next;
      return e;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  private class MyListIterator implements ListIterator<E> {
    private Node<E> current = head;
    private int index = 0;
    private Node<E> lastReturned = null;

    public MyListIterator(int index) {
      if (index < 0 || index > size) {
        throw new IndexOutOfBoundsException();
      }
      this.index = index;
      current = head;
      for (int i = 0; i < index; i++) {
        current = current.next;
      }
    }

    public boolean hasNext() {
      return index < size;
    }

    public E next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      lastReturned = current;
      E e = current.element;
      current = current.next;
      index++;
      return e;
    }

    public boolean hasPrevious() {
      return index > 0;
    }

    public E previous() {
      if (!hasPrevious()) {
        throw new NoSuchElementException();
      }
      if (current == null) {
        current = tail;
      } else {
        current = current.previous;
      }
      lastReturned = current;
      index--;
      return current.element;
    }

    public int nextIndex() {
      return index;
    }

    public int previousIndex() {
      return index - 1;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }

    public void set(E e) {
      if (lastReturned == null) {
        throw new IllegalStateException();
      }
      lastReturned.element = e;
    }

    public void add(E e) {
      throw new UnsupportedOperationException();
    }
  }

  public String toString() {
    StringBuilder sb = new StringBuilder("[");
    Node<E> current = head;
    for (int i = 0; i < size; i++) {
      sb.append(current.element);
      if (i < size - 1) {
        sb.append(", ");
      }
      current = current.next;
    }
    sb.append("]");
    return sb.toString();
  }
}
