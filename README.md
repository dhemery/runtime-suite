# Runtime Suite

A Runtime Suite is a class that finds candidate test classes at runtime, filters the candidate classes and their test methods for inclusion in the suite, and runs the surviving methods under JUnit.

You write the classes to find and filter classes and methods. I will write a few finders and filters for my own needs, such as:

 * `ClassesOnTheClasspath`: Finds all test classes in the classpath.
 * `CategoryInclusionClassFilter`: Retains classes marked (by `@Category` annotations) as belonging to specified categories, and rejects classes not in those categories.
 * `CategoryExclusionClassFilter`: Rejects classes marked (by `@Category` annotations) as belonging to specified categories, and retains classes not in those categories.


**NOTE:** This document describes my current intentions, which are subject to change on my slightest whim. This stuff isn't implemented yet. Follow along if you wish, but don't count on anything until I declare a release.

If my intentions and capriciousness don't fit your needs, feel free to grab the code from any commit and have your way with it.

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

In your suite class, declare a method of type `ClassFinder` annotated with the `@Finder` annotation. For example:

    @RunWith(RuntimeSuite.class)
    public static class MyClassFinderSuite {
        // Declare finders:
    	@Finder public ClassFinder classFinder1 = new AClassFinder();
    	@Finder public ClassFinder classFinder2 = new AnotherClassFinder();
    	...
    }

## Declaring Class Filter fields

In your suite class, declare one or more methods of type `ClassFilter` annotated with the `@Filter` annotation.

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
        List<Class<?>> find(Field finderField);
    }

`RuntimeSuite` calls the `find()` method, passing it a `Field` that represents the class finder field declared in the suite class. You can use this parameter to examine the field declaration or its declaring suite class for annotations or other information.

Write your `find()` method to find test classes to be considered as candidates for inclusion in the suite. Return the list as the return value. `RuntimeSuite` will gather all candidate classes returned from all finders, filter them using the declared filters, and run the tests that survive the filters.


## Writing a Class Filter class

Write each class filter class to implement the `ClassFilter` interface:

    public interface ClassFilter {
        List<Class<?>> filter(Field filterField, List<Class<?>> candidateClasses);
    }

`RuntimeSuite` calls the `filter()` method, passing it a `Field` that represents the class filter field declared in the suite class, and a list of candidate test classes to filter. You can use the `Field` parameter to examine the field declaration or its declaring suite class for annotations or other information.

Write your `filter()` method to determine whether to include each test class in the suite. Return the list of classes that survive the filter. `RuntimeSuite` will submit these classes to other filters (if any are declared). The classes that survive all filters are considered part of the suite.

## Declaring Method Filters
I don't yet know how I will filter methods. Candidates include:

* Fields of type `MethodFilter` annotated with `@Filter`, analogous to `ClassFilter`.
* Filters declared somewhere, and applied by `ParentRunner`.
* A custom `RunnerBuilder` specific to `RuntimeSuite`.
* Custom `RunnerBuilder`s written by users.
