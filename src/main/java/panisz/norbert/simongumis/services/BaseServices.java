package panisz.norbert.simongumis.services;

import java.util.List;

public interface BaseServices<T> {
    List<T> osszes();

    T ment(T t) throws Exception;

    void torol(T t);
}
