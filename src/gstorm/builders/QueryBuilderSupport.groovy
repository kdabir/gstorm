package gstorm.builders

import gstorm.metadata.ClassMetaData

abstract class QueryBuilderSupport {
    ClassMetaData classMetaData

    QueryBuilderSupport(ClassMetaData classMetaData) {
        this.classMetaData = classMetaData
    }

    abstract String build()
}
