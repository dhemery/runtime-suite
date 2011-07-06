package example.suites;

import org.junit.runner.RunWith;

import com.dhemery.runtimesuite.ClassFinder;
import com.dhemery.runtimesuite.Finder;
import com.dhemery.runtimesuite.RuntimeSuite;
import com.dhemery.runtimesuite.finders.ListedClasses;

import example.filters.NotAClassFinder;
import example.tests.Runnable1;
import example.tests.Runnable3;

@RunWith(RuntimeSuite.class)
public class SuiteWithGoodAndBadClassFinders {
	public ClassFinder badFinderNoAnnotation = new ListedClasses(Runnable1.class);
	@Finder public NotAClassFinder badFinderWrongType = new NotAClassFinder();
	@Finder public ClassFinder goodFinder = new ListedClasses(Runnable3.class);
}