package example.tests;

import org.junit.Test;

public class NotRunnable {
	public void noTestAnnotation() {}
	@Test public int nonVoidReturnType() { return 0; }
	@Test public void takesParameters(int i) {}
	@Test void notPublic() {}
}