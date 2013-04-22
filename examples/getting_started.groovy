@GrabResolver(name='gstorm', root='http://kdabir.github.io/mavenrepo/') @Grab('gstorm:gstorm:0.1')
@GrabConfig(systemClassLoader = true) @Grab('org.hsqldb:hsqldb:2.2.9')
import groovy.sql.*
import gstorm.*

def sql = Sql.newInstance("jdbc:hsqldb:mem:database", "sa", "", "org.hsqldb.jdbcDriver")

class Person { String name, project } // this is your model class

def g = new Gstorm(sql)
g.stormify(Person) // table automatically gets created for this class

def person = new Person(name: "kunal", project: "gstorm")
person.save() // model automatically gets this method

new Person(name: "test", project: "other").save() // save one more

def result = Person.where("name = 'kunal'") // pass any standard SQL where clause
println "result -> $result"