package gstorm.builders

import gstorm.metadata.ClassMetaData

/**
 * represents a builder that can have a where clause
 */
abstract class AbstractWhereableQueryBuilder extends AbstractQueryBuilder {

    AbstractWhereableQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

    def where(String clause) {
        query.append(SPACE).append("WHERE ${clause}")
        this
    }

    def byId() {
        this.where(classMetaData.idFieldName ?: "id" + " = ?")
    }

    @Override
    String build() {
        query.toString()
    }
}
