package gstorm

import groovy.sql.Sql
import example.Person

class GstormTest extends GroovyTestCase {

    Gstorm gstorm
    Sql sql

    void setUp() {
        sql = Sql.newInstance("jdbc:hsqldb:mem:database", "sa", "", "org.hsqldb.jdbc.JDBCDriver")
        gstorm = new Gstorm(sql)
    }

    void tearDown() {
        sql.execute("drop table person if exists")
        sql.close()
    }

    void "test createTable should create table for model"() {
        gstorm.createTable(Person)
        sql.execute("select * from person")
    }

    void "test insert should create table for model"() {
        gstorm.createTable(Person)
        gstorm.insert(new Person(name:'kunal', age:30))
        sql.eachRow("select *  from person") {
            assert it.name == 'kunal'
        }
    }
}
