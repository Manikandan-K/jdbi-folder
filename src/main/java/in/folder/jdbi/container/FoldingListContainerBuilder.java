package in.folder.jdbi.container;

import org.skife.jdbi.v2.ContainerBuilder;

public class FoldingListContainerBuilder implements ContainerBuilder<FoldingList<?>>{

    private final FoldingList<Object> list;

    public FoldingListContainerBuilder() {
        this.list = new FoldingList<>();
    }

    @Override
    public ContainerBuilder<FoldingList<?>> add(Object it) {
        list.add(it);
        return this;
    }

    @Override
    public FoldingList<?> build() {
        return list;
    }

    private void processClass(Class<?> type) {

    }

}
