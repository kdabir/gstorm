package gstorm.builders

import gstorm.metadata.ClassMetaData

class SelectQueryBuilder extends QueryBuilderSupport{
    private static final String SPACE = " "
    StringBuilder query

    SelectQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
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
