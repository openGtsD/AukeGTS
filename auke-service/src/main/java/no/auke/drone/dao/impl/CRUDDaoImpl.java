package no.auke.drone.dao.impl;

import no.auke.drone.dao.CRUDDao;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * Created by huyduong on 4/19/2015.
 */
public class CRUDDaoImpl<T> implements CRUDDao<T> {
    private static final Logger logger = LoggerFactory.getLogger(CRUDDaoImpl.class);

    private Class<T> persistentClass;
    private JdbcTemplate jdbcTemplate;
    private DataSource dataSource;

    public void setPersistentClass(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void init() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private JdbcTemplate getJdbcTemplate() {
        if(jdbcTemplate == null) {
            jdbcTemplate = new JdbcTemplate(dataSource);
        }
        return jdbcTemplate;
    }

    @SuppressWarnings("unchecked")
    protected Class<T> getPersistentClass() {
        if (persistentClass == null) {
            persistentClass = (Class<T>)
                    ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
        return persistentClass;
    }

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
            logger.error(e.getCause().toString());
        }

        return parameters;
    }

    private void setTime(T entity) {
        try {
            BeanUtils.setProperty(entity,"creationDate", new Date());
            BeanUtils.setProperty(entity,"lastUpdateTime", new Date());
        } catch(Exception e) {
            logger.error(e.getCause().toString());
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
    public T read(String field, String value) {
        Class<T> entityClass = getPersistentClass();
        String sql = "SELECT * FROM " + entityClass.getSimpleName() + " WHERE " + field + "  = ?";
        T entity = (T)getJdbcTemplate().queryForObject(
                sql, new Object[]{value},
                new BeanPropertyRowMapper<T>());
        return entity;
    }

    @Override
    public T update(T entity) {
        // TODO
        return null;
    }

    @Override
    public void delete(String field, String value) {
        Class<T> entityClass = getPersistentClass();
        String sql = "SELECT * FROM " + entityClass.getSimpleName() + " WHERE " + field + "  = ?";
        getJdbcTemplate().update(sql);
    }

    @Override
    public List getAll() {
        Class<T> entityClass = getPersistentClass();
        String sql = "SELECT * FROM " + entityClass.getSimpleName();

        List<T> entities  = getJdbcTemplate().query(sql,
                new BeanPropertyRowMapper<T>(persistentClass));

        return entities;
    }
}
