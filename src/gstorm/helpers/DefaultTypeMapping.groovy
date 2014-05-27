package gstorm.helpers

class DefaultTypeMapping {
    public static final String DEFAULT_TYPE = "VARCHAR(255)"

    public static final Map<Class, String> DEFAULT_MAPPINGS = Collections.unmodifiableMap([
            (int)               : "NUMERIC",
            (java.lang.Integer) : "NUMERIC",
            (long)              : "NUMERIC",
            (java.lang.Long)    : "NUMERIC",
            (java.util.Date)    : "TIMESTAMP",
            (java.sql.Timestamp): "TIMESTAMP",
            (java.sql.Date)     : "DATE",
            (java.sql.Time)     : "TIME"
    ])
}
