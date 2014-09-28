package in.folder.jdbi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotatedFields {

    private Map<String, AnnotatedField> values = new HashMap<>();
    private List<AnnotatedField> primaryKeys = new ArrayList<>();

    public AnnotatedField get(String key) {
        return values.get(key);
    }

    public List<AnnotatedField> getPrimaryKeys() {
        return primaryKeys;
    }

    public void add(AnnotatedField annotatedField) {
        if(annotatedField == null) return;
        if(annotatedField.isPrimaryKey()) {
            primaryKeys.add(annotatedField);
        }
        values.put(annotatedField.getName(), annotatedField);
    }
}
