package gstorm.builders

import gstorm.metadata.ClassMetaData

class DeleteQueryBuilder extends QueryBuilderSupport{

    DeleteQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

    String build() {
        "DELETE FROM ${classMetaData.tableName} WHERE ID = ?".toString() // TODO change id
    }
}
