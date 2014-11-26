package com.github.rkmk.dao;

import com.github.rkmk.DaoTest;
import com.github.rkmk.container.FoldingList;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class MultipleLevelObjectTest extends DaoTest {

    private MultipleLevelObjectDao dao;

    @BeforeClass
    public static void createTables() {
        handle.execute("create table level1 (id numeric not null, string_col varchar(100) , numeric_col float );");
        handle.execute("create table level2 (id numeric not null, string_col varchar(100) , level1_id numeric );");
    }


    @Before
    public void setUp() throws Exception {
        dao = handle.attach(MultipleLevelObjectDao.class);
        handle.execute("delete from level1");
        handle.execute("delete from level2");

    }

    @AfterClass
    public static void dropTables() throws Exception {
        handle.execute("drop table level1");
        handle.execute("drop table level2");
    }


    @Test
    public void shouldGetLevelEntriesWithLevel2Entries() throws Exception {
        Level1 l11 = Level1.builder().id(1).stringCol("Hi").numericCol(1.2).build();
        Level1 l12 = Level1.builder().id(2).stringCol("Hello").numericCol(7.2d).build();
        Level2 l21 = Level2.builder().id(101).stringCol("Hi One").level1Id(1).build();
        Level2 l22 = Level2.builder().id(102).stringCol("Hi Two").level1Id(1).build();
        Level2 l23 = Level2.builder().id(201).stringCol("Hello One").level1Id(2).build();
        Level2 l24 = Level2.builder().id(202).stringCol("Hello Two").level1Id(2).build();
        Level2 l25 = Level2.builder().id(203).stringCol("Hello Three").level1Id(2).build();

        insert(l11,l12);
        insert(l21, l22, l23, l24, l25);

        List<Level1> result = dao.getAll().getValues();

        assertEquals(2, result.size());
        Level1 row1 = result.get(0);
        assertLevel1(row1, l11);
        assertThat(row1.getLevel2List().size(), is(2));
        assertLevel2(row1.getLevel2List().get(0), l21);
        assertLevel2(row1.getLevel2List().get(1), l22);

        Level1 row2 = result.get(1);
        assertLevel1(row2, l12);
        assertThat(row2.getLevel2List().size(), is(3));
        assertLevel2(row2.getLevel2List().get(0), l23);
        assertLevel2(row2.getLevel2List().get(1), l24);
        assertLevel2(row2.getLevel2List().get(2), l25);

    }

    private void assertLevel1(Level1 actual, Level1 expected) {
        assertThat(actual.getId(), is(expected.getId()));
        assertThat(actual.getStringCol(), is(expected.getStringCol()));
        assertThat(actual.getNumericCol(), is(expected.getNumericCol()));
    }

    private void assertLevel2(Level2 actual, Level2 expected) {
        assertThat(actual.getId(), is(expected.getId()));
        assertThat(actual.getStringCol(), is(expected.getStringCol()));
        assertThat(actual.getLevel1Id(), is(expected.getLevel1Id()));
    }


    private void insert(Level1... list) {
        for (Level1 level1 : list) {
            handle.execute("insert into level1 values(?,?,?)", level1.getId(), level1.getStringCol(), level1.getNumericCol());
        }
    }

    private void insert(Level2... list) {
        for (Level2 level2 : list) {
            handle.execute("insert into level2 values(?,?,?)", level2.getId(), level2.getStringCol(), level2.getLevel1Id());
        }
    }

}


interface MultipleLevelObjectDao {

    @SqlQuery("" +
            "select l1.*                                        ," +
            "       l2.id           AS levelTwo$id              ," +
            "       l2.string_col   AS levelTwo$stringCol       ," +
            "       l2.level1_id    AS levelTwo$level1_id       " +
            "from level1 l1 join level2 l2 on l1.id = l2.level1_id     " +
            "order by l1.id, l2.id " +
            "")
    public FoldingList<Level1> getAll();
}



