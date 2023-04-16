package com.example.Hibernate.config;

import com.example.Hibernate.annotation.FieldAnnotation;
import com.example.Hibernate.annotation.TableAnnotation;
import com.example.Hibernate.utils.FieldInfo;
import com.example.Hibernate.utils.FieldTypes;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;


public class CrudRepository<T, ID> implements CrudOperations<T, ID> {
    @Autowired
    JdbcTemplate jdbcTemplate;


    //SELECT user_id, user_name, email, password FROM users
    @Override
    public List<T> findAll(Class T) {
        ArrayList<T> returnedList = new ArrayList<>();
        String tableName = getTableName(T);
        ArrayList<FieldInfo> tableFields = getTableFields(T);
        List<String> fields = tableFields.stream().map(fl -> fl.dbFieldName).collect(Collectors.toList());
        String fieldsName = StringUtils.join(fields, ',');
        String sql = "SELECT " + fieldsName + " FROM " + tableName;
        jdbcTemplate.query(sql, new ResultSetExtractor<List<T>>() {
            @Override
            public List<T> extractData(ResultSet rs) throws SQLException, DataAccessException {
                while (rs.next()) {
                    try {
                        T row = (T) T.newInstance(); // creating instance of target class ===> User u = new User();
                        for (int i = 0; i < tableFields.size(); i++) {
                            FieldInfo fieldData = tableFields.get(i);
                            Object val = getFieldValueBasedOnType(fieldData, rs);
                            // get the target method from class object by reflection
                            Method m = getFieldSetter(fieldData, T);
                            // execute method ===> u.setFieldName(val)
                            m.invoke(row, val);
                        }
                        returnedList.add((T) row);
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                             InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
                return null;
            }
        });
        return returnedList;
    }

    private Class[] getFieldClassType(FieldInfo fieldData) {
        switch (fieldData.fieldType) {
            case FieldTypes.stringType:
                return new Class[]{String.class};
            case FieldTypes.longType:
                return new Class[]{Long.class};
            default:
                return new Class[]{Object.class};
        }
    }

    private Method getFieldSetter(FieldInfo field, Class cls) throws NoSuchMethodException {
        if (field.setMethod == null) {
            String methodName = "set" + field.fieldName.substring(0, 1).toUpperCase(Locale.ROOT) + field.fieldName.substring(1);
            Class[] args = getFieldClassType(field);
            field.setMethod = cls.getMethod(methodName, args);
        }
        return field.setMethod;
    }

    private Object getFieldValueBasedOnType(FieldInfo fieldData, ResultSet rs) throws SQLException {
        switch (fieldData.fieldType) {
            case FieldTypes.stringType:
                return rs.getString(fieldData.dbFieldName);
            case FieldTypes.longType:
                return rs.getLong(fieldData.dbFieldName);
            default:
                return rs.getObject(fieldData.dbFieldName);
        }
    }

    private ArrayList<FieldInfo> getTableFields(Class T) {
        ArrayList<FieldInfo> fieldsInfo = new ArrayList<>();
        Field[] fields = T.getDeclaredFields();
        Arrays.stream(fields).forEach(fl -> fieldsInfo.add(getFieldInfo(fl, T)));
        return fieldsInfo;
    }

    private FieldInfo getFieldInfo(Field field, Class T) {
        FieldInfo info = new FieldInfo();
        Annotation[] annotations = field.getAnnotations();
        Optional<Annotation> annotation = Arrays.stream(annotations).filter(FieldAnnotation.class::isInstance).findFirst();
        if (annotation.isPresent()) {
            FieldAnnotation fieldAnnotation = ((FieldAnnotation) annotation.get());
            info.fieldName = field.getName();
            info.dbFieldName = (fieldAnnotation.name().equals("") ? info.fieldName : fieldAnnotation.name());
            info.isPrimaryKey = fieldAnnotation.isPrimary();
            info.fieldType = fieldAnnotation.type();
        }
        return info;
    }

    private String getTableName(Class T) {
        String tableName = "";
        if (T.isAnnotationPresent(TableAnnotation.class)) {
            TableAnnotation annotation = (TableAnnotation) T.getAnnotation(TableAnnotation.class);
            tableName = annotation.name();
        }
        return tableName;
    }

    //DELETE FROM users WHERE user_id = ?
    @Transactional
    @Override
    public void deleteById(Class T, ID id) {
        String tableName = getTableName(T);
        String idFieldName = getIdFieldName(T);
        String sql = "DELETE FROM " + tableName + " WHERE " + idFieldName + " =?";
        jdbcTemplate.update(sql, id);
    }


    public String getIdFieldName(Class T) {
        List<FieldInfo> fields = getTableFields(T);
        Optional<FieldInfo> idField = fields.stream()
                .filter(f -> f.isPrimaryKey && f.fieldType == 1)
                .findFirst();
        if (idField.isPresent()) {
            return idField.get().dbFieldName;
        } else {
            throw new IllegalArgumentException("No Primary key exist");
        }
    }

    //INSERT INTO users VALUES (user_id, user_name, email, password)
    @Override
    public <S extends T> S save(S entity) {
        Class c = entity.getClass();
        String tableName = getTableName(c);
        ArrayList<FieldInfo> fieldsList = getTableFields(c);
        List<String> fields = fieldsList.stream().map(fl -> fl.dbFieldName).toList();
        String fieldNames = StringUtils.join(fields, ',');
        String idFieldName = getIdFieldName(c);
        // Retrieve the last id from the database using MAX function in SQL
        String getMaxIdQuery = "SELECT MAX(" + idFieldName + ")" + " FROM " + tableName;
        Long lastId = jdbcTemplate.queryForObject(getMaxIdQuery, Long.class);

        StringJoiner valuePlaceholders = new StringJoiner(",");
        //iterate through the list of FieldInfo objects
        Object[] values = new Object[fieldsList.size()];
        for (int i = 0; i < fieldsList.size(); i++) {
            //obtain the getter method for each field using the getFieldGetter method.
            FieldInfo field = fieldsList.get(i);
            Method getter;
            try {
                getter = getFieldGetter(field, c);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            //invoke the getter method on the entity object and obtain the field value.
            Object fieldValue = null;
            try {
                fieldValue = getter.invoke(entity);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            values[i] = fieldValue;
            valuePlaceholders.add("?");

            // Generate a new id for the entity being saved
            Long newId = (lastId != null) ? lastId + 1 : 1;
            values[0] = newId; // set the id field value to the new id
        }
        String sql = "INSERT INTO " + tableName + " (" + fieldNames + ") VALUES (" + valuePlaceholders + ")";
        jdbcTemplate.update(sql, values);
        return entity;
    }

    private Method getFieldGetter(FieldInfo field, Class cls) throws Exception {
        try {
            if (field.getMethod == null) {
                String methodName = "get" + field.fieldName.substring(0, 1).toUpperCase(Locale.ROOT) + field.fieldName.substring(1);
                field.getMethod = cls.getMethod(methodName, null);
            }
            return field.getMethod;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

}