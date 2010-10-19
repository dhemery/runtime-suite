package com.dhemery.runtimesuite.examples;

import com.dhemery.runtimesuite.ClassFinder;

public class ListedClassFinder extends ListedClassNonFinder implements ClassFinder {
	public ListedClassFinder(Class<?>...classes) {
		super(classes);
	}
}