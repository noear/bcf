package webapp.utils.ext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

/**
 * Date: 2018/12/3
 * Time: 16:47
 * Author: Yukai
 * Description chained list, for a better usage
 **/
public class ChainedList<E> extends ArrayList<E> {

    public ChainedList<E> chainedAdd(E e) {
        this.add(e);
        return this;
    }

    public ChainedList<E> chainedAdd(int index, E e) {
        this.add(index, e);
        return this;
    }

    public ChainedList<E> chainedRemove(E e) {
        this.remove(e);
        return this;
    }

    public ChainedList<E> chainedRemove(int index) {
        this.remove(index);
        return this;
    }

    public ChainedList<E> chainedRemoveIf(Predicate<E> predicate) {
        this.removeIf(predicate);
        return this;
    }

    public ChainedList<E> chainedRemoveRange(int from, int to) {
        this.removeRange(from, to);
        return this;
    }

    public ChainedList<E> chainedAddAll(Collection<E> collection) {
        this.addAll(collection);
        return this;
    }
}
