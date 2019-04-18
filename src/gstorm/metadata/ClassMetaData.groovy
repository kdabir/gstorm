package gstorm.metadata

import groovy.transform.CompileStatic
import gstorm.Csv
import gstorm.Id
import gstorm.Table
import gstorm.WithoutId

import java.lang.reflect.Field

@CompileStatic
class ClassMetaData {
    final Class modelClass
    final String tableName
    final FieldMetaData idField
    private final List<FieldMetaData> fields
    private Map<String, FieldMetaData> _fieldsCache        // just to avoid iterating over list of fields and finding by name.

    ClassMetaData(Class modelClass) {
        this.modelClass = modelClass
        this.tableName = extractTableName(modelClass)
        this.idField = getIdFieldOfClass(modelClass)
        this.fields = getOtherFieldsOfClass(modelClass)
        this._fieldsCache = this.fields.collectEntries { fieldMetaData -> [fieldMetaData.name, fieldMetaData] }
    }

    FieldMetaData getAt(String fieldName) {
        this._fieldsCache[fieldName]
    }

    List<FieldMetaData> getFields() {
        Collections.unmodifiableList(this.fields);
    }

    List<String> getFieldNames() {
        Collections.unmodifiableList(this.fields*.name);
    }

    String getIdFieldName() {
        idField?.name
    }

    private String extractTableName(Class modelClass) {
        ( (Table) modelClass.getAnnotation(Table))?.value()?.trim() ?: modelClass.simpleName
    }

    boolean isWithoutId() {
        modelClass.isAnnotationPresent(WithoutId)
    }

    boolean isCsv(){
        modelClass.isAnnotationPresent(Csv)
    }

    private List<FieldMetaData> getOtherFieldsOfClass(Class modelClass) {
        fieldsDeclaredIn(modelClass)
                .findAll { !it.isAnnotationPresent(Id) }
                .collect { field -> new FieldMetaData(field) }
    }

    private FieldMetaData getIdFieldOfClass(Class modelClass) {
        def idField = fieldsDeclaredIn(modelClass).find { it.isAnnotationPresent(Id) }
        idField ? new FieldMetaData(idField) : null
    }

    private List<Field> fieldsDeclaredIn(Class modelClass) {
        modelClass.declaredFields.findAll { !it.synthetic }.asList()
    }

}
