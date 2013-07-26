package gstorm.builders

import gstorm.metadata.ClassMetaData

class SelectQueryBuilder {
    private static final String SPACE = " "
    ClassMetaData classMetaData
    StringBuilder query

    SelectQueryBuilder(ClassMetaData classMetaData) {
        this.classMetaData = classMetaData
        this.query = new StringBuilder("SELECT * FROM ${classMetaData.tableName}")
    }

    def where(String clause){
        query.append(SPACE)append("WHERE $clause")
        this
    }

    String build() {
        query.toString()
    }
}
