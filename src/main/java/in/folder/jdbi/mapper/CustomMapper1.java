package in.folder.jdbi.mapper;


import in.folder.jdbi.annotations.ColumnName;
import in.folder.jdbi.annotations.OneToMany;
import in.folder.jdbi.annotations.OneToOne;
import in.folder.jdbi.helper.FieldHelper;
import in.folder.jdbi.helper.FieldWrapper;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

import static in.folder.jdbi.helper.FieldHelper.getInstance;
import static java.util.Objects.nonNull;

public class CustomMapper1<T> implements ResultSetMapper<T>
{
    private final Class<T> type;
    private Map<String, AnnotatedField1> fields = new HashMap<>();
    private Map<String, FieldWrapper> innoruMap = new HashMap<>();
    private List<FieldMapperFactory> factories = new ArrayList<>();

    public CustomMapper1(Class<T> type) {
        this(type, new ArrayList<>());
    }

    public CustomMapper1(Class<T> type, List<FieldMapperFactory> overriddenFactories) {
        this.type = type;
        this.factories.addAll(overriddenFactories);
        this.factories.addAll(new FieldMapperFactories().getValues());
        processFields(type, "");

    }

    private void processFields(Class<?> type, String nameSpace) {
        for (Field field : getFields(type)) {
            if(field.isAnnotationPresent(OneToOne.class) ) {
                processOneToOneFields(field);
            }else if(field.isAnnotationPresent(OneToMany.class) ) {
                processOneToManyFields(field);
            }else {
                processField(nameSpace, field, type);
            }
        }
    }

    private void processField(String nameSpace, Field field, Class<?> type) {
        ColumnName annotation = field.getAnnotation(ColumnName.class);
        String name = nonNull(annotation) ? annotation.value() : field.getName();
        innoruMap.put(getResultSetFieldName(nameSpace, name), new FieldWrapper(type, field, nameSpace));
    }

    private FieldWrapper processResultSetName(String resultSetName) {
        String[] strings = resultSetName.split("\\$");

        if(strings.length ==1) {
            return innoruMap.get(resultSetName.replace("_", ""));
        }else {
            String nameSpace = strings[0];
            String nameWithoutUnderscore = strings[1].replace("-", "");
            return innoruMap.get(nameSpace + "$" + nameWithoutUnderscore);
        }
    }

    private void processOneToManyFields(Field field) {
        String nameSpace = field.getAnnotation(OneToMany.class).name();
        fields.put(nameSpace, new AnnotatedField1(field, OneToMany.class));
        processFields(FieldHelper.getParameterisedReturnType(field), nameSpace);
    }

    private void processOneToOneFields(Field field) {
        String nameSpace = field.getAnnotation(OneToOne.class).name();
        fields.put(nameSpace, new AnnotatedField1(field, OneToOne.class));
        processFields(field.getType(), nameSpace);
    }

    private String getResultSetFieldName(String nameSpace, String name) {
        String nameWithoutUnderscore = name.toLowerCase().replace("_", "");
        return nameSpace.isEmpty() ? nameWithoutUnderscore :  nameSpace.toLowerCase() + "$" + nameWithoutUnderscore;
    }

    private List<Field> getFields(Class<?> type) {
        List<Field> result = new ArrayList<>();
        Class<?> clazz = type;
        while(clazz.getSuperclass() != null) {
            result.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return result;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public T map(int row, ResultSet rs, StatementContext ctx) throws SQLException {
        HashMap<String, Object> instanceMap = new HashMap<>();
        T object = getInstance(type);
        instanceMap.put("", object);

        Set<String> nestedClassNames = getNestedClassNames(rs);

        ResultSetMetaData metadata = rs.getMetaData();

        for (int index = 1; index <= metadata.getColumnCount(); ++index) {
            String name = metadata.getColumnLabel(index).toLowerCase();
            FieldWrapper fieldWrapper = processResultSetName(name);

            if (fieldWrapper != null) {
                Class type = fieldWrapper.getFieldType();

                for (FieldMapperFactory factory : factories) {
                    if(factory.accepts(type)) {
                        Object value = factory.getValue(rs, index, type);
                        fieldWrapper.setValue(value, instanceMap);
                        break;
                    }
                }
            }
        }
        for (String nameSpace : instanceMap.keySet()) {
            AnnotatedField1 field = fields.get(nameSpace);
            if(nonNull(field) && nestedClassNames.contains(nameSpace)) {
                field.set(object, instanceMap.get(nameSpace));
            }
        }

        return object;
    }

    private Set<String> getNestedClassNames(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        Set<String> childClassNames = new HashSet<>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            String name = metaData.getColumnLabel(i).toLowerCase();
            String[] split = name.split("\\$");
            if(split.length == 2 && nonNull(rs.getObject(name))) {
                childClassNames.add(split[0]);
            }
        }
        return childClassNames;
    }



}


