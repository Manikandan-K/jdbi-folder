package com.github.rkmk.mapper;

import com.github.rkmk.DaoTest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EnumMapperTest extends DaoTest {

    EnumDao dao;

    @BeforeClass
    public static void createTables() {
        handle.execute("create table enum_table (id numeric, level  varchar );");
    }

    @Before
    public void setUp() {
        dao = handle.attach(EnumDao.class);
        handle.execute("delete from enum_table;");
    }

    @AfterClass
    public static void dropTables() throws Exception {
        handle.execute("drop table enum_table;");
    }

        @Test
    public void shouldGiveEnumValues() {
        handle.execute("insert into enum_table values(?,?)", 1, "LOW");
        handle.execute("insert into enum_table values(?,?)", 2, "MEDIUM");
        handle.execute("insert into enum_table(id) values(?)", 3);

        List<Level> levels = dao.getLevel();

        assertEquals(3, levels.size());
        assertTrue(levels.containsAll(asList(Level.LOW, Level.MEDIUM, null)));
    }

    interface EnumDao {
        @SqlQuery("select level from enum_table")
        public List<Level> getLevel();

    }

    enum Level {
        LOW, MEDIUM, HIGH
    }

}
