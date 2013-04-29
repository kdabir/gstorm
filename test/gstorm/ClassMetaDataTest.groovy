package gstorm

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

    void "test persistent field names"() {
        assert metadata.fields.keySet() == ["name", "description", "addedOn"] as Set
        assert metadata.fields.values()*.name == ["name", "description", "addedOn"]
    }

    void "test persistent field types"() {
        assert metadata.fields.values()*.type == [String, Object, Date]
    }

    void "test column names"() {
        assert metadata.fields.values()*.columnName == ["name", "description", "addedOn"]
    }

    void "test column types"() {
        assert metadata.fields.values()*.columnType == ["VARCHAR(255)", "VARCHAR(255)", "TIMESTAMP"]
    }

    void "test map like access to field"() {
        assert metadata["description"].columnName == "description"
        assert metadata["description"].type == Object
    }

}
