package com.dhemery.runtimesuite.finders;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;


import com.dhemery.runtimesuite.finders.ListedClasses;

import example.tests.Runnable1;
import example.tests.Runnable2;
import example.tests.Runnable3;


public class AListedClassesFinder {
	@SuppressWarnings("unchecked")
	@Test
	public void findsListedClasses() {
		ListedClasses listedClasses = new ListedClasses(Runnable1.class, Runnable2.class, Runnable3.class);
		assertThat(listedClasses.find(), hasItems(Runnable1.class, Runnable2.class, Runnable3.class));
	}
}
