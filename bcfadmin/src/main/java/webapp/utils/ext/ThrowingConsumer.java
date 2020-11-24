package webapp.utils.ext;

/**
 * @Created by: Yukai
 * @Date: 2019/2/13 10:04
 * @Description : Yukai is so handsome xxD
 */
@FunctionalInterface
public interface ThrowingConsumer<T, E extends Exception> {
    void accept(T t) throws E;
}
