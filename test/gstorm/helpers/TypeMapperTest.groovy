package gstorm.helpers

import gstorm.metadata.ClassMetaData
import models.ClassWithNumbers

class TypeMapperTest extends GroovyTestCase {

    void tearDown() {
        TypeMapper.instance.reset()
    }

    void "test should be able to change default type" () {
        TypeMapper.instance.defaultType = "VARCHAR(50)"
        assert TypeMapper.instance.getSqlType(TypeMapperTest) == "VARCHAR(50)"
    }

    void "test should be able get Type Mapping of class" () {
        assert TypeMapper.instance.getSqlType(Integer) == "NUMERIC"
    }

    void "test should be able get Type Mapping of primitives" () {
        def primitive = new ClassMetaData(ClassWithNumbers)['age'].type
        assert primitive.name == "int"
        assert TypeMapper.instance.getSqlType(primitive) == "NUMERIC"
    }

    void "test should be able override Type Mapping" () {
        TypeMapper.instance.setType((java.lang.Integer), "SOME_SPECIFIC_TYPE")
        assert TypeMapper.instance.getSqlType(Integer) == "SOME_SPECIFIC_TYPE"
    }
}
