package in.folder.jdbi.mapper;

import in.folder.jdbi.annotations.ColumnName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomMapperTest {

    @Mock
    ResultSet resultSet;

    @Mock
    ResultSetMetaData resultSetMetaData;

    @Mock
    StatementContext ctx;

    CustomMapper1<SampleBean> mapper = new CustomMapper1<>(SampleBean.class, new ArrayList<>());

    @Test
    public void shouldSetValueOnPrivateField() throws Exception {
        when(resultSetMetaData.getColumnCount()).thenReturn(1);
        when(resultSetMetaData.getColumnLabel(1)).thenReturn("longField");

        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
        Long aLongVal = 100l;
        when(resultSet.getLong(1)).thenReturn(aLongVal);
        SampleBean sampleBean = mapper.map(0, resultSet, ctx);

        assertSame(aLongVal, sampleBean.getLongField());
    }

    @Test
    public void shouldHandleEmptyResult() throws Exception {
        when(resultSetMetaData.getColumnCount()).thenReturn(0);
        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);

        SampleBean sampleBean = mapper.map(0, resultSet, ctx);

        assertNotNull(sampleBean);
    }

    @Test
    public void shouldBeCaseInSensitiveOfColumnAndFieldNames() throws Exception {
        when(resultSetMetaData.getColumnCount()).thenReturn(2);
        when(resultSetMetaData.getColumnLabel(1)).thenReturn("LoNgfielD");
        when(resultSetMetaData.getColumnLabel(2)).thenReturn("string_field");

        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
        Long aLongVal = 100l;
        String aStringVal = "String value";
        when(resultSet.getLong(1)).thenReturn(aLongVal);
        when(resultSet.getString(2)).thenReturn(aStringVal);
        SampleBean sampleBean = mapper.map(0, resultSet, ctx);

        assertSame(aLongVal, sampleBean.getLongField());
        assertSame(aStringVal, sampleBean.getStringField());
    }

    @Test
    public void shouldHandleNullValue() throws Exception {
        when(resultSetMetaData.getColumnCount()).thenReturn(1);
        when(resultSetMetaData.getColumnLabel(1)).thenReturn("LoNgfielD");

        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
        when(resultSet.getLong(1)).thenReturn(0l);
        when(resultSet.wasNull()).thenReturn(true);

        SampleBean sampleBean = mapper.map(0, resultSet, ctx);

        assertNull(sampleBean.getLongField());

    }

    @Test
    public void shouldSetValuesOnAllFieldAccessTypes() throws Exception {
        when(resultSetMetaData.getColumnCount()).thenReturn(4);
        when(resultSetMetaData.getColumnLabel(1)).thenReturn("longField");
        when(resultSetMetaData.getColumnLabel(2)).thenReturn("stringField");
        when(resultSetMetaData.getColumnLabel(3)).thenReturn("intField");
        when(resultSetMetaData.getColumnLabel(4)).thenReturn("bigDecimalField");

        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
        Long aLongVal = 100l;
        String aStringVal = "something";
        int aIntVal = 1;
        BigDecimal aBigDecimal = BigDecimal.TEN;

        when(resultSet.getLong(1)).thenReturn(aLongVal);
        when(resultSet.getString(2)).thenReturn(aStringVal);
        when(resultSet.getInt(3)).thenReturn(aIntVal);
        when(resultSet.getBigDecimal(4)).thenReturn(aBigDecimal);

        SampleBean sampleBean = mapper.map(0, resultSet, ctx);

        assertSame(aLongVal, sampleBean.getLongField());
        assertSame(aBigDecimal, sampleBean.getBigDecimalField());
        assertSame(aIntVal, sampleBean.getIntField());
        assertSame(aStringVal, sampleBean.getStringField());
    }

    @Test
    public void customMapperFactoryShouldTakeOverriddenMapperFactories() throws Exception {
        when(resultSetMetaData.getColumnCount()).thenReturn(1);
        when(resultSetMetaData.getColumnLabel(1)).thenReturn("bigDecimalField");
        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);

        CustomMapperFactory customMapperFactory = new CustomMapperFactory();
        customMapperFactory.register(new BigDecimalMapperFactory());
        ResultSetMapper<SampleBean> mapper = customMapperFactory.mapperFor(SampleBean.class, "");

        SampleBean sampleBean = mapper.map(0, resultSet, ctx);

        assertSame(BigDecimal.TEN, sampleBean.getBigDecimalField());
    }

    @Test
    public void shouldTakeColumnNameValueForFieldNameIfAnnotationPresent() throws Exception {
        when(resultSetMetaData.getColumnCount()).thenReturn(1);
        when(resultSetMetaData.getColumnLabel(1)).thenReturn("columnName");

        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
        String columnVal = "String value";
        when(resultSet.getString(1)).thenReturn(columnVal);
        SampleBean sampleBean = mapper.map(0, resultSet, ctx);

        assertSame(columnVal, sampleBean.getColumn());
    }

    @Test
    public void shouldMapValueForSuperClassFieldsAlso() throws Exception {
        when(resultSetMetaData.getColumnCount()).thenReturn(2);
        when(resultSetMetaData.getColumnLabel(1)).thenReturn("stringField");
        when(resultSetMetaData.getColumnLabel(2)).thenReturn("superString");

        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
        String beanString = "String value";
        String superString = "Super String value";
        when(resultSet.getString(1)).thenReturn(beanString);
        when(resultSet.getString(2)).thenReturn(superString);
        SampleBean sampleBean = mapper.map(0, resultSet, ctx);

        assertSame(beanString, sampleBean.getStringField());
        assertSame(superString, sampleBean.getSuperString());
    }

    @Test
    public void shouldMapEnumValues() throws Exception {
        when(resultSetMetaData.getColumnCount()).thenReturn(1);
        when(resultSetMetaData.getColumnLabel(1)).thenReturn("enumField");

        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
        when(resultSet.getString(1)).thenReturn("ONE");
        SampleBean sampleBean = mapper.map(0, resultSet, ctx);

        assertSame(ENUM.ONE, sampleBean.getEnumField());
    }

    @Test
    public void shouldMapNullEnumValues() throws Exception {
        when(resultSetMetaData.getColumnCount()).thenReturn(1);
        when(resultSetMetaData.getColumnLabel(1)).thenReturn("enumField");

        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
        when(resultSet.getString(1)).thenReturn(null);
        SampleBean sampleBean = mapper.map(0, resultSet, ctx);

        assertNull(sampleBean.getEnumField());
    }

}



@Getter
class SampleBean extends SuperClassBean{
    private Long longField;
    protected String stringField;
    public int intField;
    BigDecimal bigDecimalField;
    @ColumnName("columnName")
    private String column;
    private ENUM enumField;
}

@Getter
class SuperClassBean {
    private String superString;
}

@Getter
@AllArgsConstructor
enum ENUM {
    ONE("one"), TWO("two");
    private String name;
}


