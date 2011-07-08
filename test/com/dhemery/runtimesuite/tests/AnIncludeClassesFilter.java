package com.dhemery.runtimesuite.tests;

import org.junit.Test;
import static org.junit.Assert.*;


import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.filters.IncludeClasses;

import example.tests.Runnable1;
import example.tests.Runnable2;
import example.tests.Runnable3;


public class AnIncludeClassesFilter {
	@Test
	public void withAnEmptyClassList_rejectsAllClasses() {
		ClassFilter filter = new IncludeClasses();
		assertFalse(filter.passes(Runnable1.class));
		assertFalse(filter.passes(Runnable2.class));
		assertFalse(filter.passes(Runnable3.class));
	}

	@Test
	public void acceptsEachListedClass() {
		ClassFilter filter = new IncludeClasses(Runnable1.class, Runnable2.class, Runnable3.class);
		assertTrue(filter.passes(Runnable1.class));
		assertTrue(filter.passes(Runnable2.class));
		assertTrue(filter.passes(Runnable3.class));
	}

	@Test
	public void rejectsEachNonListedClass() {
		ClassFilter filter = new IncludeClasses(Runnable1.class);
		assertFalse(filter.passes(Runnable2.class));
		assertFalse(filter.passes(Runnable3.class));
	}	
}
