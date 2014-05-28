package gstorm

import groovy.sql.Sql
import models.ClassWithIdAnnotation
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * A bad test is better than no test :)
 */
class GstormAnnotatedIdTests {

    Gstorm gstorm
    Sql sql
    String tableName = ClassWithIdAnnotation.simpleName.toUpperCase()

    @Before
    void setUp() {
        sql = Sql.newInstance("jdbc:hsqldb:mem:database", "sa", "", "org.hsqldb.jdbc.JDBCDriver")
        gstorm = new Gstorm(sql)
        gstorm.stormify(ClassWithIdAnnotation)
    }

    @After
    void tearDown() {
        sql.execute("drop table ClassWithIdAnnotation if exists")
        sql.close()
    }

    @Test
    void "Annotated Id field should be used to as Primary Key in create table"() {

        assert sql.rows("select COLUMN_NAME from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = ?", [tableName])
                .collect { it.column_name }
                .contains("UID")
    }


    @Test
    void "should be able to save"() {
        new ClassWithIdAnnotation(name: "zxsawe").save()

        def result = sql.rows("select * from ${tableName}".toString())

        assert result.size() == 1
        assert result.first().name == 'zxsawe'
    }

    @Test
    void "should be able to get by annotated Id"() {
        def object = new ClassWithIdAnnotation(name: "zxsawe").save()

        def result = ClassWithIdAnnotation.get(object.uid)

        assert result.name == 'zxsawe'
    }

    @Test
    void "should be able to delete by annotated Id"() {
        def object = new ClassWithIdAnnotation(name: "zxsawe").save()

        object.delete()

        assert sql.rows("select count(*) as total_count from ${tableName}".toString()).total_count == [0]
    }

    @Test
    void "should be able to update by annotated Id"() {
        def object = new ClassWithIdAnnotation(name: "zxsawe").save()

        object.name = "updated_name"
        object.save()

        def result = sql.rows("select * from ${tableName}".toString())

        assert result.size() == 1
        assert result.first().name == "updated_name"
    }

}
