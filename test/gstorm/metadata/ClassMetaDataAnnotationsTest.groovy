package gstorm.metadata

import models.ClassWithIdAnnotation
import models.ClassWithTable
import models.ClassWithoutId


class ClassMetaDataAnnotationsTest extends GroovyTestCase {

    void "test tableName"() {
        ClassMetaData metadataWithTable = new ClassMetaData(ClassWithTable)

        assert metadataWithTable.tableName.equalsIgnoreCase("TestTable")
    }

    void "test Id"() {
        ClassMetaData metadataWithId = new ClassMetaData(ClassWithIdAnnotation)

        assert metadataWithId.idFieldName.equals("uid")
        assert metadataWithId.idField.name.equals("uid")
    }

    void "test WithoutId"() {
        ClassMetaData metadataWithId = new ClassMetaData(ClassWithoutId)

        assert metadataWithId.isWithoutId() == true
    }

    void "test Id should not be included in fields"() {
        ClassMetaData metadataWithId = new ClassMetaData(ClassWithIdAnnotation)

        assert !metadataWithId.fieldNames.contains("uid")
    }

}
