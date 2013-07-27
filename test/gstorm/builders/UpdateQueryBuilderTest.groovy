package gstorm.builders

import gstorm.metadata.ClassMetaData

class UpdateQueryBuilderTest extends GroovyTestCase {

    class Person {
        def name
        int age
    }

    def builder
    def classMetaData

    void setUp() {
        classMetaData = new ClassMetaData(Person.class)
        builder = new UpdateQueryBuilder(classMetaData)
    }

    void "test if builder is created" () {
        assertNotNull builder
    }

    void "test the generated update query" () {
        assert builder.build().toLowerCase() == "update person set name = ?, age = ?"
    }

    void "test the generated update query with where clause" () {
        assert builder.where("id = ?").build().toLowerCase() == "update person set name = ?, age = ? where id = ?"
    }

}

