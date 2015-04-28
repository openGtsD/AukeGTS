package no.auke.drone.dao.impl;

import no.auke.drone.dao.CRUDDao;
import no.auke.drone.dao.QueryBuilder;
import no.auke.drone.domain.ID;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

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

    private List<String> getIdFields() {
        List<String> ids = new ArrayList<>();
        Field[] fields = getPersistentClass().getDeclaredFields();
        for(Field field : fields) {
            if(field.isAnnotationPresent(ID.class)) {
                ids.add(field.getName());
            }
        }
        return ids;
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

    Properties prepareUpdateParameter(T entity) {
        Properties properties = new Properties();
        String[] fields = getFieldNames(entity,"");
        try {
            for(String field : fields) {
                properties.put(field, BeanUtils.getProperty(entity,field));
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }

        return properties;
    }

    private String prepareIdentificationQuery(T entity) {
        Properties properties = new Properties();
        try{
            for(String field : getIdFields()) {
                properties.put(field,BeanUtils.getProperty(entity,field));
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }

        return prepareEqualAndClause(properties);
    }

    private String prepareUpdateQuery(T entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append(entity.getClass().getSimpleName());
        sb.append(" SET ");
        Properties properties = prepareUpdateParameter(entity);
        sb.append(prepareEqualAndClause(properties));
        sb.append(" WHERE ");
        sb.append(prepareIdentificationQuery(entity));
        return sb.toString();
    }

    @Override
    public T update(T entity) {
        String sql = prepareUpdateQuery(entity);
        Object[] parameters = prepareParameter(entity);
        logger.info("processing sql " + sql + " " + parameters);
        getJdbcTemplate().update(sql);

        return entity;
    }

    @Override
    public void delete(T entity) {
        Class<T> entityClass = getPersistentClass();
        String sql = "DELETE FROM " + entityClass.getSimpleName() + " WHERE " + prepareIdentificationQuery(entity);
        getJdbcTemplate().update(sql);
    }

    @Override
    public List getAll() {
        String query = new QueryBuilder().buildSelect(getPersistentClass().getName())
                .build();

        List<T> entities  = getJdbcTemplate().query(query,
                new BeanPropertyRowMapper<T>(persistentClass));

        return entities;
    }

    @Override
    public List get(String query) {
        List<T> entities  = getJdbcTemplate().query(query,
                new BeanPropertyRowMapper<T>(persistentClass));

        return entities;
    }

    private String prepareEqualAndClause(Properties properties) {
        StringBuilder sb = new StringBuilder();
        if(properties.size() > 0) {
            List<String> strings = new ArrayList<>();
            for(Object o : properties.keySet()) {
                strings.add(o.toString() + " = '" + properties.get(o).toString() + "'");
            }
            sb.append(StringUtils.join(strings.toArray()," AND "));
        }
        return sb.toString();
    }

    @Override
    public List getByProperties(Properties properties) {
        if(properties == null || properties.size() == 0) {
            return getAll();
        }

        String query = new QueryBuilder().buildSelect(getPersistentClass().getName())
                                                      .buildWhere()
                                                      .buildEqualClause(properties)
                                                      .build();

        List<T> entities  = getJdbcTemplate().query(query,
                new BeanPropertyRowMapper<T>(persistentClass));

        return entities;
    }


}
