package com.dhemery.runtimesuite.tests;

import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;

import com.dhemery.runtimesuite.ClassFinder;
import com.dhemery.runtimesuite.ClassesOnClassPath;

import static org.fest.assertions.Assertions.*;

public class AClassesOnClasspathFinder {
	@Test public void findsAllClassesOnASingleElementClasspath() {
		ClassFinder finder = new ClassesOnClassPath("./target/test-resources/finder/classpath.one");
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

	@Ignore
	@Test public void findsAllClassesOnAMultipleElementClasspath() {
		ClassFinder finder = new ClassesOnClassPath("./target/test-resources/finder/classpath.one:./target/test-resources/finder/classpath.two");
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
}
