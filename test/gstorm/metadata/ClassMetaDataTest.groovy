package gstorm.metadata

class ClassMetaDataTest extends GroovyTestCase {

    class TestSubject {
        String name
        def description
        Date addedOn
    }

    def metadata

    void setUp() {
        metadata = new ClassMetaData(TestSubject)
    }

    void "test should create MetaData for subject"() {
        assertNotNull(metadata)
    }

    void "test tableName"() {
        assert metadata.tableName.equalsIgnoreCase("TestSubject")
    }

    void "test field are unmodifiable externally"() {
        shouldFail(UnsupportedOperationException) { metadata.fields << ["name"] }
    }

    void "test persistent field names"() {
        assert metadata.fields*.name == ["name", "description", "addedOn"]
    }

    void "test persistent field names by accessor"() {
        assert metadata.fieldNames == ["name", "description", "addedOn"]
    }

    void "test persistent field types"() {
        assert metadata.fields*.type == [String, Object, Date]
    }

    void "test column names"() {
        assert metadata.fields*.columnName == ["name", "description", "addedOn"]
    }

    void "test column types"() {
        assert metadata.fields*.columnType == ["VARCHAR(255)", "VARCHAR(255)", "TIMESTAMP"]
    }

    void "test map like access to field"() {
        assert metadata["description"].columnName == "description"
        assert metadata["description"].type == Object
    }

}
