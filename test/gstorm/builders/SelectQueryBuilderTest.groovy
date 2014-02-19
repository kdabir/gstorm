package gstorm.builders

import gstorm.metadata.ClassMetaData

class SelectQueryBuilderTest extends GroovyTestCase {

    class Person {
        Integer id
        def name
        int age
    }

    SelectQueryBuilder builder
    ClassMetaData classMetaData

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

    void "test builds query by id"() {
        assert builder.byId().build().toLowerCase() == "select * from person where id = ?"
    }

}

