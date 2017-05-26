package models

import gstorm.Csv

@Csv
class ClassWithCsvAnnotation {
    String name
    Integer age =0
}
