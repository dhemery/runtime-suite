# Runtime Suite

A Runtime Suite is a class that finds candidate test classes at runtime, filters them based on whatever criteria you choose, and runs the remaining classes them under JUnit.

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

Conceptually, `RuntimeSuite` calls each of the class finder classes and accumulates the results. It then calls each class filter and retains the classes that survive the filters. For each surviving class, it creates a Runner to run the class's tests.

## Declaring Class Finder fields

In your suite class, declare a method of type `ClassFinder` annotated with the @Finder annotation. For example:

    @RunWith(RuntimeSuite.class)
    public static class MyClassFinderSuite {
        // Declare finders:
    	@Finder public ClassFinder classFinder1 = new AClassFinder();
    	@Finder public ClassFinder classFinder2 = new AnotherClassFinder();
    	...
    }

## Declaring Class Filter fields

In your suite class, declare one or more methods of type `ClassFilter` annotated with the @Filter annotation.

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

`RuntimeSuite` calls the `find()` method, passing it a `Field` that represents the finder field. You can use this parameter to examine the field declaration or its declaring suite class for annotations or other information.

The `find()` method is where your finder class will do the work of finding candidate test classes. Return the list as the return value. `RuntimeSuite` will gather all candidate classes returned from all finders, and send them to the filters.


## Writing a Class Filter class

Write each class filter class to implement the `ClassFilter` interface:

    public interface ClassFilter {
        List<Class<?>> filter(Field filterField, List<Class<?>> candidateClasses);
    }

`RuntimeSuite` calls the `filter()` method, passing it a `Field` that represents the class filter field. You can use this parameter to examine the field declaration or its declaring suite class for annotations or other information.

The `filter()` method is where your filter class will do the work of assessing each candidate test class

## Declaring Method Filters
I don't yet know how I will filter methods. Perhaps I will create a custom RunnerBuilder for that.