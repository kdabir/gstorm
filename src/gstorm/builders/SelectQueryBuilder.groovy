package gstorm.builders

import groovy.transform.CompileStatic
import gstorm.metadata.ClassMetaData

@CompileStatic
class SelectQueryBuilder extends AbstractWhereableQueryBuilder {

    SelectQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
        this.query = new StringBuilder("SELECT * FROM ${classMetaData.tableName}")
    }

}
