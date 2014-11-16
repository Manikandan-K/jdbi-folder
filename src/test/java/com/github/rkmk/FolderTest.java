package com.github.rkmk;

import com.github.rkmk.annotations.OneToMany;
import com.github.rkmk.annotations.OneToOne;
import com.github.rkmk.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FolderTest {

    private Folder folder;

    @Before
    public void setUp() throws Exception {
        folder = new Folder();
    }


    @Test
    public void shouldFoldOneToManyRelationInsideOneToOne() {
        Level3_1toMany l3_1 = Level3_1toMany.builder().id(101).stringCol("Level 3 ..1").build();
        Level3_1toMany l3_2 = Level3_1toMany.builder().id(102).stringCol("Level 3 ..2").build();
        Level3_1toMany l3_3 = Level3_1toMany.builder().id(103).stringCol("Level 3 ..3").build();
        Level3_1toMany l3_4 = Level3_1toMany.builder().id(104).stringCol("Level 3 ..4").build();
        Level3_1toMany l3_5 = Level3_1toMany.builder().id(105).stringCol("Level 3 ..5").build();

        Level2_1to1 l2_1 = Level2_1to1.builder().id(11).stringCol("Level 2 ..1").level3_1toManyList(asList(l3_1)).build();
        Level2_1to1 l2_2 = Level2_1to1.builder().id(11).stringCol("Level 2 ..1").level3_1toManyList(asList(l3_2)).build();
        Level2_1to1 l2_3 = Level2_1to1.builder().id(12).stringCol("Level 2 ..2").level3_1toManyList(asList(l3_3)).build();
        Level2_1to1 l2_4 = Level2_1to1.builder().id(12).stringCol("Level 2 ..2").level3_1toManyList(asList(l3_4)).build();
        Level2_1to1 l2_5 = Level2_1to1.builder().id(12).stringCol("Level 2 ..2").level3_1toManyList(asList(l3_5)).build();

        Level1 l1_1 = Level1.builder().id(1).stringCol("Level 1 .. 1").level2_1to1(l2_1).build();
        Level1 l1_2 = Level1.builder().id(1).stringCol("Level 1 .. 1").level2_1to1(l2_2).build();
        Level1 l1_3 = Level1.builder().id(2).stringCol("Level 1 .. 2").level2_1to1(l2_3).build();
        Level1 l1_4 = Level1.builder().id(2).stringCol("Level 1 .. 2").level2_1to1(l2_4).build();
        Level1 l1_5 = Level1.builder().id(2).stringCol("Level 1 .. 2").level2_1to1(l2_5).build();

        List<Level1> accumulator = new ArrayList<>();
        folder.fold(accumulator, l1_1);
        folder.fold(accumulator, l1_2);
        folder.fold(accumulator, l1_3);
        folder.fold(accumulator, l1_4);
        folder.fold(accumulator, l1_5);

        assertThat(accumulator.size(), is(2));
        Level1 row1 = accumulator.get(0);
        assertThat(row1.getId(), is(1));
        assertThat(row1.getLevel2_1to1().getId(), is(11));
        assertThat(row1.getLevel2_1to1().getStringCol(), is("Level 2 ..1"));

        assertThat(row1.getLevel2_1to1().getLevel3_1toManyList().size(), is(2));
        assertLevel3_1toMany(l3_1, row1.getLevel2_1to1().getLevel3_1toManyList().get(0));
        assertLevel3_1toMany(l3_2, row1.getLevel2_1to1().getLevel3_1toManyList().get(1));

        Level1 row2 = accumulator.get(1);
        assertThat(row2.getId(), is(2));
        assertThat(row2.getLevel2_1to1().getId(), is(12));
        assertThat(row2.getLevel2_1to1().getStringCol(), is("Level 2 ..2"));

        assertThat(row2.getLevel2_1to1().getLevel3_1toManyList().size(), is(3));
        assertLevel3_1toMany(l3_3, row2.getLevel2_1to1().getLevel3_1toManyList().get(0));
        assertLevel3_1toMany(l3_4, row2.getLevel2_1to1().getLevel3_1toManyList().get(1));
        assertLevel3_1toMany(l3_5, row2.getLevel2_1to1().getLevel3_1toManyList().get(2));
    }

    @Test
    public void shouldFoldOneToManyNestedInAnotherOneToManyRelation() {
        Level3_1toMany l3_1 = Level3_1toMany.builder().id(101).stringCol("Level 3 ..1").build();
        Level3_1toMany l3_2 = Level3_1toMany.builder().id(102).stringCol("Level 3 ..2").build();
        Level3_1toMany l3_3 = Level3_1toMany.builder().id(103).stringCol("Level 3 ..3").build();
        Level3_1toMany l3_4 = Level3_1toMany.builder().id(104).stringCol("Level 3 ..4").build();
        Level3_1toMany l3_5 = Level3_1toMany.builder().id(105).stringCol("Level 3 ..5").build();

        Level2_1toMany l2_1 = Level2_1toMany.builder().id(11).stringCol("Level 2 ..1").level3_1toManyList(asList(l3_1)).build();
        Level2_1toMany l2_2 = Level2_1toMany.builder().id(11).stringCol("Level 2 ..1").level3_1toManyList(asList(l3_2)).build();
        Level2_1toMany l2_3 = Level2_1toMany.builder().id(12).stringCol("Level 2 ..2").level3_1toManyList(asList(l3_3)).build();
        Level2_1toMany l2_4 = Level2_1toMany.builder().id(12).stringCol("Level 2 ..2").level3_1toManyList(asList(l3_4)).build();
        Level2_1toMany l2_5 = Level2_1toMany.builder().id(12).stringCol("Level 2 ..2").level3_1toManyList(asList(l3_5)).build();

        Level1 l1_1 = Level1.builder().id(1).stringCol("Level 1 .. 1").level2_1toManyList(asList(l2_1)).build();
        Level1 l1_2 = Level1.builder().id(1).stringCol("Level 1 .. 1").level2_1toManyList(asList(l2_2)).build();
        Level1 l1_3 = Level1.builder().id(1).stringCol("Level 1 .. 1").level2_1toManyList(asList(l2_3)).build();
        Level1 l1_4 = Level1.builder().id(1).stringCol("Level 1 .. 1").level2_1toManyList(asList(l2_4)).build();
        Level1 l1_5 = Level1.builder().id(1).stringCol("Level 1 .. 1").level2_1toManyList(asList(l2_5)).build();

        List<Level1> accumulator = new ArrayList<>();
        folder.fold(accumulator, l1_1);
        folder.fold(accumulator, l1_2);
        folder.fold(accumulator, l1_3);
        folder.fold(accumulator, l1_4);
        folder.fold(accumulator, l1_5);

        assertThat(accumulator.size(), is(1));
        Level1 row1 = accumulator.get(0);
        assertThat(row1.getId(), is(1));
        assertThat(row1.getLevel2_1toManyList().size(), is(2));

        Level2_1toMany level2_expected_row1 = row1.getLevel2_1toManyList().get(0);
        assertLevel2_1toMany(level2_expected_row1, l2_1);
        assertThat(level2_expected_row1.getLevel3_1toManyList().size(), is(2));
        assertLevel3_1toMany(level2_expected_row1.getLevel3_1toManyList().get(0), l3_1);
        assertLevel3_1toMany(level2_expected_row1.getLevel3_1toManyList().get(1), l3_2);

        Level2_1toMany level2_expected_row2 = row1.getLevel2_1toManyList().get(1);
        assertLevel2_1toMany(level2_expected_row2, l2_3);
        assertThat(level2_expected_row2.getLevel3_1toManyList().size(), is(3));
        assertLevel3_1toMany(level2_expected_row2.getLevel3_1toManyList().get(0), l3_3);
        assertLevel3_1toMany(level2_expected_row2.getLevel3_1toManyList().get(1), l3_4);
        assertLevel3_1toMany(level2_expected_row2.getLevel3_1toManyList().get(2), l3_5);
    }

    @Test
    public void shouldFoldOneToOneAndOneToMany() {
        Level2_1to1 l2_1_1 = Level2_1to1.builder().id(11).stringCol("Level 2 ..1").build();
        Level2_1to1 l2_1_2 = Level2_1to1.builder().id(11).stringCol("Level 2 ..1").build();
        Level2_1to1 l2_1_3 = Level2_1to1.builder().id(12).stringCol("Level 2 ..2").build();
        Level2_1to1 l2_1_4 = Level2_1to1.builder().id(12).stringCol("Level 2 ..2").build();
        Level2_1to1 l2_1_5 = Level2_1to1.builder().id(12).stringCol("Level 2 ..2").build();


        Level2_1toMany l2_m_1 = Level2_1toMany.builder().id(111).stringCol("Level 2 ..1").build();
        Level2_1toMany l2_m_2 = Level2_1toMany.builder().id(112).stringCol("Level 2 ..2").build();
        Level2_1toMany l2_m_3 = Level2_1toMany.builder().id(113).stringCol("Level 2 ..3").build();
        Level2_1toMany l2_m_4 = Level2_1toMany.builder().id(114).stringCol("Level 2 ..4").build();
        Level2_1toMany l2_m_5 = Level2_1toMany.builder().id(115).stringCol("Level 2 ..5").build();

        Level1 l1_1 = Level1.builder().id(1).stringCol("Level 1 .. 1").level2_1toManyList(asList(l2_m_1)).level2_1to1(l2_1_1).build();
        Level1 l1_2 = Level1.builder().id(1).stringCol("Level 1 .. 1").level2_1toManyList(asList(l2_m_2)).level2_1to1(l2_1_2).build();
        Level1 l1_3 = Level1.builder().id(2).stringCol("Level 1 .. 2").level2_1toManyList(asList(l2_m_3)).level2_1to1(l2_1_3).build();
        Level1 l1_4 = Level1.builder().id(2).stringCol("Level 1 .. 2").level2_1toManyList(asList(l2_m_4)).level2_1to1(l2_1_4).build();
        Level1 l1_5 = Level1.builder().id(2).stringCol("Level 1 .. 2").level2_1toManyList(asList(l2_m_5)).level2_1to1(l2_1_5).build();

        List<Level1> accumulator = new ArrayList<>();
        folder.fold(accumulator, l1_1);
        folder.fold(accumulator, l1_2);
        folder.fold(accumulator, l1_3);
        folder.fold(accumulator, l1_4);
        folder.fold(accumulator, l1_5);

        assertThat(accumulator.size(), is(2));
        Level1 row1 = accumulator.get(0);
        assertThat(row1.getId(), is(1));
        assertThat(row1.getStringCol(), is("Level 1 .. 1"));
        assertLevel2_1to1(row1.getLevel2_1to1(), l2_1_1);

        assertThat(row1.getLevel2_1toManyList().size(), is(2));
        assertLevel2_1toMany(l2_m_1, row1.getLevel2_1toManyList().get(0));
        assertLevel2_1toMany(l2_m_2, row1.getLevel2_1toManyList().get(1));

        Level1 row2 = accumulator.get(1);
        assertThat(row2.getId(), is(2));
        assertThat(row2.getStringCol(), is("Level 1 .. 2"));
        assertLevel2_1to1(row2.getLevel2_1to1(), l2_1_3);

        assertThat(row2.getLevel2_1toManyList().size(), is(3));
        assertLevel2_1toMany(l2_m_3, row2.getLevel2_1toManyList().get(0));
        assertLevel2_1toMany(l2_m_4, row2.getLevel2_1toManyList().get(1));
        assertLevel2_1toMany(l2_m_5, row2.getLevel2_1toManyList().get(2));
    }

    private void assertLevel2_1to1(Level2_1to1 expected, Level2_1to1 actual) {
        assertThat(actual.getId(), is(expected.getId()));
        assertThat(actual.getStringCol(), is(expected.getStringCol()));
    }

    private void assertLevel2_1toMany(Level2_1toMany expected, Level2_1toMany actual) {
        assertThat(actual.getId(), is(expected.getId()));
        assertThat(actual.getStringCol(), is(expected.getStringCol()));
    }

    private void assertLevel3_1toMany(Level3_1toMany expected, Level3_1toMany actual) {
        assertThat(actual.getId(), is(expected.getId()));
        assertThat(actual.getStringCol(), is(expected.getStringCol()));
    }

    private<M> List<M> asList(M object) {
        List<M> list = new ArrayList<>();
        list.add(object);
        return list;
    }

}

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
class Level1 {
    @PrimaryKey
    private Integer id;
    private String stringCol;

    @OneToOne("level1OneToOne")
    private Level2_1to1 level2_1to1;

    @OneToMany("level1OneToMany")
    private List<Level2_1toMany> level2_1toManyList;

}

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
class Level2_1to1 {
    @PrimaryKey
    private Integer id;
    private String stringCol;

    @OneToMany("level_three")
    private List<Level3_1toMany> level3_1toManyList;
}

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
class Level2_1toMany {
    @PrimaryKey
    private Integer id;
    private String stringCol;
    @OneToMany("level_three")
    private List<Level3_1toMany> level3_1toManyList;

}

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
class Level3_1toMany {
    @PrimaryKey
    private Integer id;
    private String stringCol;
}

