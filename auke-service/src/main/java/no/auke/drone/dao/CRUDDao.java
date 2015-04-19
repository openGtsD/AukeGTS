package no.auke.drone.dao;

import java.util.List;

/**
 * Created by huyduong on 4/19/2015.
 */
public interface CRUDDao<T> {
    T create(T entity);
    T read(String id);
    T update(T entity);
    void delete(String id);
    List<T> getAll();
}
