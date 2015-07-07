package schermer;

/**
 * The UnimplementedFeatureException is used to assert that some code is
 * not yet written.
 */
@SuppressWarnings("serial")
public class UnimplementedFeatureException extends RuntimeException {
	public UnimplementedFeatureException(String feature) {
		super("Unimplemented feature: " + feature);
	}
}