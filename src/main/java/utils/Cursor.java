package utils;

import java.util.Iterator;
import java.util.List;

/**
 * Created by sulvto on 16-12-24.
 */
public class Cursor<E> implements Iterator {
    protected List<E> list;
    protected int index;

    public Cursor(List<E> list) {
        this(list, 0);
    }

    public Cursor(List<E> list, int index) {
        this.list = list;
        this.index = index;
    }

    @Override
    public Cursor<E> clone() {
        return new Cursor<E>(list, index);
    }

    @Override
    public boolean hasNext() {
        return index < list.size();
    }

    @Override
    public E next() {
        return list.get(index++);
    }

    public E current() {
        if (index == 0) {
            throw new Error("must not happen: Cursor#current");
        }

        return list.get(index - 1);
    }

    @Override
    public void remove() {
        list.remove(index);
    }

    @Override
    public String toString() {
        return "#<Cursor list=" + list + " index=" + index + ">";
    }
}
