package gstorm

import directree.DirTree
import groovy.sql.Sql
import models.ClassWithCsvAnnotation
import org.junit.After
import org.junit.Before
import org.junit.Test

class GstormCsvIntgTest {

    Gstorm gstorm
    Sql sql

    @Before
    void setUp() {
        DirTree.create("tmp") {
            dir("csvtest") {
                file("test1.csv") {
                    """|id,name
                       |1,test
                   """.stripMargin("|")
                }
            }
        }
        sql = Sql.newInstance("jdbc:hsqldb:file:tmp/csvtest/db", "sa", "", "org.hsqldb.jdbc.JDBCDriver")
        gstorm = new Gstorm(sql)
        gstorm.stormify(ClassWithCsvAnnotation)
    }

    @After
    void tearDown() {
        sql.execute("drop table ClassWithCsvAnnotation if exists")
        sql.close()
    }

    @Test
    void "should load the data from CSV"() {
        gstorm.setCsvFile(ClassWithCsvAnnotation, 'test1.csv;ignore_first=true')

        assert ClassWithCsvAnnotation.all.size() == 1
    }

    @Test
    void "should write the data to CSV"() {
        gstorm.setCsvFile(ClassWithCsvAnnotation, 'test1.csv;ignore_first=true')
        new ClassWithCsvAnnotation(name: "another_text_123").save()

        assert ClassWithCsvAnnotation.all.size() == 2
        assert new File("tmp/csvtest/test1.csv").text.contains("another_text_123")
    }

    @Test(expected = java.sql.SQLException)
    void "should be able to open csv as read only"() {
        gstorm.setCsvFile(ClassWithCsvAnnotation, 'test1.csv;ignore_first=true', true)
        new ClassWithCsvAnnotation(name: "another_text_123").save()
    }
}
