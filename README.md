# Runtime Suite

A Runtime Suite is a class that finds candidate test classes at runtime, filters the candidate classes and their test methods for inclusion in the suite, and runs the surviving methods under JUnit.

`RuntimeSuite` is designed to make it easy for you to write your own finders and filters, and to create suites that use them.

This project will include a few basic finders and filters. These filters are ready to use:

 * `ClassesInCategories` filter: Passes each class in any of the specified categories; rejects all other classes. (Done)
 * `ClassesNotInCategories` filter: Rejects each class in any of the specified categories; passes all other classes. (Done)
 
I intend to write these finders and filters:
 
 * `ClassesOnTheClasspath`: Finds all test classes in the classpath. (Not yet implemented.)
 * `MethodsInCategories` filter: Passes each test method in any of the specified categories; rejects all other test methods. (Not yet implemented.)
 * `MethodsNotInCategories` filter: Rejects each test method in any of the specified categories; passes all other test methods. (Not yet implemented.)


**Note:** This code is still volatile.

## Declaring Runtime Suites

Declare a class to be a runtime suite by annotating it with `@RunWith(RuntimeSuite.class)`. For example:

    @RunWith(RuntimeSuite.class)
    public static class MyClassFinderSuite {
        ...
    	@Finder public ClassFinder classFinder1 = new AClassFinder();
    	@Finder public ClassFinder classFinder2 = new AnotherClassFinder();
    	...
    	@Filter public ClassFilter classFilter1 = new AClassFilter();
    	@Filter public ClassFilter classFilter2 = new AnotherClassFilter();
    	...
    }

Conceptually, `RuntimeSuite` does the following:

* Call each class finder and accumulate the resulting candidate classes.
* Call each class filter and retain the classes that survive all filters.
* Create a `Runner` for each surviving class.
* Yield the runners to JUnit for processing.

## Declaring Class Finder fields

In your suite class, declare one or more fields of type `ClassFinder` annotated with the `@Finder` annotation. For example:

    @RunWith(RuntimeSuite.class)
    public static class MyClassFinderSuite {
        // Declare finders:
    	@Finder public ClassFinder classFinder1 = new AClassFinder();
    	@Finder public ClassFinder classFinder2 = new AnotherClassFinder();
    	...
    }

## Declaring Class Filter fields

In your suite class, declare one or more fields of type `ClassFilter` annotated with the `@Filter` annotation.

    @RunWith(RuntimeSuite.class)
    public static class MyClassFinderSuite {
        // Declare finders:
        ...

        // Declare filters
    	@Filter public ClassFilter classFilter1 = new AClassFilter();
    	@Filter public ClassFilter classFilter2 = new AnotherClassFilter();
    	...
    }


## Writing a Class Finder class

Write each class finder class to implement the `ClassFinder` interface:

    public interface ClassFinder {
        Collection<Class<?>> find();
    }

`RuntimeSuite` calls the `find()` method to find classes to run.

Write your `find()` method to find test classes to be considered as candidates for inclusion in the suite. Return the list as the return value. `RuntimeSuite` will gather all candidate classes returned from all finders, filter them using the declared filters, and run the tests that survive the filters.


## Writing a Class Filter class

Write each class filter class to implement the `ClassFilter` interface:

    public interface ClassFilter {
        boolean passes(Class<?> candidateClass);
    }

`RuntimeSuite` calls the `passes()` method once for each candidate test class.

Write your `passed()` method to determine whether to include the given class in the suite. Return `true` if the filter passes the class, `false` if the filter rejects the class. If this filter passes the class, `RuntimeSuite` will subject the class to other filters (if any are declared). The classes that survive all filters are considered part of the suite.

## Declaring Method Filters
I don't yet know how I will filter methods. Candidates include:

* Fields of type `MethodFilter` annotated with `@Filter`, analogous to `ClassFilter`.
* Filters declared somewhere, and applied by `ParentRunner`.
* A custom `RunnerBuilder` specific to `RuntimeSuite`.
* Custom `RunnerBuilder`s written by users.
