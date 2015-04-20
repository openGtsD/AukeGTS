package no.auke.drone.dao;

import java.util.List;

/**
 * Created by huyduong on 4/19/2015.
 */
public interface CRUDDao<T> {
    T create(T entity);
    T read(String field, String value);
    T update(T entity);
    void delete(String field, String value);
    List<T> getAll();
    void setPersistentClass(Class<T> persistentClass);
}
