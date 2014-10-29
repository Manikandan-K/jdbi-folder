package in.folder.jdbi.mapper;

import java.util.ArrayList;
import java.util.List;

public class AnnotatedFields {

    private List<AnnotatedField> values = new ArrayList<>();
    private List<AnnotatedField> primaryKeys = new ArrayList<>();

    public List<AnnotatedField> values() {
        return values;
    }

    public List<AnnotatedField> getPrimaryKeys() {
        return primaryKeys;
    }

    public void add(AnnotatedField annotatedField) {
        if(annotatedField == null) return;
        if(annotatedField.isPrimaryKey()) {
            primaryKeys.add(annotatedField);
        }else {
            values.add(annotatedField);
        }
    }
}
