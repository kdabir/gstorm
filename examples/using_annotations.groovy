/**
 * Using Annotations
 */

@GrabConfig(systemClassLoader = true)
@Grab('io.github.kdabir.gstorm:gstorm:0.7.1')
@Grab('org.hsqldb:hsqldb:2.3.2')
import groovy.sql.*
import gstorm.*

// using sql object explicitly to create Gstorm instance, this can be any other jdbc driver
def sql = Sql.newInstance("jdbc:hsqldb:mem:database", "sa", "", "org.hsqldb.jdbcDriver")

@Table("Employees") // if have a different table name
class Employee {
    @Id Integer empId // annotate Id if you dont want want implicit id
    String name
    String department
}

new Gstorm(sql).stormify(Employee)

new Employee(name: "John Doe", department: "Accounts").save()
new Employee(name: "Donald Duck", department: "Marketing").save()
new Employee(name: "Alfred Hitchcock", department: "Marketing").save()

Employee.where("department = 'Marketing'").each { // pass any standard SQL where clause
    println "${it.name} is in Marketing"
}
