package examples;

import org.junit.runner.RunWith;

import com.dhemery.runtimesuite.Finder;
import com.dhemery.runtimesuite.RuntimeSuite;
import com.dhemery.runtimesuite.finders.ListedClasses;


@RunWith(RuntimeSuite.class)
public class SuiteWithClassFinderDeclaredAsSubtype {
	@Finder public ListedClasses finder = new ListedClasses(Runnable1.class, Runnable2.class, Runnable3.class);
}