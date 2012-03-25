import spock.lang.*

@Grapes(
	@Grab(group='org.spockframework', module='spock-core', version='0.6-groovy-1.8')
)
class GStormStart extends Specification {
 def "should be able to find the class"() {
   expect:
   new GStorm().hello() == "hello world"
 }
}