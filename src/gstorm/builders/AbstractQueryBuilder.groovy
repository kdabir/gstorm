package gstorm.builders

import gstorm.metadata.ClassMetaData

abstract class AbstractQueryBuilder {
    public static final String SPACE = " "

    ClassMetaData classMetaData
    StringBuilder query

    AbstractQueryBuilder(ClassMetaData classMetaData) {
        this.classMetaData = classMetaData
    }

    abstract String build()
}
