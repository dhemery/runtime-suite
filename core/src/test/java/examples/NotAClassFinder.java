package examples;

import java.util.Arrays;
import java.util.Collection;


public class NotAClassFinder /* Does not implement ClassFinder */ {
	public Collection<Class<?>> find() {
		return Arrays.asList(new Class<?>[] { Runnable2.class });
	}
}