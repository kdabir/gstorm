GStorm - Groovy Single Table ORM
---
GStorm is a light-weight persistence helper that lets you persist data without any boilerplate code. Also it can act as
and ORM for CSV files.

#### Example
```groovy
class Person { String name, project }       // this is your model class

def g = new Gstorm()
g.stormify(Person)                          // table automatically gets created for this class

def person = new Person(name: "kunal", project: "gstorm")

person.save()                               // which saves object to db

def result = Person.where("name = 'kunal'") // pass any standard SQL where clause
println result

println "all records -> ${Person.all}"      // get all objects from db

person.name = "kunal dabir"
person.save()                               // saves the object back to db

println Person.get(person.id)               // loads the object by id

person.delete()                             // delete the person from db

```

#### Running it
To see gstorm in action just execute following from command line:

```bash
groovy https://raw.github.com/kdabir/gstorm/master/examples/getting_started.groovy
``` 

Provided you have [groovy installed](http://groovy-lang.org/install.html), you don't need to install
anything else.

----

### Why Gstorm?

In groovy scripts, when you need persistence, and you often feel hibernate is overkill but handwritten SQL is headache.
You don't want to connect to external Database server and still wish you could just create a table and save objects as
quickly as possible. You hate writing SQL for trivial CRUD but still want to be able to harness the power of SQL when
need be. You can then try Gstorm.

Gstorm take very simple approach to solve this problem by focusing just on needs of persistence mechanism required for
small scripts/projects.

The market is already flooded with ORMs and NoSQL databases. It's pointless to introduce anything that's heavy, complex,
rigid and tries to solve all the problems in the world.

#### What good a Single Table ORM would do? 

- Creates Tables for you
- Allows you to persist objects without worrying about CRUD SQL statements
- Very light layer, no jar baggage
- Can still fire complex SQL queries that would have been difficult in NoSQL or plain collection backed DBs
- Typically this would be useful in scripts, Not in multi-layered web applications or any complex use cases
- Doesn't handle any relationships or complex data types

Gstorm uses HSQLDB syntax internally.

---

##Getting started

GStorm is available in [jcenter](http://jcenter.bintray.com/io/github/kdabir/gstorm/gstorm) repository
Grab GStorm using 

```groovy
@GrabConfig(systemClassLoader = true)
@Grab('io.github.kdabir.gstorm:gstorm:0.7.1')
```

Create instance of `Gstorm`

```groovy
def g = new Gstorm()
```

You may pass optionally pass object of `java.sql.Connection` or `groovy.sql.Sql`

```groovy
def g = new Gstorm(sql)
```

where `sql` is an instance of `groovy.sql.Sql`

And gstormify your model

```groovy
g.stormify(Person)
```


Just go through the [example's source ](examples) and [test](test/gstorm) and have fun.

---

## Project status

[![Build Status](https://travis-ci.org/kdabir/gstorm.png)](https://travis-ci.org/kdabir/gstorm)


Gstorm is tiny project with very specific use case. There is long list of items that I would want to complete before calling it
feature complete. The project is under active development and is not yet suitable for production grade applications.

## Changelog :

### v0.7.1
- infrastructural changes to build and release
- added to jcenter repo

### v0.7
- support `@WithoutId` annotation to map to tables that do not have any id field and hence classes will not have id specific methods
- first cut of support for `@Csv` annotation to open CSV files as table. File can be set using `setCsvFile()` method of Gstorm.
- capability to provide custom type mappings as well changing the default type mapping if no type mapping is found
- ability to chain `stormify` and return the `Gstorm` instance

### v0.6
 - support `count()` method and `count` property on Model class. `count(clause)` can take a where like condition
 - support `Gstorm(Connection)` which can take `java.sql.Connection` object
 - support `Gstorm()` constructor which create in memory db (HSQLDB) automatically
 - support `Gstorm(dbpath)` constructor which creates HSQLDB with the specified file path
 - add `hsqldb` as compile/runtime dependency to project, so it will be used by default. User can always exclude it if it's not required.

### v0.5
 - support `@Id` annotation. limitation: The id has to be a numeric (Integer) field in class
 - heavy refactoring internally, keeping the api intact

### v0.4
 - support primitive numbers (`int`/`long`). Floating point numbers are stored as string as of yet.
 - Support table name to be different from the class name. Class can be annotated with `@Table("TABLE_NAME")` to specify table name.
 - support date/time to be stored.
 - internal refactoring

### v0.3
 - `enableQueryLogging` to enable sql query logging
 - `get()` to load a model by id

### v0.2
 - autogenerated `id` property on model
 - ability to `update()` or `save()` when model has `id`
 - using gradle to publish to maven repo
 - travis ci
 - added `all` method/property

### v0.1
 - initial prototype
 - `save()` and `where()` methods added
