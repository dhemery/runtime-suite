package example.filters;

import java.util.Arrays;
import java.util.Collection;

import example.tests.Runnable2;



public class NotAClassFinder /* Does not implement ClassFinder */ {
	public Collection<Class<?>> find() {
		return Arrays.asList(new Class<?>[] { Runnable2.class });
	}
}