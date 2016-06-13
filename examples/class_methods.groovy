/**
 * This script demonstrates the usage of class level methods
 */
@GrabConfig(systemClassLoader = true)
@Grab('io.github.kdabir.gstorm:gstorm:0.7.1')
import gstorm.*

class Person { String name; int age } // model class

def g = new Gstorm()
g.stormify(Person)

// create some data
def batman = new Person(name: 'Batman', age: 35).save()
def spiderman = new Person(name: 'Spiderman', age: 30).save()

// use where
assert Person.where("age > 30").collect {it.name} == ["Batman"]

// get all
assert Person.all.collect {it.name} == g.sql.rows("select * from person").collect {it.name}

// get by id
assert Person.get(batman.id).name == "Batman"
assert Person.get(123) == null // when not found

// get count
assert Person.count == 2

// count with condition
assert Person.count("age > 30") == 1
