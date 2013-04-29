package gstorm

import java.lang.reflect.Field

class FieldMetaData {
    def type, name, columnName, columnType

    FieldMetaData(Field field) {
        this.type = field.type
        this.name = field.name
        this.columnType = getTypeMapping(field.type)
        this.columnName = field.name
    }

    private def getTypeMapping(Class type) {
        switch (type) {
            case {it in [Integer, Long]}: "NUMERIC"
                break
            case {it in [java.util.Date, java.sql.Timestamp]}: "TIMESTAMP"
                break
            case java.sql.Date: "DATE"
                break
            case java.sql.Time: "TIME"
                break
            default: "VARCHAR(255)"
        }
    }
}
