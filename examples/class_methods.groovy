/**
 * This script demonstrates the usage of class level methods
 */
@GrabResolver(name='gstorm', root='http://dl.bintray.com/kdabir/maven') @Grab('gstorm:gstorm:0.6-dev')
@GrabConfig(systemClassLoader = true) @Grab('org.hsqldb:hsqldb:2.2.9')
import groovy.sql.*
import gstorm.*
import java.util.logging.Level

def sql = Sql.newInstance("jdbc:hsqldb:mem:database", "sa", "", "org.hsqldb.jdbcDriver")
def g = new Gstorm(sql)
g.enableQueryLogging(Level.INFO)



class Person { String name; int age } // model class

g.stormify(Person)

// create some data
def batman = new Person(name: 'Batman', age: 35).save()
def spiderman = new Person(name: 'Spiderman', age: 30).save()

// use where
assert Person.where("age > 30").collect {it.name} == ["Batman"]

// get all
assert Person.all.collect {it.name} == sql.rows("select * from person").collect {it.name}

// get by id
assert Person.get(batman.id).name == "Batman"
assert Person.get(123) == null // when not found

// get count
assert Person.count == 2

// count with condition
assert Person.count("age > 30") == 1
