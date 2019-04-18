package gstorm.builders

import groovy.transform.CompileStatic
import gstorm.metadata.ClassMetaData

@CompileStatic
class DeleteQueryBuilder extends AbstractWhereableQueryBuilder {

    DeleteQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
        this.query = new StringBuilder("DELETE FROM ${classMetaData.tableName}")
    }

}
