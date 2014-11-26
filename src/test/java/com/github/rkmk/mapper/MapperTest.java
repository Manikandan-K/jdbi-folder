package com.github.rkmk.mapper;

import com.github.rkmk.DaoTest;
import com.github.rkmk.annotations.OneToMany;
import com.github.rkmk.annotations.OneToOne;
import lombok.Getter;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MapperTest extends DaoTest {

    private MapperDao dao;

    @BeforeClass
    public static void createTables() {
        handle.execute("create table level1 (id numeric not null, string_col varchar(100) , numeric_col numeric );");
        handle.execute("create table level2_1to1 (id numeric not null, string_col varchar(100) , level1_id numeric );");
        handle.execute("create table level2_1to_many (id numeric not null, string_col varchar(100) , level1_id numeric );");
        handle.execute("create table level3_1to_many (id numeric not null, string_col varchar(100) , level2_id numeric );");
    }

    @Before
    public void setUp() {
        dao = handle.attach(MapperDao.class);
        handle.execute("delete from level1;");
        handle.execute("delete from level2_1to1;");
        handle.execute("delete from level2_1to_many;");
        handle.execute("delete from level3_1to_many;");

    }

    @AfterClass
    public static void dropTables() throws Exception {
        handle.execute("drop table level1;");
        handle.execute("drop table level2_1to1;");
        handle.execute("drop table level2_1to_many;");
        handle.execute("drop table level3_1to_many;");
    }

        @Test
    public void shouldMapLevel1DetailsAlongWithNestedObjects() {
        handle.execute("insert into level1 values(?,?,?)", 8, "Hi", 10);
        handle.execute("insert into level2_1to1 values(?,?,?)", 21, "Level2 OneToOne", 8);
        handle.execute("insert into level2_1to_many values(?,?,?)", 26, "Level2 OneToMany", 8);
        handle.execute("insert into level3_1to_many values(?,?,?)", 35, "Level3 OneToMany", 21);

        Level1 result = dao.getValue(8);

        assertThat(result.getId(), is(8));
        assertThat(result.getStringCol(), is("Hi"));
        assertThat(result.getNumericCol(), is(BigDecimal.TEN));
        assertThat(result.getLevel2_1to1().getId(), is(21));
        assertThat(result.getLevel2_1to1().getStringCol(), is("Level2 OneToOne"));
        assertThat(result.getLevel2_1to1().getLevel1Id(), is(8));

        assertThat(result.getLevel2_1toManySet().size(), is(1));
        Level2_1toMany level2_1toMany = result.getLevel2_1toManySet().iterator().next();
        assertThat(level2_1toMany.getId(), is(26));
        assertThat(level2_1toMany.getStringCol(), is("Level2 OneToMany"));
        assertThat(level2_1toMany.getLevel1Id(), is(8));

        assertThat(result.getLevel2_1to1().getLevel3_1toManyList().size(), is(1));
        Level3_1toMany level3_1toMany = result.getLevel2_1to1().getLevel3_1toManyList().get(0);
        assertThat(level3_1toMany.getId(), is(35));
        assertThat(level3_1toMany.getStringCol(), is("Level3 OneToMany"));
        assertThat(level3_1toMany.getLevel2Id(), is(21));
    }

}

interface MapperDao {
    @SqlQuery("" +
            "select l1.*, " +
            "       l2_11.id            AS level1OneToOne$id, " +
            "       l2_11.string_col    AS level1OneToOne$string_col, " +
            "       l2_11.level1_id     AS level1OneToOne$level1_id, " +
            "       l2_1m.id            AS level1OneToMany$id, " +
            "       l2_1m.string_col    AS level1OneToMany$string_col, " +
            "       l2_1m.level1_id     AS level1OneToMany$level1_id, " +
            "       l3_1m.id            AS level_three$id, " +
            "       l3_1m.string_col    AS level_three$string_col, " +
            "       l3_1m.level2_id     AS level_three$level2_id " +
            "from level1 l1" +
            "   join level2_1to1 l2_11  on l1.id = l2_11.level1_id " +
            "   join level2_1to_many l2_1m  on l1.id = l2_1m.level1_id " +
            "   join level3_1to_many l3_1m  on l2_11.id = l3_1m.level2_id " +
            "")
    public Level1 getValue(@Bind("id") Integer id);

}

@Getter
class Level1 {
    private Integer id;
    private String stringCol;
    private BigDecimal numericCol;

    @OneToOne("level1OneToOne")
    private Level2_1to1 level2_1to1;

    @OneToMany("level1OneToMany")
    private HashSet<Level2_1toMany> level2_1toManySet;

}

@Getter
class Level2_1to1 {
    private Integer id;
    private String stringCol;
    private Integer level1Id;

    @OneToMany("level_three")
    private List<Level3_1toMany> level3_1toManyList;
}

@Getter
class Level2_1toMany {
    private Integer id;
    private String stringCol;
    private Integer level1Id;
}

@Getter
class Level3_1toMany {
    private Integer id;
    private String stringCol;
    private Integer level2Id;
}



