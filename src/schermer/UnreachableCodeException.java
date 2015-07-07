package schermer;

/**
 * The UnreachableCodeException is used to assert that some code is
 * unreachable.
 */
@SuppressWarnings("serial")
public class UnreachableCodeException extends RuntimeException {
	public UnreachableCodeException() {
		super("Unreachable code assertion failure.");
	}
}