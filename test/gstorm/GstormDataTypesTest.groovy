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

    class ClassWithNumbers {
        String name
        int age
        long points
        float percentage
    }

    void setUp() {
        sql = Sql.newInstance("jdbc:hsqldb:mem:database", "sa", "", "org.hsqldb.jdbc.JDBCDriver")
        gstorm = new Gstorm(sql)
        gstorm.enableQueryLogging(Level.INFO)
        gstorm.stormify(ClassWithDates)
        gstorm.stormify(ClassWithNumbers)
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

    void "test if Numbers can be saved"() {
        def cwn = new ClassWithNumbers(name: "test", age: 1, percentage: 10.23, points: 123456789098765).save()

        final age = ClassWithNumbers.get(cwn.id).age

        assert age == 1 // it will come out as class: java.math.BigDecimal
    }

    void "test if Numbers can be updated"() {
        def cwn = new ClassWithNumbers(name: "test", age: 1, points: 123456789098765).save()
        cwn.age = 12
        cwn.points = 98765431123456789
        cwn.save()

        final updated = ClassWithNumbers.get(cwn.id)
        assert updated.age == 12
        assert updated.points == 98765431123456789
    }

    void "test if floats are store as string"() {
        def cwn = new ClassWithNumbers(name: "test", age: 1, percentage: 10.12).save()

        final retrievedPercentage = ClassWithNumbers.get(cwn.id).percentage
        assert retrievedPercentage == "10.12"
        assert retrievedPercentage.class == String
    }

}

