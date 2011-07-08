package com.dhemery.runtimesuite.tests;

import org.junit.Test;

import com.dhemery.runtimesuite.finders.ListedClasses;

import example.tests.Runnable1;
import example.tests.Runnable2;
import example.tests.Runnable3;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;


public class AListedClassesFinder {
	@SuppressWarnings("unchecked")
	@Test
	public void findsListedClasses() {
		ListedClasses listedClasses = new ListedClasses(Runnable1.class, Runnable2.class, Runnable3.class);
		assertThat(listedClasses.find(), hasItems(Runnable1.class, Runnable2.class, Runnable3.class));
	}
}
