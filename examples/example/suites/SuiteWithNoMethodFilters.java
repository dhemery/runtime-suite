package example.suites;

import org.junit.runner.RunWith;

import com.dhemery.runtimesuite.ClassFinder;
import com.dhemery.runtimesuite.Finder;
import com.dhemery.runtimesuite.RuntimeSuite;
import com.dhemery.runtimesuite.finders.ListedClasses;

import example.tests.Runnable;



@RunWith(RuntimeSuite.class)
public class SuiteWithNoMethodFilters {
	@Finder public ClassFinder finder = new ListedClasses(Runnable.class);
}