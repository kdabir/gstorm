package gstorm.builders

import groovy.transform.CompileStatic
import gstorm.metadata.ClassMetaData

@CompileStatic
abstract class AbstractQueryBuilder {
    public static final String SPACE = " "

    ClassMetaData classMetaData
    StringBuilder query

    AbstractQueryBuilder(ClassMetaData classMetaData) {
        this.classMetaData = classMetaData
    }

    abstract String build()
}
