package gstorm

import groovy.sql.Sql
import groovy.util.logging.Log

@Log
class Gstorm {
    Sql sql

    Gstorm(Sql sql) {
        this.sql = sql
    }

    def stormify(Class c) {
        enableQueryLogging()
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

        sql.execute("CREATE TABLE IF NOT EXISTS $table_name ($id_column_def, $column_defs)".toString())
    }

    def addStaticDmlMethodsTo(Class modelClass) {
        modelClass.metaClass.static.where = { clause ->
            def table_name = delegate.simpleName
            sql.rows("SELECT * FROM $table_name WHERE $clause".toString())
        }

         def getAll = {
            def table_name = delegate.simpleName
             sql.rows("SELECT * FROM $table_name".toString())
        }
        // alias
        modelClass.metaClass.static.getAll = getAll
        modelClass.metaClass.static.all = getAll
    }

    def addInstanceDmlMethodsTo(Class modelClass) {
        def table_name = modelClass.simpleName
        final fields = modelClass.declaredFields.findAll { !it.synthetic }.collect { it.name}
        def columns = fields.join ","
        modelClass.metaClass.id = null // add id

        modelClass.metaClass.save = {
            if (delegate.id == null) {
                def values = fields.collect { "'${delegate.getProperty(it)}'" }.join(",")
                def generted_ids = sql.executeInsert("INSERT INTO $table_name ($columns) values ($values)".toString())
                delegate.id = generted_ids[0][0]
            } else {
                def values = fields.collect { "${it}='${delegate.getProperty(it)}'" }.join(" ,")
                sql.executeUpdate("UPDATE $table_name SET $values WHERE ID=${delegate.id}".toString())
            }
            delegate
        }

        modelClass.metaClass.delete = {
            if (delegate.id != null) {
                sql.execute("DELETE FROM $table_name WHERE ID=${delegate.id}".toString())
            }
            delegate
        }
    }

    def enableQueryLogging() {
        def sqlMetaClass = Sql.class.metaClass

        sqlMetaClass.invokeMethod = { String name, args ->
            if (args) log.fine args.first() // so far the first arg has been the query.
            sqlMetaClass.getMetaMethod(name, args).invoke(delegate, args)
        }
    }
}
