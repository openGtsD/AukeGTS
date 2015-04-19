package no.auke.drone.dao.impl;

import no.auke.drone.dao.CRUDDao;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by huyduong on 4/19/2015.
 */
public class CRUDDaoImpl<T> extends JdbcDaoSupport implements CRUDDao<T> {
    private String[] getFieldNames(T entity, String prefix) {
        Field[] fields = entity.getClass().getDeclaredFields();
        String[] str = new String[fields.length];
        for(int i = 0; i < fields.length; i++) {
            str[i] = prefix + fields[i].getName();
        }
        return str;
    }

    private String prepareInsertQuery(T entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(entity.getClass().getSimpleName());
        sb.append(" (");
        Field[] fields = entity.getClass().getDeclaredFields();
        sb.append(StringUtils.join(getFieldNames(entity,""),","));
        sb.append(") ");
        sb.append("VALUES (");
        String[] str = new String[fields.length];
        Arrays.fill(str,"?");
        sb.append(StringUtils.join(str, ", "));
        sb.append(") ");

        return sb.toString();
    }

    private Object[] prepareParameter(T entity) {
        Object[] parameters = null;
        try {
            Field[] fields = entity.getClass().getDeclaredFields();
            parameters = new Object[fields.length];
            for(int i = 0; i < fields.length; i++) {
                parameters[i] = BeanUtils.getProperty(entity,fields[i].getName());
            }
        } catch (Exception e) {
            logger.error(e,e.getCause());
        }

        return parameters;
    }

    private void setTime(T entity) {
        try {
            BeanUtils.setProperty(entity,"creationDate", new Date());
            BeanUtils.setProperty(entity,"lastUpdateTime", new Date());
        } catch(Exception e) {
            logger.error(e,e.getCause());
        }
    }

    @Override
    public T create(T entity) {
        String sql = prepareInsertQuery(entity);
        Object[] parameters = prepareParameter(entity);
        logger.info("processing sql " + sql + " " + parameters);
        getJdbcTemplate().update(sql, parameters);

//        setTime(entity);
        return entity;
    }

    @Override
    public T read(String id) {
        // TODO
        return null;
    }

    @Override
    public T update(T entity) {
        // TODO
        return null;
    }

    @Override
    public void delete(String id) {
        // TODO
    }

    @Override
    public List getAll() {
        // TODO
        return null;
    }
}
