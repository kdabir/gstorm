package gstorm.builders

import gstorm.metadata.ClassMetaData

class UpdateQueryBuilder extends QueryBuilderSupport{

    UpdateQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
        final fieldNames = classMetaData.fieldNames
        final placeholders = fieldNames.collect { "${it} = ?" }.join(", ")

        this.query = new StringBuilder("UPDATE ${classMetaData.tableName} SET ${placeholders}")
    }

    def where(String clause) {
        query.append(SPACE).append("WHERE ${clause}")
        this
    }

    String build() {
        query.toString()
    }
}
