package gstorm

import groovy.sql.Sql
import example.Person

class GstormTest extends GroovyTestCase {

    Gstorm gstorm
    Sql sql

    void setUp() {
        sql = Sql.newInstance("jdbc:hsqldb:mem:database", "sa", "", "org.hsqldb.jdbc.JDBCDriver")
        gstorm = new Gstorm(sql)
        gstorm.stormify(Person)
    }

    void tearDown() {
        sql.execute("drop table person if exists")
        sql.close()
    }

    void "test that a table is created for stormified class"() {
        assert sql.rows("select * from INFORMATION_SCHEMA.TABLES where TABLE_NAME = 'PERSON'").size() == 1
        def columns_names = sql.rows("select COLUMN_NAME from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = 'PERSON'").collect {it.column_name}
        ["NAME", "AGE"].each { assert columns_names.contains(it)}
    }

    void "test that created table has id column"() {
        assert sql.rows("select COLUMN_NAME from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = 'PERSON'").collect {it.column_name}.contains("ID")
    }

    void "test that insert save a model object to table"() {
        new Person(name: 'Spiderman', age: 30).save()

        sql.eachRow("select *  from person") {
            assert it.name == 'Spiderman'
        }
    }

    void "test id property is created on model object and is set to null"() {
        def person = new Person(name: 'Spiderman', age: 30)
        assert person.hasProperty("id")
        assert person.id == null
    }

    void "test id is assigned to model after save"() {
        def person = new Person(name: 'Spiderman', age: 30)
        person.save()
        assertNotNull person.id
        println person.id

    }

    void "test where selects from table with where clause"() {
        new Person(name: 'Batman', age: 35).save()
        new Person(name: 'Spiderman', age: 30).save()

        assert Person.where("age > 30").collect {it.name} == ["Batman"]
    }
}
