package gstorm


class SelectQueryBuilder {
    private static final String SPACE = " "
    ClassMetaData classMetaData
    StringBuilder query

    SelectQueryBuilder(ClassMetaData classMetaData) {
        this.classMetaData = classMetaData
        this.query = new StringBuilder("SELECT * from ${classMetaData.tableName}")
    }

    def where(String clause){
        query.append(SPACE)append("where $clause")
        this
    }

    String build() {
        query.toString()
    }
}
