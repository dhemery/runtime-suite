package com.dhemery.runtimesuite;

import java.io.File;
import java.util.Collection;

import org.junit.Test;

import com.dhemery.runtimesuite.finders.ClassesOnClasspath;

import static org.fest.assertions.Assertions.*;

public class ClassesOnClasspathsTest {
	private static final String FINDER_EXAMPLES_PATH = "./examples-for-testing/bin/finder/";

	@Test public void findsAllClassesOnASingleElementClasspath() {
		String classpath = makeClasspath("classpath.a");
		Collection<Class<?>> found = new ClassesOnClasspath(classpath).find();
		assertThat(found).containsOnly(a.Test_a_1.class,
										a.Test_a_2.class,
										a.a.Test_aa_1.class,
										a.a.Test_aa_2.class,
										a.a.a.Test_aaa_1.class,
										a.a.a.Test_aaa_2.class,
										a.a.b.Test_aab_1.class,
										a.a.b.Test_aab_2.class,
										a.b.Test_ab_1.class,
										a.b.Test_ab_2.class);
	}

	private String makeClasspath(String path) {
		return FINDER_EXAMPLES_PATH + path;
	}

	@Test public void findsAllClassesOnAMultipleElementClasspath() {
		String classpath = makeClasspath("classpath.a")
							+ File.pathSeparator
							+ makeClasspath("classpath.b");
		Collection<Class<?>> found = new ClassesOnClasspath(classpath).find();
		assertThat(found).containsOnly(a.Test_a_1.class,
												a.Test_a_2.class,
												a.a.Test_aa_1.class,
												a.a.Test_aa_2.class,
												a.a.a.Test_aaa_1.class,
												a.a.a.Test_aaa_2.class,
												a.a.b.Test_aab_1.class,
												a.a.b.Test_aab_2.class,
												a.b.Test_ab_1.class,
												a.b.Test_ab_2.class,
												b.Test_b_1.class,
												b.Test_b_2.class,
												b.a.Test_ba_1.class,
												b.a.Test_ba_2.class,
												b.b.Test_bb_1.class,
												b.b.Test_bb_2.class,
												b.b.a.Test_bba_1.class,
												b.b.a.Test_bba_2.class,
												b.b.b.Test_bbb_1.class,
												b.b.b.Test_bbb_2.class);
	}
	
	@Test public void ignoresNonClassFiles() {
		// classpath.c contains
		//    - ./c/Test_c_1.class
		//    - ./c/not-a-test.txt
		String classpath = makeClasspath("classpath.c");
		Collection<Class<?>> found = new ClassesOnClasspath(classpath).find();
		assertThat(found).containsOnly(c.Test_c_1.class);
	}
	
	@Test public void ignoresNonTestClasses() {
		// classpath.four contains
		//    - ./d/Test_d_1.class
		//    - ./d/NotATest_d_2.class
		String classpath = makeClasspath("classpath.d");
		Collection<Class<?>> found = new ClassesOnClasspath(classpath).find();
		assertThat(found).containsOnly(d.Test_d_1.class);
	}
	
	@Test public void ignoresNonDirectoryClasspathElements() {
		Collection<Class<?>> found = new ClassesOnClasspath("no.such.directory").find();
		assertThat(found).isEmpty();
	}
}
