package gstorm.builders

import gstorm.metadata.ClassMetaData

class SelectQueryBuilderTest extends GroovyTestCase {

    class Person {
        def name
        int age
    }

    def builder
    def classMetaData

    void setUp() {
        classMetaData = new ClassMetaData(Person.class)
        builder = new SelectQueryBuilder(classMetaData)
    }

    void "test if builder is created" () {
        assertNotNull builder
    }

    void "test builds default query if nothing else is provided"() {
        assert builder.build().toLowerCase() == "select * from person"
    }

    void "test builds query with given where clause"() {
        assert builder.where("name = 'test'").build().toLowerCase() == "select * from person where name = 'test'"
    }

}

