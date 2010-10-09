package com.dhemery.filteredsuite.fixtures;

import com.dhemery.filteredsuite.ExcludeClassesWithCategories;
import com.dhemery.filteredsuite.IncludeClassesWithCategories;

@IncludeClassesWithCategories(CategoryA.class)
@ExcludeClassesWithCategories(CategoryB.class)
public class SuiteThatIncludesCategoryAandExcludesCategoryB {}