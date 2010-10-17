package com.dhemery.runtimesuite.finders;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import com.dhemery.runtimesuite.finders.ClassesOnClassPath;

import static org.fest.assertions.Assertions.*;

public class AClassesOnClasspathFinder {
	@Test public void findsAllClassesOnASingleElementClasspath() {
		String classpath = makeClasspath("classpath.a");
		Collection<Class<?>> found = new ClassesOnClassPath(classpath).find();
		assertThat(namesOf(found)).containsOnly("a.Test_a_1",
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

	private Collection<String> namesOf(Collection<Class<?>> classes) {
		Collection<String> names = new ArrayList<String>();
		for(Class<?> each : classes) names.add(each.getName());
		return names;
	}

	private String makeClasspath(String path) {
		return String.format("./examples-for-testing/bin/finder/%s", path);
	}

	@Test public void findsAllClassesOnAMultipleElementClasspath() {
		String classpath = makeClasspath("classpath.a")
							+ File.pathSeparator
							+ makeClasspath("classpath.b");
		Collection<Class<?>> found = new ClassesOnClassPath(classpath).find();
		assertThat(namesOf(found)).containsOnly("a.Test_a_1",
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
		Collection<Class<?>> found = new ClassesOnClassPath(classpath).find();
		assertThat(namesOf(found)).containsOnly("c.Test_c_1");
	}
	
	@Test public void ignoresNonTestClasses() {
		// classpath.four contains
		//    - ./d/Test_d_1.class
		//    - ./d/NotATest_d_2.class
		String classpath = makeClasspath("classpath.d");
		Collection<Class<?>> found = new ClassesOnClassPath(classpath).find();
		assertThat(namesOf(found)).containsOnly("d.Test_d_1");
	}
	
	@Test public void ignoresNonDirectoryClasspathElements() {
		Collection<Class<?>> found = new ClassesOnClassPath("no.such.directory").find();
		assertThat(found).isEmpty();
	}
}
