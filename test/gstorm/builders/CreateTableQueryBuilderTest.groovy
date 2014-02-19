package gstorm.builders

import gstorm.metadata.ClassMetaData
import models.ClassWithIdAnnotation
import models.Person

class CreateTableQueryBuilderTest extends GroovyTestCase {

    void "test if builder is created" () {
        CreateTableQueryBuilder builder = new CreateTableQueryBuilder(new ClassMetaData(Person.class))

        assertNotNull builder
    }

    void "test builds query with default id"() {
        CreateTableQueryBuilder builder = new CreateTableQueryBuilder(new ClassMetaData(Person.class))

        assert builder.build() == "CREATE TABLE IF NOT EXISTS Person (" +
                "ID NUMERIC GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                "name VARCHAR(255), age NUMERIC)"
    }

    void "test builds query with annotated id"() {
        CreateTableQueryBuilder builder = new CreateTableQueryBuilder(new ClassMetaData(ClassWithIdAnnotation.class))

        assert builder.build() == "CREATE TABLE IF NOT EXISTS ClassWithIdAnnotation (" +
                "uid NUMERIC GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                "name VARCHAR(255))"
    }
}

