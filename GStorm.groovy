@GrabConfig(systemClassLoader=true)
@Grapes([
    @Grab(group='org.hsqldb', module='hsqldb', version='2.2.7')
])
class GStorm {
    final Mode mode
    def hello (){
        return 'hello world'
    }
}

enum Mode {
    IM_SERVER, IM_EMBEDDED, FS_SERVER, FS_EMBEDDED
}