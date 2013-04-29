package gstorm

import java.lang.annotation.Retention
import static java.lang.annotation.RetentionPolicy.RUNTIME

@Retention(RUNTIME)
@interface Table {
    String value()
}
