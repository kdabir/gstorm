package gstorm

import groovy.sql.Sql

class Gstorm {

    Sql sql

    static def type_mappings = [:]
    static {
        type_mappings[Integer.TYPE] = "NUMERIC"
        type_mappings[Long.TYPE] = "NUMERIC"
    }

    Gstorm(Sql sql) {
        this.sql = sql
    }

    def createTable(Class modelClass) {
        def table_name = modelClass.simpleName
        def column_defs = modelClass.declaredFields.findAll { !it.synthetic }.collect { "${it.name} ${getTypeMapping(it.type)}" }.join(", ")
        final ddl = "CREATE TABLE $table_name ($column_defs)".toString()

        println ddl
        sql.execute(ddl)
    }

    def insert(model) {
        def table_name = model.class.simpleName
        final fields = model.class.declaredFields.findAll { !it.synthetic }.collect { it.name}
        def columns = fields.join ","
        def values = fields.collect { "'${model.getProperty(it)}'" }.join(",")

        final ddl = "INSERT INTO $table_name ($columns) values ($values)".toString()
        sql.executeInsert(ddl)
    }

    static String getTypeMapping(it) {
        type_mappings[it] ?: "VARCHAR(255)"
    }
}
