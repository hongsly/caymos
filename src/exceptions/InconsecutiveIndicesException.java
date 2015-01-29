package exceptions;

/**
 * This is an unchecked exception that is thrown whenever a method on a graph which requires
 * consecutive indices of vertices is called on an instance without consecutive vertex indices
 * @author Menghan
 *
 */
public class InconsecutiveIndicesException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InconsecutiveIndicesException() {
		super();
	}

}
