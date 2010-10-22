package com.dhemery.runtimesuite.internal;

import static java.lang.String.format;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.dhemery.runtimesuite.ClassFilter;

/**
 * <p>
 * A filter that accepts each class that declares one or more test methods.
 * This filter is applied automatically by {@link com.dhemery.runtimesuite.RuntimeSuite}
 * and is not intended for public use.
 * </p>
 * @author Dale H. Emery
 */
public class ClassesWithTestMethods implements ClassFilter {
	private final Log log = LogFactory.getLog(ClassesWithTestMethods.class); 

	@Override
	public boolean passes(Class<?> candidateClass) {
		for(Method method : candidateClass.getMethods()) {
			if(method.isAnnotationPresent(Test.class)) {
				log.info(format("%s has test methods", candidateClass));
				return true;
			}
		}
		log.info(format("%s has no test methods", candidateClass));
		return false;
	}

}
