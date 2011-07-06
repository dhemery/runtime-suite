package com.dhemery.runtimesuite.filters;


import static org.junit.Assert.*;
import org.junit.Test;


import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.filters.ExcludeClasses;

import example.tests.Runnable1;
import example.tests.Runnable2;
import example.tests.Runnable3;


public class AnExcludeClassesFilter {
	@Test
	public void withAnEmptyClassList_passesAllClasses() {
		ClassFilter filter = new ExcludeClasses();
		assertTrue(filter.passes(Runnable1.class));
		assertTrue(filter.passes(Runnable2.class));
		assertTrue(filter.passes(Runnable3.class));
	}

	@Test
	public void rejectsEachListedClass() {
		ClassFilter filter = new ExcludeClasses(Runnable1.class, Runnable2.class, Runnable3.class);
		assertFalse(filter.passes(Runnable1.class));
		assertFalse(filter.passes(Runnable2.class));
		assertFalse(filter.passes(Runnable3.class));
	}

	@Test
	public void passesEachNonListedClass() {
		ClassFilter filter = new ExcludeClasses(Runnable1.class);
		assertTrue(filter.passes(Runnable2.class));
		assertTrue(filter.passes(Runnable3.class));
	}	
}
