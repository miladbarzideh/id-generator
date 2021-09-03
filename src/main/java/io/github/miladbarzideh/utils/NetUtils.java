package io.github.miladbarzideh.utils;


import io.github.miladbarzideh.exception.IdGenerateException;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;


public class NetUtils {

    private NetUtils() {
    }

    /**
     * Retrieve the first validated local ip address(the Public and LAN ip addresses are validated).
     *
     * @return the local address
     * @throws SocketException the socket exception
     */
    public static InetAddress getLocalInetAddress() throws SocketException {

        Enumeration<NetworkInterface> enu = NetworkInterface.getNetworkInterfaces();

        while (enu.hasMoreElements()) {
            NetworkInterface ni = enu.nextElement();
            if (ni.isLoopback()) {
                continue;
            }

            Enumeration<InetAddress> addressEnumeration = ni.getInetAddresses();
            while (addressEnumeration.hasMoreElements()) {
                InetAddress address = addressEnumeration.nextElement();

                // ignores all invalidated addresses
                if (address.isLinkLocalAddress() || address.isLoopbackAddress() || address.isAnyLocalAddress()) {
                    continue;
                }

                return address;
            }
        }

        throw new IdGenerateException("No validated local address!");
    }

    /**
     * Retrieve local address
     *
     * @return the string local address
     */
    public static String getLocalAddress() {
        try {
            return getLocalInetAddress().getHostAddress();
        } catch (SocketException e) {
            throw new IdGenerateException(e);
        }
    }
}
