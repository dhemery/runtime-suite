package com.dhemery.runtimesuite.filters;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks a class as being in one or more categories.
 * A category is an arbitrary class used as a marker.
 * You might declare categories as interfaces:
 * </p>
 * <pre>
 * public interface SmokeTests {}
 * public interface FeatureUnderDevelopment {}
 * </pre>
 * <p>
 * To put a class in one or more categories,
 * annotate it with {@code @Category},
 * and list the category classes in the annotation's value. For example: 
 * </p>
 * <pre>
 * &#64;Category(SmokeTest.class, FeatureUnderDevelopment.class)
 * public class MyTestClass {
 *     ...
 * }
 * </pre>
 * @author Dale H. Emery
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Category {
	Class<?>[] value();
}
