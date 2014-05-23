package gstorm

import models.ClassWithNumbers
import models.Person
import org.junit.Test

import java.sql.Connection
import java.sql.DriverManager


class GstormConstructionTest {

    //context : creation
    @Test
    void "should create gstorm with instance of groovy Sql"() {
        def sql = groovy.sql.Sql.newInstance("jdbc:hsqldb:mem:database", "sa", "", "org.hsqldb.jdbc.JDBCDriver")
        def g = new Gstorm(sql)
        g.stormify(Person)

        assert Person.count == 0 // gstorm should work
        assert sql == g.sql
    }

    @Test
    void "should create gstorm with connection object"() {
        Class.forName("org.hsqldb.jdbcDriver");
        Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:database", "sa", "");
        def gstorm = new Gstorm(connection)
        gstorm.stormify(Person)

        assert Person.count == 0
        assert gstorm.sql.connection == connection
    }

    @Test
    void "should create gstorm with memory db with no constructor arg"() {
        def gstorm = new Gstorm()
        gstorm.stormify(Person) // should create table

        assert Person.count == 0 // gstorm should work
        assert "jdbc:hsqldb:mem:database" == gstorm.sql.connection.getMetaData().getURL()
    }

    @Test
    void "should create gstorm with file db with String constructor arg"() {
        def gstorm = new Gstorm("tmp/db/test-db")
        gstorm.stormify(Person) // should create table

        assert Person.count == 0 // gstorm should work
        assert "jdbc:hsqldb:file:tmp/db/test-db" == gstorm.sql.connection.getMetaData().getURL()
        assert new File("tmp/db/test-db.properties").exists()
    }

    @Test
    void "should be able to chain stormify"() {
        def gstorm = new Gstorm().stormify(Person).stormify(ClassWithNumbers) // should create table

        assert gstorm instanceof Gstorm
        assert Person.count == 0 // gstorm should work
        assert ClassWithNumbers.count == 0 // gstorm should work
    }

}
