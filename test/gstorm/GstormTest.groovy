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
    }


    void "test that insert save a model object to table"() {
        new Person(name: 'Spiderman', age: 30).save()

        sql.eachRow("select *  from person") {
            assert it.name == 'Spiderman'
        }
    }

    void "test where selects from table with where clause"() {
        new Person(name: 'Batman', age: 35).save()
        new Person(name: 'Spiderman', age: 30).save()

        assert Person.where("age > 30").collect {it.name} == ["Batman"]
    }
}
