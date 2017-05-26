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
                    """|id,name,age
                       |1,test,10
                       |2,best,20
                   """.stripMargin("|")
                }
            }
        }

        gstorm = new Gstorm("tmp/csvtest/db").stormify(ClassWithCsvAnnotation)
        sql = gstorm.sql
    }

    @After
    void tearDown() {
        sql.execute("drop table ClassWithCsvAnnotation if exists")
        sql.close()
    }

    @Test
    void "should load the data from CSV"() {
        gstorm.setCsvFile(ClassWithCsvAnnotation, 'test1.csv;ignore_first=true')

        assert ClassWithCsvAnnotation.all.size() == 2
    }

    @Test
    void "should write the data to CSV"() {
        gstorm.setCsvFile(ClassWithCsvAnnotation, 'test1.csv;ignore_first=true')
        new ClassWithCsvAnnotation(name: "another_text_123").save()

        assert ClassWithCsvAnnotation.all.size() == 3
        assert new File("tmp/csvtest/test1.csv").text.contains("another_text_123")
    }

    @Test(expected = java.sql.SQLException)
    void "should be able to open csv as read only"() {
        gstorm.setCsvFile(ClassWithCsvAnnotation, 'test1.csv;ignore_first=true', true)
        new ClassWithCsvAnnotation(name: "another_text_123").save()
    }
}
