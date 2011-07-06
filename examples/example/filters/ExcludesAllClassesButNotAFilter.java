package example.filters;

public class ExcludesAllClassesButNotAFilter /* Does not implement ClassFilter */ {
	public boolean passes(Class<?> candidateClass) {
		return false;
	}
}