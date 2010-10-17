package com.dhemery.runtimesuite.finders;

import java.io.File;
import java.util.Collection;

import org.junit.Test;

import com.dhemery.runtimesuite.ClassFinder;
import com.dhemery.runtimesuite.finders.ClassesOnClassPath;

import static org.fest.assertions.Assertions.*;

public class AClassesOnClasspathFinder {
	@Test public void findsAllClassesOnASingleElementClasspath() {
		String classpath = "./target/test-resources/finder/classpath.one";
		ClassFinder finder = new ClassesOnClassPath(classpath);
		Collection<Class<?>> found = finder.find();
		assertThat(found).containsOnly(a.Test_a_1.class,
										a.Test_a_2.class,
										a._1.Test_a1_1.class,
										a._1.Test_a1_2.class,
										a._1._a.Test_a1a_1.class,
										a._1._a.Test_a1a_2.class,
										a._1._b.Test_a1b_1.class,
										a._1._b.Test_a1b_2.class,
										a._2.Test_a2_1.class,
										a._2.Test_a2_2.class);
	}

	@Test public void findsAllClassesOnAMultipleElementClasspath() {
		String classpath = "./target/test-resources/finder/classpath.one"
							+ File.pathSeparator
							+ "./target/test-resources/finder/classpath.two";
		ClassFinder finder = new ClassesOnClassPath(classpath);
		Collection<Class<?>> found = finder.find();
		assertThat(found).containsOnly(a.Test_a_1.class,
										a.Test_a_2.class,
										a._1.Test_a1_1.class,
										a._1.Test_a1_2.class,
										a._1._a.Test_a1a_1.class,
										a._1._a.Test_a1a_2.class,
										a._1._b.Test_a1b_1.class,
										a._1._b.Test_a1b_2.class,
										a._2.Test_a2_1.class,
										a._2.Test_a2_2.class,
										b.Test_b_1.class,
										b.Test_b_2.class,
										b._1.Test_b1_1.class,
										b._1.Test_b1_2.class,
										b._2.Test_b2_1.class,
										b._2.Test_b2_2.class,
										b._2._a.Test_b2a_1.class,
										b._2._a.Test_b2a_2.class,
										b._2._b.Test_b2b_1.class,
										b._2._b.Test_b2b_2.class);
	}
	
	@Test public void ignoresNonClassFiles() {
		// classpath.three has a non-class file ./c/not-a-test.txt
		String classpath = "./target/test-resources/finder/classpath.three";
		ClassFinder finder = new ClassesOnClassPath(classpath);
		Collection<Class<?>> found = finder.find();
		assertThat(found).containsOnly(c.Test_c_1.class);
	}
}
