package gstorm.metadata

import gstorm.Table

class ClassMetaData {
    final Class modelClass
    final String tableName
    private final List<FieldMetaData> fields
    private Map _fieldsCache        // just to avoid iterating over list of fields and finding by name.

    ClassMetaData(Class modelClass) {
        this.modelClass = modelClass
        this.tableName = extractTableName(modelClass)
        this.fields = getFieldsOfClass(modelClass)
        this._fieldsCache = this.fields.collectEntries {fieldMetaData -> [fieldMetaData.name, fieldMetaData]}
    }

    FieldMetaData getAt(String fieldName) {
        this._fieldsCache[fieldName]
    }

    List<FieldMetaData> getFields(){
        Collections.unmodifiableList(this.fields);
    }

    List<String> getFieldNames(){
        Collections.unmodifiableList(this.fields*.name);
    }

    private String extractTableName(Class modelClass) {
        modelClass.getAnnotation(Table)?.value()?.trim() ?: modelClass.simpleName
    }

    private List<FieldMetaData> getFieldsOfClass(Class modelClass) {
        modelClass.declaredFields.findAll { !it.synthetic }.collect { field -> new FieldMetaData(field) }
    }

}
