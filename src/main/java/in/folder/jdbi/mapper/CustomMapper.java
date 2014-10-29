package in.folder.jdbi.mapper;


import in.folder.jdbi.helper.FieldWrapper;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

import static in.folder.jdbi.mapper.FieldHelper.getInstance;
import static java.util.Objects.nonNull;

public class CustomMapper<T> implements ResultSetMapper<T>
{
    private final Class<T> type;
    private Map<String, FieldWrapper> fields = new HashMap<>();
    private List<FieldMapperFactory> factories = new ArrayList<>();
    private Map<Class<?>, AnnotatedFields> annotatedFieldsMap;

    public CustomMapper(Class<T> type) {
        this(type, new ArrayList<>());
    }

    public CustomMapper(Class<T> type, List<FieldMapperFactory> overriddenFactories) {
        this.type = type;
        this.factories.addAll(overriddenFactories);
        this.factories.addAll(new FieldMapperFactories().getValues());
        this.fields = AnnotatedFieldFactory.getFields(type);
        annotatedFieldsMap = AnnotatedFieldFactory.get(type);

    }

    private FieldWrapper processResultSetName(String resultSetName) {
        String[] strings = resultSetName.split("\\$");

        if(strings.length ==1) {
            return fields.get(resultSetName.replace("_", ""));
        }else {
            String nameSpace = strings[0];
            String nameWithoutUnderscore = strings[1].replace("_", "");
            return fields.get(nameSpace + "$" + nameWithoutUnderscore);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public T map(int row, ResultSet rs, StatementContext ctx) throws SQLException {
        HashMap<Class<?>, Object> instanceMap = new HashMap<>();
        T object = getInstance(type);
        instanceMap.put(type, object);

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
        setNestedObjects(type, nestedClassNames, instanceMap);
        return object;
    }

    private void setNestedObjects(Class<?> type, Set<String> nestedClassNames, Map<Class<?>, Object> instanceMap) {
        for (AnnotatedField field : annotatedFieldsMap.get(type).values()) {
            if(nestedClassNames.contains(field.getNameSpace()) && instanceMap.containsKey(field.getType()) ) {
                field.set(instanceMap.get(field.getDeclaringClass()), instanceMap.get(field.getType()));
                setNestedObjects(field.getType(), nestedClassNames, instanceMap);
            }
        }
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


