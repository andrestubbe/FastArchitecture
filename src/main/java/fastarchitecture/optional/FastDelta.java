package fastarchitecture.optional;

/**
 * Optional Delta Primitive: Encapsulates incremental changes applied to transition state.
 */
public interface FastDelta {

    /**
     * Applies this delta to a previous state snapshot to yield a new state.
     *
     * @param previous Base state before delta application
     * @return Resulting new state snapshot
     */
    FastState apply(FastState previous);
}
