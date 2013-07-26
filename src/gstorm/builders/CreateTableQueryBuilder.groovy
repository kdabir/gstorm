package gstorm.builders

import gstorm.metadata.ClassMetaData

class CreateTableQueryBuilder {
    ClassMetaData classMetaData

    CreateTableQueryBuilder(ClassMetaData classMetaData) {
        this.classMetaData = classMetaData
    }

    String build() {
        def table_name = classMetaData.tableName
        def column_defs = classMetaData.fields.collect { field -> "${field.name} ${field.columnType}" }.join(", ")
        def id_column_def = "ID NUMERIC GENERATED ALWAYS AS IDENTITY PRIMARY KEY"

        "CREATE TABLE IF NOT EXISTS $table_name ($id_column_def, $column_defs)".toString()
    }
}
