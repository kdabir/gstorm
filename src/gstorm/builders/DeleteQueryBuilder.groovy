package gstorm.builders

import gstorm.metadata.ClassMetaData

class DeleteQueryBuilder extends AbstractWhereableQueryBuilder {

    DeleteQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
        this.query = new StringBuilder("DELETE FROM ${classMetaData.tableName}")
    }

}
