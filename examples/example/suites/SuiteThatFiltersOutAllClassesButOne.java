package example.suites;

import org.junit.runner.RunWith;

import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.ClassFinder;
import com.dhemery.runtimesuite.Filter;
import com.dhemery.runtimesuite.Finder;
import com.dhemery.runtimesuite.RuntimeSuite;
import com.dhemery.runtimesuite.filters.ExcludeClasses;
import com.dhemery.runtimesuite.finders.ListedClasses;

import example.tests.Runnable1;
import example.tests.Runnable2;
import example.tests.Runnable3;



@RunWith(RuntimeSuite.class)
public class SuiteThatFiltersOutAllClassesButOne {
	@Finder public ClassFinder classFinder1 = new ListedClasses(Runnable1.class, Runnable2.class, Runnable3.class);
	@Filter public ClassFilter classFilter1 = new ExcludeClasses(Runnable1.class);
	@Filter public ClassFilter classFilter2 = new ExcludeClasses(Runnable3.class);
}