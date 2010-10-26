package com.dhemery.runtimesuite;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import com.dhemery.runtimesuite.finders.ClassesOnClasspath;

import static java.lang.String.format;
import static org.fest.assertions.Assertions.*;

public class ClassesOnClasspathTest {
	Log log = LogFactory.getLog(ClassesOnClasspath.class);
	private static final String FINDER_EXAMPLES_PATH_FORMAT = "../%s/target/classes";

	@Before public void printSystemClasspath() {
		log.debug(format("System java.class.path is", System.getProperty("java.class.path")));
	}

	@Test public void findsAllClassesOnASingleElementClasspath() {
		String classpath = makeClasspath("classpath.a");
		Collection<Class<?>> foundClasses = new ClassesOnClasspath(classpath).find();
		assertThat(namesOf(foundClasses)).containsOnly("a.Test_a_1",
										"a.Test_a_2",
										"a.a.Test_aa_1",
										"a.a.Test_aa_2",
										"a.a.a.Test_aaa_1",
										"a.a.a.Test_aaa_2",
										"a.a.b.Test_aab_1",
										"a.a.b.Test_aab_2",
										"a.b.Test_ab_1",
										"a.b.Test_ab_2");
	}

	@Test public void findsAllClassesOnAMultipleElementClasspath() {
		String classpath = makeClasspath("classpath.a")
							+ File.pathSeparator
							+ makeClasspath("classpath.b");
		Collection<Class<?>> foundClasses = new ClassesOnClasspath(classpath).find();
		assertThat(namesOf(foundClasses)).containsOnly("a.Test_a_1",
												"a.Test_a_2",
												"a.a.Test_aa_1",
												"a.a.Test_aa_2",
												"a.a.a.Test_aaa_1",
												"a.a.a.Test_aaa_2",
												"a.a.b.Test_aab_1",
												"a.a.b.Test_aab_2",
												"a.b.Test_ab_1",
												"a.b.Test_ab_2",
												"b.Test_b_1",
												"b.Test_b_2",
												"b.a.Test_ba_1",
												"b.a.Test_ba_2",
												"b.b.Test_bb_1",
												"b.b.Test_bb_2",
												"b.b.a.Test_bba_1",
												"b.b.a.Test_bba_2",
												"b.b.b.Test_bbb_1",
												"b.b.b.Test_bbb_2");
	}
	
	@Test public void ignoresNonClassFiles() {
		// classpath.c contains
		//    - ./c/Test_c_1.class
		//    - ./c/not-a-test.txt
		String classpath = makeClasspath("classpath.c");
		Collection<Class<?>> foundClasses = new ClassesOnClasspath(classpath).find();
		assertThat(namesOf(foundClasses)).containsOnly("c.Test_c_1");
	}
	
	@Test public void ignoresNonTestClasses() {
		// classpath.d contains
		//    - ./d/Test_d_1.class
		//    - ./d/NotATest_d_2.class
		String classpath = makeClasspath("classpath.d");
		Collection<Class<?>> foundClasses = new ClassesOnClasspath(classpath).find();
		assertThat(namesOf(foundClasses)).containsOnly("d.Test_d_1");
	}
	
	@Test public void ignoresNonDirectoryClasspathElements() {
		Collection<Class<?>> foundClasses = new ClassesOnClasspath("no.such.directory").find();
		assertThat(foundClasses).isEmpty();
	}

	private String makeClasspath(String path) {
		return String.format(FINDER_EXAMPLES_PATH_FORMAT, path);
	}

	private Collection<String> namesOf(Collection<Class<?>> classes) {
		Collection<String> names = new ArrayList<String>();
		for(Class<?> c : classes) {
			names.add(c.getName());
		}
		return names;
	}
}
