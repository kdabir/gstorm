package gstorm

import groovy.sql.Sql
import groovy.util.logging.Slf4j
import gstorm.builders.CreateTableQueryBuilder
import gstorm.helpers.SqlObjectFactory
import gstorm.metadata.ClassMetaData

import java.sql.Connection
import java.util.logging.Level

@Slf4j
class Gstorm {
    Sql sql

    /**
     * Constructs Gstorm using in-memory hsqldb database
     */
    Gstorm(){
        this(SqlObjectFactory.memoryDB())
    }

    /**
     * Constructs Gstorm using disk based (persistent) hsqldb database
     *
     * @param dbPath the path of the database
     */
    Gstorm(String dbPath){
        this(SqlObjectFactory.fileDB(dbPath))
    }

    /**
     * Constructs Gstorm using provided Connection
     *
     * @param connection instance of java.sql.Connection
     */
    Gstorm(Connection connection){
        this(new Sql(connection))
    }

    /**
     * Constructs Gstorm using provided Sql instance
     *
     * @param connection instance of groovy.sql.Sql
     */
    Gstorm(Sql sql) {
        this.sql = sql
    }

    /**
     * Adds CRUD methods to the modelClass. Also creates table for class if does not exist already.
     *
     * @param modelClass
     */
    Gstorm stormify(Class modelClass) {
        ClassMetaData classMetaData = new ClassMetaData(modelClass)
        createTableFor(classMetaData)
        new ModelClassEnhancer(classMetaData, sql).enhance()
        return this
    }

    private def createTableFor(ClassMetaData metaData) {
        sql.execute(new CreateTableQueryBuilder(metaData).build())
    }

    def setCsvFile(Class modelClass, String filePath, boolean readOnly=false) {
        def tableName = new ClassMetaData(modelClass).tableName
        sql.execute("""SET TABLE $tableName SOURCE "$filePath" ${(readOnly)?"DESC":""}""".trim().toString())
        return this
    }

    def enableQueryLogging() {
        def sqlMetaClass = Sql.class.metaClass

        sqlMetaClass.invokeMethod = { String name, args ->
            if (args) log.info(args.first()) // so far the first arg has been the query.
            sqlMetaClass.getMetaMethod(name, args).invoke(delegate, args)
        }
    }
}
