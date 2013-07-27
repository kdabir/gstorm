package gstorm.builders

import gstorm.metadata.ClassMetaData

abstract class QueryBuilderSupport {
    public static final String SPACE = " "

    ClassMetaData classMetaData
    StringBuilder query

    QueryBuilderSupport(ClassMetaData classMetaData) {
        this.classMetaData = classMetaData
    }

    abstract String build()
}
