package gstorm.builders

import gstorm.metadata.ClassMetaData

class CreateTableQueryBuilderTest extends GroovyTestCase {

    class Person {
        def name
        int age
    }

    def builder
    def classMetaData

    void setUp() {
        classMetaData = new ClassMetaData(Person.class)
        builder = new CreateTableQueryBuilder(classMetaData)
    }

    void "test if builder is created" () {
        assertNotNull builder
    }

    void "test builds query with default id"() {
        assert builder.build() == "CREATE TABLE IF NOT EXISTS Person (ID NUMERIC GENERATED ALWAYS AS IDENTITY PRIMARY KEY, name VARCHAR(255), age NUMERIC)"
    }

}

