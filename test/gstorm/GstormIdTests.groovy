package gstorm

import groovy.sql.Sql
import models.ClassWithIdAnnotation
import org.junit.Before
import org.junit.Test

/**
 * A bad test is better than no test :)
 */
class GstormIdTests {

    Gstorm gstorm
    Sql sql

    @Before
    void setup() {
        sql = Sql.newInstance("jdbc:hsqldb:mem:database", "sa", "", "org.hsqldb.jdbc.JDBCDriver")
        gstorm = new Gstorm(sql)
        gstorm.stormify(ClassWithIdAnnotation)
    }

    void tearDown() {
        sql.execute("drop table ClassWithIdAnnotation if exists")
        sql.close()
    }

    @Test
    void "Annotated Id field should be used to as Primary Key in create table"() {
        def tableName = ClassWithIdAnnotation.simpleName.toUpperCase()
        assert sql.rows("select COLUMN_NAME from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = ?", [tableName])
                .collect { it.column_name }
                .contains("UID")
    }

}
