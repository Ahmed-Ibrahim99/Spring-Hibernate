package com.example.Hibernate.config;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface CrudOperations<T, ID> {
    /**
     * Returns all instances of the type.
     */
    List<T> findAll(Class T);

    /**
     * Deletes the entity with the given id.
     */
    void deleteById(Class T, ID id);

    /**
     * S, the return type of save operation, must be subtype of T.
     *
     * Saves a given entity.
     */
    <S extends T> S save(S entity) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException;

}
