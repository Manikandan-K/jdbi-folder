package in.folder.jdbi.mapper;

import java.util.ArrayList;
import java.util.List;

public class AnnotatedFields1 {

    private List<AnnotatedField1> values = new ArrayList<>();
    private List<AnnotatedField1> primaryKeys = new ArrayList<>();

    public List<AnnotatedField1> values() {
        return values;
    }

    public List<AnnotatedField1> getPrimaryKeys() {
        return primaryKeys;
    }

    public void add(AnnotatedField1 annotatedField) {
        if(annotatedField == null) return;
        if(annotatedField.isPrimaryKey()) {
            primaryKeys.add(annotatedField);
        }else {
            values.add(annotatedField);
        }
    }
}
