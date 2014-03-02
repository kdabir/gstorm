package gstorm

import groovy.sql.Sql
import groovy.util.logging.Log
import gstorm.builders.CreateTableQueryBuilder
import gstorm.metadata.ClassMetaData

import java.sql.Connection
import java.util.logging.Level

@Log
class Gstorm {
    Sql sql

    Gstorm(Connection connection){
        this.sql = new Sql(connection)
    }

    Gstorm(Sql sql) {
        this.sql = sql
    }

    def stormify(Class modelClass) {
        ClassMetaData classMetaData = new ClassMetaData(modelClass)
        createTableFor(classMetaData)
        new ModelClassEnhancer(classMetaData, sql).enhance()
    }

    private def createTableFor(ClassMetaData metaData) {
        sql.execute(new CreateTableQueryBuilder(metaData).build())
    }


    def enableQueryLogging(level = Level.FINE) {
        def sqlMetaClass = Sql.class.metaClass

        sqlMetaClass.invokeMethod = { String name, args ->
            if (args) log.log(level, args.first()) // so far the first arg has been the query.
            sqlMetaClass.getMetaMethod(name, args).invoke(delegate, args)
        }
    }
}
