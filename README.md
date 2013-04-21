GStorm - Groovy Single Table ORM
---
Gstorm is very light persitence helper for simple scenarios such as groovy scripts.

#### Example
```groovy
class Person { String name, project } // this is your model class

def g = new Gstorm(sql)
g.stormify(Person) // table automatically gets created for this class

def person = new Person(name: "kunal", project: "gstorm")
person.save() // model automatically gets this method, which saves it to db

def result = Person.where("name = 'kunal'") // pass any standard SQL where clause
println result
```

#### Running it
To see gstorm in action just execute following from command line:

```bash
groovy https://raw.github.com/kdabir/gstorm/master/examples/getting_started.groovy
``` 

Provided you have [groovy installed](http://groovy.codehaus.org/Installing+Groovy), you dont need to install anything else.

----

In groovy scripts, when you need persistence, and you often feel hibernate is overkill but handwritten SQL is headache. You don't want to connect to external Database server and still wish you could just create a table and save objects as quickly as possible. You hate writing SQL for trivial CRUD but still want to be able to harness the power of SQL when need be. You can then try Gstorm.

Gstorm take very simple approach to solve this problem by focusing just on needs of persistence mechanism required for small scripts.

The market is already flooded with ORMs and NoSQL databases. It's pointless to introduce anything that's heavy, complex, rigid and tries to solve all the problems in the world. 

#### What good a Single Table ORM would do? 

- Creates Tables for you
- Allows you to persist objects without worrying about CRUD SQL statements (so cliche !!)
- Very light layer, no jar baggage
- Can still fire complex SQL queries that would have been difficult in NoSQL or plain collection backed DBs
- Typically this would be useful in scripts, Not in multi-layered web applications or any complex scenario
- Doesn't handle any relationships or complex data types

Gstorm uses HSQLDB syntax internally.

---

##Getting started

Grab GStorm using 
```groovy
@GrabResolver(name='gstorm', root='https://raw.github.com/kdabir/mavenrepo/gh-pages/') @Grab('gstorm:gstorm:0.1')
```

Include hsqldb
```groovy
@GrabConfig(systemClassLoader = true) @Grab('org.hsqldb:hsqldb:2.0.0')
```

and gstormify your model

```groovy
new Gstorm(sql).stormify(Person) 
```

where `sql` is an instance of `groovy.sql.Sql`

Just go through the [example's source ](examples/getting_started.groovy) and [test](test/gstorm/GstormTest.groovy) and have fun.

---

## Project status

[![Build Status](https://travis-ci.org/kdabir/gstorm.png)](https://travis-ci.org/kdabir/gstorm)


Gstorm is just few days old and few groovy lines long. There is long list of items that I would want to complete before calling it feature complete. The project is in development mode and should not be used in any production scenarios.
