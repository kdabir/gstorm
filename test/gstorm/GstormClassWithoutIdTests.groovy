package gstorm

import groovy.sql.Sql
import models.ClassWithoutId
import org.junit.After
import org.junit.Before
import org.junit.Test

class GstormClassWithoutIdTests {

    Gstorm gstorm
    Sql sql
    String tableName = ClassWithoutId.simpleName.toUpperCase()

    @Before
    void setUp() {
        sql = Sql.newInstance("jdbc:hsqldb:mem:database;shutdown=true", "sa", "", "org.hsqldb.jdbc.JDBCDriver")
        gstorm = new Gstorm(sql)
        gstorm.stormify(ClassWithoutId)
    }

    @After
    void tearDown() {
        sql.execute("drop table ClassWithoutId if exists")
        sql.close()
    }

    @Test
    void "should create table without id field"() {
        def columns = sql.rows("select COLUMN_NAME from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = ?", [tableName])
                .collect { it.column_name }
        assert columns.size() == 2
        assert columns.contains("ID") == false
    }


    @Test(expected = MissingMethodException)
    void "should not have static find method as it depend on id"() {
        ClassWithoutId.find(12)
    }

    @Test
    void "should have static methods that do not depend on id"() {
        assert ClassWithoutId.where("NAME = 'test'").size == 0
        assert ClassWithoutId.count == 0
        assert ClassWithoutId.all*.name == []
    }

    @Test
    void "should be able to save object"() {
        new ClassWithoutId(name: "zxsawe", description: "this object doesn't have id").save()

        def result = sql.rows("select * from ${tableName}".toString())

        assert result.size() == 1
        assert result.first().name == 'zxsawe'
    }

    @Test
    void "should save item multiple times without updating"() {
        def object = new ClassWithoutId(name: "zxsawe", description: "this object doesn't have id").save()
        object.save() // again

        def result = sql.rows("select * from ${tableName}".toString())

        assert ClassWithoutId.count == 2
        assert ClassWithoutId.all*.name == ['zxsawe', 'zxsawe']
    }

    @Test(expected = MissingMethodException)
    void "should not have delete method as it depend on id"() {
        def object = new ClassWithoutId(name: "zxsawe", description: "this object doesn't have id")
        object.delete()
    }
}
