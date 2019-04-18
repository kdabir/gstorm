package gstorm.builders

import groovy.transform.CompileStatic
import gstorm.metadata.ClassMetaData

@CompileStatic
class CountQueryBuilder extends AbstractWhereableQueryBuilder {

    CountQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
        this.query = new StringBuilder('SELECT count(*) as "count"') // be careful with quotes here
                .append(SPACE).append("FROM ${classMetaData.tableName}")
    }

}
