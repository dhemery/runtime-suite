package examples;

public class ExcludesAllClassesButNotAFilter /* Does not implement ClassFilter */ {
	public boolean passes(Class<?> candidateClass) {
		return false;
	}
}