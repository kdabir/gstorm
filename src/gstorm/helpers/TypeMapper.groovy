package gstorm.helpers

import groovy.transform.CompileStatic

/**
 * Maintains global Java/Groovy to Sql type mappings. Mappings can be altered here.
 *
 */
@Singleton
@CompileStatic
class TypeMapper {
    private Map<Class, String> mappings = new HashMap<>(DefaultTypeMapping.DEFAULT_MAPPINGS)
    def defaultType = DefaultTypeMapping.DEFAULT_TYPE

    String getSqlType(Class fieldClass) {
        mappings.containsKey(fieldClass) ? mappings.get(fieldClass) : defaultType
    }

    TypeMapper setType(Class className, String sqlType) {
        mappings.put(className, sqlType)
        return this
    }

    void reset() {
        mappings = new HashMap<>(DefaultTypeMapping.DEFAULT_MAPPINGS)
        defaultType = DefaultTypeMapping.DEFAULT_TYPE
    }
}


