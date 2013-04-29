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

    def stormify(Class modelClass) {
        ClassMetaData classMetaData = new ClassMetaData(modelClass)
        createTableFor(classMetaData)
        addStaticDmlMethodsTo(classMetaData)
        addInstanceDmlMethodsTo(classMetaData)
    }

    private def createTableFor(ClassMetaData metaData) {
        def table_name = metaData.tableName
        def column_defs = metaData.fields.collect {field -> "${field.name} ${field.columnType}" }.join(", ")
        def id_column_def = "ID NUMERIC GENERATED ALWAYS AS IDENTITY PRIMARY KEY"

        sql.execute("CREATE TABLE IF NOT EXISTS $table_name ($id_column_def, $column_defs)".toString())
    }

    def addStaticDmlMethodsTo(ClassMetaData metaData) {
        final modelMetaClass = metaData.modelClass.metaClass
        final table_name = metaData.tableName

        modelMetaClass.static.where = { clause ->
            sql.rows("SELECT * FROM $table_name WHERE $clause".toString())
        }

        modelMetaClass.static.get = { id ->
            final result = sql.rows("SELECT * FROM $table_name WHERE ID = $id".toString())
            (result) ? result.first() : null
        }

        def getAll = {
            sql.rows("SELECT * FROM $table_name".toString())
        }
        // alias
        modelMetaClass.static.getAll = getAll
        modelMetaClass.static.all = getAll
    }

    def addInstanceDmlMethodsTo(ClassMetaData metaData) {
        final modelMetaClass = metaData.modelClass.metaClass
        final table_name = metaData.tableName
        final fieldNames = metaData.fields*.name

        modelMetaClass.id = null // add id

        modelMetaClass.save = {
            if (delegate.id == null) {
                final columns = fieldNames.join ", "
                final placeholders = fieldNames.collect { "?" }.join(", ")
                final values = fieldNames.collect { delegate.getProperty(it)}
                final generated_ids = sql.executeInsert("INSERT INTO $table_name ($columns) values (${placeholders})".toString(), values)
                delegate.id = generated_ids[0][0]
            } else {
                final placeholders = fieldNames.collect { "${it} = ?" }.join(", ")
                final values = fieldNames.collect { delegate.getProperty(it)}
                sql.executeUpdate("UPDATE $table_name SET $placeholders WHERE ID = ${delegate.id}".toString(), values)
            }
            delegate
        }

        modelMetaClass.delete = {
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
