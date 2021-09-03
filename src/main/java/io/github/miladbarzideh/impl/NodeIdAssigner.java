package io.github.miladbarzideh.impl;


import io.github.miladbarzideh.utils.NetUtils;

import java.util.Objects;

/**
 * Source: https://github.com/yezhoujie/node-ip-snowflake
 */
public class NodeIdAssigner {

    private static final String POD_IP = "POD_IP";

    private NodeIdAssigner() {
    }

    public static long ipV4ToLong(String ip) {
        String[] octets = ip.split("\\.");
        return (Long.parseLong(octets[0]) << 24) + (Integer.parseInt(octets[1]) << 16)
            + (Integer.parseInt(octets[2]) << 8) + Integer.parseInt(octets[3]);
    }

    /**
     * Generates node ID from last 16-bits of the host's IP address.
     *
     * @param bits node ID bits
     * @return assigned node ID
     */
    public static long assign(int bits) {
        int shift = 64 - bits;
        long ip = ipV4ToLong(getIpAddress());
        return (ip << shift) >>> shift;
    }

    private static String getIpAddress() {
        String podIp = System.getenv(POD_IP);
        if (Objects.nonNull(podIp) && podIp.length() != 0) {
            return podIp;
        }

        return NetUtils.getLocalAddress();
    }
}
