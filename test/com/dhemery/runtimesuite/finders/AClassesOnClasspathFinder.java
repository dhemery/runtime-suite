package com.dhemery.runtimesuite.finders;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dhemery.runtimesuite.finders.ClassesOnClasspath;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class AClassesOnClasspathFinder {
	Logger log = LoggerFactory.getLogger(ClassesOnClasspath.class);

	@Before public void printSystemClasspath() {
		log.debug("System java.class.path is {}", System.getProperty("java.class.path"));
	}

	@Test public void findsAllClassesOnASingleElementClasspath() {
		String classpath = "testbin/classpath.a";
		Collection<Class<?>> foundClasses = new ClassesOnClasspath(classpath).find();
		assertThat(namesOf(foundClasses), hasItems("example.classpath.a.Test_a_1",
										"example.classpath.a.Test_a_2",
										"example.classpath.a.a.Test_aa_1",
										"example.classpath.a.a.Test_aa_2",
										"example.classpath.a.a.a.Test_aaa_1",
										"example.classpath.a.a.a.Test_aaa_2",
										"example.classpath.a.a.b.Test_aab_1",
										"example.classpath.a.a.b.Test_aab_2",
										"example.classpath.a.b.Test_ab_1",
										"example.classpath.a.b.Test_ab_2"));
	}

	@Test public void findsAllClassesOnAMultipleElementClasspath() {
		String classpath = "testbin/classpath.a"
						+ File.pathSeparator
						+ "testbin/classpath.b";
		Collection<Class<?>> foundClasses = new ClassesOnClasspath(classpath).find();
		assertThat(namesOf(foundClasses), hasItems("example.classpath.a.Test_a_1",
												"example.classpath.a.Test_a_2",
												"example.classpath.a.a.Test_aa_1",
												"example.classpath.a.a.Test_aa_2",
												"example.classpath.a.a.a.Test_aaa_1",
												"example.classpath.a.a.a.Test_aaa_2",
												"example.classpath.a.a.b.Test_aab_1",
												"example.classpath.a.a.b.Test_aab_2",
												"example.classpath.a.b.Test_ab_1",
												"example.classpath.a.b.Test_ab_2",
												"example.classpath.b.Test_b_1",
												"example.classpath.b.Test_b_2",
												"example.classpath.b.a.Test_ba_1",
												"example.classpath.b.a.Test_ba_2",
												"example.classpath.b.b.Test_bb_1",
												"example.classpath.b.b.Test_bb_2",
												"example.classpath.b.b.a.Test_bba_1",
												"example.classpath.b.b.a.Test_bba_2",
												"example.classpath.b.b.b.Test_bbb_1",
												"example.classpath.b.b.b.Test_bbb_2"));
	}
	
	@Test public void ignoresNonClassFiles() {
		// classpath.c contains
		//    - ./c/Test_c_1.class
		//    - ./c/not-a-test.txt
		String classpath = "testbin/classpath.c";
		Collection<Class<?>> foundClasses = new ClassesOnClasspath(classpath).find();
		assertThat(namesOf(foundClasses), contains("example.classpath.c.Test_c_1"));
	}
	
	@Test public void ignoresNonTestClasses() {
		// classpath.d contains
		//    - ./d/Test_d_1.class
		//    - ./d/NotATest_d_2.class
		String classpath = "testbin/classpath.d";
		Collection<Class<?>> foundClasses = new ClassesOnClasspath(classpath).find();
		assertThat(namesOf(foundClasses), contains("example.classpath.d.Test_d_1"));
	}
	
	@Test public void ignoresNonDirectoryClasspathElements() {
		Collection<Class<?>> foundClasses = new ClassesOnClasspath("no.such.directory").find();
		assertThat(foundClasses, hasSize(0));
	}

	private Collection<String> namesOf(Collection<Class<?>> classes) {
		Collection<String> names = new ArrayList<String>();
		for(Class<?> c : classes) {
			names.add(c.getName());
		}
		return names;
	}
}
