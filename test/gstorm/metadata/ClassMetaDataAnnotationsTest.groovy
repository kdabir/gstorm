package gstorm.metadata

import gstorm.Id
import gstorm.Table

class ClassMetaDataAnnotationsTest extends GroovyTestCase {

    @Table("TestTable")
    class ClassWithTable {
        String name
    }

    class ClassWithIdAnnotation {
        @Id Integer uid
        String name
    }

    void "test tableName"() {
        ClassMetaData metadataWithTable = new ClassMetaData(ClassWithTable)

        assert metadataWithTable.tableName.equalsIgnoreCase("TestTable")
    }

    void "test Id"() {
        ClassMetaData  metadataWithId = new ClassMetaData(ClassWithIdAnnotation)

        assert metadataWithId.idFieldName.equals("uid")
        assert metadataWithId.idField.name.equals("uid")
    }

    void "test Id should not be included in fields"() {
        ClassMetaData  metadataWithId = new ClassMetaData(ClassWithIdAnnotation)

        assert !metadataWithId.fieldNames.contains("uid")
    }

}
