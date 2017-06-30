package edu.cmu.cs.mvelezce.test.util.repeat;

/**
 * Created by mvelezce on 6/30/17.
 */

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({
        java.lang.annotation.ElementType.METHOD
})

public @interface Repeat {
    public abstract int times();
}
