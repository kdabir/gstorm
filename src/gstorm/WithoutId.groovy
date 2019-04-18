package gstorm

import groovy.transform.CompileStatic

import java.lang.annotation.Retention

import static java.lang.annotation.RetentionPolicy.RUNTIME

@Retention(RUNTIME)
@CompileStatic
@interface WithoutId {
}
