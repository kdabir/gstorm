package gstorm.metadata

import gstorm.helpers.TypeMapper

import java.lang.reflect.Field

class FieldMetaData {
    String type, name, columnName, columnType

    FieldMetaData(Field field) {
        this.type = field.type
        this.name = field.name
        this.columnType = TypeMapper.instance.getSqlType(field.type)
        this.columnName = field.name
    }
}
