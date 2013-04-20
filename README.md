GStorm - Groovy Single Table ORM
---

The market is already flooded with ORMs and NoSQL databases. It's pointless to introduce anything that's heavy, complex,
rigid and tries to solve all the problems in the world.

What good a Single Table ORM would do? 

- allows you to persist objects without worrying about CRUD SQL statements (so cliche !!)
- very light layer, no jar baggage, no magic or code generation. (Tell me something new!)
- Can still fire complex SQL queries that would have been difficult in NoSQL or plain collection backed DBs
- typically would be useful in scripts, Not in multi-layered web applications or any complex scenario.

This is just the basic idea. ~~I have not yet written a single line of code yet :)~~

Gstorm uses HSQLDB syntax internally.

---------------

Simple example :

```groovy
@GrabConfig(systemClassLoader = true) @Grab('org.hsqldb:hsqldb:2.0.0')
import groovy.sql.Sql
def sql = Sql.newInstance("jdbc:hsqldb:mem:database", "sa", "", "org.hsqldb.jdbcDriver")

// this is your model class
class Person { String name, project }

def g = new Gstorm(sql)
g.stormify(Person) // table automatically gets created for this class

def person = new Person(name: "kunal", project: "gstorm")
person.save() // model automatically gets this method

def result = Person.where("name = 'kunal'") // pass any standard SQL where clause
println result
```
