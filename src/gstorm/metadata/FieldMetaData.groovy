package gstorm.metadata

import groovy.transform.CompileStatic
import gstorm.helpers.TypeMapper

import java.lang.reflect.Field

@CompileStatic
class FieldMetaData {
    Class type
    String name, columnName, columnType

    FieldMetaData(Field field) {
        this.type = field.type
        this.name = field.name
        this.columnType = TypeMapper.instance.getSqlType(field.type)
        this.columnName = field.name
    }
}
