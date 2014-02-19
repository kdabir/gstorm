package gstorm

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.Target

import static java.lang.annotation.RetentionPolicy.RUNTIME

@Retention(RUNTIME)
@Target([ElementType.FIELD, ElementType.LOCAL_VARIABLE])
@interface Id {

}