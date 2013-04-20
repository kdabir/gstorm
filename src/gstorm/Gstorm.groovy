package gstorm

import groovy.sql.Sql

class Gstorm {
    Sql sql

    Gstorm(Sql sql) {
        this.sql = sql
    }

    def stormify(Class c) {
        createTableFor(c)
        addStaticDmlMethodsTo(c)
        addInstanceDmlMethodsTo(c)
    }

    static def type_mappings = [:]
    static {
        type_mappings[Integer.TYPE] = "NUMERIC"
        type_mappings[Long.TYPE] = "NUMERIC"

    }
    static String getTypeMapping(it) {
        type_mappings[it] ?: "VARCHAR(255)"
    }


    def createTableFor(Class modelClass) {
        def table_name = modelClass.simpleName

        def column_defs = modelClass.declaredFields.findAll { !it.synthetic }.collect { "${it.name} ${getTypeMapping(it.type)}" }.join(", ")
        final ddl = "CREATE TABLE $table_name ($column_defs)".toString()
        println ddl
        sql.execute(ddl)
    }

    def addStaticDmlMethodsTo(Class modelClass) {
        modelClass.metaClass.static.where = { clause ->
            def table_name = delegate.simpleName
            final query = "SELECT * FROM $table_name WHERE $clause".toString()
            println query
            sql.rows(query)
        }
    }

    def addInstanceDmlMethodsTo(Class modelClass) {
        modelClass.metaClass.save = {
            def table_name = delegate.class.simpleName
            final fields = delegate.class.declaredFields.findAll { !it.synthetic }.collect { it.name}
            def columns = fields.join ","
            def values = fields.collect { "'${delegate.getProperty(it)}'" }.join(",")

            final insertStmt = "INSERT INTO $table_name ($columns) values ($values)".toString()
            println insertStmt
            sql.executeInsert(insertStmt)
        }
    }

}
