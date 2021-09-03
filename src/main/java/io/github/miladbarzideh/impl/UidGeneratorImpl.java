package io.github.miladbarzideh.impl;



import io.github.miladbarzideh.BitsAllocator;
import io.github.miladbarzideh.ParsedId;
import io.github.miladbarzideh.UidGenerator;
import io.github.miladbarzideh.exception.IdGenerateException;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Distributed Sequence Generator.
 * Snowflake based unique ID generator, Inspired by: https://github.com/baidu/uid-generator
 * <p>
 * This class should be used as a Singleton.
 * Make sure that you create and reuse a Single instance of SequenceGenerator per node in your distributed system cluster.
 * <p>
 * * <pre>{@code
 *  * +------+----------------------+----------------+-----------+
 *  * | sign |     delta seconds    |    node id     | sequence  |
 *  * +------+----------------------+----------------+-----------+
 *  *   1bit          29bits              24bits         10bits
 *  * }</pre>
 */
public class UidGeneratorImpl implements UidGenerator {

    /**
     * Customer epoch, unit as second. For example 2020-01-01 (ms: 1577824200000)
     */
    protected long epochSeconds = TimeUnit.MILLISECONDS.toSeconds(1577824200000L);
    protected BitsAllocator bitsAllocator;
    protected long nodeId;
    protected long sequence = 0L;
    protected long lastSecond = -1L;

    public static UidGeneratorImpl build() {
        int timeBits = 29;
        int nodeBits = 24;
        int sequenceBits = 10;
        BitsAllocator bitsAllocator = BitsAllocator.build(timeBits, nodeBits, sequenceBits);
        long nodeId = NodeIdAssigner.assign(nodeBits);

        if (nodeId > bitsAllocator.getMaxNodeId()) {
            throw new IdGenerateException("Worker id " + nodeId + " exceeds the max " + bitsAllocator.getMaxNodeId());
        }

        return new UidGeneratorImpl(bitsAllocator, nodeId);
    }

    private UidGeneratorImpl(BitsAllocator bitsAllocator, long nodeId) {
        this.bitsAllocator = bitsAllocator;
        this.nodeId = nodeId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long nextId() {
        try {
            return generateId();
        } catch (Exception e) {
            throw new IdGenerateException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParsedId parseId(long id) {

        long totalBits = BitsAllocator.TOTAL_BITS;
        long signBits = bitsAllocator.getSignBits();
        long timestampBits = bitsAllocator.getTimestampBits();
        long nodeIdBits = bitsAllocator.getNodeIdBits();
        long seqBits = bitsAllocator.getSequenceBits();

        long seq = (id << (totalBits - seqBits)) >>> (totalBits - seqBits);
        long node = (id << (timestampBits + signBits)) >>> (totalBits - nodeIdBits);
        long deltaSeconds = id >>> (nodeIdBits + seqBits);
        Date thatTime = new Date(TimeUnit.SECONDS.toMillis(epochSeconds + deltaSeconds));

        return new ParsedId()
            .setTimeStamp(thatTime)
            .setNodeId(node)
            .setSequence(seq);
    }

    public UidGeneratorImpl setNodeId(long nodeId) {
        this.nodeId = nodeId;
        return this;
    }

    protected synchronized long generateId() {

        long currentSecond = getCurrentSecond();
        validateCurrentSecond(currentSecond);

        if (currentSecond == lastSecond) {
            sequence = (sequence + 1) & bitsAllocator.getMaxSequence();
            if (sequence == 0) {
                currentSecond = getNextSecond(lastSecond);
            }
        } else {
            sequence = 0L;
        }

        lastSecond = currentSecond;

        return bitsAllocator.build(currentSecond - epochSeconds, nodeId, sequence);
    }

    private long getCurrentSecond() {
        long currentSecond = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        if (currentSecond - epochSeconds > bitsAllocator.getMaxDeltaSeconds()) {
            throw new IdGenerateException("Timestamp bits is exhausted. Refusing UID generate. Now: " + currentSecond);
        }

        return currentSecond;
    }

    private long getNextSecond(long lastTimestamp) {
        long timestamp = getCurrentSecond();
        while (timestamp <= lastTimestamp) {
            timestamp = getCurrentSecond();
        }

        return timestamp;
    }

    private void validateCurrentSecond(long currentSecond) {
        if (currentSecond < lastSecond) {
            long refusedSeconds = lastSecond - currentSecond;
            throw new IdGenerateException("Clock moved backwards. Refusing for %d seconds", refusedSeconds);
        }
    }
}
