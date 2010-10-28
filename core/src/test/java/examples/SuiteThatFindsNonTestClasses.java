package examples;

import org.junit.runner.RunWith;

import com.dhemery.runtimesuite.ClassFinder;
import com.dhemery.runtimesuite.Finder;
import com.dhemery.runtimesuite.RuntimeSuite;
import com.dhemery.runtimesuite.finders.ListedClasses;


@RunWith(RuntimeSuite.class)
public class SuiteThatFindsNonTestClasses {
	@Finder public ClassFinder nonTestClassFinder = new ListedClasses(NotRunnable.class);
}