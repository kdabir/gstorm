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
        String createSatement = new CreateTableQueryBuilder(metaData).build()
        sql.execute(createSatement)
    }

    def addStaticDmlMethodsTo(ClassMetaData metaData) {
        final modelMetaClass = metaData.modelClass.metaClass

        modelMetaClass.static.where = { clause ->
            sql.rows(new SelectQueryBuilder(metaData).where(clause).build())
        }

        modelMetaClass.static.get = { id ->
            final result = sql.rows(new SelectQueryBuilder(metaData).where("ID = ?").build(), [id])
            (result) ? result.first() : null
        }

        def getAll = {
            sql.rows(new SelectQueryBuilder(metaData).build())
        }
        // alias
        modelMetaClass.static.getAll = getAll
        modelMetaClass.static.all = getAll
    }

    def addInstanceDmlMethodsTo(ClassMetaData metaData) {
        final modelMetaClass = metaData.modelClass.metaClass
        final fieldNames = metaData.fields*.name

        modelMetaClass.id = null // add id

        modelMetaClass.save = {
            if (delegate.id == null) {
                final values = fieldNames.collect { delegate.getProperty(it)}
                final generated_ids = sql.executeInsert(new InsertQueryBuilder(metaData).build(), values)
                delegate.id = generated_ids[0][0]
            } else {
                final values = fieldNames.collect { delegate.getProperty(it)} << delegate.id
                sql.executeUpdate(new UpdateQueryBuilder(metaData).build(), values)
            }
            delegate
        }

        modelMetaClass.delete = {
            if (delegate.id != null) { sql.execute(new DeleteQueryBuilder(metaData).build(), [delegate.id]) }
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
