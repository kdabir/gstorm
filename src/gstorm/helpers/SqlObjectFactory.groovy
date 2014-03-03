package gstorm.helpers

import groovy.sql.Sql

/**
 *
 */
class SqlObjectFactory {

    static Sql memoryDB() {
        Sql.newInstance('jdbc:hsqldb:mem:database', 'sa', '', 'org.hsqldb.jdbcDriver')
    }

    static Sql fileDB(String path) {
        Sql.newInstance('jdbc:hsqldb:file:' + path, 'sa', '', 'org.hsqldb.jdbcDriver')
    }
}
