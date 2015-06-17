package no.auke.drone.dao;

import java.util.List;
import java.util.Properties;

/**
 * Created by huyduong on 4/19/2015.
 */
public interface CRUDDao<T> {
    T create(T entity);
    T read(String field, String value);
    T update(T entity);
    void delete(T entity);
    void deleteAll();
    void deleteAllByProperties(Properties properties);
    List<T> getAll();
    List<T> getTop(int top);
    List get(String query);
    T getById(String id);
    void setPersistentClass(Class<T> persistentClass);
    List getByProperties(Properties properties);
}
