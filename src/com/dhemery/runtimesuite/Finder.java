package com.dhemery.runtimesuite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Marks a public field as a class finder in a runtime suite.
 * {@code RuntimeSuite} executes each finder to find test classes to run.
 * </p>
 * <p>
 * Class finders must implement the {@link ClassFinder} interface.
 * </p>
 * @author Dale H. Emery
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Finder {}