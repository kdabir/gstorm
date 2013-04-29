package gstorm

class ClassMetaData {
    final def modelClass, tableName
    final def fields

    ClassMetaData(Class modelClass) {
        this.modelClass = modelClass
        this.tableName = modelClass.simpleName
        this.fields = modelClass.declaredFields.findAll { !it.synthetic }.collectEntries { field ->
            final metaData = new FieldMetaData(field)
            [metaData.name, metaData]
        }
    }

    def getAt(String fieldName){
        this.fields[fieldName]
    }
}
