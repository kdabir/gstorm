package gstorm

import groovy.sql.Sql
import groovy.util.logging.Log
import gstorm.builders.CreateTableQueryBuilder
import gstorm.builders.DeleteQueryBuilder
import gstorm.builders.InsertQueryBuilder
import gstorm.builders.SelectQueryBuilder
import gstorm.builders.UpdateQueryBuilder
import gstorm.metadata.ClassMetaData

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
        sql.execute(new CreateTableQueryBuilder(metaData).build())
    }

    private def addStaticDmlMethodsTo(ClassMetaData metaData) {
        final modelMetaClass = metaData.modelClass.metaClass
        def selectAllQuery = new SelectQueryBuilder(metaData).build()
        def selectByIdQuery = new SelectQueryBuilder(metaData).where("ID = ?").build()

        modelMetaClass.static.where = { clause ->
            sql.rows(new SelectQueryBuilder(metaData).where(clause).build())
        }

        modelMetaClass.static.get = { id ->
            final result = sql.rows(selectByIdQuery, [id])
            (result) ? result.first() : null
        }

        def getAll = {
            sql.rows(selectAllQuery)
        }
        // alias
        modelMetaClass.static.getAll = getAll
        modelMetaClass.static.all = getAll
    }

    private def addInstanceDmlMethodsTo(ClassMetaData metaData) {
        final modelMetaClass = metaData.modelClass.metaClass
        final fieldNames = metaData.fields*.name
        final insertQuery = new InsertQueryBuilder(metaData).build()
        final updateQuery = new UpdateQueryBuilder(metaData).where("id = ?").build()
        final deleteQuery = new DeleteQueryBuilder(metaData).where("id = ?").build()

        modelMetaClass.id = null // add id

        modelMetaClass.save = {
            if (delegate.id == null) {
                final values = fieldNames.collect { delegate.getProperty(it)}
                final generated_ids = sql.executeInsert(insertQuery, values)
                delegate.id = generated_ids[0][0]
            } else {
                final values = fieldNames.collect { delegate.getProperty(it)} << delegate.id
                sql.executeUpdate(updateQuery, values)
            }
            delegate
        }

        modelMetaClass.delete = {
            if (delegate.id != null) { sql.execute(deleteQuery, [delegate.id]) }
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
