package models

import gstorm.WithoutId

@WithoutId
class ClassWithoutId {
    String name
    def description
}
