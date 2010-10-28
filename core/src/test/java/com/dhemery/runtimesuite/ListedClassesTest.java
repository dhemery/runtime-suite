package com.dhemery.runtimesuite;

import static org.fest.assertions.Assertions.*;
import org.junit.Test;

import com.dhemery.runtimesuite.finders.ListedClasses;

import examples.Runnable1;
import examples.Runnable2;
import examples.Runnable3;

public class ListedClassesTest {
	@Test
	public void findsListedClasses() {
		ListedClasses listedClasses = new ListedClasses(Runnable1.class, Runnable2.class, Runnable3.class);
		assertThat(listedClasses.find()).containsOnly(Runnable1.class, Runnable2.class, Runnable3.class);
	}
}
