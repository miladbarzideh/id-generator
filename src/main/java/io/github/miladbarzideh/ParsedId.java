package io.github.miladbarzideh;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ParsedId {

    private Date timeStamp;
    private long nodeId;
    private long sequence;

    public Date getTimeStamp() {
        return timeStamp;
    }

    public ParsedId setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public long getNodeId() {
        return nodeId;
    }

    public ParsedId setNodeId(long nodeId) {
        this.nodeId = nodeId;
        return this;
    }

    public long getSequence() {
        return sequence;
    }

    public ParsedId setSequence(long sequence) {
        this.sequence = sequence;
        return this;
    }

    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return String.format("{\"timestamp\":\"%s\", \"nodeId\":\"%d\", \"sequence\":\"%d\"}",
            format.format(timeStamp), nodeId, sequence);
    }
}
