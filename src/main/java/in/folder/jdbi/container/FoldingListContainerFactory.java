package in.folder.jdbi.container;

import org.skife.jdbi.v2.ContainerBuilder;
import org.skife.jdbi.v2.tweak.ContainerFactory;

public class FoldingListContainerFactory implements ContainerFactory<FoldingList<?>> {

    @Override
    public boolean accepts(Class<?> type) {
        return type.equals(FoldingList.class);
    }

    @Override
    public ContainerBuilder<FoldingList<?>> newContainerBuilderFor(Class<?> type) {
        return new FoldingListContainerBuilder();
    }
}
