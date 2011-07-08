package com.dhemery.runtimesuite.helpers;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

public class SuiteRunListener extends RunListener {
	public final List<String> tests = new ArrayList<String>();

	@Override
	public void testStarted(Description description) {
		tests.add(description.getMethodName());
	}
}
