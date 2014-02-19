package gstorm.builders

import gstorm.metadata.ClassMetaData

class SelectQueryBuilder extends QueryBuilderSupport {

    SelectQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
        this.query = new StringBuilder("SELECT * FROM ${classMetaData.tableName}")
    }

    def where(String clause) {
        query.append(SPACE).append("WHERE $clause")
        this
    }

    def byId() {
        this.where(classMetaData.idFieldName ?: "id" + " = ?")
        this
    }

    String build() {
        query.toString()
    }
}
