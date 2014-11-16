package com.github.rkmk.container;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FoldingList<T> implements Iterable<T>{

    private List<T> list;

    public FoldingList(List<T> list) {
        this.list = list;
    }
    public FoldingList() {
        this.list = new ArrayList<>();
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    public void add(T object) {
        list.add(object);
    }

    public List<T> getValues(){
        return list;
    }
}
