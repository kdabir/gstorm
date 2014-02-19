@GrabResolver(name='gstorm', root='http://dl.bintray.com/kdabir/maven') @Grab('gstorm:gstorm:0.5')
@GrabConfig(systemClassLoader = true) @Grab('org.hsqldb:hsqldb:2.2.9')
import groovy.sql.*
import gstorm.*
import java.util.logging.Level

def sql = Sql.newInstance("jdbc:hsqldb:mem:database", "sa", "", "org.hsqldb.jdbcDriver")

@Table("people")
class Person { String name, project } // this is your model class

def g = new Gstorm(sql)
g.enableQueryLogging(Level.INFO)

g.stormify(Person) // table automatically gets created for this class

def person = new Person(name: "kunal", project: "gstorm")
person.save() // model automatically gets this method

def other = new Person(name: "other", project: "test").save() // save one more
println "added ${other.name} to project ${other.project}"

other.project = "gstorm"
other.save() // update it

println "all records -> ${Person.all}"

Person.where("project = 'gstorm'").each { // pass any standard SQL where clause
    println "${it.name} is working on gstorm"
}

println "removing other"
other.delete()

println "all records -> ${Person.all}"
