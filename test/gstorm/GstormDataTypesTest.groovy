package gstorm

import groovy.sql.Sql
import java.text.SimpleDateFormat
import java.util.logging.Level

class GstormDataTypesTest extends GroovyTestCase {
    Gstorm gstorm
    Sql sql
    def df

    class ClassWithDates {
        String name
        Date dateOfBirth
    }

    void setUp() {
        sql = Sql.newInstance("jdbc:hsqldb:mem:database", "sa", "", "org.hsqldb.jdbc.JDBCDriver")
        gstorm = new Gstorm(sql)
        gstorm.enableQueryLogging(Level.INFO)
        gstorm.stormify(ClassWithDates)
        df = new SimpleDateFormat("d/M/yyyy")
    }

    void tearDown() {
        sql.execute("drop table classwithdates if exists")
        sql.close()
    }

    void "test if Date can be saved"() {
        def cwd = new ClassWithDates(name: "newborn", dateOfBirth: new Date()).save()

        assert ClassWithDates.get(cwd.id).dateOfBirth instanceof Date
    }

    void "test if Date can be updated"() {
        def cwd = new ClassWithDates(name: "nicedate", dateOfBirth: df.parse("20/10/2010")).save()
        cwd.dateOfBirth = df.parse("20/11/2011")
        cwd.save()

        assert ClassWithDates.get(cwd.id).dateOfBirth == df.parse("20/11/2011")
    }

}

