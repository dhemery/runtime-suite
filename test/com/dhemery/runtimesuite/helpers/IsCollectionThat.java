package com.dhemery.runtimesuite.helpers;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class IsCollectionThat {
	
	public static <T> Matcher<List<T>> hasDuplicates() {
		return new TypeSafeMatcher<List<T>>() {

			@Override
			public boolean matchesSafely(List<T> candidate) {
				List<T> list = (List<T>) candidate;
				List<T> alreadySeen = new ArrayList<T>();
				for(T item : list) {
					if(alreadySeen.contains(item)) return true;
					alreadySeen.add(item);
				}
				return false;
			}

			@Override
			public void describeTo(org.hamcrest.Description description) {
				description.appendText("a collection that has duplicates");
			}
		};
	}
}