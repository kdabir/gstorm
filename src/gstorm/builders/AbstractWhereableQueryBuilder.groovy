package gstorm.builders

import groovy.transform.CompileStatic
import gstorm.metadata.ClassMetaData

/**
 * represents a builder that can have a where clause
 */
@CompileStatic
abstract class AbstractWhereableQueryBuilder extends AbstractQueryBuilder {

    AbstractWhereableQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

    def where(String clause) {
        query.append(SPACE).append("WHERE ${clause}")
        this
    }

    def byId() {
        def id = classMetaData.idFieldName ?: "id"
        this.where("${id} = ?")
    }

    @Override
    String build() {
        query.toString()
    }
}
