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

    // context : create table
    void "test that a table is created for stormified class"() {
        assert sql.rows("select * from INFORMATION_SCHEMA.TABLES where TABLE_NAME = 'PERSON'").size() == 1
    }

    void "test table has columns defined in the class "() {
        def columns_names = sql.rows("select COLUMN_NAME from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = 'PERSON'").collect {it.column_name}

        ["NAME", "AGE"].each { assert columns_names.contains(it)}
    }

    // context : id, create table
    void "test that created table has id column"() {
        assert sql.rows("select COLUMN_NAME from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = 'PERSON'").collect {it.column_name}.contains("ID")
    }

    void "test id property is created on model object and is set to null"() {
        def person = new Person(name: 'Spiderman', age: 30)

        assert person.hasProperty("id")
        assert person.id == null
    }

    // context : save
    void "test that save inserts a model object to table"() {
        new Person(name: 'Spiderman', age: 30).save()

        sql.eachRow("select *  from person") { assert it.name == 'Spiderman' }
    }

    void "test that generated id is assigned to model after save"() {
        def person = new Person(name: 'Spiderman', age: 30).save()

        sql.eachRow("select *  from person") { assert it.id == person.id }
    }

    void "test if an object has id, it should be updated"() {
        def person = new Person(name: 'Spiderman', age: 30).save()

        person.name = 'Batman'
        person.save()

        println sql.rows("select *  from person")
        sql.eachRow("select *  from person") { assert it.name == 'Batman' }
    }

    // context : delete
    void "test delete if object has id"() {
        def person = new Person(name: 'Spiderman', age: 30).save()
        person.delete()

        assert sql.rows("select count(*) as total_count from person").total_count == [0]
    }

    void "test should not delete if object is not saved"() {
        def person = new Person(name: 'Spiderman', age: 30)
        assert sql.rows("select count(*) as total_count from person").total_count == [0]
        person.delete()

        assert sql.rows("select count(*) as total_count from person").total_count == [0]
    }

    // context : where
    void "test where selects from table with where clause"() {
        new Person(name: 'Batman', age: 35).save()
        new Person(name: 'Spiderman', age: 30).save()

        assert Person.where("age > 30").collect {it.name} == ["Batman"]
    }

    // context : get all
    void "test all lists all records in table"() {
        new Person(name: 'Batman', age: 35).save()
        new Person(name: 'Spiderman', age: 30).save()

        assert Person.all.collect {it.name} == sql.rows("select * from person").collect {it.name}
    }

    // context : find
    void "test get should find model by id"() {
        def batman = new Person(name: 'Batman', age: 35).save()
        def spiderman = new Person(name: 'Spiderman', age: 30).save()

        assert Person.get(batman.id).name == "Batman"
    }

    void "test find model by id when id doesnt exist"() {
        def batman = new Person(name: 'Batman', age: 35).save()
        def spiderman = new Person(name: 'Spiderman', age: 30).save()

        assert Person.get(123) == null // lets not complecate it by exceptions
    }
}
