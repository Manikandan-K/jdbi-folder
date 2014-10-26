package in.folder.jdbi.mapper;


import in.folder.jdbi.helper.FieldWrapper;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

import static in.folder.jdbi.helper.FieldHelper.getInstance;
import static java.util.Objects.nonNull;

public class CustomMapper1<T> implements ResultSetMapper<T>
{
    private final Class<T> type;
    private Map<String, FieldWrapper> fields = new HashMap<>();
    private List<FieldMapperFactory> factories = new ArrayList<>();
    private Map<Class<?>, AnnotatedFields1> annotatedFieldsMap;

    public CustomMapper1(Class<T> type) {
        this(type, new ArrayList<>());
    }

    public CustomMapper1(Class<T> type, List<FieldMapperFactory> overriddenFactories) {
        this.type = type;
        this.factories.addAll(overriddenFactories);
        this.factories.addAll(new FieldMapperFactories().getValues());
        this.fields = AnnotatedFieldFactory1.getFields(type);
        annotatedFieldsMap = AnnotatedFieldFactory1.get(type);

    }

    private FieldWrapper processResultSetName(String resultSetName) {
        String[] strings = resultSetName.split("\\$");

        if(strings.length ==1) {
            return fields.get(resultSetName.replace("_", ""));
        }else {
            String nameSpace = strings[0];
            String nameWithoutUnderscore = strings[1].replace("-", "");
            return fields.get(nameSpace + "$" + nameWithoutUnderscore);
        }
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
        anotherMethod(object, type, nestedClassNames, instanceMap);
        return object;
    }

    private void anotherMethod(Object object, Class<?> type, Set<String> nestedClassNames,Map<String, Object> instanceMap) {
        AnnotatedFields1 annotatedFields = annotatedFieldsMap.get(type);

        for (AnnotatedField1 field : annotatedFields.get()) {
            if(nestedClassNames.contains(field.getNameSpace()) && instanceMap.containsKey(field.getNameSpace()) ) {
                field.set(object, instanceMap.get(field.getNameSpace()));
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


