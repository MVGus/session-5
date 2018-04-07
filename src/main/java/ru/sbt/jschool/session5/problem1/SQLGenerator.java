package ru.sbt.jschool.session5.problem1;


import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 */
public class SQLGenerator {
    public <T> String insert(Class<T> clazz) {
        if(!clazz.isAnnotationPresent(Table.class)){
            return null;
        }
        String nameTable = clazz.getAnnotation(Table.class).name();
        Field[] an = clazz.getDeclaredFields();
        ArrayList<String> fields = new ArrayList<>();
        for (int i = 0; i < an.length; i++) {
            if(an[i].isAnnotationPresent(Column.class)){
            if(!an[i].getAnnotation(Column.class).name().isEmpty()){
            fields.add(an[i].getAnnotation(Column.class).name().toLowerCase());}
            else{
                fields.add(an[i].getName().toLowerCase());
            }
            }
            if(an[i].isAnnotationPresent(PrimaryKey.class)){
                if(!an[i].getAnnotation(PrimaryKey.class).name().isEmpty()){
                    fields.add(an[i].getAnnotation(PrimaryKey.class).name().toLowerCase());}
                else{
                    fields.add(an[i].getName().toLowerCase());
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(nameTable).append("(");
        for (int i = 0; i < fields.size(); i++) {
            sb.append(fields.get(i));
            if(fields.size()>i+1){
                sb.append(", ");
            }
        }
        sb.append(") VALUES (");
        for (int i = 0; i < fields.size(); i++) {
            sb.append("?");
            if(fields.size()>i+1){
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public <T> String update(Class<T> clazz) {
        if(!clazz.isAnnotationPresent(Table.class)){
            return null;
        }
        String nameTable = clazz.getAnnotation(Table.class).name();
        ArrayList<String> fieldNameColumns = nameColumn(clazz.getDeclaredFields());
        ArrayList<String> fieldNamePrimaryKey = namePrimaryKey(clazz.getDeclaredFields());

        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(nameTable).append(" SET ");
        for (int i = 0; i < fieldNameColumns.size(); i++) {
            sb.append(fieldNameColumns.get(i)).append(" = ?");
            if(fieldNameColumns.size()>i+1){
                sb.append(", ");
            }
        }
        sb.append(" WHERE ");
        for (int i = 0; i < fieldNamePrimaryKey.size(); i++) {
            sb.append(fieldNamePrimaryKey.get(i)).append(" = ?");
            if(fieldNamePrimaryKey.size()>i+1){
                sb.append(" AND ");
            }
        }

        return sb.toString();
    }

    public <T> String delete(Class<T> clazz) {
        if(!clazz.isAnnotationPresent(Table.class)){
            return null;
        }
        String nameTable = clazz.getAnnotation(Table.class).name();
        ArrayList<String> fieldNamePrimaryKey = namePrimaryKey(clazz.getDeclaredFields());
        StringBuilder sb = new StringBuilder("DELETE FROM ");
        sb.append(nameTable).append(" WHERE ");
        for (int i = 0; i < fieldNamePrimaryKey.size(); i++) {
            sb.append(fieldNamePrimaryKey.get(i)).append(" = ?");
            if(fieldNamePrimaryKey.size()>i+1){
                sb.append(" AND ");
            }
        }

        return sb.toString();
    }

    public <T> String select(Class<T> clazz) {
        if(!clazz.isAnnotationPresent(Table.class)){
            return null;
        }
        String nameTable = clazz.getAnnotation(Table.class).name();
        ArrayList<String> fieldNameColumns = nameColumn(clazz.getDeclaredFields());
        ArrayList<String> fieldNamePrimaryKey = namePrimaryKey(clazz.getDeclaredFields());

        StringBuilder sb = new StringBuilder("SELECT ");

        for (int i = 0; i < fieldNameColumns.size(); i++) {
            sb.append(fieldNameColumns.get(i));
            if(fieldNameColumns.size()>i+1){
                sb.append(", ");
            }
        }
        sb.append(" FROM ").append(nameTable).append(" WHERE ");
        for (int i = 0; i < fieldNamePrimaryKey.size(); i++) {
            sb.append(fieldNamePrimaryKey.get(i)).append(" = ?");
            if(fieldNamePrimaryKey.size()>i+1){
                sb.append(" AND ");
            }
        }
       return sb.toString();
    }


    public ArrayList<String> nameColumn(Field[] an){
        ArrayList<String> fields = new ArrayList<>();
        for (int i = 0; i < an.length; i++) {
            if(an[i].isAnnotationPresent(Column.class)){
                if(!an[i].getAnnotation(Column.class).name().isEmpty()){
                    fields.add(an[i].getAnnotation(Column.class).name().toLowerCase());}
                else{
                    fields.add(an[i].getName().toLowerCase());
                }
            }
        }
        return fields;
    }

    public ArrayList<String> namePrimaryKey(Field[] an){
        ArrayList<String> fields = new ArrayList<>();
        for (int i = 0; i < an.length; i++) {
            if(an[i].isAnnotationPresent(PrimaryKey.class)){
                if(!an[i].getAnnotation(PrimaryKey.class).name().isEmpty()){
                    fields.add(an[i].getAnnotation(PrimaryKey.class).name().toLowerCase());}
                else{
                    fields.add(an[i].getName().toLowerCase());
                }
            }
        }
        return fields;
    }


}

