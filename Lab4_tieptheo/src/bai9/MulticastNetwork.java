
package bai9;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class MulticastNetwork {

    public static NetworkInterface choose(String[] args)
            throws IOException {

        Enumeration<NetworkInterface> enumeration =
                NetworkInterface.getNetworkInterfaces();

        if (enumeration == null) {
            throw new SocketException(
                    "Khong tim thay giao dien mang.");
        }

        List<NetworkInterface> interfaces =
                Collections.list(enumeration);

        System.out.println("=== GIAO DIEN MANG ===");

        for (NetworkInterface ni : interfaces) {
            try {
                System.out.printf(
                        "%s | %s | up=%s | multicast=%s%n",
                        ni.getName(),
                        ni.getDisplayName(),
                        ni.isUp(),
                        ni.supportsMulticast());
            } catch (SocketException e) {
                System.out.println(
                        "Khong doc duoc thong tin: " + ni.getName());
            }
        }

        // Neu co tham so, uu tien giao dien duoc chi dinh
        if (args != null && args.length > 0) {

            for (NetworkInterface ni : interfaces) {

                boolean matches =
                        args[0].equalsIgnoreCase(ni.getName())
                        || (ni.getDisplayName() != null
                        && args[0].equalsIgnoreCase(
                                ni.getDisplayName()));

                if (matches) {
                    if (isUsable(ni)) {
                        return ni;
                    }

                    throw new SocketException(
                            "Giao dien mang khong ho tro multicast.");
                }
            }

            throw new SocketException(
                    "Khong tim thay giao dien: " + args[0]);
        }

        // Uu tien giao dien cua dia chi may tinh
        NetworkInterface localInterface =
                NetworkInterface.getByInetAddress(
                        InetAddress.getLocalHost());

        if (localInterface != null
                && isUsable(localInterface)
                && !localInterface.isLoopback()
                && !localInterface.isVirtual()) {

            return localInterface;
        }

        // Tim giao dien vat ly hoac giao dien mang kha dung
        for (NetworkInterface ni : interfaces) {
            if (isUsable(ni)
                    && !ni.isLoopback()
                    && !ni.isVirtual()) {
                return ni;
            }
        }

        throw new SocketException(
                "Khong co giao dien mang phu hop.");
    }

    private static boolean isUsable(NetworkInterface ni)
            throws SocketException {

        return ni.isUp() && ni.supportsMulticast();
    }
}
