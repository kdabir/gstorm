package gstorm

import groovy.sql.Sql
import gstorm.builders.DeleteQueryBuilder
import gstorm.builders.InsertQueryBuilder
import gstorm.builders.SelectQueryBuilder
import gstorm.builders.UpdateQueryBuilder
import gstorm.metadata.ClassMetaData

class ModelClassEnhancer {
    ClassMetaData metaData
    Sql sql

    ModelClassEnhancer(ClassMetaData classMetaData, Sql sql) {
        this.metaData = classMetaData
        this.sql = sql
    }

    public void enhance() {
        addStaticDmlMethods()
        addInstanceDmlMethods()
    }

    private def addStaticDmlMethods() {
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

    private def addInstanceDmlMethods() {
        final modelMetaClass = metaData.modelClass.metaClass
        final fieldNames = metaData.fieldNames
        final insertQuery = new InsertQueryBuilder(metaData).build()
        final updateQuery = new UpdateQueryBuilder(metaData).where("id = ?").build()
        final deleteQuery = new DeleteQueryBuilder(metaData).where("id = ?").build()

        modelMetaClass.id = null // add id

        modelMetaClass.save = {
            if (delegate.id == null) {
                final values = fieldNames.collect { delegate.getProperty(it) }
                final generated_ids = sql.executeInsert(insertQuery, values)
                delegate.id = generated_ids[0][0] // pretty stupid way to extract it
            } else {
                final values = fieldNames.collect { delegate.getProperty(it) } << delegate.id
                sql.executeUpdate(updateQuery, values)
            }
            delegate
        }

        modelMetaClass.delete = {
            if (delegate.id != null) {
                sql.execute(deleteQuery, [delegate.id])
            }
            delegate
        }
    }

}
