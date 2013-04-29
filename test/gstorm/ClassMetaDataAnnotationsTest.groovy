package gstorm

class ClassMetaDataAnnotationsTest extends GroovyTestCase {

    @Table("TestTable")
    class TestSubject {
        String name
    }

    def metadata

    void setUp() {
        metadata = new ClassMetaData(TestSubject)
    }

    void "test tableName"() {
        assert metadata.tableName.equalsIgnoreCase("TestTable")
    }

}
