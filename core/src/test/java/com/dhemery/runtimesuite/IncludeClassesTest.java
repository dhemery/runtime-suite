package com.dhemery.runtimesuite;

import org.junit.Test;

import static org.fest.assertions.Assertions.*;

import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.filters.IncludeClasses;

import examples.Runnable1;
import examples.Runnable2;
import examples.Runnable3;

public class IncludeClassesTest {
	@Test
	public void withAnEmptyClassList_rejectsAllClasses() {
		ClassFilter filter = new IncludeClasses();
		assertThat(filter.passes(Runnable1.class)).isFalse();
		assertThat(filter.passes(Runnable2.class)).isFalse();
		assertThat(filter.passes(Runnable3.class)).isFalse();
	}

	@Test
	public void acceptsEachListedClass() {
		ClassFilter filter = new IncludeClasses(Runnable1.class, Runnable2.class, Runnable3.class);
		assertThat(filter.passes(Runnable1.class)).isTrue();
		assertThat(filter.passes(Runnable2.class)).isTrue();
		assertThat(filter.passes(Runnable3.class)).isTrue();
	}

	@Test
	public void rejectsEachNonListedClass() {
		ClassFilter filter = new IncludeClasses(Runnable1.class);
		assertThat(filter.passes(Runnable2.class)).isFalse();
		assertThat(filter.passes(Runnable3.class)).isFalse();
	}	
}
