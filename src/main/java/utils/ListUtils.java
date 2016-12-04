package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by sulvto on 16-12-4.
 */
public abstract class ListUtils {
    public static <T> List<T> reverse(List<T> list) {
        List<T> result = new ArrayList<>(list.size());

        ListIterator<T> it = list.listIterator(list.size());
        while (it.hasPrevious()) {
            result.add(it.previous());
        }
        return result;
    }
}
