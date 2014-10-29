package in.folder.jdbi.container;

import in.folder.jdbi.Folder;
import org.skife.jdbi.v2.ContainerBuilder;

import java.util.ArrayList;
import java.util.List;

public class FoldingListContainerBuilder implements ContainerBuilder<FoldingList<?>>{

    private final List<Object> list;
    private final Folder folder;

    public FoldingListContainerBuilder() {
        this.list = new ArrayList<>();
        this.folder = new Folder();
    }

    @Override
    public ContainerBuilder<FoldingList<?>> add(Object currentObject) {
        folder.fold(list, currentObject);
        return this;
    }

    @Override
    public FoldingList<?> build() {
        return new FoldingList<>(list);
    }

}
