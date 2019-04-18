package gstorm.builders

import groovy.transform.CompileStatic
import gstorm.metadata.ClassMetaData

@CompileStatic
class UpdateQueryBuilder extends AbstractWhereableQueryBuilder {

    UpdateQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
        final fieldNames = classMetaData.fieldNames
        final placeholders = fieldNames.collect { "${it} = ?" }.join(", ")

        this.query = new StringBuilder("UPDATE ${classMetaData.tableName} SET ${placeholders}")
    }

}
