package models

import gstorm.Id

class ClassWithIdAnnotation {
    @Id
    Integer uid
    String name
}
