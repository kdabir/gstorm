package gstorm.builders

import gstorm.metadata.ClassMetaData
import models.ClassWithCsvAnnotation
import models.ClassWithIdAnnotation
import models.ClassWithoutId
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

    void "test builds query without id"() {
        CreateTableQueryBuilder builder = new CreateTableQueryBuilder(new ClassMetaData(ClassWithoutId.class))

        assert builder.build() == "CREATE TABLE IF NOT EXISTS ClassWithoutId (" +
                "name VARCHAR(255), " +
                "description VARCHAR(255))"
    }

    void "test builds query for text tables for CSV annotated classes"() {
        CreateTableQueryBuilder builder = new CreateTableQueryBuilder(new ClassMetaData(ClassWithCsvAnnotation.class))

        assert builder.build() == "CREATE TEXT TABLE IF NOT EXISTS ClassWithCsvAnnotation (" +
                "ID NUMERIC GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                "name VARCHAR(255))"
    }
}

