package gstorm.builders

import gstorm.metadata.ClassMetaData

class CountQueryBuilder extends AbstractWhereableQueryBuilder {

    CountQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
        this.query = new StringBuilder('SELECT count(*) as "count"') // be careful with quotes here
                .append(SPACE).append("FROM ${classMetaData.tableName}")
    }

}
