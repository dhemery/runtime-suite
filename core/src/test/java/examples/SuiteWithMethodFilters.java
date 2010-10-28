package examples;

import org.junit.runner.RunWith;

import com.dhemery.runtimesuite.ClassFinder;
import com.dhemery.runtimesuite.Filter;
import com.dhemery.runtimesuite.Finder;
import com.dhemery.runtimesuite.MethodFilter;
import com.dhemery.runtimesuite.RuntimeSuite;
import com.dhemery.runtimesuite.finders.ListedClasses;

@RunWith(RuntimeSuite.class)
public class SuiteWithMethodFilters {
	@Finder public ClassFinder finder = new ListedClasses(Runnable.class);
	@Filter public MethodFilter startWithA = new MethodsThatStartWith("a_");
	@Filter public MethodFilter endWith1 = new MethodsThatEndWith("1");
}