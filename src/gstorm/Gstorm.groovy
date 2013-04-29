package gstorm

import groovy.sql.Sql
import groovy.util.logging.Log
import java.util.logging.Level

@Log
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
        type_mappings[Integer] = "NUMERIC"
        type_mappings[Long] = "NUMERIC"
        type_mappings[java.util.Date] = "TIMESTAMP"
        type_mappings[java.sql.Timestamp] = "TIMESTAMP"
        type_mappings[java.sql.Date] = "DATE"
        type_mappings[java.sql.Time] = "TIME"
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
        final table_name = modelClass.simpleName

        modelClass.metaClass.static.where = { clause ->
            sql.rows("SELECT * FROM $table_name WHERE $clause".toString())
        }

        modelClass.metaClass.static.get = { id ->
            final result = sql.rows("SELECT * FROM $table_name WHERE ID = $id".toString())
            (result) ? result.first() : null
        }

        def getAll = {
            sql.rows("SELECT * FROM $table_name".toString())
        }
        // alias
        modelClass.metaClass.static.getAll = getAll
        modelClass.metaClass.static.all = getAll
    }

    def addInstanceDmlMethodsTo(Class modelClass) {
        final table_name = modelClass.simpleName
        final fields = modelClass.declaredFields.findAll { !it.synthetic }.collect { it.name }

        modelClass.metaClass.id = null // add id

        modelClass.metaClass.save = {
            if (delegate.id == null) {
                final columns = fields.join ", "
                final placeholders = fields.collect {"?"}.join(", ")
                final values = fields.collect { delegate.getProperty(it)}
                final generated_ids = sql.executeInsert("INSERT INTO $table_name ($columns) values (${placeholders})".toString(), values)
                delegate.id = generated_ids[0][0]
            } else {
                final placeholders = fields.collect { "${it} = ?" }.join(", ")
                final values = fields.collect { delegate.getProperty(it)}
                sql.executeUpdate("UPDATE $table_name SET $placeholders WHERE ID = ${delegate.id}".toString(), values)
            }
            delegate
        }

        modelClass.metaClass.delete = {
            if (delegate.id != null) { sql.execute("DELETE FROM $table_name WHERE ID = ${delegate.id}".toString()) }
            delegate
        }
    }

    def enableQueryLogging(level = Level.FINE) {
        def sqlMetaClass = Sql.class.metaClass

        sqlMetaClass.invokeMethod = { String name, args ->
            if (args) log.log(level, args.first()) // so far the first arg has been the query.
            sqlMetaClass.getMetaMethod(name, args).invoke(delegate, args)
        }
    }
}
