package com.dhemery.runtimesuite.internal.tests;

import java.io.File;

import org.junit.Test;

public class Fiddle {
	@Test public void showClasspath() {
		System.out.println("Classpath: " + System.getProperty("java.class.path"));
	}
	
	@Test public void showCurrentWorkingDirectory() {
		System.out.println("CWD: " + new File(".").getAbsolutePath());
	}
}
