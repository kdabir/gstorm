package gstorm.builders

import gstorm.metadata.ClassMetaData

class UpdateQueryBuilder extends QueryBuilderSupport{

    UpdateQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

    String build() {
        final fieldNames = classMetaData.fields*.name
        final placeholders = fieldNames.collect { "${it} = ?" }.join(", ")

        "UPDATE ${classMetaData.tableName} SET ${placeholders} WHERE ID = ?".toString() // TODO change id
    }
}
