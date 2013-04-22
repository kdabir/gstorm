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
        def id_column_def = "ID NUMERIC GENERATED ALWAYS AS IDENTITY PRIMARY KEY"
        final ddl = "CREATE TABLE IF NOT EXISTS $table_name ($id_column_def, $column_defs)".toString()
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
        def table_name = modelClass.simpleName
        final fields = modelClass.declaredFields.findAll { !it.synthetic }.collect { it.name}
        def columns = fields.join ","
        modelClass.metaClass.id = null // add id

        modelClass.metaClass.save = {
            if (delegate.id == null) {
                def values = fields.collect { "'${delegate.getProperty(it)}'" }.join(",")
                final insertStmt = "INSERT INTO $table_name ($columns) values ($values)".toString()
                println insertStmt
                def generted_ids = sql.executeInsert(insertStmt)
                delegate.id = generted_ids[0][0]
            } else {
                def values = fields.collect { "${it}='${delegate.getProperty(it)}'" }.join(" ,")
                final updateStmt = "UPDATE $table_name SET $values WHERE ID=${delegate.id}".toString()
                println updateStmt
                sql.executeUpdate(updateStmt)
            }
            delegate
        }

        modelClass.metaClass.delete = {
            if (delegate.id != null) {
                final deleteStmt = "DELETE FROM $table_name WHERE ID=${delegate.id}".toString()
                println deleteStmt
                sql.execute(deleteStmt)
            }
            delegate
        }
    }

}
