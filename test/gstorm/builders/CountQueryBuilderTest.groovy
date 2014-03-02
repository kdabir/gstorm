package gstorm.builders

import gstorm.metadata.ClassMetaData

class CountQueryBuilderTest extends GroovyTestCase {

    class Person {
        Integer id
        def name
        int age
    }

    CountQueryBuilder builder
    ClassMetaData classMetaData

    void setUp() {
        classMetaData = new ClassMetaData(Person.class)
        builder = new CountQueryBuilder(classMetaData)
    }

    void "test builds default query if nothing else is provided"() {
        assert builder.build().toLowerCase() == 'select count(*) as "count" from person'
    }

    void "test builds query with given where clause"() {
        assert builder.where("age > 18").build().toLowerCase() == 'select count(*) as "count" from person where age > 18'
    }

}

