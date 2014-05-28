package gstorm

import groovy.sql.Sql
import models.Person
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test


class GstormPerfTest {
    Gstorm gstorm
    Sql sql
    def start, end

    @Before
    void setUp() {
        sql = Sql.newInstance("jdbc:hsqldb:mem:database", "sa", "", "org.hsqldb.jdbc.JDBCDriver")
        gstorm = new Gstorm(sql)
        gstorm.stormify(Person)
        start = System.nanoTime()
    }

    @After
    void tearDown() {
        sql.execute("drop table person if exists")
        sql.close()
    }

    @Test //@Ignore
    void "check the time taken for 1000 inserts"() {
        1000.times {new Person(name: 'Spiderman', age: 30).save()}
        assert sql.rows("select count(*) as total_count from person").total_count == [1000]
        printTimeTakenFor("1000 inserts")
    }

    void printTimeTakenFor(activity) {
        end = System.nanoTime()
        println "Time taken for $activity ${(end - start) / 1000000} ms"
    }

}

