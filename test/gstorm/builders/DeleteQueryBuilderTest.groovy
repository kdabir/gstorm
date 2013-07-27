package gstorm.builders

import gstorm.metadata.ClassMetaData

class DeleteQueryBuilderTest extends GroovyTestCase {

    class Person {
        def name
        int age
    }

    def builder
    def classMetaData

    void setUp() {
        classMetaData = new ClassMetaData(Person.class)
        builder = new DeleteQueryBuilder(classMetaData)
    }

    void "test if builder is created" () {
        assertNotNull builder
    }

    void "test the generated update query" () {
        assert builder.build().toLowerCase() == "delete from person where id = ?"
    }

}

