package com.github.rkmk.mapper;

import org.skife.jdbi.v2.ResultSetMapperFactory;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class CustomMapperFactory implements ResultSetMapperFactory {

    private List<Class<?>> excludedTypes = new ArrayList<>();
    private List<FieldMapperFactory> overriddenFactories = new ArrayList<>();
    private ConcurrentHashMap<String, CustomMapper> cache = new ConcurrentHashMap<>();

    public CustomMapperFactory() {
        excludedTypes.add(Boolean.class);
        excludedTypes.add(boolean.class);
        excludedTypes.add(Byte.class);
        excludedTypes.add(byte.class);
        excludedTypes.add(Short.class);
        excludedTypes.add(short.class);
        excludedTypes.add(Integer.class);
        excludedTypes.add(int.class);
        excludedTypes.add(Long.class);
        excludedTypes.add(long.class);
        excludedTypes.add(Float.class);
        excludedTypes.add(float.class);
        excludedTypes.add(Double.class);
        excludedTypes.add(double.class);
        excludedTypes.add(BigDecimal.class);
        excludedTypes.add(String.class);
    }

    public CustomMapperFactory(Class<?>... excludedTypes) {
        this();
        for (Class<?> excludedType : excludedTypes) {
            this.excludedTypes.add(excludedType);
        }
    }

    @Override
    public boolean accepts(Class type, StatementContext ctx) {
        return !excludedTypes.contains(type);
    }

    @Override
    public ResultSetMapper mapperFor(Class type, StatementContext ctx) {
        return type.isEnum() ? getEnumMapper(type) :  mapperFor(type, "");
    }

    public <M> CustomMapper<M> mapperFor(Class<M> type, String nameSpace) {
        String key = type.toString() + nameSpace;
        if(!cache.containsKey(key)) {
            cache.put(key, new CustomMapper<>(type, overriddenFactories));
        }
        return cache.get(key);
    }

    public EnumMapper getEnumMapper(Class<? extends Enum> type) {
        return new EnumMapper(type);
    }

    public void register(FieldMapperFactory factory) {
        overriddenFactories.add(factory);
    }
}
