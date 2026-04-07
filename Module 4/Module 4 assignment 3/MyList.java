import java.util.*;

public interface MyList<E> {
  public void add(int index, E e);
  public E get(int index);
  public int indexOf(Object e);
  public int lastIndexOf(E e);
  public E remove(int index);
  public E set(int index, E e);
  public int size();
  public boolean contains(Object e);
  public boolean add(E e);
  public boolean isEmpty();
  public boolean remove(Object e);
  public void clear();
  public java.util.Iterator<E> iterator();
}