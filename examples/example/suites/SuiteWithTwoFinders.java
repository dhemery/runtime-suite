package example.suites;

import org.junit.runner.RunWith;

import com.dhemery.runtimesuite.ClassFinder;
import com.dhemery.runtimesuite.Finder;
import com.dhemery.runtimesuite.RuntimeSuite;
import com.dhemery.runtimesuite.finders.ListedClasses;

import example.tests.Runnable1;
import example.tests.Runnable2;



@RunWith(RuntimeSuite.class)
public class SuiteWithTwoFinders {
	@Finder public ClassFinder classFinder1 = new ListedClasses(Runnable1.class);
	@Finder public ClassFinder classFinder2 = new ListedClasses(Runnable2.class);
}