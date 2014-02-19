package gstorm.builders

import gstorm.metadata.ClassMetaData

class CreateTableQueryBuilder extends AbstractQueryBuilder {

    CreateTableQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

    String build() {
        def tableName = classMetaData.tableName
        def columnDefs = classMetaData.fields.collect { field -> "${field.name} ${field.columnType}" }.join(", ")
        def idColumnDef = "${classMetaData.idFieldName ?: 'ID'} NUMERIC GENERATED ALWAYS AS IDENTITY PRIMARY KEY"

        "CREATE TABLE IF NOT EXISTS $tableName ($idColumnDef, $columnDefs)".toString()
    }
}
