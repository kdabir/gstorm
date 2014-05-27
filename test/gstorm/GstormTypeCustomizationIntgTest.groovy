package gstorm

import groovy.sql.Sql
import gstorm.helpers.TypeMapper
import models.Item


class GstormTypeCustomizationIntgTest extends GroovyTestCase {
    Gstorm gstorm
    Sql sql


    void setUp() {
        TypeMapper.instance.reset()

        TypeMapper.instance.setDefaultType("VARCHAR(64)")
        TypeMapper.instance.setType((java.lang.String), "VARCHAR(16)")

        sql = Sql.newInstance("jdbc:hsqldb:mem:randomdb;shutdown=true", "sa", "", "org.hsqldb.jdbc.JDBCDriver")
        gstorm = new Gstorm(sql)

        gstorm.stormify(Item)

    }

    void tearDown() {
        sql.execute("DROP TABLE ITEM IF EXISTS")
        sql.close()

        TypeMapper.instance.reset()
    }

    void "test should customize the types"() {
        println TypeMapper.instance.defaultType
        def hsqldbMappings = sql
                .rows("select COLUMN_NAME, DTD_IDENTIFIER from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = 'ITEM'")
                .collectEntries { [it.column_name, it.dtd_identifier] }

        assert hsqldbMappings["NAME"] == "VARCHAR(16)"
        assert hsqldbMappings["DESCRIPTION"] == "VARCHAR(64)"
    }
}
