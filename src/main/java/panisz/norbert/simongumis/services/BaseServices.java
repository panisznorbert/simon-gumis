package panisz.norbert.simongumis.services;

import panisz.norbert.simongumis.exceptions.LetezoGumiException;

import java.util.List;

public interface BaseServices<T> {
    List<T> osszes();

    T ment(T t) throws LetezoGumiException;

    void torol(T t);
}
