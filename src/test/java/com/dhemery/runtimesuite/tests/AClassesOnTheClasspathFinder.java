package com.dhemery.runtimesuite.tests;

import java.util.Collection;


import org.junit.Ignore;
import org.junit.Test;


import a.Test_a_1;
import a.Test_a_2;
import a._1.Test_a1_1;
import a._1.Test_a1_2;
import a._1._a.Test_a1a_1;
import a._1._a.Test_a1a_2;
import a._1._b.Test_a1b_1;
import a._1._b.Test_a1b_2;
import a._2.Test_a2_1;
import a._2.Test_a2_2;
import b.Test_b_1;
import b.Test_b_2;
import b._1.Test_b1_1;
import b._1.Test_b1_2;
import b._2.Test_b2_1;
import b._2.Test_b2_2;
import b._2._a.Test_b2a_1;
import b._2._a.Test_b2a_2;
import b._2._b.Test_b2b_1;
import b._2._b.Test_b2b_2;

import com.dhemery.runtimesuite.ClassFinder;
import com.dhemery.runtimesuite.ClassesOnClassPath;

import static org.fest.assertions.Assertions.*;

public class AClassesOnTheClasspathFinder {
	@Test public void showClassPath() {
		System.out.println("Classpath: " + System.getProperty("java.class.path"));
	}
	@Ignore
	@Test public void findsAllClassesOnASingleElementClasspath() {
		ClassFinder finder = new ClassesOnClassPath("./classpath.one");
		Collection<Class<?>> found = finder.find();
		assertThat(found).containsOnly(Test_a_1.class,
										Test_a_2.class,
										Test_a1_1.class,
										Test_a1_2.class,
										Test_a1a_1.class,
										Test_a1a_2.class,
										Test_a1b_1.class,
										Test_a1b_2.class,
										Test_a2_1.class,
										Test_a2_2.class);
	}

	@Ignore
	@Test public void findsAllClassesOnAMultipleElementClasspath() {
		ClassFinder finder = new ClassesOnClassPath("./classpath.one:./classpath.two");
		Collection<Class<?>> found = finder.find();
		assertThat(found).containsOnly(Test_a_1.class,
										Test_a_2.class,
										Test_a1_1.class,
										Test_a1_2.class,
										Test_a1a_1.class,
										Test_a1a_2.class,
										Test_a1b_1.class,
										Test_a1b_2.class,
										Test_a2_1.class,
										Test_a2_2.class,
										Test_b_1.class,
										Test_b_2.class,
										Test_b1_1.class,
										Test_b1_2.class,
										Test_b2_1.class,
										Test_b2_2.class,
										Test_b2a_1.class,
										Test_b2a_2.class,
										Test_b2b_1.class,
										Test_b2b_2.class);
	}
}
