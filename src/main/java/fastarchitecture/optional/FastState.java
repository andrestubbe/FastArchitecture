package fastarchitecture.optional;

/**
 * Optional State Primitive: Represents an immutable snapshot of state in reactive/CREAM systems.
 */
public interface FastState {

    /**
     * Calculates the differential changes between this state and the next state snapshot.
     *
     * @param next The subsequent state snapshot
     * @return FastDelta containing differential changes
     */
    FastDelta diff(FastState next);
}
