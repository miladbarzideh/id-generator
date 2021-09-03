package io.github.miladbarzideh;


import io.github.miladbarzideh.exception.IdGenerateException;

public class BitsAllocator {

    public static final int TOTAL_BITS = 1 << 6;

    private final int signBits;
    private final int timestampBits;
    private final int nodeIdBits;
    private final int sequenceBits;

    private final long maxDeltaSeconds;
    private final long maxNodeId;
    private final long maxSequence;

    private final int timestampShift;
    private final int nodeIdShift;

    /**
     * Static factory method with timestampBits, workerIdBits, sequenceBits<br>
     *
     * @param timestampBits timestamp bits
     * @param nodeIdBits    node ID bits
     * @param sequenceBits  sequence bits
     */
    public static BitsAllocator build(int timestampBits, int nodeIdBits, int sequenceBits) {

        int signBits = 1;
        int allocateTotalBits = signBits + timestampBits + nodeIdBits + sequenceBits;
        if (allocateTotalBits != TOTAL_BITS) {
            throw new IdGenerateException("allocate not enough 64 bits");
        }

        return new BitsAllocator(signBits, timestampBits, nodeIdBits, sequenceBits);
    }

    private BitsAllocator(int signBits, int timestampBits, int nodeIdBits, int sequenceBits) {

        this.signBits = signBits;
        this.timestampBits = timestampBits;
        this.nodeIdBits = nodeIdBits;
        this.sequenceBits = sequenceBits;

        this.maxDeltaSeconds = ~(-1L << timestampBits);
        this.maxNodeId = ~(-1L << nodeIdBits);
        this.maxSequence = ~(-1L << sequenceBits);

        this.timestampShift = nodeIdBits + sequenceBits;
        this.nodeIdShift = sequenceBits;
    }

    /**
     * Allocate bits for UID according to delta seconds & workerId & sequence<br>
     * <b>Note that: </b>The highest bit will always be 0 for sign
     *
     * @param deltaSeconds delta seconds
     * @param nodeId       node ID
     * @param sequence     sequence
     * @return ID
     */
    public long build(long deltaSeconds, long nodeId, long sequence) {
        return (deltaSeconds << timestampShift) | (nodeId << nodeIdShift) | sequence;
    }

    public int getSignBits() {
        return signBits;
    }

    public int getTimestampBits() {
        return timestampBits;
    }

    public int getNodeIdBits() {
        return nodeIdBits;
    }

    public int getSequenceBits() {
        return sequenceBits;
    }

    public long getMaxDeltaSeconds() {
        return maxDeltaSeconds;
    }

    public long getMaxNodeId() {
        return maxNodeId;
    }

    public long getMaxSequence() {
        return maxSequence;
    }

    public int getTimestampShift() {
        return timestampShift;
    }

    public int getNodeIdShift() {
        return nodeIdShift;
    }
}
