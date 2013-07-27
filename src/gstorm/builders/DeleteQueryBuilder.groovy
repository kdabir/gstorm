package gstorm.builders

import gstorm.metadata.ClassMetaData

class DeleteQueryBuilder extends QueryBuilderSupport {

    DeleteQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
        this.query = new StringBuilder("DELETE FROM ${classMetaData.tableName}")
    }

    def where(String clause) {
        query.append(SPACE).append("WHERE ${clause}")
        this
    }

    String build() {
        query.toString()
    }
}
