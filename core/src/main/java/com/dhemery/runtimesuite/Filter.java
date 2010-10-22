package com.dhemery.runtimesuite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Marks a public field as a class filter or method filter in a runtime suite.
 * {@code RuntimeSuite} executes each filter to determine which test classes and test methods to run.
 * </p>
 * <p>
 * Class filters must implement the {@link ClassFilter} interface.
 * Method filters must implement the {@link MethodFilter} interface.
 * </p>
 * @author Dale H. Emery
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Filter {}