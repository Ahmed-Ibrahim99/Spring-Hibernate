package com.example.Hibernate.utils;

import java.lang.reflect.Method;

public class FieldInfo {
    public String fieldName;
    public String dbFieldName;
    public int fieldType;
    public boolean isPrimaryKey;
    public Method getMethod;
    public Method setMethod;

    public FieldInfo() {
    }
}
