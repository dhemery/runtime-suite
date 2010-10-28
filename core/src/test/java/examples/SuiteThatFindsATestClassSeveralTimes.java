package examples;

import org.junit.runner.RunWith;

import com.dhemery.runtimesuite.ClassFinder;
import com.dhemery.runtimesuite.Finder;
import com.dhemery.runtimesuite.RuntimeSuite;
import com.dhemery.runtimesuite.finders.ListedClasses;


@RunWith(RuntimeSuite.class)
public class SuiteThatFindsATestClassSeveralTimes {
	@Finder public ClassFinder finder1 = new ListedClasses(Runnable1.class);
	@Finder public ClassFinder finder2 = new ListedClasses(Runnable1.class);
	@Finder public ClassFinder finder3 = new ListedClasses(Runnable1.class);
}